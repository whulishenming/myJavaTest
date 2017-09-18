package com.lsm.jsoup.designMode.observerPattern;

public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(String tweet);
}
