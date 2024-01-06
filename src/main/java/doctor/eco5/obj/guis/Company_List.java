package doctor.eco5.obj.guis;

import doctor.eco5.Eco5;
import doctor.eco5.obj.Company;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.utils.Gui_Helper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;

import java.util.HashMap;
import java.util.List;

public class Company_List implements InventoryProvider {
    private final boolean all;
    private final RPUser rpUser;

    public Company_List(boolean all, RPUser rpUser) {
        this.all = all;
        this.rpUser = rpUser;
    }
    @Override
    public void init(Player player, InventoryContent inventoryContent) {
        RPUser rpUser = this.rpUser;
        if (rpUser == null) {
            return;
        }
        // Когда опен. Да я в курсе, что можно к проще. Так проще для восприятия
        ItemStack is = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
        im.lore(List.of(Component.text("§3Пусто")));
        is.setItemMeta(im);
        inventoryContent.fill(ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

        is = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
        im.lore(List.of(Component.text("§3Пусто")));
        is.setItemMeta(im);
        inventoryContent.fillBorders(ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

        is = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
        im.lore(List.of(Component.text("§3Пусто")));
        is.setItemMeta(im);
        inventoryContent.fillRect(9, 36, ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));
        inventoryContent.fillRect(17, 44, ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

        HashMap<Integer, ClickableItem> companies = new HashMap<>();
        int c = -1;
        for (Company company : Eco5.companies) {
            if (c > 52) {
                break;
            }

            c = c + 1;
            if (Gui_Helper.blocked_lots.contains(c)) {
                while (Gui_Helper.blocked_lots.contains(c)) {
                    c = c + 1;
                    if (c > 100) {
                        break;
                    }
                }
            }
            Material mat = Material.AMETHYST_BLOCK;
            is = new ItemStack(mat, 1);
            im = is.getItemMeta();
            im.displayName(Component.text(company.public_name).color(TextColor.color(50, 201, 255)));
            im.lore(List.of(
                    Component.text("UUID: ").color(TextColor.color(120, 130, 228)).append(Component.text("" + company.uuid).color(TextColor.color(255, 255, 255))),
                    Component.text("Сотрудников: ").color(TextColor.color(255, 100, 228)).append(Component.text(company.members.size()).color(TextColor.color(255, 255, 255))),
                    Component.text("Должностей: ").color(TextColor.color(255, 130, 120)).append(Component.text(company.companyRangs.size()).color(TextColor.color(255, 255, 255))),
                    Component.text("Создана: ").color(TextColor.color(120, 230, 120)).append(Component.text(company.timestamp_from).color(TextColor.color(255, 255, 255)))
            ));
            is.setItemMeta(im);
            companies.put(c, ClickableItem.of(is, inventoryClickEvent -> {
                player.sendMessage(Component.text("Клик на компанию"));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }));

        }

        for (int i : companies.keySet()) {
            inventoryContent.set(i, companies.get(i));
        }
    }
}