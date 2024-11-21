package com.example.appproject05.models;

import java.io.Serializable;

public class Address implements Serializable {
    private String label;
    private String street;
    private String neighborhood;
    private String cityState;
    private String complement;
    private boolean isDefault;

    public Address(String label, String street, String neighborhood, String cityState,
                   String complement, boolean isDefault) {
        this.label = label;
        this.street = street;
        this.neighborhood = neighborhood;
        this.cityState = cityState;
        this.complement = complement;
        this.isDefault = isDefault;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
// Getters e Setters
}