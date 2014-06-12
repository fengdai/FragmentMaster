package com.fragmentmaster.sample;

import android.os.Bundle;

import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.R;
import com.fragmentmaster.transformer.DepthPageTransformer;

public class MainActivity extends MasterActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentMaster fragmentMaster = getFragmentMaster();
		fragmentMaster.setPageTransformer(false, new DepthPageTransformer());
		fragmentMaster.install(R.id.container, new Request(Home.class),
				true);
	}
}
