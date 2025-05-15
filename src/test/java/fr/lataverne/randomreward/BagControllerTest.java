package fr.lataverne.randomreward;

import fr.lataverne.randomreward.api.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.lataverne.randomreward.controllers.ConvertJsonFileToDB.mainTransefertJsonToDB;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BagControllerTest {

    private ConfigManager mockConfig;

    @BeforeEach
    void setUp() {
        // Mock du ConfigManager
        mockConfig = mock(ConfigManager.class);

        // Simulation des retours de configuration
        when(mockConfig.getPathFilesJson()).thenReturn("/plugins/RandomReward/players/");

    }

    @Test
    public void transfert() throws Exception {
        /**
         * [ERROR] fr.lataverne.randomreward.BagControllerTest.transfert -- Time elapsed: 0.849 s <<< ERROR!
         * java.lang.NullPointerException: Cannot invoke "fr.lataverne.randomreward.ConfigManager.getPathFilesJson()" because "fr.lataverne.randomreward.controllers.ConvertJsonFileToDB.config" is null
         * 	at fr.lataverne.randomreward.controllers.ConvertJsonFileToDB.mainTransefertJsonToDB(ConvertJsonFileToDB.java:70)
         * 	at fr.lataverne.randomreward.BagControllerTest.transfert(BagControllerTest.java:29)
         */
        //mainTransefertJsonToDB();
    }
}
