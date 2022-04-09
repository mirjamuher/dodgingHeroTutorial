package com.mirjamuher.dodginghero;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.mirjamuher.dodginghero.screens.GameScreen;

public class DodgingHero extends Game {
	public Resources res;
	
	@Override
	public void create () {
		res = new Resources();
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose () {
		res.dispose();
	}
}
