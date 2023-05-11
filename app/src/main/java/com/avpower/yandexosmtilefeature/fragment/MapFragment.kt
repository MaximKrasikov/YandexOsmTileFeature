package com.avpower.yandexosmtilefeature.fragment

import androidx.fragment.app.Fragment
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.modules.OfflineTileProvider
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay
import java.io.File

class MapFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

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
        MapView.setTileSystem(Proj4TileSystem.MAAAMET)
        map.overlayManager.tilesOverlay =
            TilesOverlay(tileProviderYandex,requireContext())//YandexTilesOverlay(tileProviderYandex, requireContext())// TilesOverlay(tileProviderYandex,requireContext())

        return CustomTileSource.mTileSources[6]//6
    }

}