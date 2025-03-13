package com.marchseniy.GenPictureAiBot.client;

import com.marchseniy.GenPictureAiBot.fusionbrain.models.GenerateImageResponse;
import com.marchseniy.GenPictureAiBot.fusionbrain.models.Model;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FusionBrainApi extends ApiService {
    @Multipart
    @POST("key/api/v1/text2image/run")
    Call<GenerateImageResponse> getImage(
            @Header("X-Key") String apiKey,
            @Header("X-Secret") String secretKey,
            @Part("model_id") RequestBody modelId,
            @Part("params") RequestBody params);

    @GET("key/api/v1/text2image/status/{uuid}")
    Call<GenerateImageResponse> checkGenerationStatus(
            @Header("X-Key") String apiKey,
            @Header("X-Secret") String secretKey,
            @Path("uuid") String uuid
    );

    @GET("key/api/v1/models")
    Call<List<Model>> getModels(
            @Header("X-Key") String apiKey,
            @Header("X-Secret") String secretKey
    );
}
