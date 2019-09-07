package com.example.statefull;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class MoodDialogFragment extends DialogFragment {

    public Fragment fragment;
    private MoodDialogListener mlistener;

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mlistener = (MoodDialogListener) fragment;
        } catch (ClassCastException e) {
            Log.d("Errorin", "ClassCastEx");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Context context = getContext();
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter the current mood")
                    .setItems(R.array.moods, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] moods = getResources().getStringArray(R.array.moods);
                            if (mlistener != null) {
                                mlistener.onItemClicked(moods[which]);
                            } else {
                                Log.d("mListenrer", "null");
                            }

                        }
                    });
            return builder.create();
        } else {
            return super.onCreateDialog(savedInstanceState);
        }

    }

    public interface MoodDialogListener {
        void onItemClicked(String s);
    }
}
