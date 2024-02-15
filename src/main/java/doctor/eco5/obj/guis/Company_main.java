package doctor.eco5.obj.guis;

import doctor.eco5.obj.Company;
import doctor.eco5.obj.CompanyMember;
import doctor.eco5.obj.CompanyRang;
import doctor.eco5.obj.rp.RPUser;
import doctor.eco5.types.CompanyPermission;
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
import java.util.UUID;

public class Company_main implements InventoryProvider {
    private final Company company;
    private final CompanyMember companyMember;
    private final Boolean bypass;

    public Company_main(Company company, CompanyMember companyMember, Boolean bypass) {
        this.company = company;
        this.companyMember = companyMember;
        this.bypass = bypass;
    }
    @Override
    public void init(Player player, InventoryContent inventoryContent) {
        Company company = this.company;
        CompanyMember companyMember = this.companyMember;
        Boolean bypass = this.bypass;
        RPUser rpUser;
        if (companyMember == null) {
            rpUser = RPUser.get(player);
        } else {
            rpUser = companyMember.rpUser;
        }
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

        if (companyMember != null) {
            if (companyMember.company_uuid.equals(company.uuid)) {
                is = new ItemStack(Material.TNT, 1);
                im = is.getItemMeta();
                im.displayName(Component.text("Уволиться").color(TextColor.color(255, 10, 0)));
                im.lore(List.of(Component.text("§3Пусто")));
                is.setItemMeta(im);
                inventoryContent.set(53, ClickableItem.of(is, inventoryClickEvent -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
                    company.dismiss(rpUser);
                    rpUser.open_companies();
                }));

            }
        }

