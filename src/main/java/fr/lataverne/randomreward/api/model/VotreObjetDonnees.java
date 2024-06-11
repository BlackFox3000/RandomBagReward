package fr.lataverne.randomreward.api.model;

public class VotreObjetDonnees {
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private String pseudo;
    private int score;

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}

