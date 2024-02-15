package doctor.eco5.obj.guis;

import doctor.eco5.Eco5;
import doctor.eco5.obj.ATM;
import doctor.eco5.obj.rp.RPUser;
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

import java.util.List;

public class ATM_main implements InventoryProvider {

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

        is = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Наличные").color(TextColor.color(56, 255, 91)));
        im.lore(List.of(
                Component.text("Наличными: ").color(TextColor.color(255, 235, 182)).append(Component.text("" + rpUser.balance_a).color(TextColor.color(254, 255, 251))),
                Component.text("ЛКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("в чат").color(TextColor.color(254, 255, 251))),
                Component.text("ПКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("снять со счёта").color(TextColor.color(254, 255, 251)))
        ));
        is.setItemMeta(im);
        inventoryContent.set(11, ClickableItem.of(is, inventoryClickEvent -> {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            if (inventoryClickEvent.isLeftClick()) {
                player.sendMessage(Eco5.prefix.append(Component.text("Наличными: ").color(TextColor.color(255, 235, 182)).append(Component.text("" + rpUser.balance_a).color(TextColor.color(254, 255, 251)))));
            } else {
                ATM.open_card_cash(player);
            }
        }));

        is = new ItemStack(Material.GOLD_INGOT, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Состояние счёта").color(TextColor.color(56, 255, 91)));
        im.lore(List.of(
                Component.text("На счёте: ").color(TextColor.color(255, 235, 182)).append(Component.text("" + rpUser.balance_b).color(TextColor.color(254, 255, 251))),
                Component.text("ЛКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("в чат").color(TextColor.color(254, 255, 251))),
                Component.text("ПКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("пополнить счёт").color(TextColor.color(254, 255, 251)))
        ));
        is.setItemMeta(im);
        inventoryContent.set(15, ClickableItem.of(is, inventoryClickEvent -> {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            if (inventoryClickEvent.isLeftClick()) {
                player.sendMessage(Eco5.prefix.append(Component.text("На счёте: ").color(TextColor.color(255, 235, 182)).append(Component.text("" + rpUser.balance_b).color(TextColor.color(254, 255, 251)))));
            } else {
                ATM.open_cash_card(player);
            }
        }));

        is = new ItemStack(Material.ENCHANTED_BOOK, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Штрафы").color(TextColor.color(94, 63, 255)));
        im.lore(List.of(Component.text("Собрать информацию по Вашим штрафам").color(TextColor.color(118, 116, 255))));
        is.setItemMeta(im);
        inventoryContent.set(31, ClickableItem.of(is, inventoryClickEvent -> {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            ATM.open_for_fines(player);
        }));
    }
}