package com.dblgroup14.support.dao;

public interface EditDaoInterface<T> {
    T get(int id);
    long store(T obj);
}
