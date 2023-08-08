package fr.lataverne.randomreward;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Use for debug
 */
public class Car {
    @JsonProperty("color")
    private String color;
    @JsonProperty("type")
    private String type;


    // standard getters setters
    public Car(String[] words){
        this.color = words[0];
        this.type = words[1];
    }

    @JsonCreator
    public Car(){}
}