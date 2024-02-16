package doctor.eco5.cmds;

import doctor.eco5.data.DataAdapter;
import doctor.eco5.types.DataAction;
import doctor.eco5.types.DataBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Cmd_SQL implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> sugg = new ArrayList<>();

        switch (args.length) {
            case 1 -> sugg.add("request");
            case 2 -> {
                sugg.add("upload");
                sugg.add("download");
            }
            case 3 -> {
                sugg.add("rpusers");
                sugg.add("doors");
                sugg.add("atms");
                sugg.add("fines");
                sugg.add("companies");
                sugg.add("productionPlaces");
            }
        }

        return sugg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!args[0].equalsIgnoreCase("request")) {
            return true;
        }
        DataAction da = null;
        DataBlock db;
        if (args[1].equals("upload")) { // отправляем туда
            da = DataAction.archivization;
        }
        if (args[1].equals("download")) { // загружаем оттуда
            da = DataAction.initialization;
        }
        switch (args[2]) {
            case ("doors")            -> {db = DataBlock.doors;            break;}
            case ("rpusers")          -> {db = DataBlock.rpUsers;          break;}
            case ("fines")            -> {db = DataBlock.fines;            break;}
            case ("atms")             -> {db = DataBlock.ATMs;             break;}
            case ("companies")        -> {db = DataBlock.companies;        break;}
            case ("productionPlaces") -> {db = DataBlock.productionPlaces; break;}
            case default -> {
                return true;
            }
        }
        if (da == null) {
            return true;
        }
        try {
            boolean st = DataAdapter.dataAction(db, da);
            if (da == DataAction.archivization) {
                if (st) {
                    commandSender.sendMessage("Схавало без ошибок");
                } else {
                    commandSender.sendMessage("Схавало с ошибками");
                }
            } else {
                if (st) {
                    commandSender.sendMessage("Всосали без ошибок");
                } else {
                    commandSender.sendMessage("Всосали с ошибками");
                }
            }
        } catch (Exception e) {
            commandSender.sendMessage("Хуйня, переделывай(фулл ангрил): " + e);
        }
        return true;
    }
}