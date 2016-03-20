package com.sagi.dayan.Games.Stage;

import com.sagi.dayan.Games.Elements.Wave;
import com.sagi.dayan.Games.Engine.GameEngine;
import com.sagi.dayan.Games.Utils.WaveConfig;
import com.sagi.dayan.Games.Utils.WaveConfigs;

/**
 * Holds specific stage data
 * like: number of waves, wave types, num of enemies in each wave, launch delay etc.
 */
public class BlitzStage extends Level{
	
	protected final int NUM_OF_WAVES = 4;

    public BlitzStage(int width, int height, int numOfPlayers, GameEngine engine, String stageTitle, int[] waveDelay) {
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
                numOfEnemies = 35;
                fireDelay = 0.5;
                launchDelay = 0.5;
                numOfHits = 1;
                wc = engine.getWaveConfigs().getWaveConfig(0);
                break;
            case 1:
                numOfEnemies = 35;
                fireDelay = 0.3;
                launchDelay = 0.5;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(1);
                break;
            case 2:
                numOfEnemies = 45;
                fireDelay = 0.3;
                launchDelay = 0.5;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(2);
                break;
            case 3:
                numOfEnemies = 45;
                fireDelay = 0.2;
                launchDelay = 0.4;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(3);
                break;
            case 4:
                numOfEnemies = 55;
                fireDelay = 0.2;
                launchDelay = 0.4;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(4);
                break;
            case 5:
                numOfEnemies = 55;
                fireDelay = 0.2;
                launchDelay = 0.4;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(5);
                break;
            case 6:
                numOfEnemies = 65;
                fireDelay = 0.2;
                launchDelay = 0.4;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(6);
                break;
            case 7:
                numOfEnemies = 65;
                fireDelay = 0.2;
                launchDelay = 0.4;
                numOfHits = 10;
                wc = engine.getWaveConfigs().getWaveConfig(7);
                break;
            default:
                wc = engine.getWaveConfigs().getWaveConfig(WaveConfigs.DEMO);
                break;
        }
        waves.add(new Wave(numOfEnemies, wc.getMoveVector(), fireDelay, wc.getStepDelay(), launchDelay, wc.getAcc(), "L1-ES1.png", wc.getStartX(), wc.getStartY(), this, numOfHits));
        currentWave++;
    }
}
