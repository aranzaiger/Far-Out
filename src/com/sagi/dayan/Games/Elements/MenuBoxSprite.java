package com.sagi.dayan.Games.Elements;

/**
 * Animation for box in main menu
 */
public class MenuBoxSprite extends AnimatedSprite {
    public MenuBoxSprite(int x, int y, int w, int h, int acc, String imgName, double angle, int sWidth, int sHeight, int numOfFirstFrames) {
        super(x, y, w, h, acc, imgName, angle, sWidth, sHeight, numOfFirstFrames);
    }

    @Override
    protected void initFirstAnimation(String spriteSheet, int numOfFirstFrames) {
        animations.add(new Animation("menuBox.png", 15, 150));
    }

    @Override
    public void update() {

    }
}
