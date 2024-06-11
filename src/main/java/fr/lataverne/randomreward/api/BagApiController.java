package fr.lataverne.randomreward.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BagApiController {

            /*
            /API/UUID/Update/Password
            Vérifie par itération sur les ID qu'il n'y ai pas de divergence entre les id existant sur le json et sur la bdd,
             si id existant en bdd mais inexistant sur le json alors suppression de la ligne en bdd
            Sauvegarde les items qui n'ont pas d'id dans le Json en base de donnée pour sauvegarder le bag
            Requête POST
             */
    public void convertBagYmlToBdd(){
        //
        System.out.println("convert");
    }



}
