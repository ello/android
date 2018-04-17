package co.ello.android.ello

import android.content.Intent
import android.os.Bundle


class LoginActivity : ElloActivity(), LoginProtocols.Delegate {
    private var screen: LoginProtocols.Screen? = null
    private val generator: LoginProtocols.Generator = LoginGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = LoginScreen(this)
        screen.delegate = this
        generator.delegate = this
        setContentView(screen.contentView)
        this.screen = screen
    }
    override fun onStart() {
        super.onStart()
        screen?.spinnerVisibility(true, window)
        screen?.spinnerVisibility(false, window)
    }

    override fun submit(username: String, password: String) {
        val screen = this.screen
        if (screen == null)  return

        var usernameMessage: String? = null
        if (username == "") {
            usernameMessage = "Username is required"
        }

        var passwordMessage: String? = null
        if (password == "") {
            passwordMessage = "Password is required"
        }
        else if (password.length < 8) {
            passwordMessage = "Password must be at least 8 characters"
        }

        screen.showErrors(usernameMessage, passwordMessage)
        if (usernameMessage == null && passwordMessage == null) {
            screen.spinnerVisibility(true, window)

            val queue = VolleyQueue(this)
            generator.login(queue, username, password)
        }
    }

    override fun success(credentials: Credentials) {
        val intent = Intent(this, DummyActivity::class.java)
        startActivity(intent)
    }

    override fun failure() {
        screen?.spinnerVisibility(false, window)
    }

    override fun onDestroy() {
        screen?.onDestroy()
        super.onDestroy()
    }
}
