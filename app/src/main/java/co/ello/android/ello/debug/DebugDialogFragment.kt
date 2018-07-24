package co.ello.android.ello.debug

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.TextView
import co.ello.android.ello.AppActivity
import co.ello.android.ello.AuthToken
import co.ello.android.ello.R
import com.orhanobut.hawk.Hawk
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme

class DebugDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): MaterialDialog {
        val builder = MaterialDialog.Builder(context as Context)
        builder.title("Debugging Actions")
                .theme(Theme.LIGHT)
                .customView(R.layout.debug_dialog_layout, true)
                .positiveText("Cancel")
                .positiveColor(Color.DKGRAY)

        val dialog = builder.build()
        initLayout(dialog)
        return dialog
    }

    private fun initLayout(dialog: MaterialDialog) {
        val customView = dialog.customView as View

        val environmentLabel = customView.findViewById<TextView>(R.id.environmentLabel)
        environmentLabel?.setText("Environment (using " + getEnvironment() +")")

        val environments = arrayOf<TextView>(customView.findViewById(R.id.env1), customView.findViewById(R.id.env2), customView.findViewById(R.id.env3), customView.findViewById(R.id.env4), customView.findViewById(R.id.env5))
        for (e in environments) {
            e.setOnClickListener {
                setEnvironment(e.text as String)
                activity?.finishAndRemoveTask()
            }
        }

        val logoutButton = dialog.customView?.findViewById<TextView>(R.id.logout)
        logoutButton?.setOnClickListener {
            AuthToken.logout()
            activity?.finishAndRemoveTask()
        }
    }

    private fun setEnvironment(environment: String) {
        Hawk.put(AppActivity.ENVIRONMENT_KEY, environment)
    }

    private fun getEnvironment(): String {
        return Hawk.get(AppActivity.ENVIRONMENT_KEY, AppActivity.DEFAULT_ENVIRONMENT_KEY)
    }
}