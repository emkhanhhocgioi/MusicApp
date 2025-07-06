package com.example.musicai.ClassProps

import java.util.Date

class ReturnUsers(
    var id: String,
    var username: String,
    var email: String,
    var avatarUrl: String,
    var favorites: List<Favorite>,
    var recentPlays: List<RecentPlay>,
    var recentSearches: List<RecentSearch>,

) {
    class Favorite(
        var songIds: List<String>,
        var artistIds: List<String>
    )

    class RecentPlay(
        var songIds: List<String>
    )

    class RecentSearch(
        var searchQueries: List<String>
    )
}