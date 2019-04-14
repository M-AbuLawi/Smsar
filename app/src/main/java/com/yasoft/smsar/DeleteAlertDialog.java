package com.yasoft.smsar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteAlertDialog extends AppCompatDialogFragment {

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder mBuilder=new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
             mBuilder.setTitle("Delete Property")
             .setMessage("are you sure ?")
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                         }
                     })
                    .setCancelable(true)
             ;



        return mBuilder.create();

    }
}
