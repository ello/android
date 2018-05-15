package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class LoggedOutScreen : LoggedOutProtocols.Screen {
    override var delegate: LoggedOutProtocols.Controller? = null

    override val contentView: View
    override val containerView: ViewGroup

    private val loginButton: Button
    private val joinButton: Button

    constructor(activity: Activity) {
        contentView = activity.layoutInflater.inflate(R.layout.logged_out_layout, null)
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
