package com.mirjamuher.dodginghero.logic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class Bonus extends Sprite implements Pool.Poolable {
    public static byte BONUS_TYPE_ATTACK = 0;
    public static byte BONUS_TYPE_HEALTH = 1;
    public static byte BONUS_TYPE_COIN = 2;

    private int baseNumX; // x-value of base it appears on
    private int baseNumY;

    private byte bonusType;

    public Bonus() {
        // used by pool to create Bonus objects
    }

    public void setup(int fx, int fy, byte bType, Resources res) {
        baseNumX = fx;
        baseNumY = fy;

        // Depending on type, assigns this Sprite to be a copy of indicated bonus sprite
        bonusType = bType;
        if (bType == BONUS_TYPE_ATTACK) {
            set(res.attackBonus);
        } else if (bType == BONUS_TYPE_HEALTH) {
            set(res.healthBonus);
        } else if (bType == BONUS_TYPE_COIN) {
            set(res.coinBonus);
        }
    }

    @Override
    public void reset() {

    }

    static final Pool<Bonus> bonusPool = new Pool<Bonus>() {
        @Override
        protected Bonus newObject() {
            return new Bonus();
        }
    };

    public void release() {
        bonusPool.free(this);
    }

    public static Bonus create(int fx, int fy, byte bType, Resources res) {
       Bonus bonus = bonusPool.obtain();
       bonus.setup(fx, fy, bType, res);
       return bonus;
    }

    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        // copied from Player as the bonus is spawning at the same stage
        setPosition(sizeEvaluator.getBaseX(baseNumX), sizeEvaluator.getBaseY(baseNumY));
        super.draw(batch);
    }

    public int getBaseNumX() {
        return baseNumX;
    }

    public int getBaseNumY() {
        return baseNumY;
    }

    public byte getBonusType() {
        return bonusType;
    }
}
