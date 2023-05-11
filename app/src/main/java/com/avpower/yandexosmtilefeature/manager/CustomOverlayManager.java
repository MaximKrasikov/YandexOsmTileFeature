package com.avpower.yandexosmtilefeature.manager;

import android.content.Context
import android.view.MotionEvent
import org.osmdroid.tileprovider.MapTileProviderBase
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.DefaultOverlayManager
import org.osmdroid.views.overlay.TilesOverlay


class CustomOverlayManager
/**
 * Default constructor
 */
        (tilesOverlay: TilesOverlay?) : DefaultOverlayManager(tilesOverlay) {
        /**
         * Override event & do nothing
         */
        override fun onDoubleTap(e: MotionEvent?, pMapView: MapView?): Boolean {
        return true
        }

        /**
         * Override event & do nothing
         */
        override fun onDoubleTapEvent(e: MotionEvent?, pMapView: MapView?): Boolean {
        return true
        }

        companion object {
        /**
         * Create MyOverlayManager
         */
        fun create(mapView: MapView, context: Context?): CustomOverlayManager {
        val mTileProvider: MapTileProviderBase = mapView.tileProvider
        val tilesOverlay = TilesOverlay(mTileProvider, context)
        mapView.tileProvider
        mapView.overlayManager = CustomOverlayManager(tilesOverlay)
        //mapView.overlayManager.overlays().add(overlay)
        mapView.invalidate()
        return CustomOverlayManager(tilesOverlay)
        }
        }
        }
