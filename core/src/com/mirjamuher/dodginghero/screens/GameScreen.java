package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mirjamuher.dodginghero.DodgingHero;

public class GameScreen extends DefaultScreen {
    SpriteBatch batch;


    public GameScreen(DodgingHero game) {
        super(game);
        batch = new SpriteBatch();  // responsible for sending command sto the videocard (e.g. rendering things)
    }

    @Override
    public void render(float delte) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(game.res.ground, 0, 0);
        batch.draw(game.res.wall, 0, 16);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
