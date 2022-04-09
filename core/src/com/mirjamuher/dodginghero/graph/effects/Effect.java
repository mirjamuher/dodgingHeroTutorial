package com.mirjamuher.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

// an abstract class is designed to be inherited from. They cannot be instantiated directly
public abstract class Effect implements Pool.Poolable{
    // point of this: instead of re-initiating an effect over and over, this keeps it alive in the memory until it is needed again
    protected boolean isAlive;
    protected float timeAlive;

    public Effect() {
        isAlive = false;
        timeAlive = 0;
    }

    @Override
    public void reset() {
    }

    public void init(EffectEngine parent) {
        // used when existing effect is being called into use
        isAlive = true;
        timeAlive = 0;
        parent.add(this);
    }

    public void update(float delta) {
        timeAlive += delta;
    }

    public abstract void draw(SpriteBatch batch);

    public boolean isAlive() {
        return isAlive;
    }

    public void release() {
    }
}
