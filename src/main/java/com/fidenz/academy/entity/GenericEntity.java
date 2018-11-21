package com.fidenz.academy.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name= "META_INFO")
public abstract class GenericEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private ObjectId _id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
}