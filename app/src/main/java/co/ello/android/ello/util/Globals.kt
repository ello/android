package co.ello.android.ello

import java.util.Date


class Globals {
    companion object {
        val now: Date get() = Date()
        val baseURL: String = "https://ello.co"
        val isRetina: Boolean = true
    }
}
