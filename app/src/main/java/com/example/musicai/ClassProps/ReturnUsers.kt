package com.example.musicai.ClassProps

import java.util.Date

class ReturnUsers(
    var id: String,
    var username: String,
    var email: String,
    var passwordHash: String,
    var avatarUrl: String,
    var favorites: List<Favorite>,
    var recentPlays: List<RecentPlay>,
    var createdAt: Date,
    var updatedAt: Date,
    var spotifyToken: String
) {
    class Favorite(
        var songIds: List<String>,
        var artistIds: List<String>
    )

    class RecentPlay(
        var songIds: List<String>
    )
}