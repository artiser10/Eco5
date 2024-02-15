package doctor.eco5.cmds;

import doctor.eco5.Eco5;
import doctor.eco5.InformationBlock;
import doctor.eco5.obj.Company;
import doctor.eco5.obj.rp.RPUser;
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

public class Cmd_Company implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> sugg = new ArrayList<>();
        try {
            switch (args.length) {
                case 1 -> {
                    sugg.add("get");
                    sugg.add("create");
                    sugg.add("remove");
                    sugg.add("list");
                }
                case 2 -> {
                    if (args[0].equals("get") | args[0].equals("remove")) {
                        if (Eco5.companies.size() != 0) {
                            for (Company company : Eco5.companies) {
                                sugg.add(company.uuid);
                            }
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
            String type = args[0];
            if (type.equals("remove")) {
                Company company = Company.get(args[1]);
                assert company != null;
                company.remove();
                player.sendMessage(Eco5.prefix.append(Component.text("Компания " + company.uuid + " удалена.")));
                return true;
            }
            if (type.equals("get")) {
                Company company = Company.get(args[1]);
                assert company != null;
                player.sendMessage(Eco5.prefix.append(Component.text("Компания с uuid = " + company.uuid + ": " + company)));
                return true;
            }
            if (type.equals("create")) {
                if (!(args.length == 3 || args.length == 2)) {
                    player.sendMessage(Component.text("Мало аргументов. Или много. Хз."));
                    return true;
                }
                Company company = Company.create(args[1]);
                company.hire(rpUser);
                player.sendMessage(Eco5.prefix.append(Component.text("Создана компания с uuid = " + company.uuid + ": " + company)));
                return true;
            }
            if (type.equals("list")) {
                rpUser.open_companies();
            }

            return true;
        } catch (Exception e) {
            InformationBlock.report(e.toString());
            return true;
        }
    }
}