package doctor.eco5;

import doctor.eco5.cmds.Cmd_Company;
import doctor.eco5.cmds.Cmd_Control;
import doctor.eco5.cmds.Cmd_SQL;
import doctor.eco5.data.Processes;
import doctor.eco5.data.USQLAdapter;
import doctor.eco5.obj.*;
import doctor.eco5.obj.eco.Economy;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.DataBlock;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.komiss77.Ostrov;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class Eco5 extends JavaPlugin {
    public static Component prefix = Component.text("[").color(TextColor.color(255, 246, 162)).append(Component.text("ECO5").color(TextColor.color(202, 157, 255))).append(Component.text("] ").color(TextColor.color(255, 246, 162)));
    public static JavaPlugin plugin;

    public static Set<RPUser >   rpUsers = new HashSet<>();
    public static Set<ATM    >   atms    = new HashSet<>();
    public static Set<Door   >   doors   = new HashSet<>();
    public static Set<Fine   >   fines   = new HashSet<>();
    public static Set<Company> companies = new HashSet<>();
    public static Set<ProductionPlace> productionPlaces = new HashSet<>();
    public static HashMap<Player, Location> sit = new HashMap<>();
    public static World world;
    public static Connection sql_connection;

    public static void rp_report(RPUser rpUser, int radius, String text) {
        Component c;
    }

    public static int time() {
        return ((int) (System.currentTimeMillis()/1000));
    }
    @Override
    public void onEnable() {
        InformationBlock.log_debug("Запуск плагина.");
        world = Bukkit.getWorlds().get(0);
        plugin = this;
        try {
            sql_connection = USQLAdapter.connect();
            InformationBlock.log_debug("Соединение с БД установлено.");
        } catch (SQLException e) {
            InformationBlock.log_debug("Не удалось установить соединение с базой данных.");
            InformationBlock.report("SQL3");
        }

        InformationBlock.log_debug("Отправляем тестовую ошибку.");
        InformationBlock.report("A1");

        Bukkit.getPluginManager().registerEvents(new BukkitEvents(), this);

        HashMap<String, CommandExecutor> commands = new HashMap<>();
        commands.put("sql", new Cmd_SQL());
        commands.put("company", new Cmd_Company());
        commands.put("control", new Cmd_Control());

        for (String name : commands.keySet()) {
            PluginCommand command = Bukkit.getPluginCommand(name);
            assert command != null;
            command.setExecutor(commands.get(name));
        }

        try {
            InformationBlock.log_debug("INIT RPUSERS: "          + Processes.init(DataBlock.rpUsers         ));
            InformationBlock.log_debug("INIT ATMS: "             + Processes.init(DataBlock.ATMs            ));
            InformationBlock.log_debug("INIT DOORS: "            + Processes.init(DataBlock.doors           ));
            InformationBlock.log_debug("INIT FINES: "            + Processes.init(DataBlock.fines           ));
            InformationBlock.log_debug("INIT COMPANIES: "        + Processes.init(DataBlock.companies       ));
            InformationBlock.log_debug("INIT PRODUCTIONPLACES: " + Processes.init(DataBlock.productionPlaces));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        InformationBlock.log_debug("Плагин запущен.");
        Bukkit.getScheduler().runTaskTimerAsynchronously(Eco5.plugin, Economy::Timer, 0, Economy.secondsTimer);
        Bukkit.getScheduler().runTaskTimerAsynchronously(Eco5.plugin, ProductionPlace::checker, 0, 10     );
    }


    @Override
    public void onDisable() {
        InformationBlock.log_debug("Выключение плагина.");

        try {
            InformationBlock.log_debug("ARCH RPUSERS:          " + Processes.arch(DataBlock.rpUsers         ));
            InformationBlock.log_debug("ARCH ATMS:             " + Processes.arch(DataBlock.ATMs            ));
            InformationBlock.log_debug("ARCH DOORS:            " + Processes.arch(DataBlock.doors           ));
            InformationBlock.log_debug("ARCH FINES:            " + Processes.arch(DataBlock.fines           ));
            InformationBlock.log_debug("ARCH COMPANIES:        " + Processes.arch(DataBlock.companies       ));
            InformationBlock.log_debug("ARCH PRODUCTIONPLACES: " + Processes.arch(DataBlock.productionPlaces));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        InformationBlock.log_debug("Плагин выключен.");
    }
}