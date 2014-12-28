package com.fragmentmaster.sample.entry;

import com.fragmentmaster.app.Request;

public class Entry {

    public String mTitle;
    public Request mRequest;

    public Entry(String title, Request request) {
        mTitle = title;
        mRequest = request;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
