package com.example.uimihnmanagement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.callback.ConfirmDeleteCallBack;

@SuppressLint("ValidFragment")
public class ConfirmDialog extends DialogFragment {
    ConfirmDeleteCallBack confirmDeleteCallBack;

    @SuppressLint("ValidFragment")
    public ConfirmDialog(ConfirmDeleteCallBack confirmDeleteCallBack) {
        this.confirmDeleteCallBack = confirmDeleteCallBack;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn chắc chắn muốn xóa ?").setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmDeleteCallBack.delete();
            }
        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }
}
