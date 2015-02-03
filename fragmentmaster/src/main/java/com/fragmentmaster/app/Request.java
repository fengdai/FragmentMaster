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

package com.fragmentmaster.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Description of your request to start a MasterFragment.
 */
public class Request implements Parcelable, Cloneable {

    private String mFragmentName;

    private Bundle mExtras;

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    /**
     * Create an empty Request.
     */
    public Request() {

    }

    public Request(Request o) {
        this.mFragmentName = o.mFragmentName;
        if (o.mExtras != null) {
            this.mExtras = new Bundle(o.mExtras);
        }
    }

    @Override
    protected Object clone() {
        return new Request(this);
    }

    public Request(Class<? extends IMasterFragment> clazz) {
        mFragmentName = clazz.getName();
    }

    /**
     * Create a Request from Parcel.
     */
    public Request(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        mFragmentName = in.readString();
        mExtras = in.readBundle();
    }

    public String getClassName() {
        return mFragmentName;
    }

    /**
     * Sets fragment's class name.
     */
    public void setClassName(String className) {
        if (className == null) throw new NullPointerException("class name is null");
        this.mFragmentName = className;
    }

    /**
     * Sets fragment's class.
     */
    public void setClass(Class<? extends IMasterFragment> clazz) {
        this.mFragmentName = clazz.getName();
    }

