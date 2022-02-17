package ru.gb.lesson12.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.gb.lesson12.R;
import ru.gb.lesson12.data.YesNoDialogController;

public class YesNoDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_yes_no, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        builder.setCancelable(true);

        builder.setNegativeButton("No", (dialogInterface, which) -> dialogInterface.cancel());

        builder.setPositiveButton("Yes", (dialog, which) -> {
            ((YesNoDialogController)requireContext()).createAnswer();
            dialog.dismiss();
        });

        return builder.create();
    }
}
