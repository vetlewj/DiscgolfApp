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
    val friendsRequests: ArrayList<MutableMap<String, Boolean>>?

) {
    companion object {

        fun getUsers(): List<User> {
            return listOf(
                User(
                    "1234",
                    false,
                    Date(),
                    "1321321",
                    "Ole Sveinesen",
                    "ole@ole.com",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                User(
                    "1233",
                    false,
                    Date(),
                    "dsad231dsada",
                    "Geir Geiresen",
                    "Geir@hotmail.com",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )

        }
}
}