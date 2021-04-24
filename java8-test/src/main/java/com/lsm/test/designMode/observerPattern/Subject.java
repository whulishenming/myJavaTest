package com.lsm.test.designMode.observerPattern;

public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(String tweet);
}
