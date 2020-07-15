package cn.yue.base.middle.mvvm;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collection;

public class MutableListLiveData<T> extends MutableLiveData<ArrayList<T>> {

    public MutableListLiveData() {
        super(new ArrayList<>());
    }

    @Override
    public void postValue(ArrayList<T> value) {
        super.postValue(value);
    }

    @Override
    public void setValue(ArrayList<T> value) {
        super.setValue(value);
    }

    public void add(T t) {
        ArrayList<T> list = getValue();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(t);
        postValue(list);
    }

    public void addAll(Collection<? extends T> c) {
        ArrayList<T> list = getValue();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(c);
        postValue(list);
    }

    public T remove(int index) {
        ArrayList<T> list = getValue();
        if (list == null) {
            list = new ArrayList<>();
        }
        T t = list.remove(index);
        postValue(list);
        return t;
    }

    public void clear() {
        ArrayList<T> list = getValue();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();
        postValue(list);
    }

    @Nullable
    @Override
    public ArrayList<T> getValue() {
        return super.getValue();
    }

    public int size() {
        if (getValue() != null) {
            return getValue().size();
        }
        return 0;
    }
}
