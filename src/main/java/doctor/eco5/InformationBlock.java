package doctor.eco5;

import doctor.eco5.ds.DiscordWebhook;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class InformationBlock {

    private static final boolean debug = true;

    private static final Component error_prefix = Component.text("[").color(TextColor.color(255, 222, 0)).append(Component.text("ECO5").color(TextColor.color(255, 62, 67))).append(Component.text("]").color(TextColor.color(255, 222, 0)));
    private static final HashMap<String, String> errors = new HashMap<>();
    static {
        errors.put("A1" , "Проверка системы при включении."                 );
        errors.put("I1" , "Ошибка не найдена в кодеке ошибок."              );
        errors.put("DR1", "Не удалось отправить ошибку через DiscordReport.");
        errors.put("DA1", "Не обнаружена таблица с указанным именем: ");
        errors.put("SQL3", "Не удалось подключиться к базе данных."         );
    }

    public static void report(final String error) {
        if (errors.containsKey(error)) {
            discordReport(error, errors.get(error));
        } else {
            log_error("I1", errors.get("I1") + " [" + error + "]");
        }
    }
    public static void report(final String error, final String text) {
        discordReport(error, errors.get(error) + " " + text);
    }
    public static void report(Exception e) {
        report(Arrays.toString(e.getStackTrace()));
    }

    private static void discordReport(final String code, final String text) {
        try {
            DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/1177480470527750204/4jhomX8I70r_RgvSKhdLR3bUYSEUq5q5f8Gnvxpsuz5TTGj8CF6zptoLxcE2RempttAV");
            discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setColor(Color.red).addField("Fatta - Error [" + code + "]",  text, true));
            discordWebhook.execute();
            log_error(code, text);
        }  catch (Exception e) {
            final String err_code =  "DR1";
            log_error(code, text);
            log_error(err_code, e.getMessage());
        }
    }

    private static void log_error(final String code, final String text) {
        try {
            Bukkit.broadcast(error_prefix.append(Component.text(" Обнаружена ошибка ["+code+"]! " + text).color(TextColor.color(255, 3, 0))));
            System.out.println("[ECO5] Обнаружена ошибка ["+code+"]! " + text);
        } catch (Exception e) {
            System.out.println("[ECO5] Не удалось отправить ошибку ["+code+"] в лог. Причина: " + e.getMessage() + ". Ошибка: " + text + ".");
        }
    }

    public static void broadcast(final Component component, final Sound sound) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Bukkit.broadcast(component);
            return;
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(component);
            if (sound != null) {
                player.playSound(player.getLocation(), sound, 1, 1);
            }
        }
    }
    public static void broadcast(final Component component) {
        broadcast(component, null);
    }
    public static void broadcast(final String message) {
        broadcast(message, null);
    }
    public static void broadcast(final String message, Sound sound) {
        Component component = Component.text("[").color(TextColor.color(0x4DFF00)).append(Component.text("ECO5").color(TextColor.color(0xFFDE00))).append(Component.text("]").color(TextColor.color(0x4DFF00))).append(Component.text(" '" + message + "'!").color(TextColor.color(0xFFF6A2)));
        component = component.hoverEvent(HoverEvent.showText(Component.text("Создано автоматически!").color(TextColor.color(0xCA9DFF))));
        broadcast(component, sound);
    }

    public static void log_debug(final String text) {
        if (debug) {
            Bukkit.broadcast(error_prefix.append(Component.text(" [DBG]: " + text).color(TextColor.color(96, 255, 107))));
            System.out.println("[ECO5] [DBG]: " + text);
        }
    }

}