package com.sagi.dayan.Games.Elements;

/**
 * represents a missile of enemy/player ship
 */
public class Missile extends AnimatedSprite {


    public Missile(int x, int y, int w, int h, int acc, String imgName, int numOfFrames) {
        super(x, y, w, h, acc, imgName, 0, 15, 15, numOfFrames);

    }

    @Override
    protected void initFirstAnimation(String spriteSheet, int numOfFirstFrames) {
        animations.add(new Animation(imageName, numOfFirstFrames, 500));
    }

    @Override
    public void update() {
        locY -= acceleration;
    }
}
