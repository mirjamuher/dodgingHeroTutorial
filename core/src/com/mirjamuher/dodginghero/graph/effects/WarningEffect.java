package com.mirjamuher.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class WarningEffect extends Effect{
    private static final float WARNING_TIME = 2.0f;
    private int fieldX;
    private int fieldY;
    private Resources resources;

    public static WarningEffect Create(int fx, int fy, EffectEngine parent, Resources res) {
        WarningEffect effect = warningPool.obtain();
        effect.init(fx, fy, parent, res);
        return effect;
    }

    public WarningEffect() {
    }

    public void init(int fx, int fy, EffectEngine parent, Resources res) {
        super.init(parent);
        // fake constructor because on pool-resource one can't call actual constructor (because it's already constructed)
        fieldX = fx;
        fieldY = fy;
        resources = res;
    }

    @Override
    public void draw(SpriteBatch batch, SizeEvaluator sizeEvaluator) {
        batch.begin();
        batch.draw(resources.warning, sizeEvaluator.getBaseX(fieldX), sizeEvaluator.getBaseY(fieldY));
        batch.end();
    }

    @Override
    public void update (float delta) {
        super.update(delta);
        // if our object has been alive for 2 seconds, release it
        if (timeAlive > WARNING_TIME) {
            isAlive = false;
        }
    }

    @Override
    public void release() {
        warningPool.free(this);
    }

    static Pool<WarningEffect> warningPool = new Pool<WarningEffect>() {
        @Override
        protected WarningEffect newObject() {
            return new WarningEffect();
        }
    };
}
