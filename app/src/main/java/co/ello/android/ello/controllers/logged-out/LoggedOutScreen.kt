package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class LoggedOutScreen(
    activity: Activity,
    private val delegate: LoggedOutProtocols.Controller?
    ) : LoggedOutProtocols.Screen {
    override val contentView: View = activity.layoutInflater.inflate(R.layout.logged_out_layout, null)
    override val containerView: ViewGroup = contentView.findViewById(R.id.containerView)
    private val loginButton: Button = contentView.findViewById(R.id.loginButton)
    private val joinButton: Button = contentView.findViewById(R.id.joinButton)

    init {
        joinButton.setOnClickListener {
            delegate?.showJoinScreen()
        }

        loginButton.setOnClickListener {
            delegate?.showLoginScreen()
        }
    }

}
