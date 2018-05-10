package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class LoginScreen : LoginProtocols.Screen {
    override val contentView: View

    override var delegate: LoginProtocols.Controller? = null
    override var interactive: Boolean = true
        set(value) {
            field = value
            updateInteractive()
        }

    private val continueButton: Button
    private val backButton: Button
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
        backButton = contentView.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            backButtonTapped()
        }

        errorLabel = contentView.findViewById<TextView>(R.id.errorLabel)
        usernameField = contentView.findViewById<EditText>(R.id.usernameField)
        passwordField = contentView.findViewById<EditText>(R.id.passwordField)
        usernameFieldError = contentView.findViewById<ImageView>(R.id.usernameFieldError)
        passwordFieldError = contentView.findViewById<ImageView>(R.id.passwordFieldError)

        passwordField.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                continueButtonTapped()
                true
            } else {
                false
            }
        }
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

    private fun updateInteractive() {
        usernameField.isFocusable = interactive
        usernameField.isFocusableInTouchMode = interactive
        passwordField.isFocusable = interactive
        passwordField.isFocusableInTouchMode = interactive
    }

    private fun continueButtonTapped() {
        val usernameText = usernameField.text.toString()
        val passwordText = passwordField.text.toString()
        delegate?.submit(usernameText, passwordText)
    }

    private fun backButtonTapped() {
        delegate?.cancel()
    }
}
