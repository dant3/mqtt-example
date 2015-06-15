package com.github.dant3.mqttexample.utils.adapters;

import android.widget.BaseAdapter;
import scala.collection.Seq;

public abstract class SeqAdapter<T> extends BaseAdapter implements TypedAdapter<T> {
    public abstract Seq<T> content();

    public int getCount() {
        Seq<T> data = content();
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    public T getItem(int position) {
        Seq<T> data = content();
        if (data == null) {
            return null;
        } else {
            return data.apply(position);
        }
    }

    public long getItemId(int position) {
        return position;
    }
}
