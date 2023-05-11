package com.avpower.yandexosmtilefeature.manager;

import android.content.Context
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import org.osmdroid.tileprovider.tilesource.*
import org.osmdroid.util.MapTileIndex
import java.io.File
import java.io.FileFilter
import java.util.*


class CustomTileSource {
    companion object {
        val OPENWEATHER_RADAR = OnlineTileSourceAuth(
                "Open Weather Map", 1, 22, 256, ".png", arrayOf(
                        "https://tile.openweathermap.org/map/"
                ), "Openweathermap",
                TileSourcePolicy(
                        4,
                        TileSourcePolicy.FLAG_NO_BULK
                        or TileSourcePolicy.FLAG_NO_PREVENTIVE
                        or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                        or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
                ),
                "precipitation",
                ""
        )
        //
//        val RAIN_VIEWER = object : OnlineTileSourceBase(
//            "RainViewer", 1, 15, 256, ".png", arrayOf(
//                "https://tilecache.rainviewer.com/v2/coverage/"
//            ), "RainViewer",
//            TileSourcePolicy(
//                4,
//                TileSourcePolicy.FLAG_NO_BULK
//                        or TileSourcePolicy.FLAG_NO_PREVENTIVE
//                        or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
//                        or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
//            )
//        ) {
//            override fun getTileURLString(pMapTileIndex: Long): String {
//                return baseUrl + (MapTileIndex.getZoom(pMapTileIndex)
//                    .toString() + "/" + MapTileIndex.getY(pMapTileIndex)
//                        + "/" + MapTileIndex.getX(pMapTileIndex)
//                        + mImageFilenameEnding)
//            }
//        }


        private val ESRI_IMAGERY = object : OnlineTileSourceBase(
                "ESRI World Overview", 1, 20, 256, ".jpg", arrayOf(
                        "https://clarity.maptiles.arcgis.com/arcgis/rest/services/World_Imagery/MapServer/tile/"
                ), "Esri, Maxar, Earthstar Geographics, and the GIS User Community",
                TileSourcePolicy(
                        4,
                        TileSourcePolicy.FLAG_NO_BULK
                        or TileSourcePolicy.FLAG_NO_PREVENTIVE
                        or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                        or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
                )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl + (MapTileIndex.getZoom(pMapTileIndex)
                        .toString() + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        }

        private val ESRI_WORLD_TOPO = object : OnlineTileSourceBase(
                "ESRI World TOPO",
                1,
                20,
                256,
                ".jpg",
                arrayOf(
                        "https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/"
                ),
                "Esri, HERE, Garmin, FAO, NOAA, USGS, © OpenStreetMap contributors, and the GIS User Community  ",
                TileSourcePolicy(
                        4,
                        TileSourcePolicy.FLAG_NO_BULK
                        or TileSourcePolicy.FLAG_NO_PREVENTIVE
                        or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                        or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
                )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl + (MapTileIndex.getZoom(pMapTileIndex)
                        .toString() + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        }
        private val USGS_HYDRO_CACHE = object : OnlineTileSourceBase(
                "USGS Hydro Cache",
                0,
                18,
                256,
                "",
                arrayOf(
                        "https://basemap.nationalmap.gov/arcgis/rest/services/USGSHydroCached/MapServer/tile/"
                ),
                "USGS",
                TileSourcePolicy(
                        2,
                        TileSourcePolicy.FLAG_NO_PREVENTIVE
                        or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                        or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
                )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl + (MapTileIndex.getZoom(pMapTileIndex)
                        .toString() + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        }
        private val USGS_SHADED_RELIEF = object : OnlineTileSourceBase(
                "USGS Shaded Relief Only",
                0,
                18,
                256,
                "",
                arrayOf(
                        "https://basemap.nationalmap.gov/arcgis/rest/services/USGSShadedReliefOnly/MapServer/tile/"
                ),
                "USGS",
                TileSourcePolicy(
                        2,
                        TileSourcePolicy.FLAG_NO_PREVENTIVE
                        or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                        or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
                )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl + (MapTileIndex.getZoom(pMapTileIndex)
                        .toString() + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        }

        //Yandex-карты
        //https://core-renderer-tiles.maps.yandex.net/tiles?l=map&x={x}&y={y}&z={z}&scale=1&lang=ru_RU
        //https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=23.04.30-0-b230426134730&x=616&y=319&z=10&scale=1&lang=ru_RU&ads=enabled
        //Yandex-спутник
        //https://core-sat.maps.yandex.net/tiles?l=sat&x={x}&y={y}&z={z}&scale=1&lang=ru_RU
        //Yandex-traffic
        //"https://core-jams-rdr.maps.yandex.net/1.1/tiles?trf&l=trf,trfe&lang=ru_RU&x=%s&y=%s&z=%s&scale=1&tm=%s"

        //Пример парсинга строки
        //"https://tiles.api-maps.yandex.ru/v1/tiles/?scale={s}&x={x}&y={y}&z={z}&lang=ru-RU&l=map&apikey=6a7d80ba-4f62-4ffb-865d-df47a7869ec5"

        private val YANDEX_BASE = object : OnlineTileSourceBase(
                "Yandex-Base",
                1,
                23,
                256,
                ".png",
                arrayOf(
                        "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=23.04.30-0-b230426134730&x={x}&y={y}&z={z}&scale=1&lang=ru_RU"
                )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                val zoom = MapTileIndex.getZoom(pMapTileIndex)

                return baseUrl.replace("{z}", "$zoom").replace("{x}", "$x").replace("{y}", "$y")
            }
        }


        /*
            "http://mt0.google.com/vt/lyrs=m&hl=ru&x=%s&y=%s&z=%s&s=Galileo",
            "http://mt1.google.com/vt/lyrs=m&hl=ru&x=%s&y=%s&z=%s&s=Galileo",
            "http://mt2.google.com/vt/lyrs=m&hl=ru&x=%s&y=%s&z=%s&s=Galileo",
            "http://mt3.google.com/vt/lyrs=m&hl=ru&x=%s&y=%s&z=%s&s=Galileo"*/

        //google gibrid
        //http://mt.google.com/vt/lyrs=h@169000000&hl=ru
        //http://mt.google.com/vt/lyrs=m&hl=ru&x=%s&y=%s&z=%s&s=Galileo

        private val GOOGLE = object : XYTileSource(
                "Google-Gibrid",
                0,
                18,
                256,
                ".png",
                arrayOf(
                        "http://mt.google.com/vt/lyrs=m&hl=ru&x=%s&y=%s&z=%s&s=Galileo"
                ),
                ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl + (MapTileIndex.getX(pMapTileIndex)
                        .toString() + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getZoom(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        }


        /**
         * WMS TILE SERVER
         * More research is required to get this to function correctly with overlays
         */
        val NOAA_RADAR_WMS = NOAAWmsTileSource(
                "Recent Weather Radar",
                arrayOf("https://new.nowcoast.noaa.gov/arcgis/services/nowcoast/radar_meteo_imagery_nexrad_time/MapServer/WmsServer?"),
                "1",
                "1.1.0",
                "",
                "EPSG%3A3857",
                "",
                "image/png"
        )


        /**
         * ===============================================================================================
         */
        private val MAPNIK: OnlineTileSourceBase = TileSourceFactory.MAPNIK
        private val USGS_TOPO: OnlineTileSourceBase = TileSourceFactory.USGS_TOPO
        private val OPEN_TOPO: OnlineTileSourceBase = TileSourceFactory.OpenTopo
        private val USGS_SAT: OnlineTileSourceBase = TileSourceFactory.USGS_SAT
        private val SEAMAP: OnlineTileSourceBase = TileSourceFactory.OPEN_SEAMAP
        val DEFAULT_TILE_SOURCE: OnlineTileSourceBase = TileSourceFactory.DEFAULT_TILE_SOURCE


        /**
         * The order in this list must match that in the arrays.xml under map_styles
         */
        val mTileSources: List<ITileSource> =
        listOf(
                MAPNIK,
                USGS_TOPO,
                OPEN_TOPO,
                ESRI_WORLD_TOPO,
                USGS_SAT,
                ESRI_IMAGERY,
                YANDEX_BASE,
                // GOOGLE,
        )

        fun getTileSource(aName: String): ITileSource {
            for (tileSource: ITileSource in mTileSources) {
                if (tileSource.name().equals(aName)) {
                    return tileSource;
                }
            }
            throw IllegalArgumentException("No such tile source: $aName")
        }

        fun findMapFiles(extension: String, filePath: String): Set<File> {
            val maps: MutableSet<File> = HashSet()
            // File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/");
            val f = File(
                    filePath
            )
            //File(Configuration.getInstance().osmdroidBasePath.absolutePath + File.separator + "osmdroid" + File.separator)
            if (f.exists()) {
                maps.addAll(scanMapsForgeFile(f, extension))
            }
            return maps
        }

        fun scanMapsForgeFile(f: File, extension: String): MutableCollection<File> {
            val ret: MutableCollection<File> = ArrayList()
            val files: Array<File> = f.listFiles(
                    object : FileFilter {
                override fun accept(pathname: File): Boolean {
                    return pathname.name.lowercase(Locale.getDefault()).endsWith(extension)
                }
            }
            )!!
            if (!files.isEmpty()) {
                ret.addAll(files)
            }
            return ret
        }
    }

}
