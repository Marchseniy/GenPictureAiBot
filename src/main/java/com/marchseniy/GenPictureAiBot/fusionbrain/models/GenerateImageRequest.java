package com.marchseniy.GenPictureAiBot.fusionbrain.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GenerateImageRequest {
    private String type;
    private String query;
    private String style;
    private int width;
    private int height;
    @SerializedName("num_images")
    private int numImages;
}
