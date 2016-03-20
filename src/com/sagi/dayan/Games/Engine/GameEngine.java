package com.sagi.dayan.Games.Engine;

/**
 * Created by sagi on 2/8/16.
 */

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sagi.dayan.Games.Stage.*;
import com.sagi.dayan.Games.Stage.MainMenuScene;
import com.sagi.dayan.Games.Stage.Scene;
import com.sagi.dayan.Games.Stage.SettingsMenuScene;
import com.sagi.dayan.Games.Stage.Stage;
import com.sagi.dayan.Games.Utils.Utils;
import com.sagi.dayan.Games.Utils.WaveConfigs;



public class GameEngine {
    private final int CREDIT_TIME = 10;
    public boolean gameOn , gameOver, isFirstGame;
    private JFrame frame;
    private int pWidth, pHeight, numOfPlayers;	//panel dimensions
    private Random r;
    private Stage stage;
    private Scene scene;
    private int p1CreditTime, p2CreditTime, creditTickTime = 1;
    public static final int PLAYER_WIDTH = 120, PLAYER_HEIGHT = 120;
    public static final int UP=0,RIGHT=1,DOWN=2, LEFT=3, FIRE=4, USE_CREDIT=5;
    public int p1HighScore, p2HighScore;

    private int[] p1Controlles = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_K, KeyEvent.VK_J};
    private int[] p2Controlles = {KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_Q, KeyEvent.VK_Z};

    private int p1Lives, p2Lives, p1Health, p2Health, credits, p1Score, p2Score;

    private long lastP1CreditTick, lastP2CreditTick;

    private Font gameFont;

    private WaveConfigs waveConfigs;
    private int currentLevel;

    public GameEngine(int width, int height, Stage stage){
    	p1HighScore =  p2HighScore = 0;
        this.isFirstGame = true;
        this.gameOver = true;
        this.pWidth = width;
        this.pHeight = height;
        this.stage = stage;
        currentLevel = -1;
        goToMenu();
        r = new Random();
        try{
            gameFont = Font.createFont(Font.TRUETYPE_FONT,Utils.getFontStream("transformers.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
            gameFont = null;
        } catch (IOException e) {
            e.printStackTrace();
            gameFont = null;
        }
        this.waveConfigs = new WaveConfigs();
        startNewGame();

    }


    private void resetPlayerHealth(int i){
        if (i==0){
            p1Health = 100;
        }
        else{
            p2Health = 100;
        }
    }
 




    /**
     * initialize and reset vars and timers to "new game" configuration.
     */
    private void startNewGame(){
        this.gameOn = true;
        this.currentLevel = -1;
        initGame();
    }

    /**
     * Setup all actors in the game to a new game - reset timer
     */
    private void initGame(){
        resetPlayerHealth(0);
        resetPlayerHealth(1);
        p1Score = p2Score = 0;
        credits = 3;
        p1Lives = 1;
        p2Lives = 1;
    }

    public int getP1CreditTime() {
        return p1CreditTime;
    }

    public int getP2CreditTime() {
        return p2CreditTime;
    }

    public WaveConfigs getWaveConfigs() {
        return waveConfigs;
    }




    /**
     * returns gameOver flag
     * @return
     * boolean
     */
    public boolean isGameOver(){
        return this.gameOver;
    }





    /**
     * Update all sprites, including collision handling.
     */
    public void update(){
        long now = System.currentTimeMillis();
        if(now - lastP1CreditTick >= creditTickTime * 1000){
            p1CreditTime--;
            lastP1CreditTick = now;
        }
        if(now - lastP2CreditTick >= creditTickTime * 1000){
            p2CreditTime--;
            lastP2CreditTick = now;
        }
        scene.update();
    }

    public void render(JPanel p) {
        scene.render(p);
    }

    public BufferedImage getScene() {
        return scene.getSceneImage();
    }

    public void startGame(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
        startNewGame();
        changeLevel();
    }

    public void changeLevel(){
    	System.out.println("current level: "+currentLevel);
        currentLevel++;
        stage.removeMouseListener(scene);
        stage.removeKeyListener(scene);
        switch (currentLevel){
            case 0:
                scene = new FirstStage(pWidth, pHeight, numOfPlayers, this, "-= STAGE 1.0 =-", new int[]{5, 5});
                break;
            case 1:
                scene = new SecondStage(pWidth, pHeight, numOfPlayers, this, "-= STAGE 2.0 =-", new int[]{5, 5,5});
                break;
            case 2:
                scene = new ThirdStage(pWidth, pHeight, numOfPlayers, this, "-= STAGE 3.0 =-", new int[]{5, 0,2,8});
                break;
            case 3:
                scene = new FourthStage(pWidth, pHeight, numOfPlayers, this, "-= STAGE 4.0 =-", new int[]{5, 0,0,7});
                break;
            case 4:
                scene = new FifthStage(pWidth, pHeight, numOfPlayers, this, "-= STAGE 5.0 =-", new int[]{5, 1,3,8, 10, 10});
                break;
            case 5:
                scene = new SixthStage(pWidth, pHeight, numOfPlayers, this, "-= STAGE 6.0 =-", new int[]{5, 0,0,0, 0, 0, 10, 20});
                break;
            case 6:
                scene = new BlitzStage(pWidth, pHeight, numOfPlayers, this, "-= BLITZ STAGE =-", new int[]{5, 0,0,0, 0, 0, 0, 0, 0, 0, 0});
                break;

        }
        stage.addKeyListener(scene);
        stage.addMouseListener(scene);
    }


    public void goToSettings() {
        stage.removeMouseListener(scene);
        stage.removeKeyListener(scene);
        scene = new SettingsMenuScene(pWidth, pHeight, this);
        stage.addKeyListener(scene);
        stage.addMouseListener(scene);
    }


    public int[] getP1Controlles(){
        return p1Controlles;
    }

    public int[] getP2Controlles(){
        return p2Controlles;
    }

    public Font getGameFont() {
        return gameFont;
    }

    public int getP1Lives() {
        return p1Lives;
    }
    public int getP2Lives() {
        return p2Lives;
    }

    public int getP1Health() {
        return p1Health;
    }

    public int getP2Health() {
        return p2Health;
    }

    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public int getCredits() {return credits;}


    public void useCredit(){
        credits--;
    }
    
    public void revivePlayer(int i)
    {
		useCredit();

    	if(i==0){
    		p1Health=100;
			p1Lives =3;
    	}
    	else{
    		p2Health=100;
			p2Lives =3;
    	}

    }
    

    public void setScore(int i, int score)
    {
        if (i == 0) {
            p1Score += score;
        } else {
            p2Score += score;
        }
    }
    public void setPlayerHealth(int i, int strike) {
        if (i == 0) {
            p1Health += strike;
            if(p1Health <= 0){
                p1Lives--;
                if(p1Lives > 0)
                    resetPlayerHealth(i);
                if(p1Lives <= 0){
                	p1CreditTime = 10;
                    lastP1CreditTick = System.currentTimeMillis();
                }
            }
        } else {
            p2Health += strike;
            if(p2Health <= 0){
                p2Lives--;
                if(p2Lives > 0)
                    resetPlayerHealth(i);
                if(p2Health <= 0){
                    p2CreditTime = 10;
                    lastP2CreditTick = System.currentTimeMillis();
                }
            }
        }
    }

    public void goToMenu(){
        stage.removeMouseListener(scene);
        stage.removeKeyListener(scene);
        scene = new MainMenuScene(pWidth, pHeight, this);
        stage.addKeyListener(scene);
        stage.addMouseListener(scene);
    }


    public void setGameOver(boolean gameOver) {
        if(gameOver){
            goToMenu();
            this.gameOver = true;
        }

    }


	public int getP1HighScore() {
		return p1HighScore;
	}


	public void setP1HighScore(int p1HighScore) {
		this.p1HighScore = p1HighScore;
	}


	public int getP2HighScore() {
		return p2HighScore;
	}


	public void setP2HighScore(int p2HighScore) {
		this.p2HighScore = p2HighScore;
	}
    
    
}