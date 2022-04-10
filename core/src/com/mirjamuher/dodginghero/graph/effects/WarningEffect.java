package com.mirjamuher.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class WarningEffect extends Effect{
    private static final float WARNING_TIME = 0.5f;
    private int fieldX;
    private int fieldY;
    private Resources resources;

    // interface to alert gameLogic that warningEffect has ended (and player might get hurt)
    public interface WarningEffectListener {
        public void onEffectOver(WarningEffect effect);
    }

    private WarningEffectListener warningEffectListener;

    public static WarningEffect Create(int fx, int fy, EffectEngine parent, Resources res, WarningEffectListener listener) {
        WarningEffect effect = warningPool.obtain();
        effect.init(fx, fy, parent, res, listener);
        return effect;
    }

    public WarningEffect() {
    }

    public void init(int fx, int fy, EffectEngine parent, Resources res, WarningEffectListener listener) {
        super.init(parent);
        // fake constructor because on pool-resource one can't call actual constructor (because it's already constructed)
        fieldX = fx;
        fieldY = fy;
        resources = res;
        warningEffectListener = listener;
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
        // if our WarnEffect has been alive for 2 seconds, release it and call warnEffectListener interface
        if (timeAlive > WARNING_TIME && isAlive) {
            isAlive = false;
            if (warningEffectListener != null) {
                warningEffectListener.onEffectOver(this);
            }
        }
    }

    public int getFieldX() {
        return fieldX;
    }

    public int getFieldY() {
        return fieldY;
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
