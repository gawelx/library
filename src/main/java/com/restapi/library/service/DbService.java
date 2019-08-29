package com.restapi.library.service;

import com.restapi.library.repository.GenericRepository;

import java.util.List;
import java.util.Optional;

public abstract class DbService<T> {

    private final GenericRepository<T> repository;

    public DbService(final GenericRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> get(final Long id) {
        return repository.findById(id);
    }

    public T save(final T item) {
        return repository.save(item);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

}
