/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;


import Code.UserAuth;
import GUI.CookiesDialog;
import GUI.ProfileDialog;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.swing.JOptionPane;


/**
 *
 * @author manul
 */
public class LoginPage extends javax.swing.JFrame {
    
    public static String profilename = "";
    
    private boolean checkcookies = false;
    
    private String email;

    

    /**
     * Creates new form LoginPage
     */
    public LoginPage() {
        initComponents();
        
//        CookiesDialog.checkStatus();
        if(CookiesDialog.disabledStatus && ProfileDialog.check == 0)
        {
            checkAutoLogin();
        }
//          CookiesDialog.checkAutoLogin();
        
        
        
    }
    
//    private boolean checkStatus()
//    {
//        return UserAuth.autoLogin();
//    }
    
    // Check if there's a saved session for auto-login
    private void checkAutoLogin() {
        if (UserAuth.autoLogin()) {
            JOptionPane.showMessageDialog(this, "Auto-login successful!", "Welcome Back", JOptionPane.INFORMATION_MESSAGE);
            Home home = new Home();
            home.setVisible(true);
            
//            checkcookies = false;
            
            // Ensure LoginPage closes properly
            javax.swing.SwingUtilities.invokeLater(() -> {
            this.setVisible(false);
            this.dispose();
            });
        }
    }


        // Check for 'user' cookie
//        Cookie[] cookies = getCookies();  // You need to implement this method to get cookies from the browser or system
//        if (cookies != null) {
//            String savedUser = null;
//            for (Cookie cookie : cookies) {
//                if ("user".equals(cookie.getName())) {
//                    savedUser = cookie.getValue();
//                    break;
//                }
//            }
//            if (savedUser != null) {
//                // Auto-login user
//                loginUser(savedUser);  // Perform login logic with the cookie user value
//            }
//        }
//    }
    
//    private void checkAutoLogin() 
//    {
//        String savedUser = UserAuth.readCookie();
//        if (savedUser != null) {
//            SessionManager.getInstance().setUser(savedUser);
//            JOptionPane.showMessageDialog(this, "Welcome back, " + savedUser + "!");
//            this.dispose();
//            Home home = new Home();
//            home.setVisible(true);
//            
//            LoginPage.setprofilename(savedUser);
//            
//            checkcookies = true;
//            
//        }
//    }
    
    // Method to simulate login using the servlet's login logic
//    private void loginUser(String username) {
//        try {
//            // Simulate sending a POST request to the LoginServlet
//            String url = "http://localhost:8080/GameDemo/LoginServlet";  // Update with your actual servlet URL
//            URL  obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//            // Set method and headers
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            con.setDoOutput(true);
//
//            // Prepare POST data (username and password for auto-login)
//            String urlParameters = "username=" + username;  // Replace 'dummy' with real password if required
//
//            // Send POST request
//            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
//                wr.writeBytes(urlParameters);
//                wr.flush();
//            }
//
//            // Read the response
//            int responseCode = con.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // If login is successful, show welcome message
//                JOptionPane.showMessageDialog(this, "Auto-login successful!", "Welcome Back", JOptionPane.INFORMATION_MESSAGE);
//                Home home = new Home();
//                home.setVisible(true);
//                this.dispose();  // Close login page
//            } else {
//                // Handle failed login attempt
//                JOptionPane.showMessageDialog(this, "Auto-login failed", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "An error occurred while auto-login", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
    // Add any necessary methods for cookie management, such as retrieving cookies, saving them, etc.
//    private Cookie[] getCookies() {
//        // You need to implement this to get the current cookies from the browser or session.
//        return new Cookie[0]; // Placeholder, adapt this to your environment
//    }
    
    public static void setprofilename(String name)
    {
        profilename = name;
    }
    
    public static String getprofilename()
    {
        return profilename;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        LoginBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        SignupNavigate = new javax.swing.JLabel();
        ForgotBtn = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(249, 243, 202));

        jLabel1.setFont(new java.awt.Font("Poor Richard", 1, 64)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(104, 75, 10));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Memory");

        jLabel2.setFont(new java.awt.Font("Poor Richard", 1, 64)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(104, 74, 10));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Matching Game");

        jPanel3.setBackground(new java.awt.Color(226, 203, 152));

