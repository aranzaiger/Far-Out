package com.sagi.dayan.Games.Stage;

/**
 * Represents a Scene in the game (i.e. main menu, stage etc.)
 */

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sagi.dayan.Games.Engine.GameEngine;

public abstract class Scene extends MouseAdapter implements KeyListener{

    protected int stageWidth, stageHeight;
    protected BufferedImage sceneImage;
    protected GameEngine engine;

    public Scene (int stageWidth, int stageHeight, GameEngine engine) {
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;
        this.engine = engine;
    }

    //get dimentions
    public int getStageWidth() {
        return stageWidth;
    }
    public int getStageHeight() {
        return stageHeight;
    }

    public abstract void update ();

    public abstract void render(JPanel p);

    public BufferedImage getSceneImage () {
        return sceneImage;
    }



}
