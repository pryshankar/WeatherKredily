package com.android.weatherkredily.utils.common

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.android.weatherkredily.R
import com.android.weatherkredily.ui.cityList.OnCitySubmitListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*


fun Activity.showAddCityDialog(context: Context, isCancelable : Boolean, onCitySubmitListener: OnCitySubmitListener ){

    val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_city, null)

    val inputCityLayout = mDialogView.findViewById<TextInputLayout>(R.id.inputCityLayout)
    val inputCityText  = mDialogView.findViewById<TextInputEditText>(R.id.inputCity)
    val submitButton = mDialogView.findViewById<MaterialButton>(R.id.submitButton)
    val cancelButton = mDialogView.findViewById<MaterialButton>(R.id.cancelButton)

    inputCityText.requestFocus()

    val mBuilder = MaterialAlertDialogBuilder(this).setView(mDialogView)

    val dialog = mBuilder.show()
    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE )


    dialog.setCancelable(isCancelable)
    dialog.setCanceledOnTouchOutside(isCancelable)

    submitButton.setOnClickListener {
       if(inputCityText.text.toString().trim().isNotEmpty())
           onCitySubmitListener.onCitySubmit(inputCityText.text.toString().trim().toLowerCase(Locale.getDefault()))
           dialog.cancel()
    }

    cancelButton.setOnClickListener {
        dialog.cancel()
    }

}