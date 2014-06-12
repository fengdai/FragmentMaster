package com.fragmentmaster.sample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.Request;

public class Home extends MasterFragment {

	static class Entry {

		String mTitle;
		Request mRequest;

		Entry(String title, Request request) {
			mTitle = title;
			mRequest = request;
		}

		@Override
		public String toString() {
			return mTitle;
		}
	}

	private static final List<Entry> ENTRIES = new ArrayList<Home.Entry>();

	static {
		ENTRIES.add(new Entry("Pass Data", new Request(PassData.class)));
		ENTRIES.add(new Entry("Receive Result",
				new Request(ReceiveResult.class)));
		ENTRIES.add(new Entry("Slide Enable", new Request(SlideEnable.class)));
		ENTRIES.add(new Entry("Soft Input", new Request(SoftInput.class)));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		ListView listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(new ArrayAdapter<Entry>(getActivity(),
				android.R.layout.simple_list_item_1, ENTRIES));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startFragment(ENTRIES.get(arg2).mRequest);
			}
		});
	}

}
