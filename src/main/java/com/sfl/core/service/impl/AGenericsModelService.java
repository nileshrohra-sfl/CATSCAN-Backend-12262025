package com.sfl.core.service.impl;

import com.sfl.core.domain.CatScanProduct;
import com.sfl.core.service.IGenericsModelService;
import com.sfl.core.service.mapper.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public abstract class AGenericsModelService<T, ID extends Serializable, D> implements
    IGenericsModelService<T, ID, D> {


    protected Logger logger = LoggerFactory.getLogger(getServiceClass());


    public abstract JpaRepository<T, ID> getJpaRepository();

    @Override
    public <S extends T> S save(S entity) {
        return getJpaRepository().save(entity);
    }

    public D saveAndGetDto(T entity){
        return  getMapper().toDto(this.save(entity));
    }


    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return getJpaRepository().saveAndFlush(entity);
    }


    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return getJpaRepository().saveAll(entities);
    }


    @Override
    public void delete(ID id) {
        getJpaRepository().deleteById(id);
    }


    @Override
    public void delete(T entity) {
        getJpaRepository().delete(entity);
    }


    @Override
    public void deleteAll() {
        getJpaRepository().deleteAll();
    }


    @Override
    public void deleteAllInBatch() {
        getJpaRepository().deleteAllInBatch();
    }


    @Override
    public Boolean exists(ID id) {
        return getJpaRepository().existsById(id);
    }


    @Override
    public List<T> findAll() {
        return getJpaRepository().findAll();
    }


    @Override
    public List<T> findAll(Collection<ID> ids) {
        return getJpaRepository().findAllById(ids);
    }


    @Override
    public Page<T> findAll(Pageable pageable) {
        return getJpaRepository().findAll(pageable);
    }

    public Page<D> findAllDTO(Pageable pageable) {
        return getJpaRepository().findAll(pageable).map(getMapper()::toDto);
    }

    @Override
    public Optional<T> findOne(ID id) {
        return getJpaRepository().findById(id);
    }


    @Override
    public void flush() {
        getJpaRepository().flush();
    }


    public abstract Class<?> getServiceClass();

    public abstract EntityMapper<D, T> getMapper();


}
