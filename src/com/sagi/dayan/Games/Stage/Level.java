package com.sagi.dayan.Games.Stage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sagi.dayan.Games.Elements.Background;
import com.sagi.dayan.Games.Elements.Blast;
import com.sagi.dayan.Games.Elements.Missile;
import com.sagi.dayan.Games.Elements.Player;
import com.sagi.dayan.Games.Elements.Wave;
import com.sagi.dayan.Games.Engine.CollisionUtil;
import com.sagi.dayan.Games.Engine.GameEngine;
import com.sagi.dayan.Games.Utils.Utils;

/**
 * This class holds all the logic for all the stages.
 * stages themselfs contain only specific stage data.
 */
public abstract class Level extends Scene {
	protected final double PRESS_START_PULE = 0.3;
	protected boolean toDrawStart, isStarted;
	protected int p1Speed = 10,currentWave, startingAnimationIndex, numOfPlayers, numOfWaves;

	// used to save all players, missiles, enemy waves and blasts
	protected Vector<Player> players;
	protected Vector<Missile> p1Missiles, p2Missiles, enemyMissiles;
	protected Vector<Wave> waves;
	protected Vector<Blast> blasts;
	protected Background bg;
	protected int[] yAxisStartingAnimation, waveDelay;


	protected Map<Integer, Boolean> keys;
	protected String title;
	protected JLabel stageTitle;
	protected long lastWaveTime, lastPulseTime;



	public Level(int width, int height, int numOfPlayers, GameEngine engine, String stageTitle, int[] waveDelay){
		super(width, height, engine);
		//initialize vectors
		players = new Vector<>();
		p1Missiles = new Vector<>();
		p2Missiles = new Vector<>();
		blasts = new Vector<>();
		enemyMissiles = new Vector<>();

		//save additional information
		this.waveDelay = waveDelay;
		this.lastWaveTime = System.currentTimeMillis();
		this.currentWave = 0;
		this.waves = new Vector<>();
		isStarted = false;
		keys = new HashMap<>();
		yAxisStartingAnimation = new int[]{height + (5*GameEngine.PLAYER_HEIGHT) , height - (4*GameEngine.PLAYER_HEIGHT) , height - (GameEngine.PLAYER_HEIGHT + 15)};
		startingAnimationIndex = 0;
		bg = new Background(0,0,width,height, 1, "L1-BG.jpg", 0,1000, 4760);
		this.numOfPlayers = numOfPlayers;
		this.title = stageTitle;
		this.stageTitle = new JLabel(this.title);


		//create requierd amount of players
		if(numOfPlayers == 1) {
			players.add(new Player((width / 2) + (GameEngine.PLAYER_WIDTH / 2), yAxisStartingAnimation[startingAnimationIndex],
					width, height, p1Speed, "emptyImage.png", 0, GameEngine.PLAYER_WIDTH, GameEngine.PLAYER_HEIGHT, "P1",6));
		}else{
			players.add(new Player((width / 2) + (GameEngine.PLAYER_WIDTH / 2) + GameEngine.PLAYER_WIDTH, yAxisStartingAnimation[startingAnimationIndex],
					width, height, p1Speed, "emptyImage.png", 0, GameEngine.PLAYER_WIDTH, GameEngine.PLAYER_HEIGHT, "P1", 6));
			players.add(new Player((width / 2) + (GameEngine.PLAYER_WIDTH / 2) - GameEngine.PLAYER_WIDTH*3, yAxisStartingAnimation[startingAnimationIndex],
					width, height, p1Speed, "emptyImage.png", 0, GameEngine.PLAYER_WIDTH, GameEngine.PLAYER_HEIGHT, "P2", 6));

		}

		//initialize keys
		setupKeys();
		Utils.playSound("jetSound.wav");
		lastPulseTime = System.currentTimeMillis();
		toDrawStart = true;
	}

	//initialize player 1 & 2 keys
	private void setupKeys() {
		int[] p1 = engine.getP1Controlles();
		for(int i = 0 ; i < p1.length ; i++){
			keys.put(p1[i], false);
		}
		if(numOfPlayers > 1){
			int[] p2 = engine.getP2Controlles();
			for(int i = 0 ; i < p1.length ; i++){
				keys.put(p2[i], false);
			}
		}
	}

