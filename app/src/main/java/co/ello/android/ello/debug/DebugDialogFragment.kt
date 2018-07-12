package co.ello.android.ello.debug

import android.content.DialogInterface
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import co.ello.android.ello.AppActivity

/**
 * Created by sahitpenmatcha on 7/12/18.
 */
class DebugDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val options  = arrayOf("Production", "Ninja", "Stage 1", "Stage 2", "Rainbow")
        builder.setTitle("Choose Development Environment")
               .setItems(options, DialogInterface.OnClickListener { dialog, which ->
                    // The 'which' argument contains the index position of selected item

                })
        return builder.create()
    }
}