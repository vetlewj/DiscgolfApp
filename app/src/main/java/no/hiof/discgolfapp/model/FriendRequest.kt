package no.hiof.discgolfapp.model

import android.net.Uri
import java.util.*

data class FriendRequest(
    val date: Date,
    val sentFromUid: String,
    val name: String,
    val pictureUrl: Uri?,
    val acceptRequest: Boolean?

) {

}