/*
 * Copyright 2014 Feng Dai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
