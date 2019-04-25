package alpitsolutions.com.bakingmadeeasy.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "step",
        foreignKeys = @ForeignKey(entity = TbRecipeEntity.class,
        parentColumns = "recipeId",
        childColumns = "recipeId",
        onDelete = CASCADE))
public class TbStepEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer stepId;

    private Integer recipeId;

    private String shortDescription;

    private String description;

    private String videoURL;

    private String thumbnailURL;

    @Ignore
    public TbStepEntity(Integer stepId, Integer recipeId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.stepId = stepId;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public TbStepEntity(Integer id, Integer stepId, Integer recipeId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.stepId = stepId;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
