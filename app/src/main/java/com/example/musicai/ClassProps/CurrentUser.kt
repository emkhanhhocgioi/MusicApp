package com.example.musicai.ClassProps

import java.util.Date

object CurrentUser {
    var id: String? = null
    var username: String? = null
    var email: String? = null
    var passwordHash: String? = null
    var avatarUrl: String? = null
    var favorites: List<ReturnUsers.Favorite>? = null
    var recentPlays: List<ReturnUsers.RecentPlay>? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var spotifyToken: String? = null
}