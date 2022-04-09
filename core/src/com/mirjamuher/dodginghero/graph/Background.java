package com.mirjamuher.dodginghero.graph;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mirjamuher.dodginghero.Resources;

public class Background {
    public Background() {}

    public void draw(Stage stage, Resources res) {
        stage.getBatch().begin();

        // generate background
        for (int y = 0; y < stage.getHeight(); y += Resources.TILE_SIZE) {
            for (int x = 0; x < stage.getWidth(); x += Resources.TILE_SIZE) {
                    stage.getBatch().draw(res.ground, x, y, 0, 0, Resources.TILE_SIZE, Resources.TILE_SIZE, 1.01f, 1.01f, 0); // scales by 101% which avoids black lines when resizing!
            }
        }
        for (int x = 0; x < stage.getWidth(); x += Resources.TILE_SIZE) {
            stage.getBatch().draw(res.wall, x, stage.getHeight() - Resources.TILE_SIZE, 0, 0, Resources.TILE_SIZE, Resources.TILE_SIZE, 1.01f, 1.01f, 0); // scales by 101% which avoids black lines when resizing!
        }

        stage.getBatch().end();
    }
}
