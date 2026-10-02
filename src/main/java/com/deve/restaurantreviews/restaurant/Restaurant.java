package com.deve.restaurantreviews.restaurant;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int priceRange;
    private String address;
    private String city;
    private String neighborhood;
    private String phone;
    private String website;
    private Integer establishedYear;
    private int reviewCount;
    private long ratingSum;
    private int ratingCount;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    protected Restaurant() {}

    public Restaurant(String name, int priceRange , String address, String city, String neighborhood){
        this.name = name;
        this.priceRange = priceRange;
        this.address = address;
        this.city = city;
        this.neighborhood = neighborhood;
    }

    public double getAverageRating() {
        return ratingCount == 0 ? 0.0 : (double) ratingSum / ratingCount;
    }

    // -------------- Getters

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriceRange() {
        return priceRange;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public Integer getEstablishedYear() {
        return establishedYear;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public long getRatingSum() {
        return ratingSum;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    // -------------- Setters


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriceRange(int priceRange) {
        this.priceRange = priceRange;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }
}
