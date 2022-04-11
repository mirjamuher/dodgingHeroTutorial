package com.mirjamuher.dodginghero.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mirjamuher.dodginghero.logic.objects.CharacterRecord;

public class GameProgress {
    public static int playerLives = 2;
    public static int currentCharacter = 0;
    public static int currentGold = 0;

    // stages
    public static int stages[];  // how far character has progressed

    // define character price
    public static final int CHARACTER_PRICE = 1000;
    public static int levels[];  // level of each character. 0 = locked


    // Gdx Persistance files are basic key-value storage files which work for small games. For bigger games, you need binary files
    // below defines keys, right side is understood as filename, will be saved correctly automatically
    private static final String PROGRESS_SAVE_NAME = "progress";
    private static final String SAVE_KEY_LIVES = "lives";
    private static final String SAVE_KEY_PLAYER_GOLD = "playergold";
    private static final String SAVE_KEY_PLAYER_LEVEL = "playerlevel";
    private static final String SAVE_KEY_PLAYER_STAGE = "playerstage";


    public static int getEnemyLives() {
        return 3 + stages[currentCharacter] * 2;
    }

    public static int getEnemyDamange() {
        return 1 + stages[currentCharacter] / 10;  // increases enemy damage with higher levels
    }

    public static void Save() {
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        prefs.putInteger(SAVE_KEY_LIVES, playerLives);
        prefs.putInteger(SAVE_KEY_PLAYER_GOLD, currentGold);

        // save level of each character
        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i ++) {
            prefs.putInteger(SAVE_KEY_PLAYER_LEVEL + i, levels[i]);
            prefs.putInteger(SAVE_KEY_PLAYER_STAGE + i, stages[i]);
        }

        prefs.flush();  // very important to ensure values are persisted
    }

    public static void Load() {
        levels = new int[CharacterRecord.CHARACTERS.length];
        stages = new int[CharacterRecord.CHARACTERS.length];

        // prepares preference file
        Preferences prefs = Gdx.app.getPreferences(PROGRESS_SAVE_NAME);
        // load keys & set default value
        playerLives = prefs.getInteger(SAVE_KEY_LIVES, 3);
        currentGold = prefs.getInteger(SAVE_KEY_PLAYER_GOLD, 0);

        // get level of each character; default to level 0 && get stage
        for (int i = 0; i < CharacterRecord.CHARACTERS.length; i++) {
            levels[i] = prefs.getInteger(SAVE_KEY_PLAYER_LEVEL + i, i == 0 ? 1 : 0);
            stages[i] = prefs.getInteger(SAVE_KEY_PLAYER_STAGE + i, 0);
        }
    }

    public static int getNextUpgradeCost(int currentCharacter) {
        return levels[currentCharacter] * 2;
    }

    public static int getPlayerMaxHP() {
        CharacterRecord crntCharRecord = CharacterRecord.CHARACTERS[currentCharacter];
        return crntCharRecord.getMaxHP(levels[currentCharacter]);
    }

    public static int getPlayerDamage() {
        CharacterRecord crntCharRecord = CharacterRecord.CHARACTERS[currentCharacter];
        return crntCharRecord.getDmg(levels[currentCharacter]);
    }

    public static int getPlayerHealthRestored() {
        CharacterRecord crntCharRecord = CharacterRecord.CHARACTERS[currentCharacter];
        return crntCharRecord.getHpRestored(levels[currentCharacter]);
    }

    public static float getPlayerBonusReduction() {
        CharacterRecord crntCharRecord = CharacterRecord.CHARACTERS[currentCharacter];
        return crntCharRecord.getBonusSpawnReduction(levels[currentCharacter]);
    }

    public static int getPlayerBonusReductionValue() {
        CharacterRecord crntCharRecord = CharacterRecord.CHARACTERS[currentCharacter];
        return levels[currentCharacter / crntCharRecord.levelsForBonusSpawnUpgrade];
    }

    public static void Reset(boolean resetProgress) {
        // if character is killed (not when picking a new character), bring back a stage:
        if (resetProgress) {
            stages[currentCharacter] -= 5;
            if (stages[currentCharacter] < 0) {
                stages[currentCharacter] = 0;
            }
        }
        playerLives = getPlayerMaxHP();
    }

    public static void increaseStage() {
        // going up a level gives you increasingly more gold
        currentGold += 1 + stages[currentCharacter] / 4;
        stages[currentCharacter] ++;
    }
}
