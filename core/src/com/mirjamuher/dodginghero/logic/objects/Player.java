package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;
import com.mirjamuher.dodginghero.logic.GameProgress;

public class Player extends Character {
    public static final float APPROACH_TIME = 0.5f;

    private int baseNumX;  // X position of Player
    private int baseNumY;  // Y position of Player

    private boolean winning = false;
    private float winTime = 0;

    private final int max_lives;

    public Player(int fx, int fy, Resources res, int lives) {
        super(lives);
        baseNumX = fx;
        baseNumY = fy;
        set(res.playerSprites.get(CharacterRecord.CHARACTERS[GameProgress.currentCharacter].name)); // makes this sprite a copy in every way of specified sprite
        max_lives = GameProgress.maxPlayerLives;
    }

    public int getBaseNumX() {
        return baseNumX;
    }

    public int getBaseNumY() {
        return baseNumY;
    }

    public void setBaseNumX(int fx) {
        baseNumX = fx;
    }

    public void setBaseNumY(int fy) {
        baseNumY = fy;
    }


    public void addLives(int amount) {
        lives += amount;
        if (lives > max_lives) {
            lives = max_lives;
        }
    }

    public void markVictorious() {
        // shows that player has won
        winning = true;
        winTime = timeAlive;
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        preDraw();
        // if it's the first 0.5sec of game, animate player approaching
        if (timeAlive < APPROACH_TIME) {
            float t = timeAlive / APPROACH_TIME;
            t = t * t;
            setPosition(t * sizeEvaluator.getBaseX(baseNumX), sizeEvaluator.getBaseY(baseNumY));
        } else if (winning){  // if player has won, animate him leaving to the right
            float t = 1;
            if (timeAlive - winTime < APPROACH_TIME) {  // make sure that we don't retrigger this once 0.5 seconds have passed
                t = (timeAlive - winTime) / APPROACH_TIME;
                t = t * t;
            }
            float fx = sizeEvaluator.getBaseX(baseNumX);
            setPosition(fx + t * (sizeEvaluator.getRightSideX() - fx), sizeEvaluator.getBaseY(baseNumY));
        } else {  // else just put him on right base
            setPosition(sizeEvaluator.getBaseX(baseNumX), sizeEvaluator.getBaseY(baseNumY));
        }
        super.draw(batch);
        postDraw();
    }
}
