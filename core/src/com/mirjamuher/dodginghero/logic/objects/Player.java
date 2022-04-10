package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class Player extends Character {
    private int baseNumX;  // X position of Player
    private int baseNumY;  // Y position of Player

    private final int max_lives;
    public static final float APPROACH_TIME = 0.5f;

    public Player(int fx, int fy, Resources res, int lives) {
        super(lives);
        baseNumX = fx;
        baseNumY = fy;
        set(res.player); // makes this sprite a copy in every way of specified sprite
        max_lives = lives;
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

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        preDraw();
        // if it's the first 0.5sec of game, animate player approaching
        if (timeAlive < APPROACH_TIME) {
            float t = timeAlive / APPROACH_TIME;
            t = t * t;
            setPosition(t * sizeEvaluator.getBaseX(baseNumX), sizeEvaluator.getBaseY(baseNumY));
        } else {
            setPosition(sizeEvaluator.getBaseX(baseNumX), sizeEvaluator.getBaseY(baseNumY));
        }
        super.draw(batch);
        postDraw();
    }
}
