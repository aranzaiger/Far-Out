package com.sagi.dayan.Games.Stage;

import com.sagi.dayan.Games.Elements.Wave;
import com.sagi.dayan.Games.Engine.GameEngine;
import com.sagi.dayan.Games.Utils.WaveConfig;
import com.sagi.dayan.Games.Utils.WaveConfigs;

/**
 * Holds specific stage data
 * like: number of waves, wave types, num of enemies in each wave, launch delay etc.
 */
public class FirstStage extends Level{
	
	protected final int NUM_OF_WAVES = 2;

    public FirstStage(int width, int height, int numOfPlayers, GameEngine engine, String stageTitle, int[] waveDelay) {
        super(width, height, numOfPlayers, engine, stageTitle, waveDelay);
        numOfWaves = NUM_OF_WAVES;
    }

    @Override
    protected void launchWave(long now) {
        lastWaveTime = now;
        WaveConfig wc;
        int numOfEnemies = 5, numOfHits = 1;
        double launchDelay = 0.5, fireDelay = 5;
        switch (currentWave){
            case 0:
                numOfEnemies = 5;
                fireDelay = 1.1;
                launchDelay = 2;
                numOfHits = 1;
                wc = engine.getWaveConfigs().getWaveConfig(0);
                break;
            case 1:
                numOfEnemies = 5;
                fireDelay = 0.8;
                launchDelay = 2;
                numOfHits = 1;
                wc = engine.getWaveConfigs().getWaveConfig(1);
                break;
            case 2:
                numOfEnemies = 15;
                fireDelay = 0.7;
                launchDelay = 1.5;
                numOfHits = 1;
                wc = engine.getWaveConfigs().getWaveConfig(2);
                break;
            default:
                wc = engine.getWaveConfigs().getWaveConfig(WaveConfigs.DEMO);
                break;
        }
        waves.add(new Wave(numOfEnemies, wc.getMoveVector(), fireDelay, wc.getStepDelay(), launchDelay, wc.getAcc(), "L1-ES1.png", wc.getStartX(), wc.getStartY(), this, numOfHits));
        currentWave++;
    }
}
