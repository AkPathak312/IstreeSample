package com.example.a_k_p.practice1.Model;


public class AllTarrif {

    private String category;
    private Double price;
    private Double hangerprice;

    public AllTarrif(String category, Double price, Double hangerprice) {
        this.category = category;
        this.price = price;
        this.hangerprice = hangerprice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getHangerprice() {
        return hangerprice;
    }

    public void setHangerprice(Double hangerprice) {
        this.hangerprice = hangerprice;
    }

    @Override
    public String toString() {
        return  category;
    }
}
