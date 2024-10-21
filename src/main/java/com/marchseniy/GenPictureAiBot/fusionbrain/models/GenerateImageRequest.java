package com.marchseniy.GenPictureAiBot.fusionbrain.models;

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
    private int numImages;
}
