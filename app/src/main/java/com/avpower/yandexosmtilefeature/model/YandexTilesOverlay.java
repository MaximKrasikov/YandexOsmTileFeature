package com.avpower.yandexosmtilefeature.model;

import android.graphics.Point;

import androidx.annotation.Nullable;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.TilesOverlay;


public class YandexTilesOverlay extends TilesOverlay {
    private final Rect mTileRect = new Rect();
    private final Point mTilePos = new Point();

    @Nullable
    private ColorFilter filter = null;

    private short alpha = -1;

    @Override
    public void setColorFilter(ColorFilter filter) {
        super.setColorFilter(filter);
        this.filter = filter;
    }

    public YandexTilesOverlay(MapTileProviderBase aTileProvider, Context aContext) {
        super(aTileProvider, aContext);
    }

    /**
     * Конструктор с альфой для слоя
     */
    public YandexTilesOverlay(MapTileProviderBase aTileProvider, Context aContext, short alpha) {
        super(aTileProvider, aContext);
        this.alpha = alpha;
    }

    //переопределим метод draw для отрисовки Yandex тайлов
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        //текущий масштаб
        int zoom = (int) mapView.getZoomLevelDouble();
        final Projection mapViewProjection = mapView.getProjection();
        //координаты углов видимой части карты
        BoundingBox mapViewBoundingBox = mapView.getBoundingBox();
        //получаем координаты верхнего левого и нижнего правого тайла
        double[] MercatorTL = YandexUtil.geoToMercator(new double[]{mapViewBoundingBox.getLonWest(), mapViewBoundingBox.getLatNorth()});//верхний левый
        double[] MercatorRB = YandexUtil.geoToMercator(new double[]{mapViewBoundingBox.getLonEast(), mapViewBoundingBox.getLatSouth()}); // нижний правый

        double[] TilesTL = YandexUtil.mercatorToTiles(MercatorTL);
        long[] TileTL = YandexUtil.getTile(TilesTL, zoom);

        double[] TilesRB = YandexUtil.mercatorToTiles(MercatorRB);
        long[] TileRB = YandexUtil.getTile(TilesRB, zoom);

        mTileProvider.ensureCapacity((int) ((TileRB[1] - TileTL[1] + 1) * (TileRB[0] - TileTL[0] + 1)));

        //геокоординаты верхнего левого тайла Yandex
        double[] reTiles = YandexUtil.ReGetTile(new long[]{TileTL[0], TileTL[1]}, zoom);
        long xx = (long) reTiles[0];
        long yy = (long) reTiles[1];
        double[] reMercator = YandexUtil.tileToMercator(new long[]{xx, yy});
        double[] tmp = YandexUtil.mercatorToGeo(reMercator);

        //на выходе: [55.78892895389263, 49.10888671875] - верно
        double[] test1 = YandexUtil.tileNumberToCoordinates(10427, 5119, 14);
        //должен получить на выходе 5119 10427, но получаю [5133, 10427, 0, 117] - необходимо сместить на 0 пикселей влево и на 117 пикселей вверх
        int[] test2 = YandexUtil.getMapTileFromCoordinates(55.78892895389263, 49.10888671875, 14);


        //геокоординаты верхнего левого тайла Yandex переводим в экранные координаты osmdroid
        GeoPoint geoPoint = new GeoPoint(tmp[1], tmp[0]);
        mapViewProjection.toPixels(geoPoint, mTilePos);//позиция 1-го тайла


       /* int[] mapTileFromCoordinates = YandexUtil.getMapTileFromCoordinates(mapView.getMapCenter().getLatitude(), mapView.getMapCenter().getLongitude(), zoom);
        final Drawable mapTile = mTileProvider.getMapTile(MapTileIndex.getTileIndex(zoom,mapTileFromCoordinates[1] , mapTileFromCoordinates[0]));
        mTileRect.set(mTilePos.x, mTilePos.y, mTilePos.x + 256, mTilePos.y + 256);
        mapTile.setBounds(mTileRect);
        mapTile.draw(canvas);*/

        MapView.setTileSystem(new YandexTileSystem());
        super.draw(canvas,mapViewProjection);
        //в цикле отрисовываем все видимые тайлы Yandex
        /*for (int y = (int) TileTL[1]; y <= TileRB[1]; y++) {
            int xcount = 0;
            for (int x = (int) TileTL[0]; x <= TileRB[0]; x++) {
                final Drawable currentMapTile = mTileProvider.getMapTile(MapTileIndex.getTileIndex(zoom, x, y));
                if (currentMapTile != null) {
                    mTileRect.set(mTilePos.x, mTilePos.y, mTilePos.x + 256, mTilePos.y + 256);
                    currentMapTile.setBounds(mTileRect);
                    if (alpha > 0)
                        currentMapTile.setAlpha(alpha);
                    if (filter != null)
                        currentMapTile.setColorFilter(filter);

                    currentMapTile.draw(canvas);
                    //super.draw(canvas, mapViewProjection);
                }
                xcount++;
                mTilePos.x = mTilePos.x+256;
            }
            mTilePos.x -= xcount * 256;
            mTilePos.y =mTilePos.y+ 256;
        }*/
    }
}
