package com.fidenz.academy.entity.response.marvel;

import com.fidenz.academy.entity.GenericEntity;

import javax.persistence.*;

@Embeddable
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Story extends GenericEntity {
    private int id;
    private String title;
    private String description;
    private String resourceURI;
    private String type;
    private String modified;
    private String thumbnail;

    @Embedded
    private
    EmbeddedElement creators;

    @Embedded
    private
    EmbeddedElement characters;

    @Embedded
    private
    EmbeddedElement series;

    @Embedded
    private
    EmbeddedElement comics;

    @Embedded
    private
    EmbeddedElement events;

    @Embedded
    private
    Item originalIssue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public EmbeddedElement getCreators() {
        return creators;
    }

    public void setCreators(EmbeddedElement creators) {
        this.creators = creators;
    }

    public EmbeddedElement getCharacters() {
        return characters;
    }

    public void setCharacters(EmbeddedElement characters) {
        this.characters = characters;
    }

    public EmbeddedElement getSeries() {
        return series;
    }

    public void setSeries(EmbeddedElement series) {
        this.series = series;
    }

    public EmbeddedElement getComics() {
        return comics;
    }

    public void setComics(EmbeddedElement comics) {
        this.comics = comics;
    }

    public EmbeddedElement getEvents() {
        return events;
    }

    public void setEvents(EmbeddedElement events) {
        this.events = events;
    }

    public Item getOriginalIssue() {
        return originalIssue;
    }

    public void setOriginalIssue(Item originalIssue) {
        this.originalIssue = originalIssue;
    }
}
