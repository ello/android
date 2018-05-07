package co.ello.android.ello

import com.google.gson.annotations.SerializedName


data class Credentials(
        @SerializedName("access_token")
            val accessToken: String,
        @SerializedName("refresh_token")
            val refreshToken: String?
    ) : Model() {

    var isAnonymous: Boolean = true

}
