package com.mirjamuher.dodginghero.logic;

import com.badlogic.gdx.math.MathUtils;
import com.mirjamuher.dodginghero.graph.effects.EffectEngine;
import com.mirjamuher.dodginghero.logic.objects.Player;

public class GameLogic {
    // sets maximum number of bases
    public static final int NUM_OF_BASES_X = 3;
    public static final int NUM_OF_BASES_Y = 3;

    Player player;
    EffectEngine effectEngine;

    public GameLogic() {
        // generates player at a random location of the tiles
        player = new Player(MathUtils.random(NUM_OF_BASES_X), MathUtils.random(NUM_OF_BASES_Y));
        effectEngine = new EffectEngine();
    }

    // Player Logic
    public Player getPlayer() {
        return player;
    }

    public boolean CanMove(int fx, int fy) {
        return (fx >= 0 && fx <= NUM_OF_BASES_X) && (fy >= 0 && fy <= NUM_OF_BASES_Y);
    }

    public void AssignPlayerPosition(int fx, int fy) {
        player.setBaseNumX(fx);
        player.setBaseNumY(fy);
    }

    // Effect Logic
    public void update(float delta) {
        effectEngine.update(delta);
    }

    public EffectEngine getEffectEngine() {
        return effectEngine;
    }
}
