package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;
import com.mirjamuher.dodginghero.logic.GameLogic;
import com.mirjamuher.dodginghero.logic.GameProgress;

public class Enemy extends Character {
    private static final float BASE_ATTACK_TIME = 1.0f;
    private static final float WARM_UP_TIME = 2.0f;

    private static float SCALE_TIME = 0.5f;

    private float timeSinceAttack;
    private float nextAttackTime;
    private final int attackType;

    private boolean[][] targetTiles;

    // to avoid cross-referencing between classes (enemy and GameLogic), we implement public interface passed from Gamelogic to enemy
    public interface EnemyAttackListener {
        void onAttack(boolean[][] tiles); // boolean array of size of field (x times y). If boolean value is true for tile, then the WarningEffect gets created
    }

    private EnemyAttackListener attackListener;

    public Enemy(Resources res, EnemyAttackListener listener, int type) {
        super(GameProgress.getEnemyLives());
        // for bigger projects better to have a separate setter-getter class
        attackType = type;  // according to attackType, attack-layout etc. gets set
        set(res.enemySprites.get(type));
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
        // if enough time since last attack has passed and the "warm up time" after game start has passed, attack
        if (timeSinceAttack > nextAttackTime && timeAlive > WARM_UP_TIME) {
            switch (attackType) {
                case Resources.ATTACK_VERTICAL:
                    performVerticalLineAttack();
                    break;
                case Resources.ATTACK_HORIZONAL:
                    performHorizontalLineAttack();
                    break;
                case Resources.ATTACK_DIAGONAL:
                    performDiagonalAttack();
                    break;
                case Resources.ATTACK_RANDOM:
                    performRandomAttack();
                    break;
                case Resources.ATTACK_ULTIMATE:
                    performUltimateAttack();
                    break;
            }
            attackListener.onAttack(targetTiles);
            resetAttackTime();
        }
    }

    private void performVerticalLineAttack() {
        // implement vertical attack pattern
        int col1 = MathUtils.random(GameLogic.NUM_OF_BASES_X);
        int col2 = 0;
        do {  // ensures that col2 != col1
            col2 = MathUtils.random(GameLogic.NUM_OF_BASES_X);
        } while (col1 == col2);

        for (int x = 0; x < GameLogic.NUM_OF_BASES_X + 1; x++) {
            for (int y = 0; y < GameLogic.NUM_OF_BASES_Y + 1; y++) {
                targetTiles[x][y] = (x == col1 || x == col2);  // if x equals col1 or col2, this tile becomes true
            }
        }
    }

    private void performHorizontalLineAttack() {
        // implement horizontal attack pattern
        int row1 = MathUtils.random(GameLogic.NUM_OF_BASES_Y);
        int row2 = 0;
        do {  // ensures that row2 != row1
            row2 = MathUtils.random(GameLogic.NUM_OF_BASES_Y);
        } while (row1 == row2);

        for (int x = 0; x < GameLogic.NUM_OF_BASES_X + 1; x++) {
            for (int y = 0; y < GameLogic.NUM_OF_BASES_Y + 1; y++) {
                targetTiles[x][y] = (y == row1 || y == row2);  // if y equals row1 or row2, this tile becomes true
            }
        }
    }

    private void performDiagonalAttack() {
        int dx1 = -1 + MathUtils.random(1) * 2;  // 1 .. -1
        int dx2 = -1 + MathUtils.random(1) * 2;  // 1 .. -1

        int col1 = MathUtils.random(GameLogic.NUM_OF_BASES_X);
        int col2 = 0;
        do {  // ensures that col2 != col1
            col2 = MathUtils.random(GameLogic.NUM_OF_BASES_X);
        } while (col1 == col2);

        // set all fields as false
        for (int x = 0; x < GameLogic.NUM_OF_BASES_X + 1; x++) {
            for (int y = 0; y < GameLogic.NUM_OF_BASES_Y + 1; y++) {
                targetTiles[x][y] = false;  // if x equals col1 or col2, this tile becomes true
            }
        }

        // set diagonal ones to true
        fillDiagonal(col1, dx1);
        fillDiagonal(col2, dx2);
    }

    private void fillDiagonal(int xstart, int dx) {
        for (int i=0; i <= GameLogic.NUM_OF_BASES_Y; i++) {
            int nx = xstart + i * dx;
            if (nx > GameLogic.NUM_OF_BASES_X) {
                nx = nx - GameLogic.NUM_OF_BASES_X - 1;
            }
            if (nx < 0) {
                nx = nx + GameLogic.NUM_OF_BASES_X + 1;
            }

            targetTiles[nx][i] = true;
        }
    }

    private void performRandomAttack() {
        // set all fields as false
        for (int x = 0; x < GameLogic.NUM_OF_BASES_X + 1; x++) {
            for (int y = 0; y < GameLogic.NUM_OF_BASES_Y + 1; y++) {
                targetTiles[x][y] = false;  // if x equals col1 or col2, this tile becomes true
            }
        }
        // set ten random fields to true
        for (int i = 0; i < 10; i++) {
            int nx = MathUtils.random(GameLogic.NUM_OF_BASES_X);
            int ny = MathUtils.random(GameLogic.NUM_OF_BASES_Y);
            targetTiles[nx][ny] = true;
        }
    }

    private void performUltimateAttack() {
        int rnd = MathUtils.random(3);
        switch (rnd) {
            case 0:
                performVerticalLineAttack();
                break;
            case 1:
                performHorizontalLineAttack();
                break;
            case 2:
                performDiagonalAttack();
                break;
            default:
                performRandomAttack();
                break;
        }
    }

    public void resetAttackTime() {
        timeSinceAttack = 0;
        nextAttackTime = BASE_ATTACK_TIME + MathUtils.random(2);
    }
}