        if (rpUser.isStaff()) {
            if (!rpUser.isStaff(company)) {
                is = new ItemStack(Material.END_CRYSTAL, 1);
                im = is.getItemMeta();
                im.displayName(Component.text("Вступить!").color(TextColor.color(255, 226, 86)));
                im.lore(List.of(Component.text("§3Жми для вступления в компанию!")));
                is.setItemMeta(im);
                inventoryContent.set(49, ClickableItem.of(is, inventoryClickEvent -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1);
                    company.hire(rpUser);
                    reopen(player, inventoryContent);
                }));
            }
        }

        if (rpUser.isStaff() || company.maxRang() == companyMember.rang) {
            is = new ItemStack(Material.TNT, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("Удалить!").color(TextColor.color(255, 226, 86)));
            im.lore(List.of(Component.text("§3Удалить компанию!")));
            is.setItemMeta(im);
            inventoryContent.set(45, ClickableItem.of(is, inventoryClickEvent -> {
                company.remove();
                rpUser.open_companies();
            }));
        }
        HashMap<Integer, ClickableItem> items = new HashMap<>();
        if (bypass || companyMember.hasPermission(CompanyPermission.VIEW_MEMBERS)) {
            is = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
            im.lore(List.of(Component.text("§3Пусто")));
            is.setItemMeta(im);
            inventoryContent.fillRect(10, 43, ClickableItem.of(is, inventoryClickEvent -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

            is = new ItemStack(Material.PLAYER_HEAD, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("Участники").color(TextColor.color(255, 120, 0)));
            im.lore(List.of(Component.text("Их " + company.members.size()).color(TextColor.color(120, 120, 120))));
            is.setItemMeta(im);
            inventoryContent.set(10, ClickableItem.of(is, inventoryClickEvent -> {
                ItemStack is1;
                ItemMeta im1;
                is1 = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
                im1 = is1.getItemMeta();
                im1.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
                im1.lore(List.of(Component.text("§3Пусто")));
                is1.setItemMeta(im1);
                inventoryContent.fillRect(10, 43, ClickableItem.of(is1, inventoryClickEvent1 -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));
                int c = -1;

                is1 = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
                im1 = is1.getItemMeta();
                im1.displayName(Component.text("Назад").color(TextColor.color(255, 82, 68)));
                im1.lore(List.of(
                        Component.text("§3Пусто").color(TextColor.color(255, 255, 255)))
                );
                is1.setItemMeta(im1);
                inventoryContent.set(4, ClickableItem.of(is1, inventoryClickEvent2 -> {
                    rpUser.open_companies();
                }));

                for (CompanyMember companyMember1 : company.members) {
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
                    imm.displayName(Component.text(" - ").color(TextColor.color(255, 255, 255)).append(Component.text(companyMember1.rpUser.nickname).color(TextColor.color(50, 201, 255))));
                    imm.lore(List.of(
                            Component.text("Должность: " ).color(TextColor.color(120, 130, 228)).append(Component.text("" + companyMember1.rang.name + " [" + companyMember1.rang.priority + "]").color(TextColor.color(255, 255, 255))),
                            Component.text("Полномочий: ").color(TextColor.color(255, 100, 228)).append(Component.text(companyMember1.rang.perms.size()).color(TextColor.color(255, 255, 255))),
                            Component.text("Зарплата: "  ).color(TextColor.color(255, 130, 120)).append(Component.text(companyMember1.rang.salary).color(TextColor.color(255, 255, 255))),
                            Component.text("Работает с: ").color(TextColor.color(120, 230, 120)).append(Component.text(companyMember1.timestamp_from).color(TextColor.color(255, 255, 255))),

                            Component.text("ЛКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("Открыть меню редактирования.").color(TextColor.color(254, 255, 251)))
                    ));
                    iss.setItemMeta(imm);
                    items.put(c, ClickableItem.of(iss, inventoryClickEvent1 -> {
                        if (inventoryClickEvent1.isLeftClick()) {
                            player.sendMessage(Component.text("Клик на сотрудника компании."));
                            SmartInventory.builder().size(6).title("§4Сотрудник - " + companyMember.rpUser.nickname).provider(new CompanyMember_main(companyMember, companyMember1)).build().open(rpUser.getPlayer());
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        }
                    }));

                }
                for (int i : items.keySet()) {
                    inventoryContent.set(i, items.get(i));
                }
            }));
        }
        if (bypass || companyMember.hasPermission(CompanyPermission.VIEW_MEMBERS)) {

            is = new ItemStack(Material.PLAYER_HEAD, 1);
            im = is.getItemMeta();
            im.displayName(Component.text("Должности").color(TextColor.color(255, 249, 0)));
            im.lore(List.of(Component.text("Их " + company.companyRangs.size()).color(TextColor.color(120, 120, 120))));
            is.setItemMeta(im);
            inventoryContent.set(11, ClickableItem.of(is, inventoryClickEvent -> {
                ItemStack is1;
                ItemMeta im1;
                is1 = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
                im1 = is1.getItemMeta();
                im1.displayName(Component.text("#").color(TextColor.color(60, 60, 60)));
                im1.lore(List.of(Component.text("§3Пусто")));
                is1.setItemMeta(im1);
                inventoryContent.fillRect(10, 43, ClickableItem.of(is1, inventoryClickEvent1 -> player.playSound(player.getLocation(), Sound.BLOCK_SNIFFER_EGG_PLOP, 1, 1)));

                int c = -1;
                ItemStack iss;
                ItemMeta imm;


                if (rpUser.isStaff() || (companyMember.rang.priority == company.maxRang().priority)) {
                    iss = new ItemStack(Material.END_CRYSTAL, 1);
                    imm = iss.getItemMeta();
                    imm.displayName(Component.text("Создать должность").color(TextColor.color(255, 10, 0)));
                    imm.lore(List.of(Component.text("§3Нажмите чтобы создать должность.")));
                    iss.setItemMeta(imm);
                    inventoryContent.set(49, ClickableItem.of(iss, inventoryClickEvent2 -> {
                        company.createRang(UUID.randomUUID().toString().substring(0, 4), 0, 0);
                        reopen(player, inventoryContent);
                    }));
                }

                iss = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, 1);
                imm = iss.getItemMeta();
                imm.displayName(Component.text("Назад").color(TextColor.color(255, 82, 68)));
                imm.lore(List.of(
                        Component.text("§3Пусто").color(TextColor.color(255, 255, 255)))
                );
                iss.setItemMeta(imm);
                inventoryContent.set(4, ClickableItem.of(iss, inventoryClickEvent2 -> {
                    rpUser.open_companies();
                }));

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
                    iss = new ItemStack(mat, 1);
                    imm = iss.getItemMeta();
                    imm.displayName(Component.text(" - ").color(TextColor.color(255, 255, 255)).append(Component.text(companyRang.name).color(TextColor.color(50, 201, 255))));
                    imm.lore(List.of(
                            Component.text("Ставка: " ).color(TextColor.color(120, 130, 228)).append(Component.text("" + companyRang.salary).color(TextColor.color(255, 255, 255))),
                            Component.text("Полномочий: ").color(TextColor.color(255, 100, 228)).append(Component.text(companyRang.perms.size()).color(TextColor.color(255, 255, 255))),
                            Component.text("Приоритет: "  ).color(TextColor.color(255, 130, 120)).append(Component.text(companyRang.priority).color(TextColor.color(255, 255, 255))),
                            Component.text("Кодировка прав: ").color(TextColor.color(120, 230, 120)).append(Component.text(CompanyPermission.codePerms(companyRang.perms)).color(TextColor.color(255, 255, 255))),

                            Component.text("ЛКМ -> ").color(TextColor.color(255, 235, 182)).append(Component.text("Открыть меню редактирования.").color(TextColor.color(254, 255, 251)))
                    ));
                    iss.setItemMeta(imm);
                    items.put(c, ClickableItem.of(iss, inventoryClickEvent1 -> {
                        if (inventoryClickEvent1.isLeftClick()) {
                            player.sendMessage(Component.text("Клик на должность"));
                            SmartInventory.builder().size(6).title("§4Должность - " + companyRang.name).provider(new CompanyRang_main(company, rpUser, companyRang)).build().open(rpUser.getPlayer());
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        }
                    }));

                }
                for (int i : items.keySet()) {
                    inventoryContent.set(i, items.get(i));
                }
            }));
        }
    }
}