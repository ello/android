package co.ello.android.ello

import java.util.Date


data class AmazonCredentials(
    val accessKey: String,
    val endpoint: String,
    val policy: String,
    val prefix: String,
    val signature: String
    ) : Model() {
    override val identifier = null
    override fun update(property: Property, value: Any) {}
}
