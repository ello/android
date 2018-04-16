package co.ello.android.ello

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText


class LoginScreen : LoginProtocols.Screen {
    override val contentView : View
    val continueButton: Button

    constructor(activity: Activity) {
        val inflater = activity.layoutInflater
        contentView = inflater.inflate(R.layout.login_layout, null)
        continueButton = contentView.findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            continueButtonTapped()
        }

        val usernameField = contentView.findViewById<EditText>(R.id.usernameField)
        val passwordField = contentView.findViewById<EditText>(R.id.passwordField)
        for (editText in arrayOf(usernameField, passwordField)) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let { println("char seq $it") }
                }
            })
        }
    }

    fun continueButtonTapped() {
        println("=============== LoginScreen.kt at line 21 ===============");
    }

    fun onDestroy() {
        continueButton.setOnClickListener(null)
    }

}
