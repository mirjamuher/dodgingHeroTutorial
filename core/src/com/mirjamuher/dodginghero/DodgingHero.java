package com.mirjamuher.dodginghero;

import com.badlogic.gdx.Game;
import com.mirjamuher.dodginghero.logic.GameProgress;
import com.mirjamuher.dodginghero.screens.CharacterSelectionScreen;

public class DodgingHero extends Game {
	public Resources res;
	
	@Override
	public void create () {
		res = new Resources();
		GameProgress.Load();
		SoundManager.loadSounds();
		setScreen(new CharacterSelectionScreen(this));
	}
	
	@Override
	public void dispose () {
		GameProgress.Save();
		res.dispose();
		SoundManager.releaseSounds();
	}
}
