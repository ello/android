package co.ello.android.ello

import android.app.Activity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class JoinScreen : JoinProtocols.Screen {
    private val delegate: JoinProtocols.Controller?

    override val contentView: View
    override var interactive: Boolean = true
        set(value) {
            field = value
            updateInteractive()
        }

    private val continueButton: Button
    private val backButton: Button
    private val errorLabel: TextView
    private val emailField: EditText
    private val usernameField: EditText
    private val passwordField: EditText
    private val emailFieldError: ImageView
    private val usernameFieldError: ImageView
    private val passwordFieldError: ImageView

    constructor(activity: Activity, delegate: JoinProtocols.Controller?) {
        this.delegate = delegate
        contentView = activity.layoutInflater.inflate(R.layout.join_layout, null)
        continueButton = contentView.findViewById(R.id.continueButton)
        continueButton.setOnClickListener {
            continueButtonTapped()
        }
        backButton = contentView.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            backButtonTapped()
        }

        errorLabel = contentView.findViewById(R.id.errorLabel)
        emailField = contentView.findViewById(R.id.emailField)
        usernameField = contentView.findViewById(R.id.usernameField)
        passwordField = contentView.findViewById(R.id.passwordField)
        emailFieldError = contentView.findViewById(R.id.emailFieldError)
        usernameFieldError = contentView.findViewById(R.id.usernameFieldError)
        passwordFieldError = contentView.findViewById(R.id.passwordFieldError)

        passwordField.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                continueButtonTapped()
                true
            } else {
                false
            }
        }
    }

    override fun showErrors(emailMessage: String?, usernameMessage: String?, passwordMessage: String?) {
        emailFieldError.visibility = if (emailMessage != null) View.VISIBLE else View.INVISIBLE
        usernameFieldError.visibility = if (usernameMessage != null) View.VISIBLE else View.INVISIBLE
        passwordFieldError.visibility = if (passwordMessage != null) View.VISIBLE else View.INVISIBLE

        if (emailMessage != null) {
            emailField.requestFocus()
            errorLabel.setText(emailMessage)
        }
        else if (usernameMessage != null) {
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
        emailField.isFocusable = interactive
        emailField.isFocusableInTouchMode = interactive
        usernameField.isFocusable = interactive
        usernameField.isFocusableInTouchMode = interactive
        passwordField.isFocusable = interactive
        passwordField.isFocusableInTouchMode = interactive
    }

    private fun continueButtonTapped() {
        val emailText = emailField.text.toString()
        val usernameText = usernameField.text.toString()
        val passwordText = passwordField.text.toString()
        delegate?.submit(emailText, usernameText, passwordText)
    }

    private fun backButtonTapped() {
        delegate?.cancel()
    }
}
