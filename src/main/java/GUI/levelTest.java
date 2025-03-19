/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Code.DBManager;
import GUI.ProfileDialog;
import GUI.FinishDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author manul
 */
public class levelTest extends javax.swing.JFrame {
    
    private JPanel[] cards;
    private JLabel[] labels;
    private ArrayList<String> shuffledImages;
    private Map<JLabel, String> cardMap; // Maps JLabel to its image
    private JPanel firstCard, secondCard;
    private JLabel firstLabel, secondLabel;
    private int firstIndex, secondIndex;
    private boolean isFlipping = false;
    private int currentLevel = 0; // Default level
    private int misses = 0;
    private int moves = 0;
    private int matchedPairs = 0;
    private static int points = 0;
    // List to store completed levels for the current user
    private int completedLevels = 0;
    private static final String FILE_PATH = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\players.json"; // Adjust the file path
    private static final String FILE_PATH_2 = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\images.json";
    /**
     * Creates new form levelTest
     */
    public levelTest(int currentLevel) {
        initComponents(); // Auto-generated GUI code
        this.currentLevel = currentLevel;
//        loadImagesFromJSON(currentLevel); // Load level 1 images initially
//        setupGame();
//        levelCompleted(currentLevel);
          
    }
    
    // Method to update points
    public static void setPoints(int newPoints) {
        points = newPoints;
    }

    // Method to get points
    public static int getPoints() {
        return points;
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
                JSONArray imagesArray = levelData.getJSONArray("images");
                for (int j = 0; j < imagesArray.length(); j++) {
                    shuffledImages.add(imagesArray.getString(j));
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
    
    // Setup game UI (NetBeans will generate initComponents method)
    private void setupGame() {
        
//        if (shuffledImages == null || shuffledImages.isEmpty()) {
//        System.err.println("Error: shuffledImages is empty!");
//        return; // Prevent further execution
//        }
        cards = new JPanel[]{card1, card2, card3, card4, card5, card6, card7, card8,card9, card10, card11, card12, card13, card14, card15, card16};
        labels = new JLabel[]{label1, label2, label3, label4, label5, label6, label7, label8,label9, label10, label11, label12, label13, label14, label15, label16};

        cardMap = new HashMap<>(); // Store JLabel -> Image mapping

        // Assign event handlers
        for (int i = 0; i < cards.length; i++) {
            final int index = i;
            labels[i].setText("?"); // Set default text
            cardMap.put(labels[i], shuffledImages.get(i)); // Store mapping

            cards[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    flipCard(labels[index]);
                }
            });
        }
    }
    
    private void flipCard(JLabel clickedLabel) {
        if (isFlipping || !clickedLabel.getText().equals("?")) return;

        // Load and set image inside JLabel
        String imagePath = cardMap.get(clickedLabel);
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        clickedLabel.setIcon(new ImageIcon(img));
        clickedLabel.setText(""); // Remove "?"

        if (firstLabel == null) {
            firstLabel = clickedLabel;
        } else {
            secondLabel = clickedLabel;
            isFlipping = true;

            // Delay before checking match
            Timer timer = new Timer(1000, this::checkMatch);
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
//            levelTest.setPoints(points);
//            updateScore(points);
            
        } else {
            firstLabel.setIcon(null);
            secondLabel.setIcon(null);
            firstLabel.setText("?");
            secondLabel.setText("?");
            misses++;
            txtMisses.setText("Misses: "+misses);
        }
        
//        if(points == 80)
//        {
//            FinishDialog finish = new FinishDialog(this, true);
//            finish.setLocationRelativeTo(this); // Center the dialog 
//            finish.setVisible(true); // Show pop-up
//        }
        

        firstLabel = null;
        secondLabel = null;
        isFlipping = false;
        
    }
    
    

