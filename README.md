# Memory Matching Game

This is a **Memory Matching Game** developed in **Java** using **NetBeans**. It is a classic game where players try to find matching pairs of cards by flipping them over, relying on their memory to recall where the matches are located.And It includes various features such as JSON-based data storage, user authentication, session management, API integration, and event-driven game mechanics.

## Features
- **Memory Matching Game Logic** - Flip cards, match pairs, and earn points.
- **JSON-Based Storage** - Player data, scores, and game configurations are stored in JSON.
- **User Authentication** - Sign-up and login system with encrypted credentials.
- **Session Management** - Ensures users stay logged in securely using local storage.
- **Banana API Integration** - Mini puzzle game after winning a level for bonus points.
- **Email Verification** - Gmail API integration for password reset and verification codes.
- **Event-Driven Programming** - Uses Swing GUI components for interactive gameplay.
- **Level-Based System** - Multiple levels with increasing difficulty.
- **Timer Feature** - Adds a countdown timer to complete levels within a time limit.

## Installation Guide
### Prerequisites
- **Java JDK 20+**
- **NetBeans IDE** (or any Java IDE)
- **Maven** (for dependency management)

### Setup Steps
1. **Clone or Download the Repository**
   ```sh
   git clone https://github.com/manuldi/Memory-Matching-Game.git
   cd Memory-Matching-Game
   ```

2. **Ensure JSON Data Files Exist**
   - The game stores user and game data in JSON files. After downloading, create the following JSON files inside `src/main/resources/data/`:
     - `players.json` (stores player credentials and scores)
     - `game_images.json` (stores image paths for different levels)
     - `reset_codes.json` (stores temporary email verification codes)
     
   - Images Folder: The game uses images stored in the `images/` folder. Ensure all images exist, or else the game will not display them properly.
     - If images are missing, download and place them in `images/` before running the game.

3. **Run the Game in NetBeans**
   - Open the project in **NetBeans**.
   - Ensure all dependencies are correctly installed.
   - Run the `LoginPage.java` file to start the game.

## Dependencies
This project uses the following libraries:
- **JavaMail API** - For sending emails (password reset, verification)
- **JSON-Simple** - For reading/writing JSON data
- **Banana API** - For the bonus puzzle game
- **Swing & AWT** - For UI design and event-driven programming

## Troubleshooting

1. **Game Crashing on Startup?**
   - Check if the required JSON files exist in `src/main/resources/data/`.
   - Ensure all dependencies are installed correctly.

2. **Images Not Loading?**
   - Verify that `images.json` contains valid paths to image files.
   - Ensure image files are located in the expected directory.

## Future Improvements
- **Multiplayer Mode** - Compete with friends in real-time.
- **Mobile Version** - Adapt the game for Android using JavaFX or React Native.

---
🎮 **Enjoy the Game!** 🚀
