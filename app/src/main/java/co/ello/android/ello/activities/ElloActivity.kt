package co.ello.android.ello

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.constraint.ConstraintLayout


open class ElloActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
    }

    fun constraintLayoutParams(): ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.WRAP_CONTENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

}
