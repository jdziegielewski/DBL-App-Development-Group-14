package com.dblgroup14.support.dao;

public interface EditDaoInterface<T extends Object> {
    T get(int id);
    long store(T obj);
}
