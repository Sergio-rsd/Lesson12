package ru.gb.notes.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.gb.notes.R

class YesNoDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_yes_no, null)
        val dialogView = layoutInflater.inflate(R.layout.dialog_yes_no, null)
//        from(requireContext()).inflate(R.layout.dialog_yes_no, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setCancelable(true)
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int -> dialogInterface.cancel() }
        builder.setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
            (requireContext() as YesNoDialogController).createAnswer()
            dialog.dismiss()
        }
        return builder.create()
    }
}