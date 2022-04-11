package com.mirjamuher.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mirjamuher.dodginghero.logic.objects.CharacterRecord;

import java.util.HashMap;

public class Resources {
    TextureAtlas gameSprites;
    public TextureRegion ground;
    public TextureRegion wall;
    public TextureRegion base;
    public TextureRegion warning;
    public Sprite attackBonus;
    public Sprite healthBonus;
    public Sprite coinBonus;

    public TextureRegionDrawable soundBtn[];

    public BitmapFont gameFont;

    // Player Sprites
    public Sprite player;
    public HashMap<String, Sprite> playerSprites;  // string lookups are generally not great, used here because small part of our game

    // Enemy Sprites
    public Sprite enemy;
    public HashMap<Integer, Sprite> enemySprites;
    public static final int ATTACK_VERTICAL = 0;  // spider
    public static final int ATTACK_HORIZONAL = 1;  // ghost
    public static final int ATTACK_DIAGONAL = 2;  // bat
    public static final int ATTACK_RANDOM = 3;  // slime
    public static final int ATTACK_ULTIMATE = 4;  // skeleton

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
        coinBonus = new Sprite(gameSprites.findRegion(("coin")));

        // player characters initialisation
        player = new Sprite(gameSprites.findRegion("player"));
        playerSprites = new HashMap<>();
        playerSprites.put(CharacterRecord.CHAR_NAME_HUMAN, gameSprites.createSprite("player"));
        playerSprites.put(CharacterRecord.CHAR_NAME_GHOST, gameSprites.createSprite("ghost"));
        playerSprites.put(CharacterRecord.CHAR_NAME_SKELETON, gameSprites.createSprite("skeleton"));
        playerSprites.put(CharacterRecord.CHAR_NAME_SPIDER, gameSprites.createSprite("spider"));
        playerSprites.put(CharacterRecord.CHAR_NAME_SLIME, gameSprites.createSprite("slime"));

        // enemy initialisation
        enemy = new Sprite(gameSprites.findRegion("spider"));
        enemySprites = new HashMap<>();
        enemySprites.put(ATTACK_VERTICAL, gameSprites.createSprite("spider"));
        enemySprites.put(ATTACK_HORIZONAL, gameSprites.createSprite("ghost"));
        enemySprites.put(ATTACK_DIAGONAL, gameSprites.createSprite("bat"));
        enemySprites.put(ATTACK_RANDOM, gameSprites.createSprite("slime"));
        enemySprites.put(ATTACK_ULTIMATE, gameSprites.createSprite("skeleton"));

        // sound config images
        soundBtn = new TextureRegionDrawable[4];
        for (int i = 0; i < 4; i++) {
            soundBtn[i] = new TextureRegionDrawable(gameSprites.findRegion("sound" + i));
        }
    }

    public void dispose() {
        gameSprites.dispose();
    }
}
