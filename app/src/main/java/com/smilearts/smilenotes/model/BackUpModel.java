package com.smilearts.smilenotes.model;

import java.io.Serializable;

public class BackUpModel implements Serializable {

    final String collection_name ;

    public BackUpModel(String collection_name) {
        this.collection_name = collection_name;
    }

    public String getCollection_name() {
        return collection_name;
    }
}
