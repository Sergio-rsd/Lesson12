package ru.gb.notes.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import ru.gb.notes.R
import ru.gb.notes.data.InMemoryRepoImpl
import ru.gb.notes.domain.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteDialog : DialogFragment() {
    private val repository = InMemoryRepoImpl.instance
    private var note: Note? = null
    private lateinit var arrayInterest: Array<String>
    private var selectSaved: String? = null

    //    public static final String TAG = "happy";
    private var interest = ""
    private var idNote = 0
    private lateinit var currentDateTime: TextView
    private var dataChoice: Calendar = Calendar.getInstance()

    interface NoteDialogController {
        fun update(note: Note?)
        fun create(id: Int, title: String?, description: String?, interest: String?, dataPerformance: String?)
    }

    private lateinit var controller: NoteDialogController
    override fun onAttach(context: Context) {
        if (context is NoteDialogController) {
            controller = context
        } else {
            throw IllegalStateException("Activity must implement Controller")
        }
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments
        note = args!!.getSerializable(NOTE) as Note?
        val dialog = layoutInflater.inflate(R.layout.dialog_edit_note, null)
        var title: String? = ""
        var description: String? = ""

        //  Spinner
        val spinner = dialog.findViewById<Spinner>(R.id.interest_place)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(), R.array.interest_name,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Вызываем адаптер spinner
        spinner.adapter = adapter
        arrayInterest = resources.getStringArray(R.array.interest_name)
        // Spinner end
        currentDateTime = dialog.findViewById(R.id.current_date)
        if (note != null) {
            idNote = note!!.id!!
            title = note!!.title
            description = note!!.description
            if (note!!.dataPerformance == null) {
                note!!.dataPerformance = ""
            }
            currentDateTime.text = note!!.dataPerformance
            //            setInitialDate();
            selectSaved = note!!.interest
            for (i in arrayInterest.indices) {
                if (selectSaved == arrayInterest[i]) {
                    spinner.setSelection(i)
                    break
                }
            }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    note!!.interest = arrayInterest[position]
                    interest = note!!.interest
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        val dialogTitle = dialog.findViewById<EditText>(R.id.edit_note_title)
        val dialogDescription = dialog.findViewById<EditText>(R.id.edit_note_description)
        dialogTitle.setText(title)
        dialogDescription.setText(description)
        currentDateTime.setOnClickListener { setDate() }
        val builder = AlertDialog.Builder(requireContext())
        val buttonText: String
        if (note == null) {
            buttonText = "Create"
            builder.setTitle("Create note")
            setInitialDate()
        } else {
            buttonText = "Modify"
            builder.setTitle("Modify note")
        }

//        id_note = repository.readCounter(repository.getAll());
        builder.setView(dialog)
            .setCancelable(true)
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int -> dialogInterface.cancel() }
            .setPositiveButton(buttonText) { dialogInterface: DialogInterface, _: Int ->
                if (note == null) {
                    interest = spinner.selectedItem.toString()
                    setInitialDate()
                    //                        Log.d(TAG, "Total notes: " + repository.getAll().size());
                    idNote = repository?.all!!.size
                    //                        Log.d(TAG, "ID new: " + id_note);
                    controller.create(
                        idNote,
                        dialogTitle.text.toString(),
                        dialogDescription.text.toString(),
                        interest,
                        currentDateTime.text.toString()
                    )
                } else {
//                        Log.d(TAG, "ID update: " + note.getId());
                    note!!.id = note!!.id
                    note!!.title = dialogTitle.text.toString()
                    note!!.description = dialogDescription.text.toString()
                    note!!.interest = interest
                    note!!.dataPerformance = currentDateTime.text.toString()
                    controller.update(note)
                }
                dialogInterface.dismiss()
            }
        return builder.create()
    }

    // установка начальной даты
    @SuppressLint("SimpleDateFormat")
    private fun setInitialDate() {
        val initData = SimpleDateFormat("dd.MM.yy")
        val currentDate = initData.format(dataChoice.time)
        currentDateTime.text = currentDate
    }

    // отображаем диалоговое окно для выбора даты
    private fun setDate() {
        DatePickerDialog(
            requireContext(), d,
            dataChoice[Calendar.YEAR],
            dataChoice[Calendar.MONTH],
            dataChoice[Calendar.DAY_OF_MONTH]
        )
            .show()
    }

    // установка обработчика выбора даты
    private var d = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        dataChoice[Calendar.YEAR] = year
        dataChoice[Calendar.MONTH] = monthOfYear
        dataChoice[Calendar.DAY_OF_MONTH] = dayOfMonth
        setInitialDate()
    }

    companion object {
        const val NOTE = "NOTE"
        fun getInstance(note: Note?): NoteDialog {
            val noteDialog = NoteDialog()
            val args = Bundle()
            args.putSerializable(NOTE, note)
            noteDialog.arguments = args
            return noteDialog
        }
    }
}