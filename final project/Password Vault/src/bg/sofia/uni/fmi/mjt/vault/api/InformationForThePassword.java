package bg.sofia.uni.fmi.mjt.vault.api;

public record InformationForThePassword(boolean revealedInExposure, int relativeExposureFrequency, int exposureCount) {
}
