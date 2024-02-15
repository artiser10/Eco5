package doctor.eco5.obj.guis;

import doctor.eco5.obj.Company;
import doctor.eco5.obj.CompanyMember;
import doctor.eco5.obj.CompanyRang;
import doctor.eco5.types.CompanyPermission;
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
import ru.komiss77.utils.inventory.SmartInventory;

import java.util.HashMap;
import java.util.List;

public class CompanyMember_main implements InventoryProvider {
    private final CompanyMember viewer; // смотрящий
    private final CompanyMember watched; // за кем смотрящий

    public CompanyMember_main(CompanyMember viewer, CompanyMember watched) {
        this.viewer  = viewer;
        this.watched = watched;
    }

    @Override
    public void init(Player player, InventoryContent inventoryContent) {
        CompanyMember viewer = this.viewer;
        CompanyMember watched = this.watched;
        Company company = Company.get(watched.company_uuid);

        if (!viewer.rang.perms.contains(CompanyPermission.MANAGE_MEMBERS) & viewer.rang.priority >= watched.rang.priority) {
            if (!viewer.rpUser.isStaff()) {
                viewer.rpUser.sendMessage("У вас нет права на редактирование этого сотрудника.");
                viewer.rpUser.getPlayer().closeInventory();
                return;
            }
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

        is = new ItemStack(Material.DIAMOND, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Будущая кнопка").color(TextColor.color(141, 255, 243)));
        im.lore(List.of(Component.text("§3Представьте, что тут что-то тут есть")));
        is.setItemMeta(im);
        inventoryContent.set(20, ClickableItem.of(is, inventoryClickEvent -> {
            viewer.rpUser.sendMessage("Ого! Тут тоже!");
        }));

        is = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Изменить Должность").color(TextColor.color(80, 189, 255)));
        im.lore(List.of(Component.text("§3Установить другую должность").color(TextColor.color(255, 244, 184))));
        is.setItemMeta(im);
        inventoryContent.set(22, ClickableItem.of(is, inventoryClickEvent -> {
            int c = -1;
            HashMap<Integer, ClickableItem> items = new HashMap<>();
            for (CompanyRang companyRang : company.companyRangs) {
                if (c > 52) {
                    break;
                }

                c = c + 1;
                if (Gui_Helper.blocked_lots.contains(c) | items.containsKey(c)) {
                    while (Gui_Helper.blocked_lots.contains(c)) {
                        c = c + 1;
                        if (c > 100) {
                            break;
                        }
                    }
                }
                Material mat = Material.AMETHYST_BLOCK;
                ItemStack iss = new ItemStack(mat, 1);
                ItemMeta imm = iss.getItemMeta();
                imm.displayName(Component.text(" - ").color(TextColor.color(255, 255, 255)).append(Component.text(companyRang.name).color(TextColor.color(50, 201, 255))));
                imm.lore(List.of(
                    Component.text("Приоритет: " ).color(TextColor.color(120, 130, 228)).append(Component.text(companyRang.priority + "").color(TextColor.color(255, 255, 255))),
                    Component.text("Полномочий: ").color(TextColor.color(255, 100, 228)).append(Component.text(companyRang.perms.size()).color(TextColor.color(255, 255, 255))),
                    Component.text("Зарплата: "  ).color(TextColor.color(255, 130, 120)).append(Component.text(companyRang.salary).color(TextColor.color(255, 255, 255))),

                    Component.text("Клик -> ").color(TextColor.color(255, 235, 182)).append(Component.text("Установить").color(TextColor.color(254, 255, 251)))
                ));
                iss.setItemMeta(imm);
                items.put(c, ClickableItem.of(iss, inventoryClickEvent1 -> {
                    if (viewer.rang.priority > companyRang.priority) {
                        watched.rang = companyRang;
                        reopen(player, inventoryContent);
                    } else {
                        viewer.rpUser.sendMessage("Ого! Тут тоже!");
                        viewer.rpUser.getPlayer().closeInventory();
                    }
                }));
            }
            ItemStack is1;
            ItemMeta im1;
            is1 = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
            im1 = is1.getItemMeta();
            im1.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
            im1.lore(List.of(Component.text("§3Пусто")));
            is1.setItemMeta(im1);
            inventoryContent.fillSquare(10, 43, ClickableItem.of(is1, inventoryClickEvent1 -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

            for (int i : items.keySet()) {
                inventoryContent.set(i, items.get(i));
            }

        }));

        is = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("Финансы").color(TextColor.color(90, 132, 255)));
        im.lore(List.of(
            Component.text("§3Долг компания -> сотрудник: ").color(TextColor.color(255, 120, 168)).append(Component.text("" + watched.company_must).color(TextColor.color(255, 255, 255))),
            Component.text("§3Зарплата: ").color(TextColor.color(216, 103, 255)).append(Component.text("" + watched.rang.salary).color(TextColor.color(255, 255, 255)))

        ));
        is.setItemMeta(im);
        inventoryContent.set(24, ClickableItem.of(is, inventoryClickEvent -> {

        }));

        is = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
        im = is.getItemMeta();
        im.displayName(Component.text("В меню").color(TextColor.color(255, 82, 68)));
        im.lore(List.of(
            Component.text("§3Пусто").color(TextColor.color(255, 255, 255)))
        );
        is.setItemMeta(im);
        inventoryContent.set(4, ClickableItem.of(is, inventoryClickEvent -> {
            viewer.rpUser.open_companies();
        }));

        if (viewer.rpUser.isStaff() || viewer.rang.priority > watched.rang.priority) {
            is = new ItemStack(Material.TNT, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("Уволить").color(TextColor.color(255, 10, 0)));
            im.lore(List.of(Component.text("§3Пусто")));
            is.setItemMeta(im);
            inventoryContent.set(49, ClickableItem.of(is, inventoryClickEvent -> {
                watched.dismiss();
                viewer.rpUser.sendMessage("Сотрудник успешно уволен");
                viewer.rpUser.getPlayer().closeInventory();
            }));
        }


    }
}