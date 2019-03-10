package com.vision.project.models.DTOs;

import com.vision.project.models.Restaurant;
import com.vision.project.models.UserModel;
import com.vision.project.models.UserDetails;
import java.util.*;

public class UserDto {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String country;
    private String role;
    private String profilePicture;
    private Restaurant restaurant;

    public UserDto(UserDetails userDetails){
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.age = userDetails.getAge();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.age = userDetails.getAge();
        this.country = userDetails.getCountry();
        this.profilePicture = userDetails.getProfilePicture();
        this.restaurant = userDetails.getRestaurant();
        this.role = new ArrayList<>(userDetails.getAuthorities()).get(0).getAuthority();
    }

    public UserDto(UserModel userModel){
        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.age = userModel.getAge();
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.age = userModel.getAge();
        this.country = userModel.getCountry();
        this.role = userModel.getRole();
        this.profilePicture = userModel.getProfilePicture();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
