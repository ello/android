package co.ello.android.ello

import com.google.gson.annotations.SerializedName


data class Credentials(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in") val expiresIn: String,
        @SerializedName("refresh_token") val refreshToken: String,
        @SerializedName("created_at") val createdAt: String
    )
