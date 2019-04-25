package alpitsolutions.com.bakingmadeeasy.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "ingredient",
        foreignKeys = @ForeignKey(entity = TbRecipeEntity.class,
        parentColumns = "recipeId",
        childColumns = "recipeId",
        onDelete = CASCADE))
public class TbIngredientEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer recipeId;

    private Double quantity;

    private String measure;

    private String ingredient;

    @Ignore
    public TbIngredientEntity(String ingredient) {
        this.ingredient = ingredient;
    }

    public TbIngredientEntity(Integer id, Integer recipeId, Double quantity, String measure, String ingredient) {
        this.id = id;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

}
