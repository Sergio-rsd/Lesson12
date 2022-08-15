package ru.gb.notes.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import ru.gb.notes.R
import android.content.DialogInterface
import androidx.fragment.app.DialogFragment
import ru.gb.notes.data.YesNoDialogController

class YesNoDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_yes_no, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setCancelable(true)
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, which: Int -> dialogInterface.cancel() }
        builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
            (requireContext() as YesNoDialogController).createAnswer()
            dialog.dismiss()
        }
        return builder.create()
    }
}