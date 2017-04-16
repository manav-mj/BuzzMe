package com.hungryhackers.buzzme.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hungryhackers.buzzme.R;

/**
 * Created by YourFather on 26-03-2017.
 */

public class ToolbarDescFragment extends android.support.v4.app.Fragment {
    EditText editText;

    @Override
    public void onResume() {
        editText.setFocusable(true);
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fab_toolbar_desc, container, false);
        editText = (EditText) v.findViewById(R.id.toolbar_description);
        return v;
    }

    public String getDescription(){
        if (editText!=null) {
            return editText.getText().toString();
        }
        return "";
    }

    public void setDescription(String s){
        if (editText!=null){
            editText.setText(s);
        }
    }
}
