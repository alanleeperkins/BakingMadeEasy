package alpitsolutions.com.bakingmadeeasy.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeStepEntity {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("videoURL")
    @Expose
    private String videoUrl;

    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailUrl;

    public RecipeStepEntity(Integer id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        String output = System.lineSeparator() + "id: "+ this.id;
        output += System.lineSeparator() + "shortDescription: "+ this.shortDescription;
        output += System.lineSeparator() + "description: "+ this.description;
        output += System.lineSeparator() + "videoURL: "+ this.videoUrl;
        output += System.lineSeparator() + "thumbnailUrl: "+ this.thumbnailUrl;

        return output;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
