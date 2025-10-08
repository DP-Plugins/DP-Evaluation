package com.darksoldier1404.dpev.functions;

import com.darksoldier1404.dppc.api.essentials.MoneyAPI;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.darksoldier1404.dpev.Evaluation.plugin;

public class DPEVFunction {

    public static void editItems(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c플레이어만 사용 가능한 명령어입니다.");
            return;
        }
        Player p = (Player) sender;
        plugin.items.setChannel(1);
        plugin.items.openInventory(p);
    }

    public static void editPrice(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c플레이어만 사용 가능한 명령어입니다.");
            return;
        }
        Player p = (Player) sender;
        plugin.items.setChannel(2);
        plugin.items.openInventory(p);
    }

    public static void setPrice(Player p, DInventory inv, int slot, String price) {
        int minPrice = Integer.parseInt(price.split("-")[0]);
        int maxPrice = Integer.parseInt(price.split("-")[1]);
        int page = inv.getCurrentPage();
        plugin.config.set("Prices." + page + "." + slot + ".MIN", minPrice);
        plugin.config.set("Prices." + page + "." + slot + ".MAX", maxPrice);
        saveItems(inv);
        Bukkit.getScheduler().runTask(plugin, () -> inv.openInventory(p));
    }

    public static void saveItems(DInventory inv) {
        inv.applyChanges();
        plugin.items = inv;
        ConfigUtils.savePluginConfig(plugin, plugin.items.serialize(plugin.config));
    }

    public static void openEvaluationGUI(Player p) {
        DInventory inv = new DInventory("감정", 27, plugin);
        inv.setChannel(101);
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pane.setItemMeta(meta);
        pane = NBT.setStringTag(pane, "dppc_clickcancel", "true");
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, pane);
        }
        ItemStack confirm = new ItemStack(Material.ANVIL);
        ItemMeta im = confirm.getItemMeta();
        im.setDisplayName("§a감정하기");
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        confirm.setItemMeta(im);
        confirm = NBT.setStringTag(confirm, "dpevs_confirm", "true");
        confirm = NBT.setStringTag(confirm, "dppc_clickcancel", "true");
        inv.setItem(13, null);
        inv.setItem(22, confirm);
        inv.openInventory(p);
    }

    public static void evaluateItem(Player p, ItemStack item) {
        if (item == null || item.getType().isAir()) {
            p.sendMessage("§c감정할 아이템을 빈칸에 넣어주세요.");
            return;
        }
        if (NBT.hasTagKey(item, "dpevs_price")) {
            p.sendMessage("§c이미 감정된 아이템입니다.");
            return;
        }
        int price = getRandomPrice(item);
        if (price <= 0) {
            p.sendMessage("§c해당 아이템은 감정할 수 없습니다.");
        } else {
            p.getInventory().removeItem(item);
            p.sendMessage("§a아이템 감정이 완료되었습니다. 가격: §e" + price + "원");
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore.add("§e감정가 : §e" + price + "원");
            meta.setLore(lore);
            item.setItemMeta(meta);
            NBT.setIntTag(item, "dpevs_price", price);
        }
    }

    public static int getRandomPrice(ItemStack item) {
        DInventory.PageItemSet pi = findIteminInventory(item);
        if (pi == null) return 0;
        int page = pi.getPage();
        int slot = pi.getSlot();
        int minPrice = plugin.config.getInt("Prices." + page + "." + slot + ".MIN", 0);
        int maxPrice = plugin.config.getInt("Prices." + page + "." + slot + ".MAX", 0);
        return (int) ((Math.random() * (maxPrice - minPrice + 1)) + minPrice);
    }

    public static DInventory.PageItemSet findIteminInventory(ItemStack item) {
        DInventory inv = plugin.items;
        AtomicReference<DInventory.PageItemSet> found = new AtomicReference<>(new DInventory.PageItemSet(0, 0, null));
        inv.applyAllItemChanges((pi) -> {
            if (pi.getItem().isSimilar(item)) {
                found.set(pi);
            }
        });
        return found.get() != null ? found.get() : null;
    }

    public static void openSellGUI(Player p) {
        DInventory inv = new DInventory("아이템 판매", 54, plugin);
        inv.setChannel(102);
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pane.setItemMeta(meta);
        pane = NBT.setStringTag(pane, "dppc_clickcancel", "true");
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, pane);
        }
        ItemStack confirm = new ItemStack(Material.EMERALD);
        ItemMeta im = confirm.getItemMeta();
        im.setDisplayName("§a판매하기");
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        confirm.setItemMeta(im);
        confirm = NBT.setStringTag(confirm, "dpevs_sell", "true");
        confirm = NBT.setStringTag(confirm, "dppc_clickcancel", "true");
        inv.setItem(49, confirm);
        inv.openInventory(p);
    }

    public static void sellItems(Player p, DInventory inv) {
        List<ItemStack> toSell = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item != null && !item.getType().isAir() && NBT.hasTagKey(item, "dpevs_price")) {
                toSell.add(item);
            }
        }
        if (toSell.isEmpty()) {
            p.sendMessage("§c판매할 아이템이 없습니다.");
            return;
        }
        int totalPrice = 0;
        for (ItemStack item : toSell) {
            totalPrice += NBT.getIntegerTag(item, "dpevs_price");
        }
        p.getInventory().removeItem(toSell.toArray(new ItemStack[0]));
        MoneyAPI.addMoney(p, totalPrice);
        toSell.forEach(i -> i.setAmount(0));
        inv.applyChanges();
        p.sendMessage("§a아이템 판매가 완료되었습니다. 총 판매 금액: §e" + totalPrice + "원");
    }

    public static void returnItems(Player p, DInventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null && !item.getType().isAir()) {
                if (NBT.hasTagKey(item, "dppc_clickcancel")) continue;
                p.getInventory().addItem(item);
            }
        }
    }
}
