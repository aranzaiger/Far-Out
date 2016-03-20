package com.sagi.dayan.Games.Elements;


import com.sagi.dayan.Games.Utils.Utils;

/**
 * represents an enemy ship
 */
public class EnemyShip extends AnimatedSprite {
    protected int currentStep;
    protected double stepDelay, fireDelay;
    protected long lastFireTime, lastStepTime;
    protected int[] moveVector;
    protected Wave wave;
    protected int hitsToDestroy;
    protected boolean isDone;
    protected long startExploded;

    public EnemyShip(int x, int y, int w, int h, int acc, String imgName, double angle, int sWidth, int sHeight,double fireDelay, double stepDelay, Wave wave, int[] moveVector,int numOfFirstFrames, int hitsToDestroy) {
        super(x, y, w, h, acc, imgName, angle, sWidth, sHeight, numOfFirstFrames);
        this.fireDelay = fireDelay;
        this.stepDelay = stepDelay;
        this.currentStep = 0;
        this.lastFireTime = System.currentTimeMillis();
        this.lastStepTime = System.currentTimeMillis();
        this.moveVector = moveVector;
        this.wave = wave;
        this.hitsToDestroy = hitsToDestroy;

    }

    //add enemy ship and blast animation
    @Override
    protected void initFirstAnimation(String spriteSheet, int numOfFirstFrames) {
        if(imageName == "L1-ES2.png"){
            animations.add(new Animation(imageName, numOfFirstFrames, 100));
        }else {
            animations.add(new Animation(imageName, numOfFirstFrames, 300));
        }
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if(currentAnimation == 1){
            if(now - startExploded >= 500){
                isDone = true;
            }
        }
        if(now - lastFireTime >= fireDelay*1000){
            wave.fireFromEnemy(this);
            lastFireTime = now;
        }

        if(now - lastStepTime >= stepDelay*1000 && currentStep < moveVector.length - 1){
            currentStep++;
            lastStepTime = now;
        }

        locX += acceleration * Math.cos(Math.toRadians(moveVector[currentStep]));
        locY -= acceleration * -1* Math.sin(Math.toRadians(moveVector[currentStep]));
    }
    //when enemy gets hit- lower life and set explosion sound if dead
    public void gotHit() {
        hitsToDestroy--;
        if(hitsToDestroy == 0){
            startExploded = System.currentTimeMillis();
            animations.add(new Animation("explosion.png", 16, 500));
            currentAnimation++;
            Utils.playSound("enemy_exp.wav");
        }
    }

    public boolean isDead() {
        return hitsToDestroy <= 0;
    }

    public boolean isDone(){
        return isDone;
    }





}
