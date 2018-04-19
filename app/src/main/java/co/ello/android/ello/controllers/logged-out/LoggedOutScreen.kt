package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class LoggedOutScreen : LoggedOutProtocols.Screen {
    override val contentView: View
    override val containerView: ViewGroup
    override var delegate: LoggedOutProtocols.Controller? = null

    private val loginButton: Button
    private val joinButton: Button

    constructor(activity: Activity) {
        val inflater = activity.layoutInflater
        contentView = inflater.inflate(R.layout.logged_out_layout, null)
        loginButton = contentView.findViewById<Button>(R.id.loginButton)
        joinButton = contentView.findViewById<Button>(R.id.joinButton)
        containerView = contentView.findViewById<ViewGroup>(R.id.containerView)

        joinButton.setOnClickListener {
            delegate?.showJoinScreen()
        }

        loginButton.setOnClickListener {
            delegate?.showLoginScreen()
        }
    }

}
