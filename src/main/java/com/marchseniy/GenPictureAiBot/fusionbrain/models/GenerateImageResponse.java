package com.marchseniy.GenPictureAiBot.fusionbrain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GenerateImageResponse {
    private String imageUrl;
    private String status;
}
