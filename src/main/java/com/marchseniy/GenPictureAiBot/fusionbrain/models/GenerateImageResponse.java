package com.marchseniy.GenPictureAiBot.fusionbrain.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GenerateImageResponse {
    private String uuid;
    private String status;
    private List<String> images;
}
