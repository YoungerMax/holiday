package com.pocolifo.holiday.fetchers.core;

public interface Fetcher<T> {
    T fetchData(String phrase) throws Exception;
}
