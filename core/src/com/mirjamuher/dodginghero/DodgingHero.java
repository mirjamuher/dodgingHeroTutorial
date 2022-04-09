package com.mirjamuher.dodginghero;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class DodgingHero extends ApplicationAdapter {
	SpriteBatch batch;
	Resources res;
	
	@Override
	public void create () {
		batch = new SpriteBatch();  // responsible for sending command sto the videocard (e.g. rendering things)
		res = new Resources();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(res.ground, 0, 0);
		batch.draw(res.wall, 0, 16);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		res.dispose();
	}
}
