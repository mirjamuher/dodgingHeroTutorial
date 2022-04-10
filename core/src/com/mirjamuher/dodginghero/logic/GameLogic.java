package com.mirjamuher.dodginghero.logic;

import com.badlogic.gdx.math.MathUtils;
import com.mirjamuher.dodginghero.DodgingHero;
import com.mirjamuher.dodginghero.graph.effects.EffectEngine;
import com.mirjamuher.dodginghero.graph.effects.WarningEffect;
import com.mirjamuher.dodginghero.logic.objects.Bonus;
import com.mirjamuher.dodginghero.logic.objects.Enemy;
import com.mirjamuher.dodginghero.logic.objects.Player;

import java.util.ArrayList;

public class GameLogic implements Enemy.EnemyAttackListener, WarningEffect.WarningEffectListener {
    // sets maximum number of bases
    public static final int NUM_OF_BASES_X = 3;
    public static final int NUM_OF_BASES_Y = 3;
    private static final int DEFAULT_PLAYER_LIVES = 3;
    private static final float BONUS_SPAWN_INTEVAL = 2;
    private static final int MAX_BONUSES_ON_FIELD = 3;

    public interface GameEventListener {
        // when game ends, onGameEnd will notify others of that fact
        void onGameEnd(boolean playerWon);
    }

    float gameTime;

    DodgingHero game;
    Player player;
    Enemy enemy;
    ArrayList<Bonus> bonuses;
    float lastBonusSpawnTime;

    EffectEngine effectEngine;
    GameEventListener gameEventListener;

    public GameLogic(DodgingHero game, GameEventListener listener) {
        this.game = game;
        // generates player at a random location of the tiles
        player = new Player(MathUtils.random(NUM_OF_BASES_X), MathUtils.random(NUM_OF_BASES_Y), game.res, DEFAULT_PLAYER_LIVES);
        enemy = new Enemy(game.res, this);
        effectEngine = new EffectEngine();
        gameEventListener = listener;

        bonuses = new ArrayList<Bonus>();
        gameTime = 0;
        lastBonusSpawnTime = 0;
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

        // check if there is a bonus to pick up
        for (int i = bonuses.size() - 1; i >= 0; i--) {
            Bonus currentBonus = bonuses.get(i);
            if (currentBonus.getBaseNumX() == fx && currentBonus.getBaseNumY() == fy) {

                if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_ATTACK) {
                    enemy.takeDamage(1);
                    if (enemy.getLives() <= 0) {
                        player.markVictorious();
                        gameEventListener.onGameEnd(true);
                    }
                } else if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_HEALTH) {
                    player.addLives(1);
                }

                currentBonus.release();
                bonuses.remove(i);
                break;
            }
        }
    }

    // Enemy Logic
    public Enemy getEnemy() {
        return enemy;
    }

    @Override
    public void onAttack(boolean[][] tiles) {
        // from Enemy interface
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++){
                if (tiles[x][y]) {
                    WarningEffect.Create(x, y, effectEngine, game.res, this);
                }
            }
        }
    }

    // Effect Logic
    public void update(float delta) {
        // measures gametime since game started
        gameTime += delta;

        // updates sprites
        player.update(delta);

        // if no gameover/gamewon condition was hit, update
        if (player.getLives() > 0 && enemy.getLives() > 0) {
            effectEngine.update(delta);
            enemy.update(delta);
            if (lastBonusSpawnTime + BONUS_SPAWN_INTEVAL < gameTime && bonuses.size() < MAX_BONUSES_ON_FIELD) {
                SpawnRandomBonus();
            }
        }
    }

    // handle Bonuses
    private void SpawnRandomBonus() {
        int fx = 0;
        int fy = 0;
        boolean targetNonEmpty = true;

        do {
            fx = MathUtils.random(NUM_OF_BASES_X);
            fy = MathUtils.random(NUM_OF_BASES_Y);
            targetNonEmpty = player.getBaseNumX() == fx || fy == player.getBaseNumY();

            for (int i = 0; i < bonuses.size() && (!targetNonEmpty); i++)
            {
                if (bonuses.get(i).getBaseNumX() == fx &&
                        bonuses.get(i).getBaseNumY() == fy)
                {
                    targetNonEmpty = true;
                }
            }
        } while (targetNonEmpty);

        bonuses.add(Bonus.Create(fx, fy, MathUtils.random(3) == 0 ? Bonus.BONUS_TYPE_HEALTH : Bonus.BONUS_TYPE_ATTACK, game.res));
        lastBonusSpawnTime = gameTime;
    }

    public ArrayList<Bonus> getBonuses() {
        return bonuses;
    }

    // handle Effects
    public EffectEngine getEffectEngine() {
        return effectEngine;
    }


    @Override
    public void onEffectOver(WarningEffect effect) {
        if (effect.getFieldX() == player.getBaseNumX() && effect.getFieldY() == player.getBaseNumY()) {
            player.takeDamage(1);
        }
    }
}
