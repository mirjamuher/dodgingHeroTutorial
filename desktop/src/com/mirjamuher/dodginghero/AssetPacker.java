package com.mirjamuher.dodginghero;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {
    static void pack() {
        // takes all the images and makes one big .png file
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        settings.pot = true;  // if texture-height is power of two, some engines run more smoothly

        TexturePacker.process(settings, "ignore_assets", "assets/packed", "game");
    }

    public static void main(String[] arg) {
        // call the image packer
        pack();
    }
}
