package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mirjamuher.dodginghero.DodgingHero;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.Background;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;
import com.mirjamuher.dodginghero.logic.GameLogic;
import com.mirjamuher.dodginghero.logic.objects.Bonus;
import com.mirjamuher.dodginghero.logic.objects.Player;

public class GameScreen extends DefaultScreen implements InputProcessor {
    SpriteBatch batch;

    // 8 tile height & 12 tile width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE;  // 192 pixels
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE;  // 122 pixels

    // helping with rendering objects at right spot
    private SizeEvaluator sizeEvaluator;
    private GameLogic gameLogic;

    // for rendering
    private Stage gameStage;
    private Background bg;
    private Player player;

    public GameScreen(DodgingHero game) {
        super(game);
        batch = new SpriteBatch();  // responsible for sending command to the video card (e.g. rendering things)

        // set gameStage to keep track of camera, spriteBatch, root group
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);   // Viewport manages camera and controls how stage is displayed on screen. ExtendedViewport is one of the types
        gameStage = new Stage(viewport, batch);  // InputProcessor that fires events to appropriate actors through act() method

        // initialise helper functions
        sizeEvaluator = new SizeEvaluator(gameStage, game.res, GameLogic.NUM_OF_BASES_X, GameLogic.NUM_OF_BASES_Y);
        gameLogic = new GameLogic(game);
        player = gameLogic.getPlayer();

        // create sprite objects
        bg = new Background();

        // tell gdx that GameScreen handels user input
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // First draw background, then the stage, then effects
        bg.draw(gameStage, game.res);
        drawBases();
        gameLogic.getEffectEngine().draw(batch, sizeEvaluator);

        // draw sprites
        batch.begin();
        for (Bonus bonus: gameLogic.getBonuses()) {
            bonus.draw(batch, sizeEvaluator);
        }
        player.draw(batch, sizeEvaluator);
        gameLogic.getEnemy().draw(batch, sizeEvaluator);  // logic will be swapping enemies, that's why we just get enemy here
        batch.end();

        // draw writing
        drawUI();

        gameStage.draw();
    }

    public void drawUI() {
        batch.begin();
        drawShadowedText("LIVES:" + player.getLives(), 5, gameStage.getHeight() - 7, gameStage.getWidth(), Align.left, Color.WHITE);  // our lives
        drawShadowedText("ENEMY:" + gameLogic.getEnemy().getLives(), 0, gameStage.getHeight() - 7, gameStage.getWidth() - 5, Align.right, Color.WHITE);  // enemy lives
        if (player.getLives() <= 0) {
            drawShadowedText("DEFEAT!", 0, gameStage.getViewport().getScreenY() + gameStage.getHeight() / 2, gameStage.getWidth(), Align.center, Color.RED);
        }
        batch.end();
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

        // if gameover, stop updating game logic
        if (player.getLives() > 0) {
            gameLogic.update(delta);  // updates gamelogic (effects so far)
        }
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
    }

    public void attemptMove(int dx, int dy) {
        // if player can legally move, assign new position and move sprite
        // if player is out of lives, lock movement
        if (player.getLives() > 0 && gameLogic.CanMove(player.getBaseNumX() + dx, player.getBaseNumY() + dy)) {
            gameLogic.AssignPlayerPosition(player.getBaseNumX() + dx, player.getBaseNumY() + dy);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
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

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        gameStage.dispose();
        Gdx.input.setInputProcessor(null);  // if we move to new screen, this screen no longer handels user input
    }
}
