package com.fragmentmaster.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.Request;

public class PassData extends MasterFragment {
	public static final String EXTRA_KEY_INDEX = "index";
	private int mIndex = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pass_data_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Request request = getRequest();
		mIndex = request.getIntExtra(EXTRA_KEY_INDEX, 0);
		TextView indexView = (TextView) view.findViewById(R.id.index);
		indexView.setText(String.valueOf(mIndex));

		Button nextButton = (Button) view.findViewById(R.id.next);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Request request = new Request(PassData.class);
				request.putExtra(EXTRA_KEY_INDEX, mIndex + 1);
				startFragment(request);
			}
		});
	}

	@Override
	public String toString() {
		return "PassData: " + Integer.toString(mIndex);
	}
}
