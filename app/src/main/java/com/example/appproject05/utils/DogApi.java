package com.example.appproject05.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DogApi {
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";

    public static String getDogImage() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (Exception e) {
            // Log ou tratamento de erro pode ser adicionado aqui
            return null;
        }
    }

    // Método adicional para busca assíncrona
    public interface DogImageCallback {
        void onImageUrlReceived(String imageUrl);
        void onError(Exception e);
    }

    public static void fetchRandomDogImage(DogImageCallback callback) {
        new Thread(() -> {
            try {
                String jsonResponse = getDogImage();
                if (jsonResponse != null) {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResponse);
                    String imageUrl = jsonObject.getString("message");

                    // Retornar na thread principal
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            callback.onImageUrlReceived(imageUrl)
                    );
                } else {
                    throw new Exception("Falha ao obter imagem");
                }
            } catch (Exception e) {
                // Retornar erro na thread principal
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                        callback.onError(e)
                );
            }
        }).start();
    }
}