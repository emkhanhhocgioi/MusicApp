package com.example.musicai.ClassProps

class Song (var id : String, var title: String, var artist: String, var album: String, var duration: Int, var coverUrl: String,var externalUrl : String) {

    init {
        this.id = id
        this.title = title
        this.artist = artist
        this.album = album
        this.duration = duration
        this.coverUrl = coverUrl
        this.externalUrl = externalUrl

    }

    override fun toString(): String {
        return "Song(id='$id', title='$title', artist='$artist', album='$album', duration=$duration, coverUrl='$coverUrl')"
    }

    fun getSongDetails(): Song {
        return Song(id, title, artist, album, duration, coverUrl,externalUrl)
    }

    fun setSongDetails(id:String, title: String, artist: String, album: String, duration: Int, coverUrl: String,externalUrl: String) {
        this.id = id
        this.title = title
        this.artist = artist
        this.album = album
        this.duration = duration
        this.coverUrl = coverUrl
        this.externalUrl = externalUrl
    }


}