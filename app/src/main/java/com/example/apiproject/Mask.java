package com.example.apiproject;

public class Mask {
    private int id_toothpaste;
    private String name_toothpaste;
    private int abrasiveness_index;
    private int volume;
    private String country_of_origin;
    private double price;
    private String Image;

    public Mask(int id_toothpaste, String name_toothpaste, int abrasiveness_index,String country_of_origin, int volume,double prise, String image) {
        this.id_toothpaste = id_toothpaste;
        this.name_toothpaste = name_toothpaste;
        this.abrasiveness_index = abrasiveness_index;
        this.country_of_origin = country_of_origin;
        this.volume = volume;
        this.price = prise;
        this.Image = image;


    }

    public Mask(String name_toothpaste, int abrasiveness_index,String country_of_origin, int volume,double price, String image) {
        this.name_toothpaste = name_toothpaste;
        this.abrasiveness_index = abrasiveness_index;
        this.country_of_origin = country_of_origin;
        this.volume = volume;
        this.price = price;
        this.Image = image;
    }


    public void setId_toothpaste(int id_toothpaste) {
        this.id_toothpaste = id_toothpaste;
    }

    public void setName_toothpaste(String name_toothpaste) {
        this.name_toothpaste = name_toothpaste;
    }

    public void setAbrasiveness_index(int abrasiveness_index) {
        this.abrasiveness_index = abrasiveness_index;
    }

    public void setVolume(int volume) {
        volume = volume;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId_toothpaste() {
        return id_toothpaste;
    }

    public String getName_toothpaste() {
        return name_toothpaste;
    }

    public int getAbrasiveness_index() {
        return abrasiveness_index;
    }
    public double getPrice() {
        return price;
    }
    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public Integer getVolume() {
        return volume;
    }

    public String getImage() {
        return Image;
    }
}

