package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class Enemy extends Sprite {

    Enemy(Resources res) {
        set(res.enemy);
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        setPosition(sizeEvaluator.getEnemyX(this), sizeEvaluator.getEnemyY(this));
        super.draw(batch); // sprite drawing method that takes care of the drawing for us
    }
}
