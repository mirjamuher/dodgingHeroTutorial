package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mirjamuher.dodginghero.DodgingHero;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.SoundManager;
import com.mirjamuher.dodginghero.graph.Background;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;
import com.mirjamuher.dodginghero.logic.GameLogic;
import com.mirjamuher.dodginghero.logic.GameProgress;
import com.mirjamuher.dodginghero.logic.objects.Bonus;
import com.mirjamuher.dodginghero.logic.objects.Player;

public class GameScreen extends DefaultScreen implements GameLogic.GameEventListener {
    SpriteBatch batch;

    // 8 tile height & 12 tile width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE;  // 192 pixels
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE;  // 122 pixels

    private static final float SHAKE_TIME_ON_IMG = 0.3f;
    private static final float SHAKE_DIST = 4.0f;  // shakes 4 pixels

    // helping with rendering objects at right spot
    private SizeEvaluator sizeEvaluator;
    private GameLogic gameLogic;

    // for rendering
    private Stage gameStage;
    private Background bg;
    private Player player;
    private ImageButton sndBtn;

    // fade timers
    public static final float GAME_FADEIN = 0.5f;
    public static final float GAME_FADEOUT = 0.5f;

    public GameScreen(final DodgingHero game) {
        super(game);
        batch = new SpriteBatch();  // responsible for sending command to the video card (e.g. rendering things)

        // set gameStage to keep track of camera, spriteBatch, root group
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);   // Viewport manages camera and controls how stage is displayed on screen. ExtendedViewport is one of the types
        gameStage = new Stage(viewport, batch);  // InputProcessor that fires events to appropriate actors through act() method

        // initialise helper functions
        sizeEvaluator = new SizeEvaluator(gameStage, game.res, GameLogic.NUM_OF_BASES_X, GameLogic.NUM_OF_BASES_Y, gameStage.getWidth());
        gameLogic = new GameLogic(game, this);
        player = gameLogic.getPlayer();

        // create sprite objects
        bg = new Background();

