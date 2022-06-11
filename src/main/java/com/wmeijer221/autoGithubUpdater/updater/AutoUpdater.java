package com.wmeijer221.autoGithubUpdater.updater;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AutoUpdater {
    CompletableFuture<List<String>> fetchUpdates(String endpoint);

    CompletableFuture<Boolean> downloadVersion(String version);
}
