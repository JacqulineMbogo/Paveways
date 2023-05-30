package com.paveways.Home;

public class Categories_Model {

    String id, name,model;

    public Categories_Model(String id, String name, String model) {
        this.id = id;
        this.name = name;
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
