package com.sagi.dayan.Games.Elements;

import com.sagi.dayan.Games.Elements.AnimatedSprite.Animation;

/*
	represents a blast when player ship explodes
 */
public class Blast extends AnimatedSprite {

	protected boolean isDone;
	protected int numOfFirstFrames;
	public Blast(int x, int y, String imgName, int numOfFirstFrames) {
		super(x, y, 0, 0, 0, imgName, 0, 15, 15, numOfFirstFrames);
		// TODO Auto-generated constructor stub
		isDone = false;
		this.numOfFirstFrames = numOfFirstFrames;
	}

	@Override
	protected void initFirstAnimation(String spriteSheet, int numOfFirstFrames) {
		animations.add(new Animation("explosion.png", 16, 500));
	}

	//set done when finished iterating on all frames
	@Override
	public void update() {
		System.out.println("curr: "+currentAnimation+", total: "+numOfFirstFrames);
		if (getFrameNum() == numOfFirstFrames-1)
			isDone=true;
		// TODO Auto-generated method stub

	}

	public boolean isDone(){
		return isDone;
	}
}
