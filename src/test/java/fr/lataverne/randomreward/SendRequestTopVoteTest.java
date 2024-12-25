package fr.lataverne.randomreward;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

public class SendRequestTopVoteTest {

    CommandSender sender = null;
    String playerName = "Black_Fox3000";
    String webSite = "test.org";


    @Test
    public void sendNotification(){
        SendRequestTopVote.send(sender, playerName, webSite);

    }


}
