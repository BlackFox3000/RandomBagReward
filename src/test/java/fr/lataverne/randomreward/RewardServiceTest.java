package fr.lataverne.randomreward;

import fr.lataverne.randomreward.api.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class RewardServiceTest {

    private ConfigManager mockConfig;
    ConfigManager config;

    @BeforeEach
    void setUp() {
        // Mock du ConfigManager
        mockConfig = mock(ConfigManager.class);

        // Simulation des retours de configuration
        when(mockConfig.getUrlReward()).thenReturn("http://localhost/ApiRandomReward/getReward.php");
        when(mockConfig.getTokenReward()).thenReturn("VOTRE_TOKEN_SECRET");
        when(mockConfig.getDebug()).thenReturn(true);

        // Injecter le mock dans la classe RequestPost
        config = mockConfig;
    }

    @Test
    void testGetRewardPost() throws Exception {
        // Simuler un uuuid pour le test
        String uuid = "7bdb142b-50ce-482b-a635-b50fde7051ac";

        // Appel de la méthode à tester
        List<Map<String, Object>> rewards = RewardService.getRewards(uuid, config);

        // Vérifier que getUrlNotification et getPassPhraseNotification ont été appelés
        verify(mockConfig, times(1)).getTokenReward();
        verify(mockConfig, times(1)).getUrlReward();

        System.out.println("Récompenses reçues : " + rewards);
    }

    @Test
    void testAddRewardPost() throws Exception {
        // Simuler les arguments de test
        String uuid = "39c8a914-abc8-4c09-a736-deff9bcb6017";
        String plugin = "pluginTest";
        String item = "itemTest";
        int count = 1;


        // Appel de la méthode à tester
        RewardService.addReward(uuid, plugin, item, count, config);

        // Vérifier que getUrlNotification et getPassPhraseNotification ont été appelés
        verify(mockConfig, times(1)).getTokenReward();
        verify(mockConfig, times(1)).getUrlReward();
        verify(mockConfig, times(1)).getDebug();

        System.out.println("Récompenses ajoutée ");
    }

    @Test
    void testDeleteRewardPost() throws Exception {
        //simulation d'un id (doit exister !)
        int id = 35;

        // Appel de la méthode à tester
        RewardService.deleteReward(id, config);

        // Vérifier que getUrlNotification et getPassPhraseNotification ont été appelés
        verify(mockConfig, times(1)).getTokenReward();
        verify(mockConfig, times(1)).getUrlReward();
        verify(mockConfig, times(1)).getDebug();

        System.out.println("Récompense supprimée : "+id);
    }

}
