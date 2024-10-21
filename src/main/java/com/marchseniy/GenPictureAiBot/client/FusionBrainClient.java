package com.marchseniy.GenPictureAiBot.client;

import com.marchseniy.GenPictureAiBot.config.FusionBrainConfig;
import com.marchseniy.GenPictureAiBot.fusionbrain.models.GenerateImageRequest;
import com.marchseniy.GenPictureAiBot.fusionbrain.models.GenerateImageResponse;
import retrofit2.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@PropertySource("application.properties")
public class FusionBrainClient extends Client<FusionBrainApi> {
    private static final String BASE_URL = "https://api-key.fusionbrain.ai/";
    private final String apiKey;
    private final String secretKey;

    @Autowired
    public FusionBrainClient(FusionBrainConfig fusionBrainConfig) {
        apiKey = fusionBrainConfig.getApiKey();
        secretKey = fusionBrainConfig.getSecretKey();
    }

    public CompletableFuture<GenerateImageResponse> getImage(String prompt, String style, int width, int height) {
        GenerateImageRequest request = new GenerateImageRequest("GENERATE", prompt, style, width, height, 1);
        Call<GenerateImageResponse> call = service.getImage(apiKey, secretKey, request);
        return getFutureObject(call);
    }

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    protected Class<FusionBrainApi> getService() {
        return FusionBrainApi.class;
    }
}

