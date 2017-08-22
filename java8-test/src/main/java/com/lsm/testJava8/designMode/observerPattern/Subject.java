package com.lsm.testJava8.designMode.observerPattern;

public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(String tweet);
}