	@Override
	public void update() {
		bg.update();
		movePlayers();

		//vectors to hold objects that needs to be removed
		Vector<Wave> wavesToRemove = new Vector<>();
		Vector<Blast> blastTRM = new Vector<>();

		//check if enough time has passed and a new wave needs to be launched
		long now = System.currentTimeMillis();
		if(currentWave < numOfWaves && now - lastWaveTime >= waveDelay[currentWave] * 1000){
			launchWave(now);
		}

		//if still in new game start animation - advance animation
		if(startingAnimationIndex < 3 && !isStarted){
			if(startingAnimationIndex == 0){
				startingAnimationIndex++;
			}
			if(players.get(0).getLocY() > yAxisStartingAnimation[startingAnimationIndex] && startingAnimationIndex == 1){
				for(int i = 0 ; i < players.size() ; i++){
					players.get(i).setLocY((int)players.get(i).getLocY() - (p1Speed));
				}
				if(players.get(0).getLocY() <= yAxisStartingAnimation[startingAnimationIndex]){
					startingAnimationIndex++;
				}
			}else{

				for(int i = 0 ; i < players.size() ; i++){
					players.get(i).setLocY((int)players.get(i).getLocY() + (p1Speed - 5));
				}
				if(players.get(0).getLocY() >= yAxisStartingAnimation[startingAnimationIndex]){
					startingAnimationIndex++;
				}
			}
		}
		//if finished game start animation - update players, missiles, enemy waves
		//if waves are done - remove them
		else{
			isStarted = true;
			for(int i = 0 ; i < players.size() ; i++){
				players.get(i).update();
			}

			for(int i = 0 ; i < p1Missiles.size() ; i++){
				p1Missiles.get(i).update();
			}
			for(int i = 0 ; i < p2Missiles.size() ; i++){
				p2Missiles.get(i).update();
			}
			for(int i = 0 ; i < enemyMissiles.size() ; i++){
				enemyMissiles.get(i).update();
			}

			for(int i = 0 ; i < waves.size() ; i++){
				waves.get(i).update();
				if(waves.get(i).isWaveOver()) {
					wavesToRemove.add(waves.get(i));
				}
			}
			waves.removeAll(wavesToRemove);

			//check and set high score
			if(engine.getP1Score() > engine.getP1HighScore())
				engine.setP1HighScore(engine.getP1Score());
			if(engine.getP2Score() > engine.getP2HighScore())
				engine.setP2HighScore(engine.getP2Score());
		}

		//check all collisions
		checkCollision();
		engine.setGameOver(isGameOver());

		//if all waves are done switch stage
		if(currentWave >= numOfWaves && waves.size()==0)
		{
			engine.changeLevel();
		}


		//remove finished blasts and update existing
		for(int i =0; i<blasts.size();i++){
			if (blasts.get(i).isDone()){
				blastTRM.add(blasts.get(i));
			}
			blasts.get(i).update();
		}
		blasts.removeAll(blastTRM);

	}

	protected abstract void launchWave(long time);

	//determine if game is over according to player 1 & 2
	private boolean isGameOver(){
		if(numOfPlayers == 1) {
			return players.get(0).isGameOver();
		}else{
			return players.get(0).isGameOver() && players.get(1).isGameOver();
		}
	}

