package com.mirjamuher.dodginghero.graph;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mirjamuher.dodginghero.Resources;

public class SizeEvaluator {
    private Stage measuredStage;
    private Resources res;

    // 4x4 field indicated by our max values:
    private final int MAX_NUM_BASES_X;  // max number of bases in x direction
    private final int MAX_NUM_BASES_Y; // max number of bases in y direction

    // how much extra distance between the bases
    private static final int BASE_MARGIN = 3;


    public SizeEvaluator(Stage stage, Resources resources, int maxBaseX, int maxBaseY) {
       // returns coordinates of every base that is being drawn
       measuredStage = stage;
       res = resources;
       MAX_NUM_BASES_X = maxBaseX;
       MAX_NUM_BASES_Y = maxBaseY;
   }

   // Player functinality

    /**
     *
     * @param crntBaseNumX current index of the base in x direction
     * @return
     */
   public float getBaseX(int crntBaseNumX) {
       // get center of screen (offset to left by tilesize) times maxTileSize
       return measuredStage.getWidth() / 2 - (Resources.TILE_SIZE + BASE_MARGIN) * (MAX_NUM_BASES_X + 1 - crntBaseNumX);
   }

    /**
     * take the index of the given base and returns magic (half of height minus 66% of tilesize + margin times half the number of basis plus one minus the current base index)
     * @param crntBaseNumY curernt index of the base in y direction
     * @return
     */
   public float getBaseY(int crntBaseNumY) {
       return measuredStage.getHeight() / 2 - ((Resources.TILE_SIZE + BASE_MARGIN) * 2 / 3.0f) * ((MAX_NUM_BASES_Y + 1) / 2.0f - crntBaseNumY);
   }

   // Enemy functionality
    public float getEnemyX(Sprite enemy) {
       return (measuredStage.getWidth() * 3 / 4) - enemy.getWidth() / 2;
    }

    public float getEnemyY(Sprite enemy) {
       return (measuredStage.getHeight() / 2 - enemy.getHeight() / 2);
    }
}
