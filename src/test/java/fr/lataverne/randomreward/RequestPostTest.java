package fr.lataverne.randomreward;

import static org.mockito.Mockito.*;

import fr.lataverne.randomreward.api.RequestPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestPostTest {

    private ConfigManager mockConfig;

    @BeforeEach
    void setUp() {
        // Mock du ConfigManager
        mockConfig = mock(ConfigManager.class);

        // Simulation des retours de configuration
        when(mockConfig.getUrlNotification()).thenReturn("http://localhost/ApiRandomReward");
        when(mockConfig.getTokenNotification()).thenReturn("VOTRE_TOKEN_SECRET");
        when(mockConfig.getUrlGetVote()).thenReturn("http://localhost/ApiRandomReward/getVote.php?pseudo=");
        when(mockConfig.getUrlGetVotes()).thenReturn("http://localhost/ApiRandomReward/getVotes.php");

        // Injecter le mock dans la classe RequestPost
        RequestPost.config = mockConfig;
    }

    @Test
    void testSendNotificationVotePost() throws Exception {
        // Simuler un pseudo pour le test
        String pseudo = "";
        String url = mockConfig.getUrlGetVote()+pseudo;
        String url2 = mockConfig.getUrlGetVotes();
        String url3 = mockConfig.getUrlGetVotes()+"?nbMonth=202503";

        // Appel de la méthode à tester
        RequestPost.sendNotificationVotePost(pseudo);

        // Vérifier que getUrlNotification et getPassPhraseNotification ont été appelés
        verify(mockConfig, times(1)).getUrlNotification();
        verify(mockConfig, times(1)).getTokenNotification();
        verify(mockConfig, times(1)).getUrlGetVote();
        verify(mockConfig, times(2)).getUrlGetVotes();

        RequestPost.sendGet(url);
        RequestPost.sendGet(url2);
        System.out.println(url3);
        RequestPost.sendGet(url3);
    }
}
