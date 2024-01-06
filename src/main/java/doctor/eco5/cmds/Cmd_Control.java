package doctor.eco5.cmds;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.obj.Company;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.utils.Environment_Control;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cmd_Control implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> sugg = new ArrayList<>();
        try {
            switch (args.length) {
                case 1 -> {
                    sugg.add("weather");
                    sugg.add("time");
                }
                case 2 -> {
                    switch (args[0]) {
                        case ("weather") -> {
                            sugg.add("rain");
                            sugg.add("thunder");
                            sugg.add("sun");
                        }
                        case ("time") -> {
                            sugg.add("morning");
                            sugg.add("day");
                            sugg.add("evening");
                            sugg.add("night");
                            sugg.add(".stop");
                            sugg.add(".start");
                        }
                    }
                }
            }
        } catch (Exception e) {
            InformationBlock.report(e.toString());
        }
        return sugg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            Player player = Bukkit.getPlayer(commandSender.getName());
            RPUser rpUser = RPUser.get(player);
            assert rpUser != null;
            assert player != null;

            switch (args.length) {
                case 0 -> {
                    // длина ноль, эт зачем
                }
                case 1 -> {
                    // одно слово, зач такое
                }
                case 2 -> {
                    String type = args[0];
                    String detail = args[1];

                    switch (type) {
                        case ("weather") -> {
                            switch (detail) {
                                case ("rain")    -> Environment_Control.rain();
                                case ("thunder") -> Environment_Control.thunder();
                                case ("sun")     -> Environment_Control.sun();
                            }
                        }
                        case ("time") -> {
                            switch (detail) {
                                case ("morning") -> Environment_Control.morning();
                                case ("day")     -> Environment_Control.day();
                                case ("evening") -> Environment_Control.evening();
                                case ("night")   -> Environment_Control.night();
                                case (".stop")   -> Environment_Control.stopTime();
                                case (".start")  -> Environment_Control.startTime();
                            }
                        }
                    }
                }
            }

            return true;
        } catch (Exception e) {
            InformationBlock.report(e.toString());
            return true;
        }
    }
}