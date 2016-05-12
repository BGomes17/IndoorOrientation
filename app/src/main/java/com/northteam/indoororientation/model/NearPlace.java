package com.northteam.indoororientation.model;

/**
 * @author beatrizgomes
 * Date 11/05/2016
 */
public class NearPlace {

    int id;
    float distance;
    char compass;

    public NearPlace(int id, float distance, char compass) {
        this.id = id;
        this.distance = distance;
        this.compass = compass;
    }
}
