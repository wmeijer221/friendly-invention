package com.wmeijer221.autoGithubUpdater.updater;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public record GithubUpdate(String name, String description, LocalDate releaseDate, List<Pair<String, String>> assets) {

}
