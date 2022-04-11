package com.mirjamuher.dodginghero.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mirjamuher.dodginghero.DodgingHero;
import com.mirjamuher.dodginghero.logic.GameProgress;
import com.mirjamuher.dodginghero.logic.objects.CharacterRecord;

public class CharacterSelectionScreen extends DefaultScreen {

    Stage uiStage;  // responsible for UI controls (labels, buttons & display)

    public CharacterSelectionScreen(DodgingHero game) {
        super(game);

        // create scalable viewport and create a stage with it. Stage will use its own batch
        FitViewport viewport = new FitViewport(160, 120);
        uiStage = new Stage(viewport);
        Gdx.input.setInputProcessor(uiStage);  // set stage as inputprocessor: all inputs will be forwarded to it so it can react to interactions

        // initialise start button
        prepareUI();
    }

    void prepareUI() {
        uiStage.clear();

        // design button & label
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.res.gameFont;
        buttonStyle.fontColor = Color.WHITE;
        Label.LabelStyle textStyle = new Label.LabelStyle(game.res.gameFont, Color.WHITE);

        // checks if current character is unlocked before allowing you to play
        if (GameProgress.levels[GameProgress.currentCharacter] == 0) {
            TextButton unlockButton = new TextButton("UNLOCK (1000 G)", buttonStyle);
            unlockButton.setPosition((uiStage.getWidth() - unlockButton.getWidth()) / 2, uiStage.getHeight() / 6);
            unlockButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (GameProgress.currentGold >= GameProgress.CHARACTER_PRICE) {
                        GameProgress.currentGold -= GameProgress.CHARACTER_PRICE;
                        GameProgress.levels[GameProgress.currentCharacter] = 1;
                        prepareUI();
                    }
                }
            });
            uiStage.addActor(unlockButton);
        } else {
            // create button
            TextButton startButton = new TextButton("START", buttonStyle);
            startButton.setPosition((uiStage.getWidth() - startButton.getWidth()) / 2, uiStage.getHeight() * 5 / 6);
            // enable clicking leading to next screen
            startButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    dispose(); // dispose character selection screen
                    game.setScreen(new GameScreen(game));
                }
            });
            uiStage.addActor(startButton);

            // upgrade button
            TextButton upgradeButton = new TextButton("LvlUp (" + GameProgress.getNextUpgradeCost(GameProgress.currentCharacter) + ")", buttonStyle);
            upgradeButton.setPosition((uiStage.getWidth() - upgradeButton.getWidth()) / 2, uiStage.getHeight() / 6);
            // enable clicking leading to next screen
            upgradeButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (GameProgress.currentGold >= GameProgress.getNextUpgradeCost(GameProgress.currentCharacter)) {
                        GameProgress.currentGold -= GameProgress.getNextUpgradeCost(GameProgress.currentCharacter);
                        GameProgress.levels[GameProgress.currentCharacter] +=1;
                        prepareUI();
                    }
                }
            });
            uiStage.addActor(upgradeButton);
        }

        // add character
        Image heroSprite = new Image(game.res.playerSprites.get(CharacterRecord.CHARACTERS[GameProgress.currentCharacter].name));
        heroSprite.setPosition((uiStage.getWidth() - heroSprite.getWidth()) / 4, (uiStage.getHeight() - heroSprite.getHeight()) / 2);
        uiStage.addActor(heroSprite);

        // add stats label
        Label stat = prepareStatLabel("DMG: " + GameProgress.getPlayerDamage(), uiStage.getWidth() / 2, heroSprite.getY() + heroSprite.getHeight(), textStyle);
        stat = prepareStatLabel("HP: " + GameProgress.getPlayerMaxHP(), uiStage.getWidth() / 2, stat.getY() - 10, textStyle);
        stat = prepareStatLabel("HEAL: " + GameProgress.getPlayerHealthRestored(), uiStage.getWidth() / 2, stat.getY() - 10, textStyle);
        prepareStatLabel("BND: " + GameProgress.getPlayerBonusReductionValue(), uiStage.getWidth() / 2, stat.getY() - 10, textStyle);


        // add levels
        int lvl = GameProgress.levels[GameProgress.currentCharacter];
        Label statusText = new Label(lvl > 0 ? "LVL" + lvl : "LOCKED", textStyle);
        statusText.setPosition(heroSprite.getX() + (heroSprite.getWidth() - statusText.getWidth()) / 2, heroSprite.getY() - statusText.getHeight() - 8);
        uiStage.addActor(statusText);

        // add scrolling buttons
        TextButton nextButton = new TextButton(">>>", buttonStyle);
        nextButton.setPosition(uiStage.getWidth() * 5 / 6 - nextButton.getWidth() / 2, uiStage.getHeight() * 5 / 6);
        nextButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameProgress.currentCharacter += 1;
                if (GameProgress.currentCharacter == CharacterRecord.CHARACTERS.length) {
                    GameProgress.currentCharacter = 0;
                }
                prepareUI();
            }
        });
        uiStage.addActor(nextButton);

        TextButton prevButton = new TextButton("<<<", buttonStyle);
        prevButton.setPosition(uiStage.getWidth() / 6 - nextButton.getWidth() / 2, uiStage.getHeight() * 5 / 6);
        prevButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameProgress.currentCharacter -= 1;
                if (GameProgress.currentCharacter == -1) {
                    GameProgress.currentCharacter = CharacterRecord.CHARACTERS.length - 1;
                }
                prepareUI();
            }
        });
        uiStage.addActor(prevButton);

        // add coin image
        Image coinImage = new Image(game.res.coinBonus);
        coinImage.setPosition(1, 1);
        uiStage.addActor(coinImage);

        // add coin label of current amount
        Label coinAmntLbl = new Label("" + GameProgress.currentGold, textStyle);
        coinAmntLbl.setPosition(coinImage.getX() + coinImage.getWidth() + 3, coinImage.getY() + (coinImage.getHeight() - coinAmntLbl.getHeight()) / 2);
        uiStage.addActor(coinAmntLbl);
    }

    @Override
    public void render (float delta) {
        // clears screen, moves actors, draws new screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.act(delta);
        uiStage.draw();
    }


    // helper function for stats label
    private Label prepareStatLabel(String text, float x, float  y, Label.LabelStyle textStyle) {
        Label lbl = new Label(text, textStyle);
        lbl.setAlignment(Align.left);
        lbl.setPosition(x, y);
        uiStage.addActor(lbl);
        return lbl;
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
        uiStage.getViewport().update(w, h, true);
    }

    @Override
    public void dispose() {
        // very important to clean up!
        Gdx.input.setInputProcessor(null);
        uiStage.dispose();
        super.dispose();
    }
}
