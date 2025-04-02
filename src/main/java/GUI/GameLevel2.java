/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Code.DBManager;
import Code.MusicPlayer;

import Code.UserAuth;
import GUI.ProfileDialog;
import GUI.FinishDialog;
//import static GUI.GameLevel.currentLevel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author manul
 */
public class GameLevel2 extends javax.swing.JFrame {

    private JPanel[] cards;
    private JLabel[] labels;
    private ArrayList<String> shuffledImages;
    private Map<JLabel, String> cardMap; // Maps JLabel to its image
    private JPanel firstCard, secondCard;
    private JLabel firstLabel, secondLabel;
    private int firstIndex, secondIndex;
    private boolean isFlipping = false;
    public static int currentLevel = 0; // Default level
    private int misses = 0;
    private int moves = 0;
    private int matchedPairs = 0;
    public static int points = 0;
    private static int levels = 0;
    private int timeLeft = 60;
    private javax.swing.Timer gameTimer;
    private MusicPlayer backgroundMusic;
    /**
     * Creates new form GameLevel
     */
    public GameLevel2(int currentLevel) {
        initComponents();
        this.currentLevel = currentLevel;
        txtLevel.setText("Level: "+currentLevel);
        resetPoints();  // Reset points for the next level
        loadImagesFromJSON(currentLevel); // Load level 1 images initially
        setupGame();
        startTimer();
        
        // Start background music
        backgroundMusic = new MusicPlayer("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\marimba-tropical-african-travel-game-197517.wav"); // Update path
        backgroundMusic.play();
        
    }
    
    
    // Function to load images from JSON
    private void loadImagesFromJSON(int level) {
        try {
        // Read JSON file as a string
        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\images.json")));
        
        // Convert string to JSON object
        JSONObject jsonObject = new JSONObject(content);
        JSONArray levelsArray = jsonObject.getJSONArray("levels");

        shuffledImages = new ArrayList<>();

        for (int i = 0; i < levelsArray.length(); i++) {
            JSONObject levelData = levelsArray.getJSONObject(i);
            int jsonLevel = levelData.getInt("level");

            if (jsonLevel == level) {
                JSONArray imagesArray = levelData.getJSONArray("images"); //Parses the JSON to extract images for the given level.
                for (int j = 0; j < imagesArray.length(); j++) {
                    shuffledImages.add(imagesArray.getString(j)); //Stores the extracted images in shuffledImages list.
                }
                break;
            }
        }

        // Shuffle images
        Collections.shuffle(shuffledImages);

    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    // Setup game UI //Sets up the game board and event listeners.
    private void setupGame() {
        

        cards = new JPanel[]{card1, card2, card3, card4, card5, card6, card7, card8,card9, card10, card11, card12, card13, card14, card15, card16,card17,card18,card19,card20};
        labels = new JLabel[]{label1, label2, label3, label4, label5, label6, label7, label8,label9, label10, label11, label12, label13, label14, label15, label16,label17, label18, label19, label20};

        cardMap = new HashMap<>(); // Store JLabel -> Image mapping

        // Assign event handlers
        for (int i = 0; i < cards.length; i++) {
            final int index = i;
            labels[i].setText(" "); // Set default text
            cardMap.put(labels[i], shuffledImages.get(i)); // Store mapping //Maps each JLabel to its corresponding image path in cardMap.

            cards[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    flipCard(labels[index]); //Adds a mouse click listener to each card to trigger flipCard() when clicked.
                }
            });
        }
    }
    
