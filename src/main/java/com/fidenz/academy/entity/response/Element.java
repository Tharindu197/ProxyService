package com.fidenz.academy.entity.response;
import com.fidenz.academy.entity.GenericEntity;

import javax.persistence.*;
import java.util.List;

@Embeddable
@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
public class Element extends GenericEntity {

    @Embedded
    private Coordinates coord;

    @Embedded
    private Sys sys;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "weather")
    private List<Weather> weather;

    @Embedded
    private Main main;

    private int visibility;

    @Embedded
    private Wind wind;

    @Embedded
    private Cloud clouds;

    private long dt;

    private int id;

    private String name;

    public Coordinates getCoor() {
        return getCoord();
    }

    public void setCoor(Coordinates coor) {
        this.setCoord(coor);
    }

    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Cloud getClouds() {
        return clouds;
    }

    public void setClouds(Cloud clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

