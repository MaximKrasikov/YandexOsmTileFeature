package com.avpower.yandexosmtilefeature;

public class Proj4TileSystem extends TileSystem {

    public static final Proj4TileSystem MAAAMET;
    public static final Proj4TileSystem MERCATOR;

    private static final CoordinateTransformFactory CT_FACTORY;
    private static final CRSFactory CRS_FACTORY;
    private static final CoordinateReferenceSystem CRS_WGS84;

    static {
        CT_FACTORY = new CoordinateTransformFactory();
        CRS_FACTORY = new CRSFactory();
        CRS_WGS84 = CRS_FACTORY.createFromParameters(
                "EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

        MERCATOR = new Proj4TileSystem(
                "EPSG:3857",
                "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs",
                new BoundingBox(85.05112877980659, 180, -85.05112877980659, -180),
                new RectF(-20037508, -20037508, 20037508, 20037508));
        MAAAMET = new Proj4TileSystem(
                "EPSG:3301",
                "+proj=lcc +lat_1=59.33333333333334 +lat_2=58 +lat_0=57.51755393055556 +lon_0=24 +x_0=500000 +y_0=6375000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs",
                new BoundingBox(60.4349, 29.4338, 56.7458, 20.373),
                new RectF(40500, 5993000, 1064500, 7017000));
    }

    private final BoundingBox mGeographicBoundingBox;
    private final RectF mProjectedBoundingBox;
    private final CoordinateTransform mFromWgs84;
    private final CoordinateTransform mToWgs84;

    public Proj4TileSystem(String pName, String pProj4, BoundingBox pGeographicBoundingBox, RectF pProjectedBoundingBox) {
        CoordinateReferenceSystem crs = CRS_FACTORY.createFromParameters(pName, pProj4);

        mGeographicBoundingBox = pGeographicBoundingBox;
        mProjectedBoundingBox = pProjectedBoundingBox;
        mFromWgs84 = CT_FACTORY.createTransform(CRS_WGS84, crs);
        mToWgs84 = CT_FACTORY.createTransform(crs, CRS_WGS84);
    }

    @Override
    public double getX01FromLongitude(final double pLongitude) {
        return toPixels(mGeographicBoundingBox.getCenterLatitude(), pLongitude).x;
    }

    @Override
    public double getY01FromLatitude(final double pLatitude) {
        return toPixels(pLatitude, mGeographicBoundingBox.getCenterLongitude()).y;
    }

    @Override
    public double getLongitudeFromX01(final double pX01) {
        return fromPixels(pX01, 0.0).x;
    }

    @Override
    public double getLatitudeFromY01(final double pY01) {
        return fromPixels(0.0, pY01).y;
    }

    @Override
    public double getMinLatitude() {
        return mGeographicBoundingBox.getLatSouth();
    }

    @Override
    public double getMaxLatitude() {
        return mGeographicBoundingBox.getLatNorth();
    }

    @Override
    public double getMinLongitude() {
        return mGeographicBoundingBox.getLonWest();
    }

    @Override
    public double getMaxLongitude() {
        return mGeographicBoundingBox.getLonEast();
    }

    @NonNull
    private ProjCoordinate toPixels(double pLatitude, double pLongitude) {
        ProjCoordinate src = new ProjCoordinate(pLongitude, pLatitude), dst = new ProjCoordinate();

        dst = mFromWgs84.transform(src, dst);
        src.x = (dst.x - mProjectedBoundingBox.left) / (mProjectedBoundingBox.right - mProjectedBoundingBox.left);
        src.y = (mProjectedBoundingBox.bottom - dst.y) / (mProjectedBoundingBox.bottom - mProjectedBoundingBox.top);

        return src;
    }

    @NonNull
    private ProjCoordinate fromPixels(double pX, double pY) {
        ProjCoordinate src = new ProjCoordinate(
                mProjectedBoundingBox.left + pX * (mProjectedBoundingBox.right - mProjectedBoundingBox.left),
                mProjectedBoundingBox.bottom - pY * (mProjectedBoundingBox.bottom - mProjectedBoundingBox.top)), dst = new ProjCoordinate();

        return mToWgs84.transform(src, dst);
    }
}
