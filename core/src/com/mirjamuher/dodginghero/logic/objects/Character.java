package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mirjamuher.dodginghero.SoundManager;
import com.mirjamuher.dodginghero.logic.GameProgress;

public class Character extends Sprite {
    public static final float BLINK_TIME_AFTER_DMG = 0.25f;

    protected int lives;  // descendents (e.g. Player, Enemy) can access this variable
    protected float timeAlive;
    private float timeDmgWasTaken;  // when damage was taken

    public Character(int lives) {
        this.lives = lives;
        timeAlive = 0;
        timeDmgWasTaken = -1;
    }

    public int getLives() {
        return lives;
    }

    public void takeDamage(int val) {
        SoundManager.playSwingSound();
        timeDmgWasTaken = timeAlive;  // sets the time when dmg was taken to noe
        lives -= val;
        if (lives <= 0) {
            lives = 0;
        }
        GameProgress.playerLives = lives;
    }

    public void update(float delta) {
        timeAlive += delta;
    }

    public void preDraw() {
        // set the transparency of the Sprite (to show damage)
        if (timeAlive < timeDmgWasTaken + BLINK_TIME_AFTER_DMG) {
            // if we are still blinking from taking damage, t ensures that the reappearance is gradual
            float t = (timeAlive - timeDmgWasTaken) / BLINK_TIME_AFTER_DMG;  // 0 ... 1
            t = t * t;
            setColor(1, 1, 1, t);  // keeping rgb the same, just changing transparency
        }
    }

    public void postDraw() {
        setColor(1, 1, 1, 1);
    }

    public float getTimeDmgWasTaken() {
        return timeDmgWasTaken;
    }

    public float getTimeAlive() {
        return timeAlive;
    }
}
