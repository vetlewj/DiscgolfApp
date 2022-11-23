package no.hiof.discgolfapp.model

import android.net.Uri
import java.util.*

data class FriendRequest(
    val date: Date,
    val senderUid: String,
    val receiverUid: String,
    val name: String,
    val pictureUrl: Uri?,
    val acceptRequest: Boolean?

) {
    companion object {

        fun getFriendRequests(): List<FriendRequest> {
            return listOf(
                FriendRequest(
                    Date(),
                    "dsadad2dsada",
                    "1321321",
                    "Ole Sveinesen",
                    null,
                    null,
                    ),
                FriendRequest(
                    Date(),
                    "dsadad2dsada",
                    "1321321",
                    "Ole Sveinesen",
                    null,
                    null,
                ),
            )
        }

}