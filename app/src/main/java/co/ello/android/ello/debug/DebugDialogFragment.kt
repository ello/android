package co.ello.android.ello.debug

import android.content.DialogInterface
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import co.ello.android.ello.AppActivity
import com.orhanobut.hawk.Hawk

/**
 * Created by sahitpenmatcha on 7/12/18.
 */
class DebugDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert)
        val options  = arrayOf("Production", "Ninja", "Stage 1", "Stage 2", "Rainbow")
        builder.setTitle("Change Environment from " +getEnvironment())
               .setItems(options, DialogInterface.OnClickListener { _, which ->
                  setEnvironment(options[which])
                  activity?.finishAndRemoveTask()
               })
        return builder.create()
    }

    private fun setEnvironment(environment: String) {
        Hawk.put(AppActivity.ENVIRONMENT_KEY, environment)
    }

    private fun getEnvironment(): String {
        return Hawk.get(AppActivity.ENVIRONMENT_KEY, "Rainbow")
    }
}