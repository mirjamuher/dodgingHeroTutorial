package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mirjamuher.dodginghero.DodgingHero;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.Background;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;
import com.mirjamuher.dodginghero.logic.GameLogic;
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
        batch = new SpriteBatch();  // responsible for sending command sto the video card (e.g. rendering things)

        // set gameStage to keep track of camera, spriteBatch, root group
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);   // Viewport manages camera and controls how stage is displayed on screen. ExtendedViewport is one of the types
        gameStage = new Stage(viewport, batch);  // InputProcessor that fires events to appropriate actors through act() method

        // initialise helper functions
        sizeEvaluator = new SizeEvaluator(gameStage, game.res, GameLogic.NUM_OF_BASES_X, GameLogic.NUM_OF_BASES_Y);
        gameLogic = new GameLogic();
        player = gameLogic.getPlayer();

        // create sprite objects
        bg = new Background();
        player.set(game.res.player);  // makes sprite a copy of the game.res.player sprite
        refreshPlayer(); // sets position of sprite in line with correct base

        // tell gdx that GameScreen handels user input
        Gdx.input.setInputProcessor(this);
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
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // First draw background, then the stage
        bg.draw(gameStage, game.res);
        drawBases();

        batch.begin();
        player.draw(batch);
        batch.end();

        gameStage.draw();
    }

    public void update(float delta) {
        gameStage.act(delta);  // updates each actor in gameStage based on time since last call (delta)
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);  // tells gameStage that window was resized and to center the Camera
        refreshPlayer();
    }

    public void refreshPlayer(){
        player.setPosition(sizeEvaluator.getBaseX(player.getBaseNumX()), sizeEvaluator.getBaseY(player.getBaseNumY()));
    }

    public void AttemptMove(int dx, int dy) {
        // if player can legally move, assign new position and move sprite
        if (gameLogic.CanMove(player.getBaseNumX() + dx, player.getBaseNumY() + dy)) {
            gameLogic.AssignPlayerPosition(player.getBaseNumX() + dx, player.getBaseNumY() + dy);
            refreshPlayer();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                AttemptMove(0, 1);
                break;
            case Input.Keys.DOWN:
                AttemptMove(0, -1);
                break;
            case Input.Keys.RIGHT:
                AttemptMove(1, 0);
                break;
            case Input.Keys.LEFT:
                AttemptMove(-1, 0);
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
