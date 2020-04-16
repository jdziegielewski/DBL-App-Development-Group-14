package com.dblgroup14.support.dao;

/**
 * The interface for Queries
 * @param <T>
 */
public interface EditDaoInterface<T extends Object> {
    T get(int id);
    long store(T obj);
}
