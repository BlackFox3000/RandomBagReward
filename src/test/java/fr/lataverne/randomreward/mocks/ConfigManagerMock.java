package fr.lataverne.randomreward.mocks;

public class ConfigManagerMock {
    String passPhraseNotification;
    String UrlNotification;
    String PassPhraseStorage;
    String UrlStorage;

    public ConfigManagerMock(String urlNotification,
                             String passPhraseNotification,
                             String passPhraseStorage,
                             String urlStorage) {
        this.UrlNotification = urlNotification;
        this.passPhraseNotification = passPhraseNotification;
        this.PassPhraseStorage = passPhraseStorage;
        this.UrlStorage = urlStorage;
    }

    public String getPassPhraseNotification() {
        return passPhraseNotification;
    }

    public String getUrlNotification() {
        return UrlNotification;
    }

    public String getPassPhraseStorage() {
        return PassPhraseStorage;
    }

    public String getUrlStorage() {
        return UrlStorage;
    }
}
