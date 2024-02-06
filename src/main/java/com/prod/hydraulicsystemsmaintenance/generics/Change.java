package com.prod.hydraulicsystemsmaintenance.generics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Change<K, V> {
    K requester;
    V change;

    public Change(K requester, V change) {
        this.requester = requester;
        this.change = change;
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
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm"));
        return STR."\{change.toString()} on \{formattedDateTime}";
    }
}
