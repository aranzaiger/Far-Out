package com.sagi.dayan.Games.Utils;

/**
 * this class configures the structure of a wave
 */
public  class WaveConfig {
    protected int[] moveVector;
    protected double stepDelay;
    protected int acc;
    protected int startX;
    protected int startY;

    public  WaveConfig(int[] moveVector, double stepDelay, int acc, int staryX, int startY){
        this.moveVector = moveVector;
        this.stepDelay = stepDelay;
        this.acc = acc;
        this.startX = staryX;
        this.startY = startY;
    }


    public int[] getMoveVector() {
        return moveVector;
    }

    public double getStepDelay() {
        return stepDelay;
    }

    public int getAcc() {
        return acc;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }
}
