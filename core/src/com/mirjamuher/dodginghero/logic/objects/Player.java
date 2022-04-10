package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class Player extends Sprite {
    private int baseNumX;  // X position of Player
    private int baseNumY;  // Y position of Player

    public Player(int fx, int fy, Resources res) {
        baseNumX = fx;
        baseNumY = fy;
        set(res.player); // makes this sprite a copy in every way of specified sprite
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

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        setPosition(sizeEvaluator.getBaseX(baseNumX), sizeEvaluator.getBaseY(baseNumY));
        super.draw(batch);
    }
}
