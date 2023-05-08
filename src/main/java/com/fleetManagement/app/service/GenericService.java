package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.GenericEntity;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService<T extends GenericEntity> {
    T update(final Long id, final T entity) throws ElementNotFoundException;

    T findById(final Long id) throws ElementNotFoundException;

    T save(final T entity) throws ElementAlreadyExistException;

    boolean delete(final Long id) throws ElementNotFoundException;
    List<T> search(String keyword, Pageable pageable) throws BusinessException;
    public long countAll();

    // TODO add pagination + sort + filter
    public List<T> findAll() throws BusinessException;
    T patch(T entity) ;

    //Page<T> findAll(final Pageable pageable, final List<Filter> filters);
}
