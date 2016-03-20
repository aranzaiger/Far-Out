package com.sagi.dayan.Games.Utils;

import java.util.Vector;

/**
 * this class configs settings and movement vector for different kinds of waves
 */
public class WaveConfigs  {
    public static final int DEMO = 0;
    Vector<WaveConfig> configs;

    public WaveConfigs(){
        configs = new Vector<>();
//        int[] moveVector, double stepDelay, int acc, int staryX, int startY
		
		//middle top to left top
        configs.add(new WaveConfig(new int[]{90,90,120, 120, 150, 150, 270} , 0.5, 8,500 , -30));
		
		//middle top to right top
        configs.add(new WaveConfig(new int[]{90,90, 60, 60, 30, 30, 270} , 0.5, 8,500 , -30));
		
		//top left to middle top
		configs.add(new WaveConfig(new int[]{90,90, 60, 60, 30, 30, 270} , 0.5, 8,100 , -30));
		
		//top right to middle top
		configs.add(new WaveConfig(new int[]{90,90,120, 120, 150, 150, 270} , 0.5, 8,900 , -30));
		
		//right buttom to middle bottom
        configs.add(new WaveConfig(new int[]{270,270,300, 300, 330, 330, 90} , 0.5, 8,100 , 1000));
		
		//left buttom to middle bottom
        configs.add(new WaveConfig(new int[]{270,270,240, 240, 210, 210, 90} , 0.5, 8,900 , 1000));
		
		//middle right to middle right
		configs.add(new WaveConfig(new int[]{180,180,180,180,180,90, 90, 0} , 0.5, 8,999 , 400));
		
		//middle left to middle left
		configs.add(new WaveConfig(new int[]{0,0,0,0,0,90, 90, 180} , 0.5, 8,-10 , 400));

    }

    //return requested wave configuration
    public WaveConfig getWaveConfig(int config){
        if (config < 0 || configs.size() <= config)
            throw new IllegalArgumentException("no such config...");
        return configs.get(config);
    }
}