        txtUsername.setBackground(new java.awt.Color(221, 157, 66));
        txtUsername.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(204, 204, 204));
        txtUsername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsername.setText("Enter Your Email");
        txtUsername.setBorder(null);
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsernameFocusLost(evt);
            }
        });
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        txtPassword.setBackground(new java.awt.Color(221, 157, 66));
        txtPassword.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(204, 204, 204));
        txtPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPassword.setText("Enter Password");
        txtPassword.setBorder(null);
        txtPassword.setEchoChar('\u0000');
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        LoginBtn.setBackground(new java.awt.Color(221, 157, 66));
        LoginBtn.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        LoginBtn.setForeground(new java.awt.Color(104, 74, 10));
        LoginBtn.setText("Login");
        LoginBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LoginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginBtnMouseClicked(evt);
            }
        });
        LoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginBtnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Don't have a account? ");

        SignupNavigate.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        SignupNavigate.setText("Sign up");
        SignupNavigate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SignupNavigate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SignupNavigateMouseClicked(evt);
            }
        });

        ForgotBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ForgotBtn.setText("Forget Password?");
        ForgotBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ForgotBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ForgotBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(SignupNavigate, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 166, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LoginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ForgotBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPassword)
                            .addComponent(txtUsername))
                        .addGap(56, 56, 56))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ForgotBtn)
                .addGap(13, 13, 13)
                .addComponent(LoginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SignupNavigate, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\pngtree-cute-cat-comic-cute-animal-comic-small-animal-cartoon-decoration-png-image_2296892-removebg-preview.png")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\50f97bce611b9709ef562fa46d743967-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 62, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void LoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LoginBtnActionPerformed

    private void SignupNavigateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignupNavigateMouseClicked
        // TODO add your handling code here:
        SignupPage signUp = new SignupPage(); // Create an instance of SignUpPage
        signUp.setVisible(true); // Show SignUpPage
        this.dispose(); // Close LoginPage
    }//GEN-LAST:event_SignupNavigateMouseClicked

    private void LoginBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoginBtnMouseClicked
        // TODO add your handling code here:
//        Home home = new Home(); // Create an instance of SignUpPage
//        home.setVisible(true); // Show SignUpPage
//        this.dispose(); // Close LoginPage
        
        checkcookies = true;
        
            email = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (email==null && password==null) {
               JOptionPane.showMessageDialog(this, "Please enter your username and password before login!"); 
            }
            else if (UserAuth.login(email, password))
            {
                profilename = UserAuth.Profile(email, password); //pass the value through userAuth profile method
                LoginPage.setprofilename(profilename);
                
                JOptionPane.showMessageDialog(this, "Login Successful!");
                Home home = new Home(); // Create an instance of HomePage
                home.setVisible(true); // Show Login page
                this.dispose(); // Close signup window
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }  
            
        
    }//GEN-LAST:event_LoginBtnMouseClicked

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        // TODO add your handling code here:
        if(txtUsername.getText().equals("Enter Your Email"))
        {
            txtUsername.setText("");
        }
    }//GEN-LAST:event_txtUsernameFocusGained

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusLost
        // TODO add your handling code here:
        if(txtUsername.getText().equals(""))
        {
            txtUsername.setText("Enter Your Email");
        }
    }//GEN-LAST:event_txtUsernameFocusLost

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        // TODO add your handling code here:
        if(txtPassword.getText().equals("Enter Password"))
        {
            txtPassword.setText("");
        }
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        // TODO add your handling code here:
        if(txtPassword.getText().equals(""))
        {
            txtPassword.setText("Enter Password");
        }
    }//GEN-LAST:event_txtPasswordFocusLost

    private void ForgotBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ForgotBtnMouseClicked
        // TODO add your handling code here:
        
//        JOptionPane.showMessageDialog(this, "Login Successful!");
        ResetPassword reset = new ResetPassword(); // Create an instance of HomePage
        reset.setVisible(true); // Show Login page
        this.dispose(); // Close signup window
    }//GEN-LAST:event_ForgotBtnMouseClicked

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
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ForgotBtn;
    private javax.swing.JButton LoginBtn;
    private javax.swing.JLabel SignupNavigate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
