package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mirjamuher.dodginghero.DodgingHero;
import com.mirjamuher.dodginghero.Resources;
import com.mirjamuher.dodginghero.graph.Background;
import com.mirjamuher.dodginghero.graph.SizeEvaluator;

public class GameScreen extends DefaultScreen {
    SpriteBatch batch;
    // 8 tile height & 12 tile width
    public static final int SCREEN_W = 12 * Resources.TILE_SIZE;  // 192 pixels
    public static final int SCREEN_H = 8 * Resources.TILE_SIZE;  // 122 pixels

    // for getting the bases positioned
    public static final int MAX_BASES_X_DIR = 3;
    public static final int MAX_BASES_Y_DIR = 3;
    private SizeEvaluator sizeEvaluator;

    // for rendering
    private Stage gameStage;
    private Background bg;

    public GameScreen(DodgingHero game) {
        super(game);
        batch = new SpriteBatch();  // responsible for sending command sto the video card (e.g. rendering things)

        // set gameStage to keep track of camera, spriteBatch, root group
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);   // Viewport manages camera and controls how stage is displayed on screen. ExtendedViewport is one of the types
        gameStage = new Stage(viewport, batch);  // InputProcessor that fires events to appropriate actors through act() method

        // create Background object
        bg = new Background();

        // creates sizeEvaluator
        sizeEvaluator = new SizeEvaluator(gameStage, game.res, MAX_BASES_X_DIR, MAX_BASES_Y_DIR);
    }

    public void drawBases() {
        batch.begin();
        // draw max_base_x + 1 rows of bases consisting of max_base_y + 1 columns of bases --> draw 4x4 bases
        for (int x = 0; x <= MAX_BASES_X_DIR; x++) {
            for (int y = 0; y <= MAX_BASES_Y_DIR; y++) {
                batch.draw(game.res.base, sizeEvaluator.getBaseX(x), sizeEvaluator.getBaseY(y));
            }
        }
        // temporary:
        batch.draw(game.res.player, sizeEvaluator.getBaseX(1), sizeEvaluator.getBaseY(1) + 2);

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
        gameStage.draw();
    }

    public void update(float delta) {
        gameStage.act(delta);  // updates each actor in gameStage based on time since last call (delta)
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameStage.getViewport().update(width, height, true);  // tells gameStage that window was resized and to center the Camera
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        gameStage.dispose();
    }
}
