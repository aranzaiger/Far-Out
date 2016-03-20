package com.sagi.dayan.Games.Stage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.sagi.dayan.Games.Engine.GameEngine;
import com.sagi.dayan.Games.Utils.Utils;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

/**
 * Settings menu - currently controls menu.
 * not possible to change keys yet
 */
public class SettingsMenuScene extends Scene {
    private BufferedImage background;


    public SettingsMenuScene(int stageWidth, int stageHeight, GameEngine engine) {
        super(stageWidth, stageHeight, engine);
        try {
            background = ImageIO.read(Utils.getImageResourceAsURL("Settigns_menu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(JPanel p) {
        sceneImage = new BufferedImage(this.stageWidth, this.stageHeight, Image.SCALE_FAST);
        Graphics g = sceneImage.getGraphics();
        g.drawImage(background, 0,0,p);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                Utils.playSound("menuItem.wav");
                engine.goToMenu();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