    // Switch levels dynamically
    private void switchLevel(int level) {
        currentLevel = level;
        loadImagesFromJSON(level);
        setupGame();
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
            labels[i].setText("?");
            labels[i].setIcon(null);
            cards[i].setEnabled(true);
            cardMap.put(labels[i], shuffledImages.get(i)); // Update map
        }
    }
    
    public void levelCompleted(int levelNumber) {
        
    String username = LoginPage.getprofilename();
    JSONArray users = DBManager.readJSONFile(FILE_PATH);

    for (int i = 0; i < users.length(); i++) {
        JSONObject user = users.getJSONObject(i);
        if (user.getString("username").equals(username)) { // Find user
            completedLevels = user.getInt("completedLevel"); // Default 0

            if (levelNumber > completedLevels) { // Only update if higher
                user.put("completedLevel", levelNumber);
                DBManager.writeJSONFile("users.json", users); // Save changes
                System.out.println("Completed Level Updated: " + levelNumber);
            }
            
            break;
            }
        }
    }
    
    // Update Score in JSON After Winning
    public void updateScore(int earnedScore) {
    JSONArray users = DBManager.readJSONFile(FILE_PATH);
    String username = LoginPage.getprofilename();

    for (int i = 0; i < users.length(); i++) {
        JSONObject user = users.getJSONObject(i);
        if (user.getString("username").equals(username)) { // Find user
            int currentScore = user.getInt("score"); // Default to 0 if missing
            user.put("score", currentScore + earnedScore); // Add earned score
            // Save updated data back to JSON
            DBManager.writeJSONFile("users.json", users);
        }
        
        break;
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(249, 243, 202));

        card1.setBackground(new java.awt.Color(226, 203, 152));
        card1.setForeground(new java.awt.Color(255, 255, 255));

        label1.setText("jLabel1");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card2.setBackground(new java.awt.Color(226, 203, 152));

        label2.setText("jLabel1");

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card3.setBackground(new java.awt.Color(226, 203, 152));

        label3.setText("jLabel1");

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        card4.setBackground(new java.awt.Color(226, 203, 152));

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
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        card5.setBackground(new java.awt.Color(226, 203, 152));

        label5.setText("jLabel1");

        javax.swing.GroupLayout card5Layout = new javax.swing.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label5, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );

        card6.setBackground(new java.awt.Color(226, 203, 152));

        label6.setText("jLabel1");

        javax.swing.GroupLayout card6Layout = new javax.swing.GroupLayout(card6);
        card6.setLayout(card6Layout);
        card6Layout.setHorizontalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card6Layout.setVerticalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        card7.setBackground(new java.awt.Color(226, 203, 152));

        label7.setText("jLabel1");

        javax.swing.GroupLayout card7Layout = new javax.swing.GroupLayout(card7);
        card7.setLayout(card7Layout);
        card7Layout.setHorizontalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        card7Layout.setVerticalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        card8.setBackground(new java.awt.Color(226, 203, 152));

        label8.setText("jLabel1");

        javax.swing.GroupLayout card8Layout = new javax.swing.GroupLayout(card8);
        card8.setLayout(card8Layout);
        card8Layout.setHorizontalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card8Layout.setVerticalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        card9.setBackground(new java.awt.Color(226, 203, 152));

        label9.setText("jLabel1");

        javax.swing.GroupLayout card9Layout = new javax.swing.GroupLayout(card9);
        card9.setLayout(card9Layout);
        card9Layout.setHorizontalGroup(
            card9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        card9Layout.setVerticalGroup(
            card9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        card10.setBackground(new java.awt.Color(226, 203, 152));

        label10.setText("jLabel1");

        javax.swing.GroupLayout card10Layout = new javax.swing.GroupLayout(card10);
        card10.setLayout(card10Layout);
        card10Layout.setHorizontalGroup(
            card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card10Layout.setVerticalGroup(
            card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card10Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        card11.setBackground(new java.awt.Color(226, 203, 152));

        label11.setText("jLabel1");

        javax.swing.GroupLayout card11Layout = new javax.swing.GroupLayout(card11);
        card11.setLayout(card11Layout);
        card11Layout.setHorizontalGroup(
            card11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card11Layout.setVerticalGroup(
            card11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card11Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        card12.setBackground(new java.awt.Color(226, 203, 152));

        label12.setText("jLabel1");

        javax.swing.GroupLayout card12Layout = new javax.swing.GroupLayout(card12);
        card12.setLayout(card12Layout);
        card12Layout.setHorizontalGroup(
            card12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        card12Layout.setVerticalGroup(
            card12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card12Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(label12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card13.setBackground(new java.awt.Color(226, 203, 152));

        label13.setText("jLabel1");

        javax.swing.GroupLayout card13Layout = new javax.swing.GroupLayout(card13);
        card13.setLayout(card13Layout);
        card13Layout.setHorizontalGroup(
            card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card13Layout.setVerticalGroup(
            card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card13Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        card14.setBackground(new java.awt.Color(226, 203, 152));

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
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        card15.setBackground(new java.awt.Color(226, 203, 152));

        label15.setText("jLabel1");

        javax.swing.GroupLayout card15Layout = new javax.swing.GroupLayout(card15);
        card15.setLayout(card15Layout);
        card15Layout.setHorizontalGroup(
            card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card15Layout.setVerticalGroup(
            card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card15Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        card16.setBackground(new java.awt.Color(226, 203, 152));

        label16.setText("jLabel1");

        javax.swing.GroupLayout card16Layout = new javax.swing.GroupLayout(card16);
        card16.setLayout(card16Layout);
        card16Layout.setHorizontalGroup(
            card16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        card16Layout.setVerticalGroup(
            card16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card16Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtMisses.setText("Misses:");

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

        txtPoints.setText("  points ");

        Homebtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Homebtn.setIcon(new javax.swing.ImageIcon("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\home.png")); // NOI18N
        Homebtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Homebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HomebtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(card5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(card13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(card9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(card6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(card14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(card10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(RestartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(txtMoves, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMisses, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(162, 162, 162)
                        .addComponent(txtPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Homebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMisses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMoves, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(RestartBtn)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(Homebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(card4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(card10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(card9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(card15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(card16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(card14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(card13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(txtPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(levelTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(levelTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(levelTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(levelTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        int currentLevel = 0;
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new levelTest(currentLevel).setVisible(true);
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
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card3;
    private javax.swing.JPanel card4;
    private javax.swing.JPanel card5;
    private javax.swing.JPanel card6;
    private javax.swing.JPanel card7;
    private javax.swing.JPanel card8;
    private javax.swing.JPanel card9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label11;
    private javax.swing.JLabel label12;
    private javax.swing.JLabel label13;
    private javax.swing.JLabel label14;
    private javax.swing.JLabel label15;
    private javax.swing.JLabel label16;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private javax.swing.JLabel txtMisses;
    private javax.swing.JLabel txtMoves;
    private javax.swing.JLabel txtPoints;
    // End of variables declaration//GEN-END:variables
}
