package doctor.eco5.obj.guis;

import doctor.eco5.obj.eco.TransAction_Handler;
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

public class ATM_card_cash implements InventoryProvider {

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
        //--------------------------------------------------------------------------------------------------------------
        is = new ItemStack(Material.COAL_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 50").color(TextColor.color(255, 38, 34)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(10,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 50);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));

        is = new ItemStack(Material.IRON_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 100").color(TextColor.color(255, 153, 51)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(19,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 100);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));

        is = new ItemStack(Material.COPPER_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 250").color(TextColor.color(255, 244, 65)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(28,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 250);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));

        is = new ItemStack(Material.REDSTONE_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 500").color(TextColor.color(170, 255, 66)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(37,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 500);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));
        //--------------------------------------------------------------------------------------------------------------
        is = new ItemStack(Material.LAPIS_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 1000").color(TextColor.color(41, 255, 27)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(16,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 1000);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));

        is = new ItemStack(Material.GOLD_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 2500").color(TextColor.color(64, 255, 139)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(25,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 2500);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));

        is = new ItemStack(Material.DIAMOND_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта 5000").color(TextColor.color(78, 252, 255)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(34,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, 5000);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));

        is = new ItemStack(Material.EMERALD_ORE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Снять со счёта  " + rpUser.balance_b).color(TextColor.color(63, 115, 255)));
        im.lore(List.of(Component.text("§3Снять с банковского счёта и получить наличные.")));
        is.setItemMeta(im);
        inventoryContent.set(43,ClickableItem.of(is, inventoryClickEvent -> {
            TransAction_Handler.from_card_to_cash(rpUser, rpUser.balance_b + 0);
            player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
        }));
    }
}