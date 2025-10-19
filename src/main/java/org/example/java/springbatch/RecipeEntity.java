package org.example.java.springbatch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Contient")
    private String continent;

    @JsonProperty("Country_State")
    private String countryState;

    @JsonProperty("cuisine")
    private String cuisine;

    @JsonProperty("title")
    private String title;

    @JsonProperty("URL")
    private String url;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("total_time")
    private Integer totalTime;

    @JsonProperty("prep_time")
    private Integer prepTime;

    @JsonProperty("cook_time")
    private Integer cookTime;

    @JsonProperty("description")
    @Column(length = 2000)
    private String description;

    @ElementCollection
    @Column(length = 5000)
    private List<String> ingredients;

    @ElementCollection
    @Column(length = 5000)
    private List<String> instructions;

    @ElementCollection
    private Map<String,String> nutrients;

    @JsonProperty("serves")
    private String serves;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountryState() {
        return countryState;
    }

    public void setCountryState(String countryState) {
        this.countryState = countryState;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public Map<String, String> getNutrients() {
        return nutrients;
    }

    public void setNutrients(Map<String, String> nutrients) {
        this.nutrients = nutrients;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }
}