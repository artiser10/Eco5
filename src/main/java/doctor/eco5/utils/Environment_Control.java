package doctor.eco5.utils;

import doctor.eco5.Eco5;
import org.bukkit.GameRule;

public class Environment_Control {
    public static void rain() {
        rain(86400);
    } // environmental control
    public static void rain(int duration) {
        Eco5.world.setWeatherDuration(duration);
    }

    public static void sun() {
        Eco5.world.setWeatherDuration(0);
    }

    public static void thunder() {
        thunder(86400);
    }
    public static void thunder(int duration) {
        Eco5.world.setThunderDuration(duration);
        Eco5.world.setThundering(true);
    }

    public static void time(int ticks) {
        Eco5.world.setTime(ticks);
    }

    public static void morning() {
        time(0);
    }

    public static void day() {
        time(6000);
    }

    public static void evening() {
        time(12000);
    }

    public static void night() {
        time(18000);
    }

    public static void startTime() {
        Eco5.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        Eco5.world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);

    }

    public static void stopTime() {
        Eco5.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Eco5.world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
    }
}