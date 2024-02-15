package doctor.eco5.obj.guis;

import doctor.eco5.obj.Company;
import doctor.eco5.obj.CompanyMember;
import doctor.eco5.obj.CompanyRang;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.CompanyPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InputButton;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;

import java.util.List;

public class CompanyRang_main implements InventoryProvider {
    private final Company company;
    private final RPUser viewer;
    private final CompanyRang cr;

    public CompanyRang_main(Company company, RPUser viewer, CompanyRang cr) {
        this.company = company;
        this.viewer  = viewer ;
        this.cr = cr;
    }

    @Override
    public void init(Player player, InventoryContent inventoryContent) {
        Company company = this.company;
        RPUser viewer   = this.viewer ;


        CompanyMember cm = company.getStaff(viewer);
        if (viewer.isStaff() || cm.rang == company.maxRang() || (cm.rang.priority > cr.priority && cm.hasPermission(CompanyPermission.MANAGE_MEMBERS))) {
            // ok!
        } else {
            viewer.sendMessage("У вас нет права на редактирование этой должности.");
            viewer.getPlayer().closeInventory();
            return;
        }

        // Когда опен. Да я в курсе, что можно к проще. Так проще для восприятия
        ItemStack is = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
        im.lore(List.of(Component.text("§3Пусто")));
        is.setItemMeta(im);
        inventoryContent.fill(ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

        is = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
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

        is = new ItemStack(Material.LECTERN, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Изменить имя").color(TextColor.color(141, 255, 243)));
        im.lore(List.of(Component.text("§3Нажмите чтобы изменить имя должности")));
        is.setItemMeta(im);
        inventoryContent.set(20, new InputButton(InputButton.InputType.ANVILL, is, cr.name, data ->{
            cr.name = data;
            reopen(player, inventoryContent);
        }));

        is = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("В меню").color(TextColor.color(255, 82, 68)));
        im.lore(List.of(
                Component.text("§3Пусто").color(TextColor.color(255, 255, 255)))
        );
        is.setItemMeta(im);
        inventoryContent.set(4, ClickableItem.of(is, inventoryClickEvent -> {
            viewer.open_companies();
        }));

        if (viewer.isStaff() || (company.isStaff(viewer) && (company.getStaff(viewer).rang.priority > cr.priority || company.getStaff(viewer).rang.priority == company.maxRang().priority))) {
            is = new ItemStack(Material.TNT, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("Удалить должность").color(TextColor.color(255, 10, 0)));
            im.lore(List.of(Component.text("§3Нажмите чтобы удалить эту должность")));
            is.setItemMeta(im);
            inventoryContent.set(49, ClickableItem.of(is, inventoryClickEvent -> {
                company.removeRang(cr);
                viewer.sendMessage("Должность успешно удалена");
                viewer.getPlayer().closeInventory();
            }));
        }

        is = new ItemStack(Material.BEACON, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Изменить приоритет").color(TextColor.color(141, 255, 243)));
        im.lore(List.of(Component.text("§3Нажмите чтобы изменить приоритет")));
        is.setItemMeta(im);
        inventoryContent.set(22, new InputButton(InputButton.InputType.ANVILL, is, "" + cr.priority, data ->{
            try {
                int pr = Integer.parseInt(data);
                if (viewer.isStaff() || (company.isStaff(viewer) && company.getStaff(viewer).rang.priority > pr)) {
                    cr.priority = pr;
                    viewer.sendMessage("Приоритет установлен");
                } else {
                    viewer.sendMessage("Ошибка! Ваш приоритет ниже, либо равен устанавливаемому!");
                }
            } catch (Exception ignored) {
                // парс не провести, не int ввели
            }
            reopen(player, inventoryContent);
        }));

        is = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Изменить зарплату").color(TextColor.color(141, 255, 243)));
        im.lore(List.of(Component.text("§3Нажмите чтобы изменить зарплату")));
        is.setItemMeta(im);
        inventoryContent.set(24, new InputButton(InputButton.InputType.ANVILL, is, "" + cr.salary, data ->{
            try {
                cr.salary = Integer.parseInt(data);
            } catch (Exception ignored) {

            }
            reopen(player, inventoryContent);
        }));



    }
}