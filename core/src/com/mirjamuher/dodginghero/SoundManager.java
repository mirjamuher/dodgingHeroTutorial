package com.mirjamuher.dodginghero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class SoundManager {
    /*
        swing.wav -> swing0.ogg
        swing2.wav -> swing1.ogg
        swing3.wav -> swing2.ogg
        coin.wav -> coin.ogg
        bite-small.wav -> heal.ogg
        fantozzi-sandl1 -> walk0.ogg
        fantozzi-sandl2 -> walk1.ogg
        Fantozzi-SandR1 -> walk2.ogg
        Attack sounds: http://opengameart.org/content/rpg-sound-pack by artisticdude
        Battle Music: http://opengameart.org/content/8-bit-music-pack-loopable#comment-55568 by CodeManu
        Footsteps: http://opengameart.org/content/fantozzis-footsteps-grasssand-stone by Fantozzi (submitted by qubodup)
    */

    public static AssetManager assets = new AssetManager();  // loads files asynchronically, but we're loading them all at once because it's a small project

    public static void loadSounds() {
        for (int i = 0; i < 3; i++) {
            assets.load(Gdx.files.internal("music/swing" + i + ".ogg").path(), Sound.class);
            assets.load(Gdx.files.internal("music/walk" + i + ".ogg").path(), Sound.class);
        }

        assets.load(Gdx.files.internal("music/coin.ogg").path(), Sound.class);
        assets.load(Gdx.files.internal("music/heal.ogg").path(), Sound.class);

        assets.finishLoading();  // while assets are loading, thread is "sleeping"
    }

    public static void releaseSounds() {
        assets.dispose();
    }

    private static void playSoundRandomVolume(Sound sound, float min, float max) {
        // to make the game seem more varied, play sounds at random volumes
        if (sound != null) {
            sound.play(MathUtils.random(min, max));
        }
    }

    public static void playSwingSound() {
        Sound s = assets.get("music/swing" + MathUtils.random(2) + ".ogg", Sound.class);
        playSoundRandomVolume(s, 0.6f, 0.7f);
    }

    public static void playWalkSound() {
        Sound s = assets.get("music/walk" + MathUtils.random(2) + ".ogg", Sound.class);
        playSoundRandomVolume(s, 0.5f, 0.6f);
    }

    public static void playCoinSound() {
        Sound s = assets.get("music/coin.ogg", Sound.class);
        playSoundRandomVolume(s, 0.6f, 0.7f);
    }

    public static void playHealSound() {
        Sound s = assets.get("music/heal.ogg", Sound.class);
        playSoundRandomVolume(s, 0.6f, 0.7f);
    }
}
