package com.darksoldier1404.dpev.events;

import com.darksoldier1404.dpev.functions.DPEVFunction;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.events.dinventory.DInventoryClickEvent;
import com.darksoldier1404.dppc.events.dinventory.DInventoryCloseEvent;
import com.darksoldier1404.dppc.utils.NBT;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.darksoldier1404.dpev.Evaluation.currentEdit;
import static com.darksoldier1404.dpev.Evaluation.plugin;

public class DPEVEvent implements Listener {

    @EventHandler
    public void onInventoryClose(DInventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        DInventory inv = e.getDInventory();
        if (inv.isValidHandler(plugin)) {
            if (inv.isValidChannel(1)) { // item edit save
                DPEVFunction.saveItems(inv);
                p.sendMessage("§a아이템 설정이 저장되었습니다.");
                return;
            }
            if (inv.isValidChannel(101)) {
                inv.applyChanges();
                ItemStack item = inv.getItem(13);
                if (item != null && !item.getType().isAir()) {
                    p.getInventory().addItem(item.clone());
                }
                return;
            }
            if (inv.isValidChannel(102)) { // sell inventory close
                inv.applyChanges();
                DPEVFunction.returnItems(p, inv);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(DInventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory && e.getInventory() instanceof DInventory) {
            DInventory inv = e.getDInventory();
            if (inv.isValidHandler(plugin) && inv.isValidChannel(101)) {
                e.setCancelled(true);
                if (inv.getItem(13) != null) {
                    return;
                }
                ItemStack item = e.getCurrentItem();
                if (item == null || item.getType().isAir()) {
                    return;
                }
                ItemStack clone = item.clone();
                item.setAmount(item.getAmount() - 1);
                e.setCurrentItem(item);
                clone.setAmount(1);
                inv.setItem(13, clone);
                inv.applyChanges();
                return;
            }
        }
        DInventory inv = e.getDInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv.isValidHandler(plugin)) {
            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType().isAir()) {
                return;
            }
            if (inv.isValidChannel(101)) {
                if (e.getSlot() == 13) {
                    e.setCancelled(true);
                    p.getInventory().addItem(item.clone());
                    inv.setItem(13, null);
                    inv.applyChanges();
                    return;
                }
                if (NBT.hasTagKey(item, "dpevs_confirm")) {
                    e.setCancelled(true);
                    DPEVFunction.evaluateItem(p, inv.getItem(13));
                    inv.applyChanges();
                    return;
                }
            }
            if (inv.isValidChannel(102)) { // sell
                if (NBT.hasTagKey(item, "dpevs_sell")) {
                    e.setCancelled(true);
                    DPEVFunction.sellItems(p, inv);
                    inv.applyChanges();
                    return;
                }
            }
            if (inv.isValidChannel(2)) { // price edit with chat
                p.closeInventory();
                plugin.currentEdit.put(p.getUniqueId(), Tuple.of(e.getSlot(), inv));
                p.sendMessage("채팅으로 가격을 설정 해주세요. : MIN-MAX");
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (currentEdit.containsKey(p.getUniqueId())) {
            int slot = currentEdit.get(p.getUniqueId()).getA();
            DInventory inv = currentEdit.get(p.getUniqueId()).getB();
            if (inv != null && inv.isValidHandler(plugin) && inv.getChannel() == 2) {
                e.setCancelled(true);
                String message = e.getMessage();
                if (message.matches("\\d+-\\d+")) {
                    DPEVFunction.setPrice(p, inv, slot, message);
                }
                currentEdit.remove(p.getUniqueId());
            }
        }
    }
}
