package com.fidenz.academy.util.proxy;

import com.fidenz.academy.entity.GenericEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IWebProxyServiceHelper<T extends GenericEntity, A> {

    List<T> getData(String URL, Class<A> responseClass, Class<? extends T> entityClass, String wrapperFieldName);

    T getData(String URL, Class<A> responseClass, Class<? extends T> entityClass, Object id, String idFieldName, String wrapperFieldName);
}
