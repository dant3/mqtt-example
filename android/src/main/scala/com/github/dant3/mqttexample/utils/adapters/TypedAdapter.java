package com.github.dant3.mqttexample.utils.adapters;

import android.widget.Adapter;

public interface TypedAdapter<T> extends Adapter {
    public T getItem(int position);
}
