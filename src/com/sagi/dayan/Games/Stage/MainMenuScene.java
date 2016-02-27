package com.sagi.dayan.Games.Stage;

import com.sagi.dayan.Games.Elements.MenuBoxSprite;
import com.sagi.dayan.Games.Engine.GameEngine;
import com.sagi.dayan.Games.Utils.Utils;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by sagi on 2/24/16.
 */
public class MainMenuScene extends Scene {

    private MenuBoxSprite menuBox;
    private int menuItem;


    private final int MAX_ITEM_INDEX = 3, X_AXIS=560, ITEM_EXIT = 3, ITEM_SETTINGS = 2, ITEM_2P = 1, ITEM_1P = 0;
    private int[] axis = {370, 480, 590, 700};



    public MainMenuScene(int stageWidth, int stageHeight, GameEngine engine) {
        super(stageWidth, stageHeight, engine);
        menuItem = 0;
        menuBox = new MenuBoxSprite(X_AXIS, axis[menuItem], stageWidth, stageHeight, 0, "menu.jpg", 0, 425, 110); //bImage is the background... not trivial
        try {
            AudioPlayer.player.start(new AudioStream(Utils.getSoundResourceAsStream("intro_LowQuality.wav")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        menuBox.setLocY(axis[menuItem]);
        menuBox.update();


    }

    @Override
    public void render(JPanel p) {
        sceneImage = new BufferedImage(this.stageWidth, this.stageHeight, Image.SCALE_FAST);
        Graphics g = sceneImage.getGraphics();
        g.drawImage(menuBox.getbImage(), 0, 0, p);
        menuBox.drawSprite(g, p);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_UP:
                if(menuItem == 0)
                    menuItem = MAX_ITEM_INDEX;
                else
                    menuItem--;
                Utils.playSound("menuSelect.wav");
                break;
            case KeyEvent.VK_DOWN:
                if(menuItem == MAX_ITEM_INDEX)
                    menuItem = 0;
                else
                    menuItem++;
                Utils.playSound("menuSelect.wav");
                break;
            case KeyEvent.VK_ENTER:
                switch(menuItem){
                    case ITEM_1P:
                    case ITEM_2P:
                        Utils.playSound("menuItem.wav");
                        engine.startGame(menuItem+1);
                        break;
                    case ITEM_SETTINGS:
                        Utils.playSound("menuItem.wav");
                        engine.goToSettings();
                        break;
                    case ITEM_EXIT:
                        System.exit(0);
                        break;
                }

        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
