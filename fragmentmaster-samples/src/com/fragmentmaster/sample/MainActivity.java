package com.fragmentmaster.sample;

import android.os.Bundle;

import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;

public class MainActivity extends MasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentMaster fragmentMaster = getSupportFragmentMaster();
        fragmentMaster.install(R.id.container, new Request(Home.class), true);
    }
}
