package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mirjamuher.dodginghero.DodgingHero;

public class CharacterSelectionScreen extends DefaultScreen {

    Stage uiStage;  // responsible for UI controls (labels, buttons & display)
    int currentCharacter;

    public CharacterSelectionScreen(DodgingHero game) {
        super(game);
        currentCharacter = 0;

        // create scalable viewport and create a stage with it. Stage will use its own batch
        FitViewport viewport = new FitViewport(160, 120);
        uiStage = new Stage(viewport);
        Gdx.input.setInputProcessor(uiStage);  // set stage as inputprocessor: all inputs will be forwarded to it so it can react to interactions

        // initialise start button
        prepareUI();
    }

    void prepareUI() {
        // design button
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.res.gameFont;
        buttonStyle.fontColor = Color.WHITE;

        // create button
        TextButton startButton = new TextButton("START", buttonStyle);
        startButton.setPosition((uiStage.getWidth() - startButton.getWidth()) / 2, uiStage.getHeight() / 6);

        // enable clicking leading to next screen
        startButton.addListener(new ClickListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose(); // dispose character selection screen
                game.setScreen(new GameScreen(game));
            }
        });

        uiStage.addActor(startButton);

        // add character scrolling
        Image heroSprite = new Image(game.res.player);
        heroSprite.setPosition((uiStage.getWidth() - heroSprite.getWidth()) / 2, (uiStage.getHeight() - heroSprite.getHeight()) / 2);
        uiStage.addActor(heroSprite);

        TextButton nextButton = new TextButton(">>>", buttonStyle);
        nextButton.setPosition(uiStage.getWidth() * 5 / 6 - nextButton.getWidth() / 2, uiStage.getHeight()/2);
        uiStage.addActor(nextButton);

        TextButton prevButton = new TextButton("<<<", buttonStyle);
        prevButton.setPosition(uiStage.getWidth() / 6, uiStage.getHeight() / 2);
        uiStage.addActor(prevButton);
    }

    @Override
    public void render (float delta) {
        // clears screen, moves actors, draws new screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void dispose() {
        // very important to clean up!
        Gdx.input.setInputProcessor(null);
        uiStage.dispose();
        super.dispose();
    }
}
