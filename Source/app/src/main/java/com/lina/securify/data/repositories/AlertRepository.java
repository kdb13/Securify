package com.lina.securify.data.repositories;

public class AlertRepository {

    private static AlertRepository instance;

    private AlertRepository() { }

    public static AlertRepository getInstance() {
        if (instance == null)
            instance = new AlertRepository();

        return instance;
    }


}
