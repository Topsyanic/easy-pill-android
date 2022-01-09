package com.cb008264.easy_pill_android.model;

import java.io.Serializable;

public class Medicine implements Serializable {
    private String medicineId;
    private String name;
    private String description;
    private String supplierId;
    private String weight;
    private String price;
    private String imagePath;
    private String quantity;
    private String requirePres;

    public Medicine (){
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRequirePres() {
        return requirePres;
    }

    public void setRequirePres(String requirePres) {
        this.requirePres = requirePres;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId='" + medicineId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", weight='" + weight + '\'' +
                ", price='" + price + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", quantity='" + quantity + '\'' +
                ", requirePres='" + requirePres + '\'' +
                '}';
    }
}
