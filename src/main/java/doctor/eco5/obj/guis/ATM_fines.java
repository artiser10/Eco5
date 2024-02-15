package doctor.eco5.obj.guis;

import doctor.eco5.Eco5;
import doctor.eco5.obj.ATM;
import doctor.eco5.obj.Fine;
import doctor.eco5.obj.eco.TransAction_Handler;
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

public class ATM_fines implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContent inventoryContent) {
        RPUser rpUser = RPUser.get(player);
        if (rpUser == null) {
            return;
        }
        // Когда опен. Да я в курсе, что можно к проще. Так проще для восприятия
        ItemStack is = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("#").color(TextColor.color(60,60,60)));
        im.lore(List.of(Component.text("§3Пусто")));
        is.setItemMeta(im);
        inventoryContent.fill(ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

        is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("#").color(TextColor.color(60,60,60)));
        im.lore(List.of(Component.text("§3Пусто")));
        is.setItemMeta(im);
        inventoryContent.fillBorders(ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

        HashMap<Integer, ClickableItem> fines = new HashMap<>();
        int c = -1;
        for (Fine fine : Eco5.fines) {
            if (c > 52) {
                break;
            }
            if (fine.uuid.equals(rpUser.uuid)) {
                c = c + 1;
                if (Gui_Helper.blocked_lots.contains(c)) {
                    while (Gui_Helper.blocked_lots.contains(c)) {
                        c = c + 1;
                        if (c > 100) {
                            break;
                        }
                    }
                }
                Material mat = Material.COAL_ORE;
                if (fine.amount >= 100) {
                    mat = Material.IRON_ORE;
                }
                if (fine.amount >= 250) {
                    mat = Material.COPPER_ORE;
                }
                if (fine.amount >= 500) {
                    mat = Material.REDSTONE_ORE;
                }
                if (fine.amount >= 1000) {
                    mat = Material.LAPIS_ORE;
                }
                if (fine.amount >= 2500) {
                    mat = Material.DIAMOND_ORE;
                }
                if (fine.amount >= 5000) {
                    mat = Material.EMERALD_ORE;
                }
                if (fine.amount >= 10000) {
                    mat = Material.NETHERITE_BLOCK;
                }
                is = new ItemStack(mat, 1);
                im = is.getItemMeta();
                im.displayName(Component.text("Штраф [" + fine.amount + "]").color(TextColor.color(50, 201, 255)));
                im.lore(List.of(
                        Component.text("Номер: ").color(TextColor.color(120, 130, 228)).append(Component.text("" + fine.uuid).color(TextColor.color(255, 255, 255))),
                        Component.text("Сумма: ").color(TextColor.color(255, 100, 228)).append(Component.text("" + fine.amount).color(TextColor.color(255, 255, 255))),
                        Component.text("Описание: ").color(TextColor.color(255, 130, 120)).append(Component.text("" + fine.desc).color(TextColor.color(255, 255, 255))),
                        Component.text("Выдан: ").color(TextColor.color(120, 230, 120)).append(Component.text("" + fine.company).color(TextColor.color(255, 255, 255)))
                ));
                is.setItemMeta(im);
                fines.put(c, ClickableItem.of(is, inventoryClickEvent -> {
                    TransAction_Handler.pay_fine(rpUser, fine);
                    ATM.open_for_fines(player);
                }));
            }
        }

        for (int i : fines.keySet()) {
            inventoryContent.set(i, fines.get(i));
        }
    }
}