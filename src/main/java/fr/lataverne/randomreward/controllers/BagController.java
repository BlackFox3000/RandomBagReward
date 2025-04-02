package fr.lataverne.randomreward.controllers;

import fr.lataverne.randomreward.api.ApiRequestManager;

public class BagController {

    ApiRequestManager apiRequestManager;

    public BagController(ApiRequestManager apiRequestManager) {
        this.apiRequestManager = apiRequestManager;
    }

    public String getBag(String uuid){
        String jsonBag= null;
        jsonBag = this.apiRequestManager.getBag(uuid);
        return jsonBag;
    }
}
