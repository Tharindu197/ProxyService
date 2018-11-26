package com.fidenz.academy.services;

import com.fidenz.academy.entity.GenericEntity;
import com.fidenz.academy.repo.IGenericRepository;
import com.fidenz.academy.util.ApiCallProcessor;
import com.fidenz.academy.util.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

abstract class WebProxyServiceHelper<T extends GenericEntity, A> {

    @Autowired
    protected IGenericRepository repository;

    private boolean isDbEmpty(List<T> elements) {
        return elements == null || elements.size() == 0;
    }

    private T lookUp(List<T> entities, Class<? extends T> lookUpClass, String fieldName, Object search) {
        for (T entity : entities) {
            try {
                if (entity != null && entity.getClass() == lookUpClass) {
                    //find getter and get value
                    Object result = new PropertyDescriptor(fieldName, lookUpClass).getReadMethod().invoke(entity);
                    if (result.equals(search)) {
                        return entity;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private T fetchSingleFromApiToDb(String URL, Class<A> responseClass, Class<? extends T> entityClass, String entityFieldName) {
        A response = ApiCallProcessor.processApiCall(URL, responseClass);
        if (responseClass.equals(entityClass)) {
            //returns response: if, required entity is the response itself
            return (T) response;
        } else {
            T entity = null;
            if (response != null) {
                try {
                    String[] entitySiteMap = entityFieldName.split("\\.");
                    Object result = new PropertyDescriptor(entitySiteMap[0], responseClass).getReadMethod().invoke(response);
                    if (entitySiteMap.length > 1) {
                        for (int i = 1; i < entitySiteMap.length; i++) {
                            result = new PropertyDescriptor(entitySiteMap[i], result.getClass()).getReadMethod().invoke(result);
                        }
                    }
                    if (result instanceof GenericEntity) {
                        entity = (T) result;
                        entity.setTimestamp(new Date());
                    } else if (result instanceof List) {
                        //if the found entity is a list, then it might be an object wrapper (array).
                        //decompose array and get the first object to return single
                        try {
                            List<T> listWrapper = (List<T>) result;
                            entity = listWrapper.get(0);
                            entity.setTimestamp(new Date());
                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
                        }
                    }
                    //save to Db
                    repository.save(entity);
                } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                    e.printStackTrace();
                }
            }
            return entity;
        }
    }

    private List<T> fetchAllFromApiToDb(String URL, Class<A> responseClass, Class<?> entityClass, String entityFieldName) {
        A response = ApiCallProcessor.processApiCall(URL, responseClass);
        if (responseClass.equals(entityClass)) {
            //returns response: if, required entity is the response itself
            return (List<T>) response;
        } else {
            List<T> entities = null;
            if (response != null) {
                try {
                    String[] entitySiteMap = entityFieldName.split("\\.");
                    Object result = new PropertyDescriptor(entitySiteMap[0], responseClass).getReadMethod().invoke(response);
                    if (entitySiteMap.length > 1) {
                        for (int i = 1; i < entitySiteMap.length; i++) {
                            result = new PropertyDescriptor(entitySiteMap[i], result.getClass()).getReadMethod().invoke(result);
                        }
                    }
                    if (result instanceof List) {
                        //if the found entity is a list, then it might be an object wrapper (array).
                        //decompose array and get the first object to return single
                        try {
                            entities = (List<T>) result;
                            for (T entity : entities) {
                                entity.setTimestamp(new Date());
                            }
                        } catch (ClassCastException cce) {
                            cce.printStackTrace();
                        }
                    } else {
                        throw new InvalidListException("Specified field is not a List. Unable to Serialize!");
                    }
                    //save to Db
                    for (T entity : entities) {
                        repository.save(entity);
                    }
                } catch (IllegalAccessException | InvocationTargetException | IntrospectionException | InvalidListException e) {
                    e.printStackTrace();
                }
            }
            return entities;
        }
    }

    protected List<T> getData(String URL, Class<A> responseClass, Class<? extends T> entityClass, String wrapperFieldName) {
        List<T> entities = repository.retrieveResponses();

        if (isDbEmpty(entities)) { //no records in the db, call external api
            entities = (List<T>) fetchAllFromApiToDb(URL, responseClass, entityClass, wrapperFieldName);
        } else {
            if (EntityValidator.isExpired(entities.get(0))) { //expired. delete entity and call api
                for (T entity : entities) {
                    repository.delete(entity);
                }
                entities = (List<T>) fetchAllFromApiToDb(URL, responseClass, entityClass, wrapperFieldName);
            }
        }
        return entities;
    }

    protected T getData(String URL, Class<A> responseClass, Class<? extends T> entityClass, Object id, String idFieldName, String wrapperFieldName) {
        List<T> entities = repository.retrieveResponses();
        T entity = (T) lookUp(entities, entityClass, idFieldName, id);

        if (isDbEmpty(entities) || entity == null) { //no records in the db, call external api
            entity = (T) fetchSingleFromApiToDb(URL, responseClass, entityClass, wrapperFieldName);
        } else {
            if (EntityValidator.isExpired(entity)) { //expired. delete entity and call api
                repository.delete(entity);
                entity = (T) fetchSingleFromApiToDb(URL, responseClass, entityClass, wrapperFieldName);
            }
        }
        return entity;
    }
}
