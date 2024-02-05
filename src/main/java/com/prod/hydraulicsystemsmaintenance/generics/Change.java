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

    @Override
    public String toString() {
        return STR."\{requester} \{change}";
    }
}
