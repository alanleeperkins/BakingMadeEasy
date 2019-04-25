package alpitsolutions.com.bakingmadeeasy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeEntity {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("servings")
    @Expose
    private Integer servings;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("ingredients")
    @Expose
    private List<RecipeIngredientEntity> ingredients;

    @SerializedName("steps")
    @Expose
    protected List<RecipeStepEntity> steps;

    public RecipeEntity(Integer id, String name, Integer servings, String image, List<RecipeIngredientEntity> ingredients, List<RecipeStepEntity> steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    @Override
    public String toString() {
        String output = System.lineSeparator() + "name: "+ this.name;
        output += System.lineSeparator() + "image: "+ this.image;
        output += System.lineSeparator() + "servings: "+ this.servings;

        return output;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<RecipeIngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStepEntity> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStepEntity> steps) {
        this.steps = steps;
    }
}

