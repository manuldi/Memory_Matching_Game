/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;
import Code.GmailService;
import GUI.LoginPage;
import org.json.JSONArray; //JSON Processing API to reads and writes user data.
import org.json.JSONObject; //JSON Processing API to reads and writes user data.
import java.io.*; //Java File I/O API to reads and writes session data in cookies.
import java.nio.file.Files; //Java File I/O API to reads and writes session data in cookies.
import java.nio.file.Paths;//Java File I/O API to reads and writes session data in cookies.
//import java.rmi.Naming;
//import java.rmi.RemoteException;
//import java.rmi.server.RemoteServer;
//import java.util.List;
//import Code.LoginImplementation;
import GUI.CookiesDialog;
import java.security.MessageDigest; //Java Cryptography API to used for hashing and salting passwords.
import java.security.SecureRandom; //Java Cryptography API to used for hashing and salting passwords.
import java.text.SimpleDateFormat; //Date API to Handles cookie expiration.
import java.util.Base64; //Java Cryptography API to used for hashing and salting passwords.
import java.util.regex.Pattern;
import java.time.Instant; //Date API to Handles cookie expiration.
import java.util.Date; //Date API to Handles cookie expiration. 

/**
 *
 * @author manul
 */


public class UserAuth {
    private static final String FILE_PATH = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\players.json";
    
    private static final String COOKIE_FILE = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\cookies.json";
    
    private static final String CODES_FILE = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\resetCodes.json";
    
    private static String profilename;
    
    private static int completedlevels;
    
    private static int score;
    
    private static String sessionCookie; // Store session cookie
    
    private static final int COOKIE_EXPIRATION_DAYS = 7;
    
    public static String sessionToken = null;
    
