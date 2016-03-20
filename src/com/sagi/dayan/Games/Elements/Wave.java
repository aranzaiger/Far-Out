package com.sagi.dayan.Games.Elements;

import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import com.sagi.dayan.Games.Stage.Level;

/**
 * Created by sagi on 3/11/16.
 */
public class Wave {

    protected Level level;
    protected  int enemyMaxAmount, currentAmount, acc, startX, startY;
    protected double stepDelay,fireDelay, launchDelay;
    protected int[] moveVector;
    protected Vector<EnemyShip> enemies;
    protected Vector<Missile> bullets;
    protected long lastLaunchTime;
    protected String imageName;
    protected int hitsToDestroy;
    protected Random r;
    protected boolean isShipOfTypeOne;

    public Wave(int enemyMaxAmount, int[] moveVector, double fireDelay, double stepDelay, double launchDelay, int acc, String imageName, int startX, int startY, Level stage, int hitsToDestroy){
        this.enemies = new Vector<>();
        this.bullets = new Vector<>();
        this.enemyMaxAmount = enemyMaxAmount;
        this.currentAmount = 0;
        this.imageName = imageName;
        this.level = stage;
        this.fireDelay = fireDelay;
        this.launchDelay = launchDelay;
        this.acc = acc;
        this.startX = startX;
        this.startY = startY;
        this.stepDelay = stepDelay;
        this.moveVector = moveVector;
        this.lastLaunchTime = System.currentTimeMillis();
        this.hitsToDestroy = hitsToDestroy;
        this.r = new Random();
        int odds = r.nextInt(100);
        isShipOfTypeOne = (odds > 60) ? true : false;
    }

    public void update(){
        long now = System.currentTimeMillis();
        Vector <EnemyShip> enemiesToRemove = new Vector<>();
        if(now - lastLaunchTime >= launchDelay * 1000 && currentAmount <= enemyMaxAmount){
            // Create (RANDOM) new enemy
            if(isShipOfTypeOne){
            enemies.add(new EnemyShip(startX, startY, level.getStageHeight(), level.getStageHeight(), acc, imageName, 0, 15, 15, fireDelay, stepDelay, this, moveVector, 8, hitsToDestroy));
            }else{
                enemies.add(new EnemyShip(startX, startY, level.getStageHeight(), level.getStageHeight(), acc, "L1-ES2.png", 0, 15, 15, fireDelay, stepDelay, this, moveVector, 2, hitsToDestroy));
            }
            lastLaunchTime = now;
            currentAmount++;
        }
        for (int i = 0; i < enemies.size() ; i++){
            enemies.get(i).update();
            if (enemies.get(i).isDone()  || enemies.get(i).isOutOfScreen()) {

                enemiesToRemove.add(enemies.get(i));
            }
        }
        for (int i = 0; i < bullets.size() ; i++){
            bullets.get(i).update();
        }
        enemies.removeAll(enemiesToRemove);

    }

    public void render(Graphics g, JPanel p){
        for (int i = 0; i < bullets.size() ; i++){
            bullets.get(i).drawSprite(g, p);
        }
        for (int i = 0; i < enemies.size() ; i++){
            enemies.get(i).drawSprite(g, p);
        }
    }

    public void fireFromEnemy(EnemyShip e){
    	if(!e.isDead())
    		level.enemyFire(e.getCenterX(), (int)(e.getLocY() + e.getsHeight()), -(e.getAcceleration() + 2));
    }

    public Vector <EnemyShip> getEnemies() {
        return enemies;
    }

    public void enemyHit(EnemyShip es) {
        es.gotHit();
    }
    public boolean isWaveOver() {
        return enemies.size() == 0 && currentAmount >= enemyMaxAmount;
    }
}
