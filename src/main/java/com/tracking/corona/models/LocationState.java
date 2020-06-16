package com.tracking.corona.models;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LocationState {

    private String state;
    private String country;
    private int lastTotalCases;
    private int newTotalCases;



}
