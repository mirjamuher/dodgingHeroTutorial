package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    private int baseNumX;  // X position of Player
    private int baseNumY;  // Y position of Player

    public Player(int fx, int fy) {
        baseNumX = fx;
        baseNumY = fy;
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
}
