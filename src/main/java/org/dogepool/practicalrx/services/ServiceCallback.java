package org.dogepool.practicalrx.services;

public interface ServiceCallback<T> {

    void onSuccess(T value);

}
