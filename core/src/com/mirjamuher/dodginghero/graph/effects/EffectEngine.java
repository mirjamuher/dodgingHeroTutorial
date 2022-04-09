package com.mirjamuher.dodginghero.graph.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class EffectEngine {
    // contains list of effects; every program update it goes through list, updates all effects and draws them
    // if an effect is expired, it gets removed from the list

    List<Effect> effects;

    public EffectEngine() {
        effects = new ArrayList<Effect>();
    }

    public void add(Effect effect) {
        effects.add(effect);
    }

    public void update(float delta) {
        int i = 0;
        // iterates through list
        while (i < effects.size()) {
            // updates effect time duration
            effects.get(i).update(delta);
            if (effects.get(i).isAlive) {
                i++;
            } else {
                // if the effect is not alive, we release it and remove it from the list
                effects.get(i).release();
                effects.remove(i);  // don't need to i++ because objects move to the left
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).draw(batch);
        }
    }

    public void clear() {
        // shortcut to reset all stored effects
        while (effects.size() > 0) {
            effects.get(0).reset();
            effects.remove(0);
        }
    }
}
