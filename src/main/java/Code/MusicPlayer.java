/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.io.File;
import javax.sound.sampled.*;

/**
 *
 * @author manul
 */
public class MusicPlayer {
    
    private Clip clip;

    // Constructor to load and play music
    public MusicPlayer(String musicFilePath) {
        try {
            File musicFile = new File(musicFilePath);
            if (!musicFile.exists()) {
                System.out.println("Music file not found: " + musicFilePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to start playing music
    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    // Method to stop music
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
    
}
