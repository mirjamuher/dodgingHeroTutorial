package com.mirjamuher.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {
    TextureAtlas gameSprites;
    public TextureRegion ground;
    public TextureRegion wall;
    public TextureRegion base;
    public TextureRegion warning;

    // Player is a Sprite
    public Sprite player;

    public static final int TILE_SIZE = 16;

    public Resources() {
        // loading the game sprites from the atlas (summary) file and assigning them
        gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
        ground = gameSprites.findRegion("ground");
        wall = gameSprites.findRegion("wall");
        base = gameSprites.findRegion("base");
        warning = gameSprites.findRegion("warning");

        player = new Sprite(gameSprites.findRegion("player"));
    }

    public void dispose() {
        gameSprites.dispose();
    }
}
