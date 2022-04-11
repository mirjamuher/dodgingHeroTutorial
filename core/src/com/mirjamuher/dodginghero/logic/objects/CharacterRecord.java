package com.mirjamuher.dodginghero.logic.objects;

public class CharacterRecord {
    // stores baseline stats of all characters
    public final int levelForHPUpgrade;  // x levels HP goes up
    public final int levelForHpRegenUpgrade;  // x levels for HP bonus increasing
    public final int levelForAttackUpgrade;
    public final int levelsForBonusSpawnUpgrade;

    public final String name;

    public CharacterRecord(int lvlHP, int lvlRegen, int lvlAttack, int lvlBonus, String name) {
        levelForHPUpgrade = lvlHP;
        levelForHpRegenUpgrade = lvlRegen;
        levelForAttackUpgrade = lvlAttack;
        levelsForBonusSpawnUpgrade = lvlBonus;
        this.name = name;
    }

    // Player character options
    public static String CHAR_NAME_HUMAN = "Human";
    public static String CHAR_NAME_SPIDER = "Spider";
    public static String CHAR_NAME_SKELETON = "Mr. Skeletal";
    public static String CHAR_NAME_GHOST = "Ghost";
    public static String CHAR_NAME_SLIME = "Slimey";

    public static CharacterRecord[] CHARACTERS = {
            new CharacterRecord(2, 2, 4, 4, CHAR_NAME_HUMAN),
            new CharacterRecord(3, 6, 3, 3, CHAR_NAME_SPIDER),
            new CharacterRecord(6, 12, 1, 3, CHAR_NAME_SKELETON),
            new CharacterRecord(4, 4, 2, 4, CHAR_NAME_GHOST),
            new CharacterRecord(3, 3, 4, 1, CHAR_NAME_SLIME),
    };

    public int getMaxHP(int lvl) {
        return 3 + lvl / levelForHPUpgrade;
    }

    public int getDmg(int lvl) {
        return 1 + lvl / levelForAttackUpgrade;
    }

    public int getHpRestored(int lvl) {
        return 1 + lvl / levelForHpRegenUpgrade;
    }

    public float getBonusSpawnReduction(int lvl) {
        int bonusSpawnLvl = lvl / levelsForBonusSpawnUpgrade;
        return bonusSpawnLvl / (30.0f * bonusSpawnLvl);  // 30 enables diminishing returns
    }
}
