package org.dogepool.practicalrx.services;

public interface CoinServiceCallback<T> {

    void onSuccess(T value);

}