        // initialise sound buttons
        sndBtn = new ImageButton(game.res.soundBtn[GameProgress.soundVolume]);
        sndBtn.setPosition(gameStage.getWidth() - sndBtn.getWidth() - 10, 10);
        sndBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.adjustVolume();
                sndBtn.getStyle().imageUp = game.res.soundBtn[GameProgress.soundVolume]; // sets the button to look unpressed with new sound icon
                super.touchUp(event, x, y, pointer, button); // resets functionality to unpressed
            }
        });
        gameStage.addActor(sndBtn);

        // play bg-music
        SoundManager.playBattleMusic();

        // add listener to this gamestage
        gameStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        attemptMove(0, 1);
                        break;
                    case Input.Keys.DOWN:
                        attemptMove(0, -1);
                        break;
                    case Input.Keys.RIGHT:
                        attemptMove(1, 0);
                        break;
                    case Input.Keys.LEFT:
                        attemptMove(-1, 0);
                        break;
                }
                return false;
            }
        });

        // tell gdx that GameScreen handels user input
        Gdx.input.setInputProcessor(gameStage);

        // add fade-in when screen starts
        gameStage.addAction(
                new Action() {
                    float time = 0;
                    @Override
                    public boolean act(float delta) {
                        time += delta;
                        float t = time / GAME_FADEIN;
                        t *= t;  // quadratic easing

                        if (t > 1.0f){
                            t = 1.0f;  // to ensure transparency doesn't get messed up
                        }
                        batch.setColor(1, 1, 1, t);
                        return time >= GAME_FADEIN;
                    }
                }
        );
    }

    @Override
    public void render(float delta) {
        // update everything call
        update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // First draw background, then the stage, then effects
        bg.draw(gameStage, game.res);
        drawBases();

        // draw sprites
        batch.begin();
        for (Bonus bonus: gameLogic.getBonuses()) {
            bonus.draw(batch, sizeEvaluator);
        }
        player.draw(batch, sizeEvaluator);
        gameLogic.getEnemy().draw(batch, sizeEvaluator);  // logic will be swapping enemies, that's why we just get enemy here
        batch.end();

        // draw effects above player and other sprites
        gameLogic.getEffectEngine().draw(batch, sizeEvaluator);

        // center camera
        gameStage.getCamera().position.set(gameStage.getWidth() / 2, gameStage.getHeight() / 2, 0);

        // shake screen if gamer was hurt more recently than the shake time is set
        if (player.getLives() > 0 && player.getTimeAlive() - player.getTimeDmgWasTaken() < SHAKE_TIME_ON_IMG) {
            gameStage.getCamera().translate(-(SHAKE_DIST / 2) + MathUtils.random(SHAKE_DIST), -(SHAKE_DIST / 2) + MathUtils.random(SHAKE_DIST), 0);
        }
        gameStage.getCamera().update();

        // draw writing
        drawUI();

        gameStage.draw();
    }

    public void drawUI() {
        batch.begin();
        // lives
        drawShadowedText("LIVES:" + player.getLives(), 5, gameStage.getHeight() - 7, gameStage.getWidth(), Align.left, Color.WHITE);  // our lives
        drawShadowedText("ENEMY:" + gameLogic.getEnemy().getLives(), 0, gameStage.getHeight() - 7, gameStage.getWidth() - 5, Align.right, Color.WHITE);  // enemy lives

        // coins
        batch.draw(game.res.coinBonus, gameStage.getViewport().getScreenX() + 2, gameStage.getViewport().getScreenY() + 5);
        drawShadowedText("" + GameProgress.currentGold, gameStage.getViewport().getScreenX() + game.res.coinBonus.getWidth() + 4, gameStage.getViewport().getScreenY() + 8 + game.res.coinBonus.getHeight() / 2, gameStage.getWidth() - 4, Align.left, Color.WHITE);

        // defeat/victory
        if (player.getLives() <= 0) {
            ShowGameResult("DEFEAT!");
        }
        else if (gameLogic.getEnemy().getLives() <= 0) {
            ShowGameResult("VICTORY!");
        }
        batch.end();
    }

    private void ShowGameResult(String str) {
        drawShadowedText(str, 0, gameStage.getViewport().getScreenY() + gameStage.getHeight() / 2, gameStage.getWidth(), Align.center, Color.RED);
    }

    private void drawShadowedText(String str, float xPos, float yPos, float width, int align, Color color) {
        // draw thick shadow first
        game.res.gameFont.setColor(Color.BLACK);
        for (int i = -1; i < 2; i ++) {
            for (int j = -1; j < 2; j++) {
                game.res.gameFont.draw(batch, str, xPos + i, yPos + j, width, align, false);
            }
        }

        // draw actual lettering with specified colour
        game.res.gameFont.setColor(color);
        game.res.gameFont.draw(batch, str, xPos, yPos, width, align, false);
    }

    public void update(float delta) {
        gameStage.act(delta);  // updates each actor in gameStage based on time since last call (delta)
        gameLogic.update(delta);  // updates gamelogic (effects so far)
    }

    public void drawBases() {
        batch.begin();
        // draw max_base_x + 1 rows of bases consisting of max_base_y + 1 columns of bases --> draw 4x4 bases
        for (int x = 0; x <= GameLogic.NUM_OF_BASES_X; x++) {
            for (int y = 0; y <= GameLogic.NUM_OF_BASES_Y; y++) {
                batch.draw(game.res.base, sizeEvaluator.getBaseX(x), sizeEvaluator.getBaseY(y));
            }
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);  // tells gameStage that window was resized and to center the Camera
        sizeEvaluator.setRightSideX(gameStage.getWidth());
    }

    public void attemptMove(int dx, int dy) {
        // if player can legally move, assign new position and move sprite
        // if player is out of lives of enemy is defeated, lock movement
        if (player.getLives() > 0 && gameLogic.getEnemy().getLives() > 0 && gameLogic.CanMove(player.getBaseNumX() + dx, player.getBaseNumY() + dy)) {
            SoundManager.playWalkSound();
            gameLogic.AssignPlayerPosition(player.getBaseNumX() + dx, player.getBaseNumY() + dy);
        }
    }

    @Override
    public void onGameEnd(final boolean playerWon) {
        gameStage.addAction(
                Actions.sequence(  // allows multiple actions
                        // add actions to actors. in this project we have Sprites, not actors, so this is a bit more cumbersome than it needs to be
                        new Action() {
                            float time = 0;

                            @Override
                            public boolean act(float delta) {
                                time += delta;
                                float t = time / GAME_FADEOUT;
                                t += t;
                                batch.setColor(1, 1, 1, 1 - t);  // this ensures that transparency moves towards 0
                                return time >= GAME_FADEOUT;
                            }
                        },
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                dispose(); // disposes current screen
                                if (playerWon) {
                                    game.setScreen(new GameScreen(game));
                                } else {
                                    game.setScreen(new CharacterSelectionScreen(game));
                                }
                                return true;
                            }
                        }
                )
        );
    }

    @Override
    public void onBonusPickup(byte bonusType) {
        if (bonusType == Bonus.BONUS_TYPE_COIN) {
            SoundManager.playCoinSound();
        } else if (bonusType == Bonus.BONUS_TYPE_HEALTH) {
            SoundManager.playHealSound();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        SoundManager.stopBattleMusic();
        batch.dispose();
        gameStage.dispose();
        Gdx.input.setInputProcessor(null);  // if we move to new screen, this screen no longer handels user input
    }
}
