package co.ello.android.ello

import android.os.Bundle
import android.view.View
import android.graphics.drawable.AnimationDrawable
import android.support.constraint.ConstraintLayout


class LoginActivity : ElloActivity() {
    private var screen: LoginProtocols.Screen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = LoginScreen(this)
        setContentView(screen.contentView)
        this.screen = screen
    }
}
