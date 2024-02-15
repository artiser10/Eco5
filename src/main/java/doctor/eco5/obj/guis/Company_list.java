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
import ru.komiss77.utils.PlayerInput;
import ru.komiss77.utils.inventory.*;

import java.util.HashMap;
import java.util.List;

public class Company_list implements InventoryProvider {
    private final boolean all;
    private final RPUser rpUser;

    public Company_list(boolean all, RPUser rpUser) {
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
        if (rpUser.isStaff()) {
            is = new ItemStack(Material.END_CRYSTAL, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("Создать!").color(TextColor.color(255, 226, 86)));
            im.lore(List.of(Component.text("§3Жми для создания компании!")));
            is.setItemMeta(im);

            inventoryContent.set(49, new InputButton(InputButton.InputType.ANVILL, is, "имя_компании", data ->{
                Company company = Company.create(data);
                company.hire(rpUser, company.defaultRang());
                reopen(player, inventoryContent);
            }));

        }

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

            is = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("В меню").color(TextColor.color(255, 82, 68)));
            im.lore(List.of(
                    Component.text("§3Пусто").color(TextColor.color(255, 255, 255)))
            );
            is.setItemMeta(im);
            inventoryContent.set(4, ClickableItem.of(is, inventoryClickEvent -> {
                rpUser.open_companies();
            }));

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
                    Component.text("Создана: ").color(TextColor.color(120, 230, 120)).append(Component.text(company.timestamp_from).color(TextColor.color(255, 255, 255))),
                    Component.text("Баланс: ").color(TextColor.color(230, 226, 73)).append(Component.text(company.balance).color(TextColor.color(255, 255, 255))),

                    Component.text("ЛКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("Открыть меню компании.").color(TextColor.color(254, 255, 251)))
            ));
            is.setItemMeta(im);
            companies.put(c, ClickableItem.of(is, inventoryClickEvent -> {
                SmartInventory.builder().size(6).title("§4Компания " + company.public_name).provider(new Company_main(company, company.getStaff(rpUser), rpUser.isStaff())).build().open(rpUser.getPlayer());
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }));

        }

        for (int i : companies.keySet()) {
            inventoryContent.set(i, companies.get(i));
        }
    }
}