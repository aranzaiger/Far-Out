package com.sagi.dayan.Games.Elements;

import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import javax.rmi.CORBA.Util;
import javax.swing.JPanel;

import com.sagi.dayan.Games.Stage.Level;
import com.sagi.dayan.Games.Utils.Utils;

/**
 * represents a single wave of enemy ships.
 * this class will manage a wave of enemies, create them when needed & create missiles when needed.
 * timing of creating and shooting is done using system time.
 */
public class Wave {

    protected Level level;      //used to ask for a misslie to be created
    protected  int enemyMaxAmount, currentAmount, hitsToDestroy, acc, startX, startY;
    protected double stepDelay,fireDelay, launchDelay;
    protected int[] moveVector;
    protected Vector<EnemyShip> enemies;
    protected Vector<Missile> bullets;
    protected long lastLaunchTime;
    protected String imageName;
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

        //decides which ship img to use
        int odds = r.nextInt(100);
        isShipOfTypeOne = (odds > 60) ? true : false;
    }


    public void update(){
        long now = System.currentTimeMillis();
        Vector <EnemyShip> enemiesToRemove = new Vector<>();

        // Create (RANDOM) new enemy if enough time passed since last launch (launchDelay)
        if(now - lastLaunchTime >= launchDelay * 1000 && currentAmount <= enemyMaxAmount){
            //choose img
            if(isShipOfTypeOne){
            enemies.add(new EnemyShip(startX, startY, level.getStageHeight(), level.getStageHeight(), acc, imageName, 0, 15, 15, fireDelay, stepDelay, this, moveVector, 8, hitsToDestroy));
            }else{
                enemies.add(new EnemyShip(startX, startY, level.getStageHeight(), level.getStageHeight(), acc, "L1-ES2.png", 0, 15, 15, fireDelay, stepDelay, this, moveVector, 2, hitsToDestroy));
            }
            Utils.playSound("enemy_enter.wav");
            lastLaunchTime = now;   //save new time
            currentAmount++;
        }
        //update enemies & missiles, and remove the ones who are dead\OOS
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

    //render missiles and enemy ships
    public void render(Graphics g, JPanel p){
        for (int i = 0; i < bullets.size() ; i++){
            bullets.get(i).drawSprite(g, p);
        }
        for (int i = 0; i < enemies.size() ; i++){
            enemies.get(i).drawSprite(g, p);
        }
    }

    //if enemy is not dead create a new missile
    public void fireFromEnemy(EnemyShip e){
    	if(!e.isDead()) {
            level.enemyFire(e.getCenterX(), (int) (e.getLocY() + e.getsHeight()), -(e.getAcceleration() + 2));
            Utils.playSound("enemy_relese.wav");
        }
    }

    //returns all enemys in wave
    public Vector <EnemyShip> getEnemies() {
        return enemies;
    }

    //used when an enemy gets hit to remove life
    public void enemyHit(EnemyShip es) {
        es.gotHit();
    }

    //returns true if all enemy ships launched and died or OOS
    public boolean isWaveOver() {
        return enemies.size() == 0 && currentAmount >= enemyMaxAmount;
    }
}
