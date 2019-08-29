package com.restapi.library.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenericRepository<T> extends CrudRepository<T, Long> {

    @Override
    List<T> findAll();

}
