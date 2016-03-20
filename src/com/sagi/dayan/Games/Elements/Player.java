package com.sagi.dayan.Games.Elements;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Represents a player ship
 */
public class Player extends AnimatedSprite {
    private final int NORMAL_ANIMATION = 0, RIGHT_ANIMATION = 1, LEFT_ANIMATION = 2, PADDING_BOTTOM = 35, MORTAL_DELAY = 3;
    private int hDirection = 0, vDirection = 0;
    private String imagePrefix;
    private boolean ableToFire, isMortal, toDraw;
    private int fireDelay;
    private double imortalPulse = 0.2;
    private long lastFired, lastDrawn, created;
    private int startX, startY;
    private boolean isGameOver;




    public Player(int x, int y, int w, int h, int acc, String imgName, double angle, int sWidth, int sHeight, String imagePrefix, int numOfFirstFrames) {
        super(x, y, w, h, acc, imgName, angle, sWidth, sHeight, numOfFirstFrames);
        this.imagePrefix = imagePrefix;
        initFirstAnimation("", numOfFirstFrames);
        this.ableToFire = true;
        fireDelay = 200;
        lastFired = System.currentTimeMillis();
        lastDrawn = lastFired;
        created = lastDrawn;
        setImageDimensions();
        isMortal = false;
        toDraw = true;
        startX = locX;
        startY = locY;
        isGameOver = false;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    //when player revived  - reset it to default position
    public void resetPlayer(){
        locX = startX;
        locY = startY;
        isMortal = false;
        lastDrawn = System.currentTimeMillis();
        created = lastDrawn;
    }
    
    @Override
    protected void initFirstAnimation(String spriteSheet, int numOfFirstFrames) {
        if(imagePrefix == null)
            return;
        System.out.println(imagePrefix);
        animations.add(new Animation(imagePrefix+"StraighSheet.png", 7, 200));
        animations.add(new Animation(imagePrefix+"RightSheet.png", 7, 200));
        animations.add(new Animation(imagePrefix+"LeftSheet.png", 7, 200));
    }

    public boolean isMortal() {
        return isMortal;
    }

    //besides position update, updated whether ship can fire or not
    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if(now - lastFired >= fireDelay){
            ableToFire = true;
        }else{
            ableToFire = false;
        }
        locX += vDirection * acceleration;
        locY -= hDirection * acceleration;
        if(locX < 0)
            locX = 0;
        else if(locX > pWidth - animations.get(currentAnimation).getCurrentFrame().getWidth()){
            locX = pWidth - animations.get(currentAnimation).getCurrentFrame().getWidth();
        }

        if(locY < 0)
            locY = 0;
        else if(locY > pHeight - animations.get(currentAnimation).getCurrentFrame().getHeight() - PADDING_BOTTOM){
            locY = pHeight - animations.get(currentAnimation).getCurrentFrame().getHeight() - PADDING_BOTTOM;
        }



    }

    //set horizontal and vertical direction
    public void sethDirection(int direction) {
        this.hDirection = direction;
    }
    public void setvDirection(int direction) {
        this.vDirection = direction;
        if(direction != NORMAL_ANIMATION){
            currentAnimation = (direction == 1) ? RIGHT_ANIMATION : LEFT_ANIMATION;
        }else{
            currentAnimation = 0;
        }
    }


    public boolean isAbleToFire() {
        return ableToFire;
    }

    public void setAbleToFire(boolean ableToFire) {
        this.ableToFire = ableToFire;
    }

    public int getFireDelay() {
        return fireDelay;
    }

    public void setFireDelay(int fireDelay) {
        this.fireDelay = fireDelay;
    }

    //update time since last fired
    public void updateFireTime(){
        lastFired = System.currentTimeMillis();
    }


    //if player was dead dont draw (creates blinking effect)
    @Override
    public void drawSprite(Graphics g, JPanel p){
        long now = System.currentTimeMillis();
        if(isMortal){
            super.drawSprite(g, p);
        }else{
            if(now - lastDrawn >= imortalPulse * 1000){
                toDraw = !toDraw;
                lastDrawn = now;
                if(now-created >= MORTAL_DELAY * 1000){
                    isMortal = true;
                }
            }
            if(toDraw) {
                super.drawSprite(g, p);
            }
        }
    }
}
