package com.example.studiomerge.lib;

import java.util.Observable;

/**
 * This class encapsulates a single variable of any type in an
 * Observable object. It can be used to send data between the
 * start and end points of an asynchronous call.
 *
 * The wrapped value cannot be of a primitive type.
 *
 * This class has no getter method or toString() method.
 *
 * @param <T> type of the wrapped value
 */
public class MultiObservable<T> extends Observable {

    private T value;

    public MultiObservable() {}

    /**
     * Set the wrapped value and notify observers. The observers will
     * receive a reference to the new value.
     *
     * @param value new value
     */
    public void setValue(T value) {
        this.value = value;

        setChanged();
        notifyObservers(this.value);
    }
}
