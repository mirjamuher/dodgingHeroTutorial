package com.mirjamuher.dodginghero.logic;

import com.badlogic.gdx.Game;
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
    private float BONUS_SPAWN_INTEVAL = 1;
    private static final int MAX_BONUSES_ON_FIELD = 3;

    public interface GameEventListener {
        // when game ends, onGameEnd will notify others of that fact
        void onGameEnd(final boolean playerWon);
        void onBonusPickup(byte bonusType);
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
        player = new Player(MathUtils.random(NUM_OF_BASES_X), MathUtils.random(NUM_OF_BASES_Y), game.res, GameProgress.playerLives);
        enemy = new Enemy(game.res, this, MathUtils.random(game.res.enemySprites.size() - 1)); // generates a random enemy
        effectEngine = new EffectEngine();
        gameEventListener = listener;

        bonuses = new ArrayList<Bonus>();
        BONUS_SPAWN_INTEVAL = 2.0f * (GameProgress.getPlayerBonusReduction());
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
                gameEventListener.onBonusPickup(currentBonus.getBonusType());  // plays sound
                if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_ATTACK) {
                    enemy.takeDamage(GameProgress.getPlayerDamage());
                    // if win game condition fulfilled
                    if (enemy.getLives() <= 0) {
                        GameProgress.increaseStage();
                        GameProgress.playerLives = player.getLives();
                        player.markVictorious();
                        gameEventListener.onGameEnd(true);
                    }
                } else if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_HEALTH) {
                    player.addLives(GameProgress.getPlayerHealthRestored());
                } else if (currentBonus.getBonusType() == Bonus.BONUS_TYPE_COIN) {
                    GameProgress.currentGold += 1000;
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

        // 1/8 change for health, 1/3 change for coin, rest attack
        int rnd = MathUtils.random(7);
        byte activeBonus = Bonus.BONUS_TYPE_ATTACK;
        if (rnd > 6) {
            activeBonus = Bonus.BONUS_TYPE_HEALTH;
        }
        else if (rnd > 4) {
            activeBonus = Bonus.BONUS_TYPE_COIN;
        }

        bonuses.add(Bonus.create(fx, fy, activeBonus, game.res));
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
        // when effect is over, if the effect is where the player is, player takes damage
        if (effect.getFieldX() == player.getBaseNumX() && effect.getFieldY() == player.getBaseNumY()) {
            player.takeDamage(GameProgress.getEnemyDamange());
            if (player.getLives() <= 0) {
                gameEventListener.onGameEnd(false);
                GameProgress.Reset(true);  // reset level player is at when he dies
            }
        }
    }
}
