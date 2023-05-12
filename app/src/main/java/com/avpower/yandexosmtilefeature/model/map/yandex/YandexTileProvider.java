package com.avpower.yandexosmtilefeature.model.map.yandex;

/*тестовый провайдер для яндекса*/
class YandexTileProvider(
        private val width:Int=256,
        private val height:Int=256
) :UrlTileProvider(width,height){


private val base="https://core-renderer-tiles.maps.yandex.net/tiles?l=map&x={x}&y={y}&z={z}&scale=1&lang=ru_RU"
private val sputnik="https://core-sat.maps.yandex.net/tiles?l=sat&x={x}&y={y}&z={z}&scale=1&lang=ru_RU"


        override fun getTileUrl(x:Int,y:Int,zoom:Int):URL{
        return URL(base.replace("{z}","$zoom").replace("{x}","$x").replace("{y}","$y"))
        }

        fun toOsmBaseTile():OnlineTileSourceBase{
        return object:OnlineTileSourceBase("Yandex-Base",1,20,width.coerceAtMost(height),"",arrayOf(base),"© Yandex"){
        override fun getTileURLString(pMapTileIndex:Long):String{
        val x=MapTileIndex.getX(pMapTileIndex)
        val y=MapTileIndex.getY(pMapTileIndex)
        val zoom=MapTileIndex.getZoom(pMapTileIndex)
        return base.replace("{z}","$zoom").replace("{x}","$x").replace("{y}","$y")
        }
        }
        }
        fun toOsmSputnikTile():OnlineTileSourceBase{
        return object:OnlineTileSourceBase("Yandex-Sputnik",1,20,width.coerceAtMost(height),"",arrayOf(sputnik),"© Yandex"){
        override fun getTileURLString(pMapTileIndex:Long):String{
        val x=MapTileIndex.getX(pMapTileIndex)
        val y=MapTileIndex.getY(pMapTileIndex)
        val zoom=MapTileIndex.getZoom(pMapTileIndex)
        return sputnik.replace("{z}","$zoom").replace("{x}","$x").replace("{y}","$y")
        }
        }
        }
        }