    public static String name = "";
    
    
    // Generate a random salt //adds randomness to make hash cracking harder
    public static String generateSalt() {
        SecureRandom random = new SecureRandom(); //generates a random 16-byte salt.
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt); //converts it into a string
    }

    // Hash password with salt using SHA-256 //converts a password into an unreadable format.
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); //SHA-256 is a cryptographic hash function.
            md.update(salt.getBytes()); //Adds salt to the hashing process.
            byte[] hashedPassword = md.digest(password.getBytes()); //Hashes the password.
            return Base64.getEncoder().encodeToString(hashedPassword); //Converts hashed bytes into a readable format.
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Sign up Function
    public static boolean signUp(String name, String email, String password) 
    {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);

        for (int i = 0; i < users.length(); i++) 
        {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) 
            {
                return false; // Email already exists
            }
        }

        // Generate salt and hash password
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        JSONObject newUser = new JSONObject();
        newUser.put("Name", name);
        newUser.put("email", email);
        newUser.put("salt", salt);
        newUser.put("password", hashedPassword);
        newUser.put("score", 0);
        newUser.put("completedLevel", 0);

        users.put(newUser);
        DBManager.writeJSONFile(FILE_PATH, users);
        return true;
    }


    // LOGIN FUNCTION
    
    public static boolean login(String email, String password) {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
//            if (user.getString("username").equals(username)) {
                name = user.getString("Name");
                String storedSalt = user.getString("salt");
                String storedHash = user.getString("password");
                String hashedInputPassword = hashPassword(password, storedSalt);
                
                if (storedHash.equals(hashedInputPassword)) { //Compares stored hashed password with input password (hashed with stored salt)
                    sessionToken = generateSalt(); //generates a sessionToken for authentication.
//                    saveCookie(name);
                    System.out.println("Login Successful.");
                    return true;
                }
            }
        }
        return false; // Login failed
    }
    
    public static boolean sendPasswordResetCode(String email) 
    {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);

    for (int i = 0; i < users.length(); i++) {
        JSONObject user = users.getJSONObject(i);
        if (user.getString("email").equals(email)) 
        {
            String resetCode = generateResetCode();
                try {
                    // Send the reset code via email
                        GmailService.sendEmail(email, "Memory Matching Game","Thanks for joining us.We're glad to have you on board."
                                + "\nYou're receiving this email because you have registered in our game.\n"
                        + "\nThe verification code is only valid for the next 15 minutes.\n\n\n" 
                        + resetCode+ "\b\n\n\n"+"Thanks,\n" +"Admin.");
                
//                        GmailService.sendEmail(email, "Memory Matching Game", "Thanks for joining us.We're glad to have you on board.\nYou're receiving this email because you have registered in our game.\n"
//                        + "\nThe verification code is only valid for the next 15 minutes.\n\n\n" 
//                        + resetCode+ "\b\n\n\n"+"Thanks,\n" +"Admin.");
                        saveResetCode(email, resetCode);
                        return true;
                }   catch (Exception e) 
                {
                    System.out.println("Not send the email");
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    
    private static String generateResetCode() 
    {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }


    
    // Save or update the reset code for a user
    private static void saveResetCode(String email, String resetCode) {
        JSONArray resetCodes = DBManager.readJSONFile(CODES_FILE);

        // Check if an entry for the email already exists
        boolean found = false;
        for (int i = 0; i < resetCodes.length(); i++) {
            JSONObject entry = resetCodes.getJSONObject(i);
            if (entry.getString("email").equals(email)) {
                entry.put("code", resetCode); // Update existing code
                found = true;
                break;
            }
        }

        // If no existing entry, create a new one
        if (!found) {
            JSONObject newEntry = new JSONObject();
            newEntry.put("email", email);
            newEntry.put("code", resetCode);
            resetCodes.put(newEntry);
        }

        // Write back to file
        DBManager.writeJSONFile(CODES_FILE, resetCodes);
}
    
    // Delete reset code after successful password change
    private static void deleteResetCode(String email) 
    {
        JSONArray resetCodes = DBManager.readJSONFile(CODES_FILE);
        JSONArray updatedCodes = new JSONArray();

        for (int i = 0; i < resetCodes.length(); i++) 
        {
            JSONObject entry = resetCodes.getJSONObject(i);
            if (!entry.getString("email").equals(email)) {
            updatedCodes.put(entry); // Keep only codes not related to the email
            }
        }

    // Write updated codes back to the file
    DBManager.writeJSONFile(CODES_FILE, updatedCodes);
    }

    // Resend code function
    public static boolean resendCode(String email) {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);
        boolean userExists = false;

        // Check if the email exists in the users list
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                userExists = true;
                break;
            }
        }

        if (!userExists) {
            System.out.println("Email not found.");
            return false; // Email doesn't exist
        }

        // Generate a new reset code
        String newResetCode = generateResetCode();

        try {
            // Send email with the new code
            GmailService.sendEmail(email, "Memory Matching Game - Password Reset Code (Resend)", "Your new reset code is: " + newResetCode);

            // Save the new reset code in the file
            saveResetCode(email, newResetCode);

            return true;
        } catch (Exception e) {
            System.out.println("Failed to resend the reset code.");
            e.printStackTrace();
            return false;
        }
    }
    

    
    // Modify the resetPassword function to remove the used reset code
    public static void resetPassword(String email, String code, String newPassword) 
    {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);

        for (int j = 0; j < users.length(); j++) 
        {
            JSONObject user = users.getJSONObject(j);
            if (user.getString("email").equals(email)) 
            {
                    String salt = generateSalt();
                    user.put("salt", salt);
                    user.put("password", hashPassword(newPassword, salt));

                    DBManager.writeJSONFile(FILE_PATH, users);

                    // Delete the used reset code
                    // deleteResetCode(email);

            }
        }
    }
    
    public static void saveCookie(String username) {

        try {
            JSONObject cookieData = new JSONObject();
            cookieData.put("username", username);
            cookieData.put("sessionToken", sessionToken);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String expiryDate = sdf.format(new Date(System.currentTimeMillis() + (COOKIE_EXPIRATION_DAYS * 86400000L)));
            cookieData.put("expiry", expiryDate);
            
            JSONArray cookies = new JSONArray();
            cookies.put(cookieData);
            Files.write(Paths.get(COOKIE_FILE), cookies.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean autoLogin() {
        
        try {
            if (!Files.exists(Paths.get(COOKIE_FILE))) return false;
            String content = new String(Files.readAllBytes(Paths.get(COOKIE_FILE)));
            JSONArray cookies = new JSONArray(content);
            
            if (cookies.length() == 0) return false;
            JSONObject cookie = cookies.getJSONObject(0);
            
            String expiryDateStr = cookie.getString("expiry");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expiryDate = sdf.parse(expiryDateStr);
            
            sessionToken = cookie.getString("sessionToken");
            
            name = cookie.getString("username");
            
            if (new Date().after(expiryDate)) {
                deleteExpiredCookies();
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    private static void deleteExpiredCookies() {
        try {
            Files.deleteIfExists(Paths.get(COOKIE_FILE));
            System.out.println("Expired cookies deleted.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // DISPLAY NAME FUNCTION
    public static String Profile(String email, String password) {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (login(email, password)) {
                profilename = user.getString("Name");
            }
        }
        return profilename;
        
    }
    
    public static void addcompleteLevels (String username, int levels)
    {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);
        
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("Name").equals(username)) 
            { // Find user
                int completedLevels = user.getInt("completedLevel"); // Default 0

                if (levels > completedLevels) 
                { // Only update if higher
                    user.put("completedLevel", levels);
                    DBManager.writeJSONFile(FILE_PATH, users); // Save changes
                    System.out.println("Completed Level Updated: " + levels);
                }

           }
        }

    }
    
    public static void updateScore(String username, int earnedScore) 
    {
        JSONArray users = DBManager.readJSONFile(FILE_PATH);
    

        for (int i = 0; i < users.length(); i++) 
        {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("Name").equals(username)) 
            { // Find user
                int currentScore = user.getInt("score"); // Default to 0 if missing
                user.put("score", currentScore + earnedScore);// Add earned score
                
                // Save updated data back to JSON
                DBManager.writeJSONFile(FILE_PATH, users);
                System.out.println("Score Updated for " + username + ": " + (currentScore + earnedScore)); // Debug
            }

        }

    }

    
    public static void logout() {
        
        if(sessionToken != null)
        {

            sessionToken = null;
            CookiesDialog.disabledStatus = false;
            System.out.println("Logged out successfully!");
        }
        
    }

}
