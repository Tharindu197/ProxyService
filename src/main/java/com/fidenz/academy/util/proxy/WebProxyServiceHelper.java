package com.fidenz.academy.util.proxy;

import com.fidenz.academy.entity.GenericEntity;
import com.fidenz.academy.log.InvalidListException;
import com.fidenz.academy.repo.IGenericRepository;
import com.fidenz.academy.util.ApiCallProcessor;
import com.fidenz.academy.util.EntityValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class WebProxyServiceHelper<T extends GenericEntity, A> implements IWebProxyServiceHelper<T, A> {

    private static Logger log = Logger.getLogger(WebProxyServiceHelper.class.getName());

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

    //this method is to mine an object which derives from GenericEntity. Mining happens navigating through supplied wrapperFieldName.
    private Object findGenericEntityFamilyObject(Class<A> responseClass, A apiResponse, String wrapperFieldName) {
        String[] entitySiteMap = wrapperFieldName.split("\\.");
        Object result = null;
        try {
            result = new PropertyDescriptor(entitySiteMap[0], responseClass).getReadMethod().invoke(apiResponse);
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
        }
        if (entitySiteMap.length > 1) {
            for (int i = 1; i < entitySiteMap.length; i++) {
                try {
                    result = new PropertyDescriptor(entitySiteMap[i], result.getClass()).getReadMethod().invoke(result);
                } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<T> populateEntityList(Object apiResponse) throws InvalidListException {
        //check if mined object is an object derived from GenericEntity
        //then push the object to a list wrapper
        List<T> entities= null;
        if (apiResponse instanceof GenericEntity) {
            entities = new ArrayList<>();
            T entity = (T) apiResponse;
            entity.setTimestamp(new Date());
            entities.add(entity);
        }
        //else check if mined object is already a list of objects.
        //might be a possible list populated from a JSON array.
        //then try to cast each of them to GenericEntity type. if fails to cast,
        //then the specified entityClass is not derived from GenericEntity.
        else if (apiResponse instanceof List) {
            try {
                entities = (List<T>) apiResponse;
                for (T entity : entities) {
                    entity.setTimestamp(new Date());
                }
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
        }  else {
            //if not a list of GenericEntity type objects or a single GenericEntity type object,
            //we cannot find a GenericEntity type object by navigating through the specified wrapperFieldName.
            //throw a serialization error.
            throw new InvalidListException("Unable to Serialize the external API output! Make sure the wrapperFieldName is correct, and the specified entityClass derives from GenericEntity!");
        }
        return entities;
    }

    @SuppressWarnings("unchecked")
    private List<T> fetchFromApiToDb(String URL, Class<A> responseClass, Class<?> entityClass, String wrapperFieldName) throws InvalidListException {
        log.info("Fetching data from " + URL + " to " + entityClass.getName());
        A apiResponse = ApiCallProcessor.processApiCall(URL, responseClass);
        if (apiResponse == null) {
            throw new InvalidListException("Unable to Serialize the external API output! Make sure the URL returns the expected response!");
        }
        if (!responseClass.equals(entityClass)) {
            //mining is required to find a GenericEntity type object from the API Response
            //by navigating through wrapperFieldName.
            apiResponse = (A) findGenericEntityFamilyObject(responseClass, apiResponse, wrapperFieldName);
        }
        //generate a java.util.List of GenericEntity type from the API Response
        List<T> entities = populateEntityList(apiResponse);
        //save each object in the list to Db
        for (T entity : entities) {
            repository.save(entity);
        }
        log.info("Saved fetched data " + entityClass.getName() + " in the cache.");
        return entities;
    }

    @SuppressWarnings("unchecked")
    //OVERLOADED METHOD: GET ALL DATA; cleint requests all data related to the requested entity class
    public List<T> getData(String URL, Class<A> responseClass, Class<? extends T> entityClass, String wrapperFieldName) {
        //get, check for data in the db at first place
        List<T> entities = repository.retrieveResponses();
        //no need to lookup for a specific entity with id since client requests all.
        //if db is empty or no data returned from the db for the specified entity type,
        //fetch new data from external api to the db
        if (isDbEmpty(entities)) { //no records in the db, call external api
            try {
                entities = (List<T>) fetchFromApiToDb(URL, responseClass, entityClass, wrapperFieldName);
            } catch (InvalidListException e) {
                e.printStackTrace();
            }
        } else {
            //data found on db. handle data.
            entities = handleDataFoundOnDb(URL, responseClass, entityClass, wrapperFieldName, entities);
        }
        return entities;
    }

    @SuppressWarnings("unchecked")
    //OVERLOADED METHOD: GET SINGLE DATA; client requests a single object related to the requested entity class
    public T getData(String URL, Class<A> responseClass, Class<? extends T> entityClass, Object id, String idFieldName, String wrapperFieldName) {
        //get, check for data in the db at first place
        List<T> entities = repository.retrieveResponses();
        //check if data is available for the entity with required id
        T entity = (T) lookUp(entities, entityClass, idFieldName, id);
        //if db is empty or no data returned from the db for the specified entity type,
        //fetch new data from external api to the db
        if (isDbEmpty(entities) || entity == null) { //no records in the db, call external api
            try {
                entity = ((List<T>) fetchFromApiToDb(URL, responseClass, entityClass, wrapperFieldName)).get(0);
            } catch (InvalidListException e) {
                e.printStackTrace();
            }
        } else {
            //data found on db. wrap found entity in a list and handle it.
            List<T> wrapped = new ArrayList<>();
            wrapped.add(entity);
            entity = handleDataFoundOnDb(URL, responseClass, entityClass, wrapperFieldName, wrapped).get(0);
        }
        return entity;
    }

    private List<T> handleDataFoundOnDb(String URL, Class<A> responseClass, Class<? extends T> entityClass, String wrapperFieldName, List<T> entities) {
        log.info("Cached data found for: " + entityClass.getName());
        //expiration check: Validating first entity of a list is enough since
        //a list related to a particular request is once inserted at the same time to the db
        if (EntityValidator.isExpired(entities.get(0))) {
            //expired. delete all entities
            for (T entity : entities) {
                repository.delete(entity);
            }
            log.info("Chached data has expired. Expired data removed from the cache.");
            //fetch new data from external api to the db
            try {
                entities = (List<T>) fetchFromApiToDb(URL, responseClass, entityClass, wrapperFieldName);
            } catch (InvalidListException e) {
                e.printStackTrace();
            }
        }
        return entities;
    }
}
