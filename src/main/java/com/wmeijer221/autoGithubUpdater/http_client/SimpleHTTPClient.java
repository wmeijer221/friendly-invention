package com.wmeijer221.autoGithubUpdater.http_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SimpleHTTPClient {
    public static CompletableFuture<String> get(String endpoint) {
        CompletableFuture<String> future = new CompletableFuture<>();

        java.util.concurrent.ForkJoinPool.commonPool().submit(() -> {
            try {

                URL url = new URL(endpoint);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                int status = con.getResponseCode();

                if (status != 200)
                    throw new Exception("request failed");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                future.complete(content.toString());
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });

        return future;
    }
}
