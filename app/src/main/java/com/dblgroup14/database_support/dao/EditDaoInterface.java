package com.dblgroup14.database_support.dao;
/**
 * The interface for classes holding queries for a specific type
 * @param <T>
 */
public interface EditDaoInterface<T> {
    T get(int id);
    long store(T obj);
}