    private void flipCard(JLabel clickedLabel) {
        if (isFlipping || !clickedLabel.getText().equals(" ")) return; //Prevents flipping more than two cards at a time

        // Load and set image inside JLabel
        String imagePath = cardMap.get(clickedLabel); //Retrieves the image path from cardMap.
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        clickedLabel.setIcon(new ImageIcon(img)); //Sets the image to the JLabel and removes placeholder text.
        clickedLabel.setText(""); // Remove "?"

        if (firstLabel == null) {
            firstLabel = clickedLabel;
        } else {
            secondLabel = clickedLabel;
            isFlipping = true;

            // Delay before checking match
            javax.swing.Timer timer = new javax.swing.Timer(1000, this::checkMatch);
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    // Check if two selected cards match
    private void checkMatch(ActionEvent e) {
        
        
        moves++;
        txtMoves.setText("Moves: "+moves);
        if (cardMap.get(firstLabel).equals(cardMap.get(secondLabel))) {
            firstLabel.getParent().setEnabled(false);
            secondLabel.getParent().setEnabled(false);
            points += 10;

            txtPoints.setText(points+" points");

            
        } else {
            firstLabel.setIcon(null);
            secondLabel.setIcon(null);
            firstLabel.setText(" ");
            secondLabel.setText(" ");
            misses++;
            txtMisses.setText("Misses: "+misses);
        }
        
        
        if(points >= 100)
        {
            gameTimer.stop();
            // Handle level complete logic
            if(UserAuth.sessionToken != null)
            {
                updateProfileAfterLevel();
            }
            
            if(currentLevel == 3){
                FinishDialog3 finish = new FinishDialog3(this, true);
                finish.setLocationRelativeTo(this); // Center the dialog 
                finish.setVisible(true); // Show pop-up
            }
            else if (currentLevel == 4)
            {
                FinishDialog4 finish = new FinishDialog4(this, true);
                finish.setLocationRelativeTo(this); // Center the dialog 
                finish.setVisible(true); // Show pop-up
                
            }
            
            dispose();
        }
        
        
        firstLabel = null;
        secondLabel = null;
        isFlipping = false;
        
    }
    
    public void updateProfileAfterLevel() {
        
        String username = UserAuth.name;
//         Ensure points and level are passed properly
        UserAuth.updateScore(username, points);
        UserAuth.addcompleteLevels(username, currentLevel);
        
    }
    
    @Override
    public void dispose() {
    if (backgroundMusic != null) {
        backgroundMusic.stop();
    }
        super.dispose();
    }
    
    private void startTimer() {
        gameTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                txtTimer.setText("Time Left: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    gameTimer.stop();
                    gameOver();
                }
            }
        });
        gameTimer.start();
    }
    
    private void gameOver() {
//        JOptionPane.showMessageDialog(this, "Time's up! Your score: " + points, "Game Over", JOptionPane.INFORMATION_MESSAGE);
//        updateProfileAfterLevel();
        GameOver2 over = new GameOver2(this, true);
        over.setLocationRelativeTo(this); // Center the dialog 
        over.setVisible(true); // Show pop-up
        this.dispose();
    }
    
    public static void resetPoints() {
        points = 0; // Reset points each time a new level is started
    }
    
    private void restartGame() {
        // Reset variables
        matchedPairs = 0;
        moves = 0;
        misses = 0;
        points = 0;
        firstLabel = null;
        secondLabel = null;
        isFlipping = false;

        txtMoves.setText("Moves: 0");
        txtMisses.setText("Misses: 0");
        txtPoints.setText("0 points");

        // Reload images and shuffle
        loadImagesFromJSON(currentLevel);

        // Reset all cards
        for (int i = 0; i < cards.length; i++) {
            labels[i].setText(" ");
            labels[i].setIcon(null);
            cards[i].setEnabled(true);
            cardMap.put(labels[i], shuffledImages.get(i)); // Update map
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        card1 = new javax.swing.JPanel();
        label1 = new javax.swing.JLabel();
        card2 = new javax.swing.JPanel();
        label2 = new javax.swing.JLabel();
        card3 = new javax.swing.JPanel();
        label3 = new javax.swing.JLabel();
        card4 = new javax.swing.JPanel();
        label4 = new javax.swing.JLabel();
        card5 = new javax.swing.JPanel();
        label5 = new javax.swing.JLabel();
        card6 = new javax.swing.JPanel();
        label6 = new javax.swing.JLabel();
        card7 = new javax.swing.JPanel();
        label7 = new javax.swing.JLabel();
        card8 = new javax.swing.JPanel();
        label8 = new javax.swing.JLabel();
        card9 = new javax.swing.JPanel();
        label9 = new javax.swing.JLabel();
        card10 = new javax.swing.JPanel();
        label10 = new javax.swing.JLabel();
        card11 = new javax.swing.JPanel();
        label11 = new javax.swing.JLabel();
        card12 = new javax.swing.JPanel();
        label12 = new javax.swing.JLabel();
        card13 = new javax.swing.JPanel();
        label13 = new javax.swing.JLabel();
        card14 = new javax.swing.JPanel();
        label14 = new javax.swing.JLabel();
        card15 = new javax.swing.JPanel();
        label15 = new javax.swing.JLabel();
        card16 = new javax.swing.JPanel();
        label16 = new javax.swing.JLabel();
        txtMisses = new javax.swing.JLabel();
        txtMoves = new javax.swing.JLabel();
        RestartBtn = new javax.swing.JButton();
        txtPoints = new javax.swing.JLabel();
        Homebtn = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtTimer = new javax.swing.JLabel();
        txtLevel = new javax.swing.JLabel();
        card17 = new javax.swing.JPanel();
        label17 = new javax.swing.JLabel();
        card18 = new javax.swing.JPanel();
        label18 = new javax.swing.JLabel();
        card19 = new javax.swing.JPanel();
        label19 = new javax.swing.JLabel();
        card20 = new javax.swing.JPanel();
        label20 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(249, 243, 202));

        card1.setBackground(new java.awt.Color(226, 203, 152));
        card1.setForeground(new java.awt.Color(255, 255, 255));

        label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label1.setText("jLabel1");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card2.setBackground(new java.awt.Color(226, 203, 152));

        label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label2.setText("jLabel1");

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card3.setBackground(new java.awt.Color(226, 203, 152));

        label3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label3.setText("jLabel1");

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label3, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label3, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );

        card4.setBackground(new java.awt.Color(226, 203, 152));

        label4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label4.setText("jLabel1");

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        card5.setBackground(new java.awt.Color(226, 203, 152));

        label5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label5.setText("jLabel1");

        javax.swing.GroupLayout card5Layout = new javax.swing.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card6.setBackground(new java.awt.Color(226, 203, 152));

        label6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label6.setText("jLabel1");

        javax.swing.GroupLayout card6Layout = new javax.swing.GroupLayout(card6);
        card6.setLayout(card6Layout);
        card6Layout.setHorizontalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label6, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );
        card6Layout.setVerticalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card7.setBackground(new java.awt.Color(226, 203, 152));

        label7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label7.setText("jLabel1");

        javax.swing.GroupLayout card7Layout = new javax.swing.GroupLayout(card7);
        card7.setLayout(card7Layout);
        card7Layout.setHorizontalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label7, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );
        card7Layout.setVerticalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card8.setBackground(new java.awt.Color(226, 203, 152));

        label8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label8.setText("jLabel1");

        javax.swing.GroupLayout card8Layout = new javax.swing.GroupLayout(card8);
        card8.setLayout(card8Layout);
        card8Layout.setHorizontalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label8, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );
        card8Layout.setVerticalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card9.setBackground(new java.awt.Color(226, 203, 152));

        label9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label9.setText("jLabel1");

        javax.swing.GroupLayout card9Layout = new javax.swing.GroupLayout(card9);
        card9.setLayout(card9Layout);
        card9Layout.setHorizontalGroup(
            card9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card9Layout.setVerticalGroup(
            card9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card10.setBackground(new java.awt.Color(226, 203, 152));

        label10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label10.setText("jLabel1");

        javax.swing.GroupLayout card10Layout = new javax.swing.GroupLayout(card10);
        card10.setLayout(card10Layout);
        card10Layout.setHorizontalGroup(
            card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card10Layout.setVerticalGroup(
            card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card11.setBackground(new java.awt.Color(226, 203, 152));

        label11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label11.setText("jLabel1");

        javax.swing.GroupLayout card11Layout = new javax.swing.GroupLayout(card11);
        card11.setLayout(card11Layout);
        card11Layout.setHorizontalGroup(
            card11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label11, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );
        card11Layout.setVerticalGroup(
            card11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label11, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );

        card12.setBackground(new java.awt.Color(226, 203, 152));

        label12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label12.setText("jLabel1");

        javax.swing.GroupLayout card12Layout = new javax.swing.GroupLayout(card12);
        card12.setLayout(card12Layout);
        card12Layout.setHorizontalGroup(
            card12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label12, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );
        card12Layout.setVerticalGroup(
            card12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label12, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );

        card13.setBackground(new java.awt.Color(226, 203, 152));

        label13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label13.setText("jLabel1");

        javax.swing.GroupLayout card13Layout = new javax.swing.GroupLayout(card13);
        card13.setLayout(card13Layout);
        card13Layout.setHorizontalGroup(
            card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label13, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );
        card13Layout.setVerticalGroup(
            card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label13, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addContainerGap())
        );

        card14.setBackground(new java.awt.Color(226, 203, 152));

        label14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label14.setText("jLabel1");

        javax.swing.GroupLayout card14Layout = new javax.swing.GroupLayout(card14);
        card14.setLayout(card14Layout);
        card14Layout.setHorizontalGroup(
            card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card14Layout.setVerticalGroup(
            card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label14, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addContainerGap())
        );

        card15.setBackground(new java.awt.Color(226, 203, 152));

        label15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label15.setText("jLabel1");

        javax.swing.GroupLayout card15Layout = new javax.swing.GroupLayout(card15);
        card15.setLayout(card15Layout);
        card15Layout.setHorizontalGroup(
            card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label15, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );
        card15Layout.setVerticalGroup(
            card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card16.setBackground(new java.awt.Color(226, 203, 152));

        label16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label16.setText("jLabel1");

        javax.swing.GroupLayout card16Layout = new javax.swing.GroupLayout(card16);
        card16.setLayout(card16Layout);
        card16Layout.setHorizontalGroup(
            card16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label16, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );
        card16Layout.setVerticalGroup(
            card16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label16, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtMisses.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txtMisses.setForeground(new java.awt.Color(104, 74, 10));
        txtMisses.setText("Misses:");

        txtMoves.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txtMoves.setForeground(new java.awt.Color(104, 74, 10));
        txtMoves.setText("Moves:");

        RestartBtn.setBackground(new java.awt.Color(221, 157, 66));
        RestartBtn.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        RestartBtn.setForeground(new java.awt.Color(104, 74, 10));
        RestartBtn.setText("Restart");
        RestartBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        RestartBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RestartBtnMouseClicked(evt);
            }
        });
        RestartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestartBtnActionPerformed(evt);
            }
        });

        txtPoints.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txtPoints.setForeground(new java.awt.Color(104, 74, 10));
        txtPoints.setText("  points ");

        Homebtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Homebtn.setIcon(new javax.swing.ImageIcon("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\home.png")); // NOI18N
        Homebtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Homebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HomebtnMouseClicked(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\star_3640184 (1).png")); // NOI18N

        txtTimer.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txtTimer.setForeground(new java.awt.Color(104, 74, 10));
        txtTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTimer.setText("Time Left:   s");

        txtLevel.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        txtLevel.setForeground(new java.awt.Color(104, 74, 10));
        txtLevel.setText("Level:");

        card17.setBackground(new java.awt.Color(226, 203, 152));
        card17.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        label17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label17.setText("jLabel1");

        javax.swing.GroupLayout card17Layout = new javax.swing.GroupLayout(card17);
        card17.setLayout(card17Layout);
        card17Layout.setHorizontalGroup(
            card17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        card17Layout.setVerticalGroup(
            card17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        card18.setBackground(new java.awt.Color(226, 203, 152));

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("jLabel1");

        javax.swing.GroupLayout card18Layout = new javax.swing.GroupLayout(card18);
        card18.setLayout(card18Layout);
        card18Layout.setHorizontalGroup(
            card18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label18, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );
        card18Layout.setVerticalGroup(
            card18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label18, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );

        card19.setBackground(new java.awt.Color(226, 203, 152));

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setText("jLabel1");

        javax.swing.GroupLayout card19Layout = new javax.swing.GroupLayout(card19);
        card19.setLayout(card19Layout);
        card19Layout.setHorizontalGroup(
            card19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card19Layout.createSequentialGroup()
                .addComponent(label19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card19Layout.setVerticalGroup(
            card19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card20.setBackground(new java.awt.Color(226, 203, 152));

        label20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label20.setText("jLabel1");

        javax.swing.GroupLayout card20Layout = new javax.swing.GroupLayout(card20);
        card20.setLayout(card20Layout);
        card20Layout.setHorizontalGroup(
            card20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        card20Layout.setVerticalGroup(
            card20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RestartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(txtLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(card13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(card4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(card7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(card8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(card11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(card12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(card15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(card16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtMoves, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtMisses, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 130, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Homebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(RestartBtn))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMisses, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMoves)
                        .addComponent(txtLevel)))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(card17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(card18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(card15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(card13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(card14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(50, 50, 50))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Homebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(443, 443, 443)
                .addComponent(txtTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RestartBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RestartBtnMouseClicked
        // TODO add your handling code here:
        restartGame();
    }//GEN-LAST:event_RestartBtnMouseClicked

    private void RestartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestartBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RestartBtnActionPerformed

    private void HomebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomebtnMouseClicked
        // TODO add your handling code here:
        Home home = new Home(); // Create an instance of SignUpPage
        home.setVisible(true); // Show SignUpPage
        this.dispose(); // Close LoginPage
    }//GEN-LAST:event_HomebtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameLevel2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameLevel2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameLevel2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameLevel2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        int currentLevel = 0;

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameLevel2(currentLevel).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Homebtn;
    private javax.swing.JButton RestartBtn;
    private javax.swing.JPanel card1;
    private javax.swing.JPanel card10;
    private javax.swing.JPanel card11;
    private javax.swing.JPanel card12;
    private javax.swing.JPanel card13;
    private javax.swing.JPanel card14;
    private javax.swing.JPanel card15;
    private javax.swing.JPanel card16;
    private javax.swing.JPanel card17;
    private javax.swing.JPanel card18;
    private javax.swing.JPanel card19;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card20;
    private javax.swing.JPanel card3;
    private javax.swing.JPanel card4;
    private javax.swing.JPanel card5;
    private javax.swing.JPanel card6;
    private javax.swing.JPanel card7;
    private javax.swing.JPanel card8;
    private javax.swing.JPanel card9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label11;
    private javax.swing.JLabel label12;
    private javax.swing.JLabel label13;
    private javax.swing.JLabel label14;
    private javax.swing.JLabel label15;
    private javax.swing.JLabel label16;
    private javax.swing.JLabel label17;
    private javax.swing.JLabel label18;
    private javax.swing.JLabel label19;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label20;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private javax.swing.JLabel txtLevel;
    private javax.swing.JLabel txtMisses;
    private javax.swing.JLabel txtMoves;
    private javax.swing.JLabel txtPoints;
    private javax.swing.JLabel txtTimer;
    // End of variables declaration//GEN-END:variables
}
