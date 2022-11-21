package no.hiof.discgolfapp.model

import java.util.Date

class User(
    val id: Int,
    val guest: Boolean,
    val dateCreated: Date,
    var name: String?,
    var email: String?,
    var pictureUrl: String?,
    val scoreCards: ArrayList<ScoreCard>?,
    val discs: ArrayList<Disc>?,
    val friends: ArrayList<User>?,
    val throws: ArrayList<Throw>?,
    val friendsRequests: ArrayList<MutableMap<String, Boolean>>

) {
}