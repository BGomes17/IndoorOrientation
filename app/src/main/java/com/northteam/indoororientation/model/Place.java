package com.northteam.indoororientation.model;

/**
 * @author beatrizgomes
 * Date 11/05/2016
 */
public class Place {

    private final String id;
    private final String name;

    public Place(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
