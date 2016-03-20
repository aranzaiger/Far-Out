package com.sagi.dayan.Games.Stage;

import com.sagi.dayan.Games.Elements.Wave;
import com.sagi.dayan.Games.Engine.GameEngine;
import com.sagi.dayan.Games.Utils.WaveConfig;
import com.sagi.dayan.Games.Utils.WaveConfigs;

/**
 * Created by sagi on 3/19/16.
 */
public class ThirdStage extends Level{
	
	protected final int NUM_OF_WAVES = 4;

    public ThirdStage(int width, int height, int numOfPlayers, GameEngine engine, String stageTitle, int[] waveDelay) {
        super(width, height, numOfPlayers, engine, stageTitle, waveDelay);
        numOfWaves = NUM_OF_WAVES;
    }

    @Override
    protected void launchWave(long now) {
        lastWaveTime = now;
        System.out.println("New Wave!! " + currentWave + ", Time: " + now);
        WaveConfig wc;
        int numOfEnemies = 5, numOfHits = 1;
        double launchDelay = 0.5, fireDelay = 5;
        switch (currentWave){
            case 0:
                numOfEnemies = 10;
                fireDelay = 0.8;
                launchDelay = 1.5;
                numOfHits = 1;
                wc = engine.getWaveConfigs().getWaveConfig(7);
                break;
            case 1:
                numOfEnemies = 15;
                fireDelay = 0.7;
                launchDelay = 1.4;
                numOfHits = 1;
                wc = engine.getWaveConfigs().getWaveConfig(2);
                break;
            case 2:
                numOfEnemies = 25;
                fireDelay = 0.5;
                launchDelay = 1.4;
                numOfHits = 2;
                wc = engine.getWaveConfigs().getWaveConfig(0);
                break;
            case 3:
                numOfEnemies = 5;
                fireDelay = 0.4;
                launchDelay = 1;
                numOfHits = 4;
                wc = engine.getWaveConfigs().getWaveConfig(1);
                break;
            default:
                wc = engine.getWaveConfigs().getWaveConfig(WaveConfigs.DEMO);
                break;
        }
        waves.add(new Wave(numOfEnemies, wc.getMoveVector(), fireDelay, wc.getStepDelay(), launchDelay, wc.getAcc(), "L1-ES1.png", wc.getStartX(), wc.getStartY(), this, numOfHits));
        currentWave++;
    }
}
