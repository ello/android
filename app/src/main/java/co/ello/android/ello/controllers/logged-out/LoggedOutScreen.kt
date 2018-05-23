package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class LoggedOutScreen : LoggedOutProtocols.Screen {
    private val delegate: LoggedOutProtocols.Controller?

    override val contentView: View
    override val containerView: ViewGroup

    private val loginButton: Button
    private val joinButton: Button

    constructor(activity: Activity, delegate: LoggedOutProtocols.Controller?) {
        this.delegate = delegate

        contentView = activity.layoutInflater.inflate(R.layout.logged_out_layout, null)
        loginButton = contentView.findViewById(R.id.loginButton)
        joinButton = contentView.findViewById(R.id.joinButton)
        containerView = contentView.findViewById(R.id.containerView)

        joinButton.setOnClickListener {
            delegate?.showJoinScreen()
        }

        loginButton.setOnClickListener {
            delegate?.showLoginScreen()
        }
    }

}
