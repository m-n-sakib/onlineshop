package com.example.cuetplex;

import java.util.ArrayList;
import java.util.HashMap;

public class product_show_data_model {

    private String Image;
    private String Name,Price,id;

    public product_show_data_model() {
    }

    public product_show_data_model(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public product_show_data_model(String Image, String Name, String Price) {
        this.Image = Image;
        this.Name = Name;
        this.Price = Price;
    }

    public String getImage() {
        return Image;
    }
    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }
}
