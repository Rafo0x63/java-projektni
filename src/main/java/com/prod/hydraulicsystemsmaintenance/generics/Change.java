package com.prod.hydraulicsystemsmaintenance.generics;

public class Change<K, V> {
    K requester;
    V change;

    public Change(K requester, V change) {
        this.requester = requester;
        this.change = change;
    }

    public void save() {

    }

    public K getRequester() {
        return requester;
    }

    public void setRequester(K requester) {
        this.requester = requester;
    }

    public V getChange() {
        return change;
    }

    public void setChange(V change) {
        this.change = change;
    }


    @Override
    public String toString() {
        return change.toString();
    }
}
