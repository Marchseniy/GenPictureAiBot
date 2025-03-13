package com.marchseniy.GenPictureAiBot.client;

import com.marchseniy.GenPictureAiBot.config.FusionBrainConfig;
import com.marchseniy.GenPictureAiBot.fusionbrain.models.GenerateImageResponse;
import com.marchseniy.GenPictureAiBot.fusionbrain.models.Model;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@PropertySource("application.properties")
public class FusionBrainClient extends Client<FusionBrainApi> {
    private static final String BASE_URL = "https://api-key.fusionbrain.ai/";
    private final String apiKey;
    private final String secretKey;

    @Autowired
    public FusionBrainClient(FusionBrainConfig fusionBrainConfig) {
        apiKey = "Key " + fusionBrainConfig.getApiKey();
        secretKey = "Secret " + fusionBrainConfig.getSecretKey();
    }

    public CompletableFuture<GenerateImageResponse> getImage(String prompt, String style, int width, int height) {
        String paramsJson = "{\"type\": \"GENERATE\", \"numImages\": 1, \"style\": \"" + style + "\", \"width\": " + width +
                ", \"height\": " + height + ", \"generateParams\": {\"query\": \"" + prompt + "\"}}";

        RequestBody params = RequestBody.create(MediaType.parse("application/json"), paramsJson);
        RequestBody modelId = RequestBody.create(MediaType.parse("text/plain"), "4");

        Call<GenerateImageResponse> call = service.getImage(apiKey, secretKey, modelId, params);

        return getFutureObject(call);
    }

    public CompletableFuture<List<Model>> getModels() {
        Call<List<Model>> call = service.getModels(apiKey, secretKey);

        return getFutureObject(call);
    }

    public CompletableFuture<GenerateImageResponse> checkGenerationStatus(String uuid) {
        Call<GenerateImageResponse> call = service.checkGenerationStatus(apiKey, secretKey, uuid);

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
