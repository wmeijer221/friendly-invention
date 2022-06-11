package com.wmeijer221.autoGithubUpdater;

import java.util.List;

import com.wmeijer221.autoGithubUpdater.updater.GithubAutoUpdater;
import com.wmeijer221.autoGithubUpdater.updater.GithubUpdate;
import com.wmeijer221.autoGithubUpdater.util.AppVersionUtility;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        try {

            AppVersionUtility.getAppVersion();
        } catch (Exception ignored) {
        }

        GithubAutoUpdater updater = new GithubAutoUpdater("wmeijer221", "friendly-invention");
        List<GithubUpdate> updates = updater.fetchUpdates().join();

        System.out.println(updates);

    }
}
