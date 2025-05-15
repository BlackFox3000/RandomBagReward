package fr.lataverne.randomreward;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnvironmentDetector {

    public static boolean isRunningInMinecraft() {
        try {
            Class<?> bukkitClass = Class.forName("org.bukkit.Bukkit");
            Object server = bukkitClass.getMethod("getServer").invoke(null);
            return server != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static void log(String message) {
        if (isRunningInMinecraft()) {
            // Log côté serveur Minecraft
            org.bukkit.Bukkit.getLogger().info("[Plugin] " + message);
        } else {
            // Log côté IntelliJ ou console standard
            System.out.println("[Dev] " + message);
        }
    }
}
