package com.avpower.yandexosmtilefeature.fragment

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.avpower.yandexosmtilefeature.Proj4TileSystem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.modules.OfflineTileProvider
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay
import java.io.File

class MapFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {


    map.setTileSource(loadOnlineTileSourceBase())


    private fun chooseMapStyle() {
        /// Prepare dialog and its items
        val builder = MaterialAlertDialogBuilder(requireContext())
        val mapStyles = arrayOf<CharSequence>(
            "OpenStreetMap",
            "USGS TOPO",
            "OPEN TOPO",
            "ESRI WORLD TOPO",
            "USGS SATELLITE",
            "ESRI WORLD OVERVIEW",
            "OFFLINE MAP",
            "YANDEX_BASE"
            //"GOOGLE"
        )

        /// Load preferences and its value
        val mapStyleInt = mPrefs.getInt(mapStyleId, 0)
        builder.setSingleChoiceItems(mapStyles, mapStyleInt) { dialog, which ->
            debug("Set mapStyleId pref to $which")
            val editor: SharedPreferences.Editor = mPrefs.edit()
            editor.putInt(mapStyleId, which)
            editor.apply()
            dialog.dismiss()
            map.setTileSource(loadOnlineTileSourceBase())
            renderDownloadButton()
            drawOverlays()
        }
        val dialog = builder.create()
        dialog.show()
    }

    /*==============================================Working with map layers==============================================================*/
    private fun loadOnlineTileSourceBase(): ITileSource {
        val id = mPrefs.getInt(mapStyleId, 0)
        debug("mapStyleId from prefs: $id")
        if (id == 6) {
            return addMbTilesLayer()
        }else if(id == 7){ //Yandex_Base
            map.setUseDataConnection(true)
            map.setMultiTouchControls(true)
            //map.tileProvider = MapTileProviderBasic(context, YandexTileProvider().toOsmBaseTile())
            return addYandexLayout()//YandexTileProvider().toOsmBaseTile()
        }
        else {
            MapView.setTileSystem(Proj4TileSystem.MERCATOR)
            map.resetScrollableAreaLimitLatitude()
            map.resetScrollableAreaLimitLongitude()
            map.setUseDataConnection(true)
            try {
                map.tileProvider = MapTileProviderBasic(context, CustomTileSource.mTileSources[id])
            } catch (ex: ArrayIndexOutOfBoundsException) {
                map.tileProvider = MapTileProviderBasic(context, CustomTileSource.mTileSources[0])
                ex.printStackTrace()
            }
            return CustomTileSource.mTileSources.getOrNull(id)
                ?: CustomTileSource.DEFAULT_TILE_SOURCE
        }
    }

    private fun addMbTilesLayer(): ITileSource {
        val mapfiles: Set<File> = CustomTileSource.findMapFiles(".mbtiles", model.mbtilesPath)
        val maps = mapfiles.toTypedArray()
        if (!maps.isEmpty()) {
            showToast(R.string.mbtiles_files_count)//maps.size
            map.setUseDataConnection(false)
            val tileProvider = OfflineTileProvider(SimpleRegisterReceiver(context), maps)
            map.tileProvider = tileProvider
            val archives = tileProvider.archives
            val tileSources = archives[0].tileSources
            var source: String? = ""
            if (!tileSources.isEmpty()) {
                source = tileSources.iterator().next()
            } else {
                //map.setTileSource(TileSourceFactory.MAPNIK);
                showToast(R.string.zoom_to_another_file)
            }
            /*val bounds = BoundingBox(
                52.3350745713, 40.0807890155, 44.3614785833, 22.0856083513
            )*/
            //map.zoomToBoundingBox(bounds, true)
            //map.setScrollableAreaLimitDouble(bounds)
            map.setMultiTouchControls(true)
            //map.getController().setCenter(map.getMapCenter());
            //map.controller.setZoom(FileBasedTileSource.getSource(source).getMinimumZoomLevel().toDouble())

            return FileBasedTileSource.getSource(source) as FileBasedTileSource
        } else {
            //setMessage("${getString(R.string.update_firmware)}?")
            //showSnackbar("${getString(R.string.in_current_folder_path_not_exist_file)}: "+model.mbtilesPath)
            showToastByString("Используется текущая папка: " + model.mbtilesPath)
            showToast(R.string.source_mbtiles_not_found)
            showToast(R.string.standard_map_resource)

            return CustomTileSource.mTileSources[0]
        }
    }


    //https://github.com/janekp/osmdroid/blob/example/%23971/app/src/main/res/layout/activity_main.xml
    private fun addYandexLayout() : ITileSource{
        //use EPSG3395
        val tileProviderYandex =  MapTileProviderBasic(requireContext(),CustomTileSource.mTileSources[6])
        tileProviderYandex.setTileRequestCompleteHandler(map.tileRequestCompleteHandler)
        map.tileProvider = tileProviderYandex

        //MapView.setTileSystem(YandexTileSystem())
        var currentTileSystem = MapView.getTileSystem()
        MapView.setTileSystem(Proj4TileSystem.YANDEX)
        map.overlayManager.tilesOverlay =TilesOverlay(tileProviderYandex,requireContext())//YandexTilesOverlay(tileProviderYandex, requireContext())// TilesOverlay(tileProviderYandex,requireContext())
        currentTileSystem = MapView.getTileSystem()

        return CustomTileSource.mTileSources[6]//6
    }

}