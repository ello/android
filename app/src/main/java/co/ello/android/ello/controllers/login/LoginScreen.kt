package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class LoginScreen : LoginProtocols.Screen {
    override val contentView: View

    override var delegate: LoginProtocols.Controller? = null

    private val continueButton: Button
    private val spinnerContainer: View
    private val spinnerImageView: ImageView
    private val errorLabel: TextView
    private val usernameField: EditText
    private val passwordField: EditText
    private val usernameFieldError: ImageView
    private val passwordFieldError: ImageView

    constructor(activity: Activity) {
        val inflater = activity.layoutInflater
        contentView = inflater.inflate(R.layout.login_layout, null)
        continueButton = contentView.findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            continueButtonTapped()
        }

        errorLabel = contentView.findViewById<TextView>(R.id.errorLabel)
        spinnerContainer = contentView.findViewById<View>(R.id.spinnerContainer)
        spinnerImageView = contentView.findViewById<ImageView>(R.id.spinner)
        usernameField = contentView.findViewById<EditText>(R.id.usernameField)
        passwordField = contentView.findViewById<EditText>(R.id.passwordField)
        usernameFieldError = contentView.findViewById<ImageView>(R.id.usernameFieldError)
        passwordFieldError = contentView.findViewById<ImageView>(R.id.passwordFieldError)

        val rotationAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotationAnimation.duration = 250
        rotationAnimation.repeatCount = Animation.INFINITE
        rotationAnimation.repeatMode = Animation.RESTART
        rotationAnimation.interpolator = LinearInterpolator()
        spinnerImageView.startAnimation(rotationAnimation)
    }

    override fun showErrors(usernameMessage: String?, passwordMessage: String?) {
        usernameFieldError.visibility = if (usernameMessage != null) View.VISIBLE else View.INVISIBLE
        passwordFieldError.visibility = if (passwordMessage != null) View.VISIBLE else View.INVISIBLE

        if (usernameMessage != null) {
            usernameField.requestFocus()
            errorLabel.setText(usernameMessage)
        }
        else if (passwordMessage != null) {
            passwordField.requestFocus()
            errorLabel.setText(passwordMessage)
        }
        else {
            errorLabel.setText("")
        }
    }

    override fun spinnerVisibility(visibile: Boolean, window: Window) {
        spinnerContainer.visibility = if (visibile) View.VISIBLE else View.INVISIBLE

        if (visibile) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            spinnerContainer.setFocusable(true)
            spinnerContainer.requestFocus()

            usernameField.isFocusable = false
            passwordField.isFocusable = false
        }
        else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
            spinnerContainer.isFocusable = false
            spinnerContainer.clearFocus()

            usernameField.isFocusable = true
            usernameField.isFocusableInTouchMode = true
            passwordField.isFocusable = true
            passwordField.isFocusableInTouchMode = true
        }
    }

    private fun continueButtonTapped() {
        val usernameText = usernameField.text.toString()
        val passwordText = passwordField.text.toString()
        delegate?.submit(usernameText, passwordText)
    }

    override fun onDestroy() {
        continueButton.setOnClickListener(null)
    }
}