    @Override
    public int describeContents() {
        return (mExtras != null) ? mExtras.describeContents() : 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFragmentName);
        dest.writeBundle(mExtras);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, boolean)
     */
    public boolean getBooleanExtra(String name, boolean defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getBoolean(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, byte)
     */
    public byte getByteExtra(String name, byte defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getByte(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, short)
     */
    public short getShortExtra(String name, short defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getShort(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, char)
     */
    public char getCharExtra(String name, char defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getChar(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, int)
     */
    public int getIntExtra(String name, int defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getInt(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, long)
     */
    public long getLongExtra(String name, long defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getLong(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra(), or
     * the default value if no such item is present
     * @see #putExtra(String, float)
     */
    public float getFloatExtra(String name, float defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getFloat(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name         The name of the desired item.
     * @param defaultValue the value to be returned if no value of the desired type is
     *                     stored with the given name.
     * @return the value of an item that previously added with putExtra() or the
     * default value if none was found.
     * @see #putExtra(String, double)
     */
    public double getDoubleExtra(String name, double defaultValue) {
        return mExtras == null ? defaultValue : mExtras.getDouble(name,
                defaultValue);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no String value was found.
     * @see #putExtra(String, String)
     */
    public String getStringExtra(String name) {
        return mExtras == null ? null : mExtras.getString(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no CharSequence value was found.
     * @see #putExtra(String, CharSequence)
     */
    public CharSequence getCharSequenceExtra(String name) {
        return mExtras == null ? null : mExtras.getCharSequence(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no Parcelable value was found.
     * @see #putExtra(String, Parcelable)
     */
    public <T extends Parcelable> T getParcelableExtra(String name) {
        return mExtras == null ? null : mExtras.<T>getParcelable(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no Parcelable[] value was found.
     * @see #putExtra(String, Parcelable[])
     */
    public Parcelable[] getParcelableArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getParcelableArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no ArrayList<Parcelable> value was found.
     * @see #putParcelableArrayListExtra(String, ArrayList)
     */
    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(
            String name) {
        return mExtras == null ? null : mExtras
                .<T>getParcelableArrayList(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no Serializable value was found.
     * @see #putExtra(String, Serializable)
     */
    public Serializable getSerializableExtra(String name) {
        return mExtras == null ? null : mExtras.getSerializable(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no ArrayList<Integer> value was found.
     * @see #putIntegerArrayListExtra(String, ArrayList)
     */
    public ArrayList<Integer> getIntegerArrayListExtra(String name) {
        return mExtras == null ? null : mExtras.getIntegerArrayList(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no ArrayList<String> value was found.
     * @see #putStringArrayListExtra(String, ArrayList)
     */
    public ArrayList<String> getStringArrayListExtra(String name) {
        return mExtras == null ? null : mExtras.getStringArrayList(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no boolean array value was found.
     * @see #putExtra(String, boolean[])
     */
    public boolean[] getBooleanArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getBooleanArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no byte array value was found.
     * @see #putExtra(String, byte[])
     */
    public byte[] getByteArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getByteArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no short array value was found.
     * @see #putExtra(String, short[])
     */
    public short[] getShortArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getShortArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no char array value was found.
     * @see #putExtra(String, char[])
     */
    public char[] getCharArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getCharArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no int array value was found.
     * @see #putExtra(String, int[])
     */
    public int[] getIntArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getIntArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no long array value was found.
     * @see #putExtra(String, long[])
     */
    public long[] getLongArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getLongArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no float array value was found.
     * @see #putExtra(String, float[])
     */
    public float[] getFloatArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getFloatArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no double array value was found.
     * @see #putExtra(String, double[])
     */
    public double[] getDoubleArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getDoubleArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no String array value was found.
     * @see #putExtra(String, String[])
     */
    public String[] getStringArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getStringArray(name);
    }

    /**
     * Retrieve extended data from the request.
     *
     * @param name The name of the desired item.
     * @return the value of an item that previously added with putExtra() or
     * null if no Bundle value was found.
     * @see #putExtra(String, Bundle)
     */
    public Bundle getBundleExtra(String name) {
        return mExtras == null ? null : mExtras.getBundle(name);
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The boolean data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getBooleanExtra(String, boolean)
     */
    public Request putExtra(String name, boolean value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBoolean(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The byte data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getByteExtra(String, byte)
     */
    public Request putExtra(String name, byte value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putByte(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The char data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getCharExtra(String, char)
     */
    public Request putExtra(String name, char value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putChar(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The short data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getShortExtra(String, short)
     */
    public Request putExtra(String name, short value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putShort(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The integer data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getIntExtra(String, int)
     */
    public Request putExtra(String name, int value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putInt(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The long data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getLongExtra(String, long)
     */
    public Request putExtra(String name, long value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putLong(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The float data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getFloatExtra(String, float)
     */
    public Request putExtra(String name, float value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putFloat(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The double data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getDoubleExtra(String, double)
     */
    public Request putExtra(String name, double value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putDouble(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The String data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getStringExtra(String)
     */
    public Request putExtra(String name, String value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putString(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The CharSequence data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getCharSequenceExtra(String)
     */
    public Request putExtra(String name, CharSequence value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharSequence(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The Parcelable data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getParcelableExtra(String)
     */
    public Request putExtra(String name, Parcelable value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelable(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The Parcelable[] data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getParcelableArrayExtra(String)
     */
    public Request putExtra(String name, Parcelable[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelableArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The ArrayList<Parcelable> data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getParcelableArrayListExtra(String)
     */
    public Request putParcelableArrayListExtra(String name,
            ArrayList<? extends Parcelable> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelableArrayList(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The ArrayList<Integer> data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getIntegerArrayListExtra(String)
     */
    public Request putIntegerArrayListExtra(String name,
            ArrayList<Integer> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putIntegerArrayList(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The ArrayList<String> data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getStringArrayListExtra(String)
     */
    public Request putStringArrayListExtra(String name, ArrayList<String> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putStringArrayList(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The Serializable data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getSerializableExtra(String)
     */
    public Request putExtra(String name, Serializable value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putSerializable(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The boolean array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getBooleanArrayExtra(String)
     */
    public Request putExtra(String name, boolean[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBooleanArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The byte array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getByteArrayExtra(String)
     */
    public Request putExtra(String name, byte[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putByteArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The short array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getShortArrayExtra(String)
     */
    public Request putExtra(String name, short[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putShortArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The char array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getCharArrayExtra(String)
     */
    public Request putExtra(String name, char[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The int array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getIntArrayExtra(String)
     */
    public Request putExtra(String name, int[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putIntArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The byte array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getLongArrayExtra(String)
     */
    public Request putExtra(String name, long[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putLongArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The float array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getFloatArrayExtra(String)
     */
    public Request putExtra(String name, float[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putFloatArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The double array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getDoubleArrayExtra(String)
     */
    public Request putExtra(String name, double[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putDoubleArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The String array data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getStringArrayExtra(String)
     */
    public Request putExtra(String name, String[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putStringArray(name, value);
        return this;
    }

    /**
     * Add extended data to the extra.
     *
     * @param name  The name of the extra data, with package prefix.
     * @param value The Bundle data value.
     * @return Returns the same extra object, for chaining multiple calls into a
     * single statement.
     * @see #putExtras
     * @see #removeExtra
     * @see #getBundleExtra(String)
     */
    public Request putExtra(String name, Bundle value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBundle(name, value);
        return this;
    }

    /**
     * Copy all extras in 'src' in to this extra.
     *
     * @param src Contains the extras to copy.
     * @see #putExtra
     */
    public Request putExtras(Request src) {
        if (src.mExtras != null) {
            if (mExtras == null) {
                mExtras = new Bundle(src.mExtras);
            } else {
                mExtras.putAll(src.mExtras);
            }
        }
        return this;
    }

    /**
     * Add a set of extended data to the extra.
     *
     * @param extras The Bundle of extras to add to this extra.
     * @see #putExtra
     * @see #removeExtra
     */
    public Request putExtras(Bundle extras) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putAll(extras);
        return this;
    }

    /**
     * Completely replace the extras in the Request with the extras in the given
     * Request.
     *
     * @param src The exact extras contained in this Request are copied into the
     *            target request, replacing any that were previously there.
     */
    public Request replaceExtras(Request src) {
        mExtras = src.mExtras != null ? new Bundle(src.mExtras) : null;
        return this;
    }

    /**
     * Completely replace the extras in the Request with the given Bundle of
     * extras.
     *
     * @param extras The new set of extras in the Request, or null to erase all
     *               extras.
     */
    public Request replaceExtras(Bundle extras) {
        mExtras = extras != null ? new Bundle(extras) : null;
        return this;
    }

    /**
     * Remove extended data from the request.
     *
     * @see #putExtra
     */
    public void removeExtra(String name) {
        if (mExtras != null) {
            mExtras.remove(name);
            if (mExtras.size() == 0) {
                mExtras = null;
            }
        }
    }
}
