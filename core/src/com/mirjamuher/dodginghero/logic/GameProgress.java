package com.mirjamuher.dodginghero.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mirjamuher.dodginghero.logic.objects.CharacterRecord;

public class GameProgress {
    public static int playerLives = 2;
    public static int maxPlayerLives = 3;
    public static int playerDamage = 1;
    public static int currentLevel = 0;
    public static int currentCharacter = 0;
    public static int currentGold = 0;

    // define character price
    public static final int CHARACTER_PRICE = 1000;
    public static int levels[];  // level of each character. 0 = locked


    // Gdx Persistance files are basic key-value storage files which work for small games. For bigger games, you need binary files
    // below defines keys, right side is understood as filename, will be saved correctly automatically
    private static final String PROGRESS_SAVE_NAME = "progress";
    private static final String SAVE_KEY_LIVES = "lives";
    private static final String SAVE_KEY_LIVES_MAX = "livemax";
    private static final String SAVE_KEY_CURRENT_LEVEL = "currentlevel";
    private static final String SAVE_KEY_PLAYER_DAMAGE = "playerdamage";
    private static final String SAVE_KEY_PLAYER_GOLD = "playergold";
    private static final String SAVE_KEY_PLAYER_LEVEL = "playerlevel";

    public static int getEnemyLives() {
        return 3 + currentLevel * 2;
    }

    public static void Save() {
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        prefs.putInteger(SAVE_KEY_LIVES, playerLives);
        prefs.putInteger(SAVE_KEY_LIVES_MAX, maxPlayerLives);
        prefs.putInteger(SAVE_KEY_CURRENT_LEVEL, currentLevel);
        prefs.putInteger(SAVE_KEY_PLAYER_DAMAGE, playerDamage);
        prefs.putInteger(SAVE_KEY_PLAYER_GOLD, currentGold);

        // save level of each character
        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i ++) {
            prefs.putInteger(SAVE_KEY_PLAYER_LEVEL + i, levels[i]);
        }

        prefs.flush();  // very important to ensure values are persisted
    }

    public static void Load() {
        levels = new int[CharacterRecord.CHARACTERS.length];

        // prepares preference file
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        // load keys & set default value
        playerLives = prefs.getInteger(SAVE_KEY_LIVES, 3);
        maxPlayerLives = prefs.getInteger(SAVE_KEY_LIVES_MAX, 3);
        currentLevel = prefs.getInteger(SAVE_KEY_CURRENT_LEVEL, 0);
        playerDamage = prefs.getInteger(SAVE_KEY_PLAYER_DAMAGE, 1);
        currentGold = prefs.getInteger(SAVE_KEY_PLAYER_GOLD, 0);

        // get level of each character; default to level 0
        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i++) {
            levels[i] = prefs.getInteger(SAVE_KEY_PLAYER_LEVEL + i, i == 0 ? 1 : 0);
        }
    }

    public static int getNextUpgradeCost(int currentCharacter) {
        return levels[currentCharacter] * 2;
    }

    public static void Reset() {
        playerLives = 3;
        maxPlayerLives = 3;
        currentLevel = 0;
        playerDamage = 1;
    }
}
