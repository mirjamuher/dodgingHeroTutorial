package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;
import com.mirjamuher.dodginghero.logic.GameLogic;
import com.mirjamuher.dodginghero.logic.GameProgress;

public class Enemy extends Character {
    private static final float BASE_ATTACK_TIME = 3.0f;

    private static float SCALE_TIME = 0.5f;

    private float timeSinceAttack;
    private float nextAttackTime;

    private boolean[][] targetTiles;

    // to avoid cross-referencing between classes (enemy and GameLogic), we implement public interface passed from Gamelogic to enemy
    public interface EnemyAttackListener {
        void onAttack(boolean[][] tiles); // boolean array of size of field (x times y). If boolean value is true for tile, then the WarningEffect gets created
    }

    private EnemyAttackListener attackListener;

    public Enemy(Resources res, EnemyAttackListener listener) {
        super(GameProgress.getEnemyLives());
        set(res.enemy);
        resetAttackTime();
        attackListener = listener;

        // initialise boolean array
        targetTiles = new boolean[GameLogic.NUM_OF_BASES_X + 1][];
        for (int i = 0; i < GameLogic.NUM_OF_BASES_Y + 1; i++) {
            targetTiles[i] = new boolean[GameLogic.NUM_OF_BASES_Y + 1];
        }
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        preDraw();
        setPosition(sizeEvaluator.getEnemyX(this), sizeEvaluator.getEnemyY(this));
        if (timeAlive < SCALE_TIME) {
            float t = timeAlive/SCALE_TIME;
            t = t * t;
            setScale(t);
        } else {
            setScale(1);
        }
        super.draw(batch); // sprite drawing method that takes care of the drawing for us
        postDraw();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        timeSinceAttack += delta;
        if (timeSinceAttack > nextAttackTime) {
            // implement attack pattern
            int col1 = MathUtils.random(GameLogic.NUM_OF_BASES_X);
            int col2 = 0;
            do {  // ensures that col2 != col1
                col2 = MathUtils.random(GameLogic.NUM_OF_BASES_X);
            } while (col1 == col2);

            for (int x = 0; x < GameLogic.NUM_OF_BASES_X + 1; x++) {
                for (int y=0; y < GameLogic.NUM_OF_BASES_Y + 1; y++) {
                    targetTiles[x][y] = (x == col1 || x == col2);  // if x equals col1 or col2, this tile becomes true
                }
            }
            attackListener.onAttack(targetTiles);
            resetAttackTime();
        }
    }

    public void resetAttackTime() {
        timeSinceAttack = 0;
        nextAttackTime = BASE_ATTACK_TIME + MathUtils.random(2);
    }
}
