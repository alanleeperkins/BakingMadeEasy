package alpitsolutions.com.bakingmadeeasy.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.List;

@Entity(tableName = "recipe",
        indices={
            @Index(value="id"),
            @Index(value="recipeId", unique = true)
        })
public class TbRecipeEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer recipeId;

    private String name;

    @Ignore
    private List<TbIngredientEntity> ingredients;

    @Ignore
    private List<TbStepEntity> steps;

    private Integer servings;

    private  String image;

    @Ignore
    public TbRecipeEntity(Integer recipeId, String name, Integer servings, String image) {
        this.recipeId = recipeId;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public TbRecipeEntity(Integer id, Integer recipeId, String name, Integer servings, String image) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TbIngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<TbIngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<TbStepEntity> getSteps() {
        return steps;
    }

    public void setSteps(List<TbStepEntity> steps) {
        this.steps = steps;
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
}
