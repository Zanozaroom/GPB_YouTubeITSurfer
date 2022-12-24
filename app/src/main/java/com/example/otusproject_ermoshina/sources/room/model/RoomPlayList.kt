package com.example.otusproject_ermoshina.sources.room.model

import com.example.otusproject_ermoshina.base.YTPlayListOfChannel

data class RoomPlayList(
    val id: Int,
    val idChannel: String,
    val idPlayList: String,
    val imageList: String,
    val titleListVideo: String,
    val titleChannel: String
) {
    fun toYTPlayList() = YTPlayListOfChannel(
        idChannel = idChannel,
        idList = idPlayList,
        imageList = imageList,
        titleListVideo = titleListVideo,
        titleChannel = titleChannel
    )
}
