package com.mirjamuher.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {
    TextureAtlas gameSprites;
    public TextureRegion ground;
    public TextureRegion wall;

    public Resources() {
        // loading the game sprites from the atlas (summary) file and assigning them
        gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
        ground = gameSprites.findRegion("ground");
        wall = gameSprites.findRegion("wall");
    }

    public void dispose() {
        gameSprites.dispose();
    }
}
