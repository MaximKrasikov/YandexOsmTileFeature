package com.avpower.yandexosmtilefeature;

public class CustomTileSource extends TMSOnlineTileSourceBase {

    public CustomTileSource() {
        super("kaart", 0, 19, 256, ".png", new String[]{ "https://tiles.maaamet.ee/tm/tms/1.0.0/" });
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        return getBaseUrl() + getTileRelativeFilenameString(pMapTileIndex);
    }
}