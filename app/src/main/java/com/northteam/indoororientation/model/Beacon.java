package com.northteam.indoororientation.model;

import java.util.ArrayList;

/**
 * @author beatrizgomes
 * Date 11/05/2016
 */
public class Beacon {

    int id;
    private String uiniqueId;
    private String name;
    private String namePlace;
    private ArrayList<Edge> adj;
    private ArrayList<NearPlace> adjNear;

    public Beacon(int id, String uiniqueId, String name, String namePlace) {
        this.id = id;
        this.uiniqueId = uiniqueId;
        this.name = name;
        this.namePlace = namePlace;
    }

    public void addAdj(Edge e) {
        if(!this.adj.contains(e)) {
            adj.add(e);
        }
    }

    public void addAdjNear(NearPlace np) {
        if(!this.adjNear.contains(np)) {
            adjNear.add(np);
        }
    }

    public String getUiniqueId() {
        return uiniqueId;
    }

    public void setUiniqueId(String uiniqueId) {
        this.uiniqueId = uiniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }
}
