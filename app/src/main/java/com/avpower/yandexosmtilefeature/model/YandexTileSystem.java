package com.avpower.yandexosmtilefeature.model;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;


//osmdroid only uses EPSG:3857
//I need (EPSG:3395) for yandex
// transform 3395 to 3857
public class YandexTileSystem extends TileSystem {

    private static final RectF BOUNDS = new RectF(40500, 5993000, 1064500, 7017000);
    //private static final CoordinateTransform CT_FROM_WGS84;
    private static final CoordinateTransform CT_FROM_YANDEX;

    static {
        CoordinateTransformFactory CT_FACTORY = new CoordinateTransformFactory();
        CRSFactory CRS_FACTORY = new CRSFactory();

        CoordinateReferenceSystem MERCATOR = CRS_FACTORY.createFromParameters( "EPSG:3857", "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs");
        CoordinateReferenceSystem YAND = CRS_FACTORY.createFromParameters("EPSG:3395", "+proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");

        CoordinateReferenceSystem CRS_WGS84 = CRS_FACTORY.createFromParameters("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");
        CoordinateReferenceSystem CRS = CRS_FACTORY.createFromParameters("EPSG:3301", "+proj=lcc +lat_1=59.33333333333334 +lat_2=58 +lat_0=57.51755393055556 +lon_0=24 +x_0=500000 +y_0=6375000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs");

        //CT_FROM_WGS84 = CT_FACTORY.createTransform(CRS_WGS84, CRS);
        CT_FROM_YANDEX = CT_FACTORY.createTransform(YAND, MERCATOR);

    }

    public static GeoPoint fromWgs84(GeoPoint pGeopoint) {
        ProjCoordinate src = new ProjCoordinate(pGeopoint.getLongitude(), pGeopoint.getLatitude()), dst = new ProjCoordinate();

        //dst = CT_FROM_WGS84.transform(src, dst);
        dst = CT_FROM_YANDEX.transform(src, dst);
        return new GeoPoint(dst.y, dst.x);
    }

    @Override
    public double getX01FromLongitude(final double pLongitude) {
        return (pLongitude - BOUNDS.left) / (BOUNDS.right - BOUNDS.left);
    }

    @Override
    public double getY01FromLatitude(final double pLatitude) {
        return (BOUNDS.bottom - pLatitude) / (BOUNDS.bottom - BOUNDS.top);
    }

    @Override
    public double getLongitudeFromX01(final double pX01) {
        return (BOUNDS.left + pX01 * (BOUNDS.right - BOUNDS.left));
    }

    @Override
    public double getLatitudeFromY01(final double pY01) {
        return (BOUNDS.bottom - pY01 * (BOUNDS.bottom - BOUNDS.top));
    }

    @Override
    public double getMinLatitude() {
        return BOUNDS.top;
    }

    @Override
    public double getMaxLatitude() {
        return BOUNDS.bottom;
    }

    @Override
    public double getMinLongitude() {
        return BOUNDS.left;
    }

    @Override
    public double getMaxLongitude() {
        return BOUNDS.right;
    }

    @Override
    public double cleanLongitude(final double pLongitude) {
        return Clip(pLongitude, getMinLongitude(), getMaxLongitude());
    }
}
