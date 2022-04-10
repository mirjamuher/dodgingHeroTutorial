package com.mirjamuher.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {
    TextureAtlas gameSprites;
    public TextureRegion ground;
    public TextureRegion wall;
    public TextureRegion base;
    public TextureRegion warning;
    public Sprite attackBonus;
    public Sprite healthBonus;

    public BitmapFont gameFont;

    // Player is a Sprite
    public Sprite player;
    public Sprite enemy;

    public static final int TILE_SIZE = 16;

    public Resources() {
        // loading the game sprites from the atlas (summary) file and assigning them
        gameSprites = new TextureAtlas(Gdx.files.internal("packed/game.atlas"));
        ground = gameSprites.findRegion("ground");
        wall = gameSprites.findRegion("wall");
        base = gameSprites.findRegion("base");
        warning = gameSprites.findRegion("warning_new");

        gameFont = new BitmapFont(Gdx.files.internal("gamefont16.fnt"), Gdx.files.internal("gamefont16.png"), false);

        attackBonus = new Sprite(gameSprites.findRegion(("attack")));
        healthBonus = new Sprite(gameSprites.findRegion(("health")));

        player = new Sprite(gameSprites.findRegion("player"));
        enemy = new Sprite(gameSprites.findRegion("spider"));
    }

    public void dispose() {
        gameSprites.dispose();
    }
}
