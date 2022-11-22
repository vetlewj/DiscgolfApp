package no.hiof.discgolfapp.model

import java.util.Date

data class User(
    val authUid: String = "",
    val guest: Boolean? = null ,
    val dateCreated: Date? = null,
    val documetRefUid: String? = null,
    var name: String? = "",
    var email: String?= "",
    var pictureUrl: String? = "",
    val scoreCards: ArrayList<ScoreCard>? = null,
    val discs: ArrayList<Disc>? = null,
    val friends: ArrayList<User>? = null,
    val throws: ArrayList<Throw>? = null,
    val friendsRequests: MutableMap<String, Boolean>? = null

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