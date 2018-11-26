package com.fidenz.academy.repo;

import com.fidenz.academy.entity.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface IGenericRepository <T extends GenericEntity, String extends Serializable> extends JpaRepository<T, String> {
    @Query("SELECT g FROM GenericEntity g")
    public List<T> retrieveResponses();
}