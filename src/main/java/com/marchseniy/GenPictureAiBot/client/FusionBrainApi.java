package com.marchseniy.GenPictureAiBot.client;

import com.marchseniy.GenPictureAiBot.fusionbrain.models.GenerateImageRequest;
import com.marchseniy.GenPictureAiBot.fusionbrain.models.GenerateImageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FusionBrainApi extends ApiService {
    @POST("key/api/v1/text2image/run")
    Call<GenerateImageResponse> getImage(
            @Header("X-Key") String apiKey,
            @Header("X-Secret") String secretKey,
            @Body GenerateImageRequest request
    );
}
