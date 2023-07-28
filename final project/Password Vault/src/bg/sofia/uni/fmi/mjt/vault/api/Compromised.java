package bg.sofia.uni.fmi.mjt.vault.api;

import bg.sofia.uni.fmi.mjt.vault.exception.CompromisedException;

public class Compromised {
    public static void isItCompromised(InformationForThePassword information) {
        final int minLimit = 3;
        if (information.revealedInExposure() || information.exposureCount() > 0 ||
                information.relativeExposureFrequency() > minLimit) {
            throw new CompromisedException("Your password is not saved in our system, because it is compromised! " +
                    System.lineSeparator() + "We recommend you to add a new one!" + System.lineSeparator());
        }
    }
}
