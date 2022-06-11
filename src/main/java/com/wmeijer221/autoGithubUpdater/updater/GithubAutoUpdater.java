package com.wmeijer221.autoGithubUpdater.updater;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wmeijer221.autoGithubUpdater.http_client.SimpleHTTPClient;
import com.wmeijer221.autoGithubUpdater.util.AppVersionUtility;

public class GithubAutoUpdater {
    private final String owner;
    private final String repository;

    public GithubAutoUpdater(String owner, String repository) {
        this.owner = owner;
        this.repository = repository;
    }

    public CompletableFuture<List<GithubUpdate>> fetchUpdates() {
        String url = "https://api.github.com/repos/%s/%s/releases".formatted(this.owner, this.repository);
        return fetchUpdates(url);
    }

    public CompletableFuture<List<GithubUpdate>> fetchUpdates(String endpoint) {
        return SimpleHTTPClient.get(endpoint).thenApply((response) -> {
            JsonArray jObj = JsonParser.parseString((String) response).getAsJsonArray();

            ArrayList<GithubUpdate> updates = new ArrayList<>();

            for (int i = 0; i < jObj.size(); i++) {
                JsonObject element = jObj.get(i).getAsJsonObject();

                try {
                    // ignores invalid releases
                    if (isDraftOrPrerelease(element) || isNotNewerVersion(element))
                        continue;
                } catch (Exception ignored) {
                }

                // Generates update information.
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                LocalDate releaseDate = LocalDate.parse(element.get("published_at").getAsString(), formatter);
                List<Pair<String, String>> assetLinks = new ArrayList<>();
                for (JsonElement assetElement : element.get("assets").getAsJsonArray()) {
                    JsonObject asset = assetElement.getAsJsonObject();
                    String assetType = asset.get("content_type").getAsString();
                    String downloadUrl = asset.get("browser_download_url").getAsString();
                    assetLinks.add(Pair.of(assetType, downloadUrl));
                }

                GithubUpdate update = new GithubUpdate(
                        element.get("name").getAsString(), element.get("body").getAsString(),
                        releaseDate,
                        assetLinks);

                updates.add(update);
            }

            return updates;
        });
    }

    private boolean isDraftOrPrerelease(JsonObject response) {
        return response.get("draft").getAsBoolean()
                || response.get("prerelease").getAsBoolean();
    }

    private boolean isNotNewerVersion(JsonObject response) throws IOException, XmlPullParserException {
        String otherVersion = response.get("tag_name").getAsString();
        String currentVersion = "v" + AppVersionUtility.getAppVersion();
        return currentVersion.compareTo(otherVersion) >= 0;
    }

    public CompletableFuture<Boolean> downloadVersion(GithubUpdate update) {

        return null;
    }

}
