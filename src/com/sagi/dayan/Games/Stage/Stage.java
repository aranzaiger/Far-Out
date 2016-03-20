package com.sagi.dayan.Games.Stage;

import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import com.sagi.dayan.Games.Engine.GameEngine;

/**
 * Created by sagi on 2/8/16.
 */
public class Stage extends JPanel implements Runnable{

    private Vector<Scene> scenes;
    private int currentScene;
    private int fWidth, fHeight;
    private long  startTime;
    private GameEngine engine;



    public Stage(int width, int height) {
        this.setDoubleBuffered(true);
        this.fHeight = height;
        this.setSize(width, height);
        this.setVisible(true);
        this.scenes = new Vector<>();
        this.engine = new GameEngine(width, height, this);
        startTime = System.currentTimeMillis();
        currentScene = 0;



        this.setFocusable(true);
        this.requestFocus();
    }

    public synchronized void update() {
        engine.update();
    }

    public synchronized void render() {
        engine.render(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(engine.getScene(),0,0,this);
        g.dispose();
    }

    @Override
    public void run() {
        double nsPerTick = 1000000000D / 60D; // ( 1 Billion / 60)
        long lastTimer = System.currentTimeMillis();
        boolean toRender = false;
        long lastTime = System.nanoTime();
        int ticks = 0;
        int frames = 0;
        double delta = 0;

        while(engine.gameOn){

            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            if (delta >= 1) {
                update();
                ticks++;
                delta -= 1;
                toRender = true;
            }

            if (toRender) { // render
                frames++;
                render();
                toRender = false;
                repaint();
            }

            try {
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - lastTimer > 1000) {
                lastTimer += 1000;
                if(frames <= 35){
                    System.err.println("Ticks: " + ticks + "\tFps: " + frames);
                }else{
                    System.out.println("Ticks: " + ticks + "\tFps: " + frames);
                }
                frames = 0;
                ticks = 0;
            }

        }
    }


    @Override
    public void addNotify(){
        super.addNotify();
        (new Thread(this)).start();
    }

}
