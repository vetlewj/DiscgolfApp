package no.hiof.discgolfapp.model

class User(
    val uid: Int,
    var name: String,
    var email: String,
    var pictureUrl: String,
    val scoreCards: ArrayList<ScoreCard>?,
    val discs: ArrayList<Disc>?,
    val friends: ArrayList<User>?,
    val throws: ArrayList<Throw>
) {
}