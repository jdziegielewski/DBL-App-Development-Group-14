package com.dblgroup14.support.dao;

public interface HostDaoInterface<T extends Object> {
    T get(int id);
    long store(T obj);
}
