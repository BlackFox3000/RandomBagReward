package fr.lataverne.randomreward;

import static org.mockito.Mockito.*;

import fr.lataverne.randomreward.api.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

    private ConfigManager mockConfig;

    @BeforeEach
    void setUp() {
        // Mock du ConfigManager
        mockConfig = mock(ConfigManager.class);

        // Simulation des retours de configuration
        when(mockConfig.getUrlNotification()).thenReturn("http://localhost/ApiRandomReward/voteNotification.php");
        when(mockConfig.getTokenNotification()).thenReturn("MON_TOKEN_SECRET");
        when(mockConfig.getDebug()).thenReturn(true);
        // Injecter le mock dans la classe RequestPost
    }

    @Test
    void testSendNotificationVotePost() throws Exception {
        // Simuler un uuid pour le test
        String uuid = "7bdb142b-50ce-482b-a635-b50fde7051ac";
        String nbMonth = "202503";

        // Appel de la méthode à tester
        System.out.println("0");
        NotificationService.sendNotificationVotePost(uuid, mockConfig);

        // Vérifier que getUrlNotification et getPassPhraseNotification ont été appelés
        verify(mockConfig, times(1)).getUrlNotification();
        verify(mockConfig, times(1)).getTokenNotification();
        verify(mockConfig, times(1)).getDebug();

        System.out.println("1");
        NotificationService.getVotesForUuid(uuid, mockConfig);
        System.out.println("2");
        NotificationService.getAllVotes(mockConfig);
        System.out.println("3");
        NotificationService.getVotesForUuid(uuid,nbMonth, mockConfig);
        System.out.println("4");
        NotificationService.getAllVotes(nbMonth, mockConfig);
    }
}
