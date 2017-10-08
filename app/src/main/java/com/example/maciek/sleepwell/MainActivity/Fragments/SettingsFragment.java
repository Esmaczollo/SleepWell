package com.example.maciek.sleepwell.MainActivity.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.maciek.sleepwell.R;

/**
 * Created by Maciek on 27.04.2017.
 */

public class SettingsFragment extends Fragment {
    Button buttonSound;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        buttonSound.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Button", "Klika nam buttonSound");
//                Intent intent = new Intent(getActivity(),SleepingActivity.class); // This is not working in class witch extends Fragment
//                startActivity(intent);
//
//            }
//        });
    }


}