	//set player 1 & 2 movement direction
	private void movePlayers() {
		/**
		 * Player 1 Movement:
		 */
		if(keys.get(engine.getP1Controlles()[GameEngine.UP]) ){ //UP
			players.get(0).sethDirection(1);
		}
		if(keys.get(engine.getP1Controlles()[GameEngine.DOWN])){ // DOWN
			players.get(0).sethDirection(-1);
		}
		if(!keys.get(engine.getP1Controlles()[GameEngine.UP]) && !keys.get(engine.getP1Controlles()[GameEngine.DOWN])){ // Not up Or Down
			players.get(0).sethDirection(0);
		}
		if(keys.get(engine.getP1Controlles()[GameEngine.LEFT])) { // Left
			players.get(0).setvDirection(-1);
		}
		if(keys.get(engine.getP1Controlles()[GameEngine.RIGHT])) { // Right
			players.get(0).setvDirection(1);
		}
		if(!keys.get(engine.getP1Controlles()[GameEngine.LEFT]) && !keys.get(engine.getP1Controlles()[GameEngine.RIGHT])){ // Not right or left
			players.get(0).setvDirection(0);
		}
		if(keys.get(engine.getP1Controlles()[GameEngine.FIRE]) ){
			if(players.get(0).isAbleToFire() && engine.getP1Health() > 0){
				//SOUND
				Utils.playSound("player_laser.wav");
				p1Missiles.add(new Missile(players.get(0).getCenterX() - 15, (int)players.get(0).getLocY(),getStageWidth(),getStageHeight(),players.get(0).getAcceleration() + 3, "P1Laser.png", 4));
				players.get(0).updateFireTime();
			}
		}
		if(keys.get(engine.getP1Controlles()[GameEngine.USE_CREDIT]) && engine.getP1Health() <= 0 ){
			if(engine.getCredits() > 0) {
				engine.revivePlayer(0);
				players.get(0).resetPlayer();

			}
		}

		/**
		 * Player 2 Movement
		 */
		if(numOfPlayers > 1){
			if(keys.get(engine.getP2Controlles()[GameEngine.UP]) ){ //UP
				players.get(1).sethDirection(1);
			}
			if(keys.get(engine.getP2Controlles()[GameEngine.DOWN])){ // DOWN
				players.get(1).sethDirection(-1);
			}
			if(!keys.get(engine.getP2Controlles()[GameEngine.UP]) && !keys.get(engine.getP2Controlles()[GameEngine.DOWN])){ // Not up Or Down
				players.get(1).sethDirection(0);
			}
			if(keys.get(engine.getP2Controlles()[GameEngine.LEFT])) { // Left
				players.get(1).setvDirection(-1);
			}
			if(keys.get(engine.getP2Controlles()[GameEngine.RIGHT])) { // Right
				players.get(1).setvDirection(1);
			}
			if(!keys.get(engine.getP2Controlles()[GameEngine.LEFT]) && !keys.get(engine.getP2Controlles()[GameEngine.RIGHT])){ // Not right or left
				players.get(1).setvDirection(0);
			}
			if(keys.get(engine.getP2Controlles()[GameEngine.FIRE]) ){
				if(players.get(1).isAbleToFire() && engine.getP2Health() > 0){
					//SOUND
					Utils.playSound("player_laser.wav");
					p2Missiles.add(new Missile(players.get(1).getCenterX() - 15, (int)players.get(1).getLocY(),getStageWidth(),getStageHeight(),players.get(1).getAcceleration() + 3, "P1Laser.png", 4));
					players.get(1).updateFireTime();
				}
			}
			if(keys.get(engine.getP2Controlles()[GameEngine.USE_CREDIT]) && engine.getP2Health() <= 0 ){
				if(engine.getCredits() > 0) {
					engine.revivePlayer(1);
					players.get(1).resetPlayer();

				}
			}
		}
	}

