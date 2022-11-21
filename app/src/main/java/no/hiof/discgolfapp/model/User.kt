package no.hiof.discgolfapp.model

import java.util.Date

class User(
    val authUid: String,
    val guest: Boolean,
    val dateCreated: Date,
    val documetRefUid: String,
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