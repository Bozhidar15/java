package bg.sofia.uni.fmi.mjt.vault.api;


import bg.sofia.uni.fmi.mjt.vault.exception.CompromisedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CompromisedTest {

    @Test
    public void testInvalidResponse() {
        try (MockedStatic<Exposure> exposureMockedStatic = Mockito.mockStatic(Exposure.class)) {
            exposureMockedStatic.when(() -> Exposure.getNewsByParameters(Mockito.anyString()))
                    .thenReturn(new InformationForThePassword(false, 2,
                            0));


        }
        assertThrows(CompromisedException.class, () -> Compromised.isItCompromised(Exposure
                        .getNewsByParameters("Boy")),
                "should throw exception for not correctly creating uri");
    }
}
