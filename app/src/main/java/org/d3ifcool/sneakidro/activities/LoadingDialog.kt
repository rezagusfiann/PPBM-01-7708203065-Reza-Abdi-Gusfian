package org.d3ifcool.sneakidro.activities

import android.app.Activity
import android.app.AlertDialog
import org.d3ifcool.sneakidro.R

class LoadingDialog(private val mActivity:Activity) {
    private lateinit var isdialog : AlertDialog
    fun startLoading() {
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.custom_dialog, null)

        val bulider = AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        isdialog = bulider.create()
        isdialog.show()
    }

    fun isDismiss() {
        isdialog.dismiss()
    }
}