	//render all sprites and elements
	@Override
	public void render(JPanel p) {
		sceneImage = new BufferedImage(this.stageWidth, this.stageHeight, Image.SCALE_FAST);
		Graphics g = sceneImage.getGraphics();

		bg.drawSprite(g, p);
		Color c = g.getColor();
		Font f = engine.getGameFont();

		//if game didn't start yet - print stage title
		if(!isStarted){
			if(f == null) {
				f = g.getFont();
			}
			f = f.deriveFont(60F);
			g.setColor(Color.DARK_GRAY);
			g.setFont(f);

			// Get the FontMetrics
			FontMetrics metrics = g.getFontMetrics(f);
			// Determine the X coordinate for the text
			int x = (stageWidth - metrics.stringWidth(this.title)) / 2;
			// Determine the Y coordinate for the text
			int y = ((stageHeight - metrics.getHeight()) / 2) - metrics.getAscent();
			g.drawString(this.title, x, y);
			g.setColor(c);
		}

		if (isGameOver())
		{
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//print score
		f = f.deriveFont(15F);
		g.setFont(f);

		//print life bar
		for(int i=0; i<players.size(); i++){

			g.setColor(Color.WHITE);
			g.drawRect(15,35*(i+1)+10,100,10);

			g.setColor(Color.GREEN);

			if (i==0 && engine.getP1Health()<=30)
				g.setColor(Color.RED);
			else if( i==1 && engine.getP2Health()<=30)
				g.setColor(Color.RED);

			g.fillRect(15,35*(i+1)+10,((i == 0) ? engine.getP1Health() : engine.getP2Health()),10);

		}


		//print credits
		g.setColor(Color.WHITE);
		g.drawString("Credits: "+ engine.getCredits(), stageWidth/2, 30);


		//draw all missiles
		for(int i = 0 ; i < p1Missiles.size() ; i++){
			p1Missiles.get(i).drawSprite(g,p);
		}
		for(int i = 0 ; i < p2Missiles.size() ; i++){
			p2Missiles.get(i).drawSprite(g,p);
		}
		for(int i = 0 ; i < enemyMissiles.size() ; i++){
			enemyMissiles.get(i).drawSprite(g,p);
		}

		//draw player 1 & 2 details (score, health & lives)
		for(int i = 0 ; i < players.size() ; i++){
			if(i == 0){
				if(engine.getP1Health() > 0){
					players.get(i).drawSprite(g,p);
					g.setColor(Color.WHITE);
					g.drawString("Player "+ (i+1) +" - Lives: " + ((i == 0) ? engine.getP1Lives() : engine.getP2Lives())+ ", Score: " + ((i == 0) ? engine.getP1Score() : engine.getP2Score()), 15, 35*(i+1));
				}else{
					if(players.get(i).isGameOver()){
						renderGameOver(g, p, i);
					}else{
						renderPressStart(g, p, i);
					}
				}
			}else{
				if(engine.getP2Health() > 0){
					players.get(i).drawSprite(g,p);
					g.setColor(Color.WHITE);
					g.drawString("Player "+ (i+1) +" - Lives: " + ((i == 0) ? engine.getP1Lives() : engine.getP2Lives())+ ", Score: " + ((i == 0) ? engine.getP1Score() : engine.getP2Score()), 15, 35*(i+1));
				}else{
					if(players.get(i).isGameOver()){
						renderGameOver(g, p, i);
					}else{
						renderPressStart(g, p, i);
					}
				}
			}
		}

		//render all waves
		for(int i = 0 ; i < waves.size() ; i++){
			waves.get(i).render(g,p);
		}

		//render all blasts
		for(int i =0; i<blasts.size();i++){
			blasts.get(i).drawSprite(g, p);
		}

	}

	protected void renderPressStart(Graphics g, JPanel p, int i){
		long now = System.currentTimeMillis();
		if(now - lastPulseTime >= PRESS_START_PULE * 1000){
			toDrawStart = !toDrawStart;
			lastPulseTime = now;
		}
		if(i == 0){
			g.drawString(engine.getP1CreditTime()+"", 15, 35 * (i + 1));
			if(engine.getP1CreditTime() <= 0){
				players.get(i).setGameOver(true);
			}
		}else{
			g.drawString(engine.getP2CreditTime()+"", 15, 35 * (i + 1));
			if(engine.getP2CreditTime() <= 0){
				players.get(i).setGameOver(true);
			}

		}
		if(toDrawStart) {
			g.drawString("PRESS START", 45, 35 * (i + 1));

		}
		players.get(i).setLocY(-500);
		players.get(i).setLocX(-500);
	}

	protected void renderGameOver(Graphics g, JPanel p, int i){
		System.out.println("HERE");
		long now = System.currentTimeMillis();
		players.get(i).setGameOver(true);
		if(now - lastPulseTime >= PRESS_START_PULE * 1000){
			toDrawStart = !toDrawStart;
			lastPulseTime = now;
		}
		if(toDrawStart) {
			g.drawString("P" + (i+1) + " GAME OVER", 15, 35 * (i + 1));
		}
	}


	//check all possible collisions
	public void checkCollision() {

		//vectors to store objects that needs to be removed
		Vector<Missile> p1MTR, p2MTR, eMTR;
		eMTR = new Vector<>();
		p1MTR = new Vector<>();
		p2MTR = new Vector<>();

		// remove missiles - out of screen
		for( int i = 0; i < enemyMissiles.size(); i++) {
			if (enemyMissiles.get(i).isOutOfScreen()) {
				eMTR.add(enemyMissiles.get(i));
			}
		}
		for( int i = 0; i < p1Missiles.size(); i++) {
			if (p1Missiles.get(i).isOutOfScreen()) {
				p1MTR.add(p1Missiles.get(i));
			}
		}
		if (players.size() > 1) {
			for( int i = 0; i < p2Missiles.size(); i++) {
				if (p2Missiles.get(i).isOutOfScreen()) {
					p2MTR.add(p2Missiles.get(i));
				}
			}
		}

		//for each player check collisions
		for (int i = 0; i < players.size(); i++) {

			//player vs. enemy missile
			for (int j = 0; j < enemyMissiles.size(); j++) {
				if(CollisionUtil.collidesWith(players.get(i),enemyMissiles.get(j))){
					if ((i==0 && engine.getP1Health()==10) ||(i==1 && engine.getP2Health()==10)) {
						blasts.add(new Blast((int) players.get(i).getLocX(), (int) players.get(i).getLocY(), "explosion.png", 15));
					}
					playerHit(i);
					if(playerIsAlive(i)) {
						eMTR.add(enemyMissiles.get(j));
					}
				}
			}

			//player vs. enemy ship
			for (int j = 0; j < waves.size(); j++) {
				// Ship hits enemy
				for (int k = 0; k < waves.get(j).getEnemies().size(); k++) {
					if (CollisionUtil.collidesWith(waves.get(j).getEnemies().get(k), players.get(i))) {
						if(!waves.get(j).getEnemies().get(k).isDead()){
							if ((i==0 && engine.getP1Health()==10) || (i==1 && engine.getP2Health()==10)) {
								blasts.add(new Blast((int) players.get(i).getLocX(), (int) players.get(i).getLocY(), "explosion.png", 15));
							}
							playerHit(i);
						}
						if(playerIsAlive(i)) {
							waves.get(j).enemyHit(waves.get(j).getEnemies().get(k));
						}
					}
				}
			}

			//check for each player seperatly for score
			//player 1 missile vs. enemy
			if(i == 0){
				for(int m = 0 ; m < p1Missiles.size() ; m++){
					for (int j = 0; j < waves.size(); j++) {
						for (int k = 0; k < waves.get(j).getEnemies().size(); k++) {
							if (CollisionUtil.collidesWith(waves.get(j).getEnemies().get(k), p1Missiles.get(m))) {
								waves.get(j).enemyHit(waves.get(j).getEnemies().get(k)); //remove enemy life

								if (waves.get(j).getEnemies().get(k).isDead()) //if enemy is dead
									engine.setScore(i,10);
								p1MTR.add(p1Missiles.get(m));
							}
						}
					}
				}

			}

			//player 2 missile vs. enemy
			else {
				for(int m = 0 ; m < p2Missiles.size() ; m++){
					for (int j = 0; j < waves.size(); j++) {
						for (int k = 0; k < waves.get(j).getEnemies().size(); k++) {
							if (CollisionUtil.collidesWith(waves.get(j).getEnemies().get(k), p2Missiles.get(m))) {

								waves.get(j).enemyHit(waves.get(j).getEnemies().get(k)); //remove enemy life
								if (waves.get(j).getEnemies().get(k).isDead()) //if enemy is dead
									engine.setScore(i,10);
								p2MTR.add(p2Missiles.get(m));

							}
						}
					}
				}
			}
		}


		//remove all objects that needs to be removed
		p1Missiles.removeAll(p1MTR);
		p2Missiles.removeAll(p2MTR);
		enemyMissiles.removeAll(eMTR);

	}

	//checks if player is alive
	protected boolean playerIsAlive(int i){
		if(i == 0){
			return !(engine.getP1Lives() <= 0);
		}else{
			return !(engine.getP2Lives() <= 0);
		}
	}

	//reduce player health and play hit audio
	protected void playerHit(int i){
		if(players.get(i).isMortal()){
			Utils.playSound((i == 0) ? "player_1_hit.wav" : "player_2_hit.wav");
			engine.setPlayerHealth(i, -10);
			if(i == 0){
				if(engine.getP1Health() == 100){
					players.get(i).resetPlayer();
				}
			}else{
				if(engine.getP2Health() == 100){
					players.get(i).resetPlayer();
				}
			}
		}
	}

	//create a new enemy missile
	public void enemyFire(int x, int y, int acc) {
		enemyMissiles.add(new Missile(x, y,getStageWidth(),getStageHeight(), acc,"E1-Fire.png", 15));
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if(isStarted)
			keys.put(keyEvent.getKeyCode(), true);

	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		if(isStarted)
			keys.put(keyEvent.getKeyCode(), false);
	}


}
