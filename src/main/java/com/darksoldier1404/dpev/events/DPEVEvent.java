package com.darksoldier1404.dpev.events;

import com.darksoldier1404.dpev.functions.DPEVFunction;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.NBT;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import static com.darksoldier1404.dpev.Evaluation.currentEdit;
import static com.darksoldier1404.dpev.Evaluation.plugin;

public class DPEVEvent implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getInventory().getHolder() == null) return;
        if (e.getInventory().getHolder() instanceof DInventory) {
            DInventory inv = (DInventory) e.getInventory().getHolder();
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
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null) {
            return;
        }
        if (e.getClickedInventory().getHolder() instanceof DInventory) {
            DInventory inv = (DInventory) e.getClickedInventory().getHolder();
            Player p = (Player) e.getWhoClicked();
            if (inv.isValidHandler(plugin)) {
                ItemStack item = e.getCurrentItem();
                if (item == null || item.getType().isAir()) {
                    return;
                }
                if (inv.isValidChannel(101)) {
                    if (NBT.hasTagKey(item, "dpevs_confirm")) {
                        e.setCancelled(true);
                        inv.applyChanges();
                        DPEVFunction.evaluateItem(p, inv.getItem(13));
                        return;
                    }
                }
                if (inv.isValidChannel(102)) { // sell
                    if (NBT.hasTagKey(item, "dpevs_sell")) {
                        e.setCancelled(true);
                        inv.applyChanges();
                        DPEVFunction.sellItems(p, inv);
                        return;
                    }
                }
                if (NBT.hasTagKey(item, "dppc_prevpage")) {
                    inv.applyChanges();
                    inv.prevPage();
                    e.setCancelled(true);
                    return;
                }
                if (NBT.hasTagKey(item, "dppc_nextpage")) {
                    inv.applyChanges();
                    inv.nextPage();
                    e.setCancelled(true);
                    return;
                }
                if (NBT.hasTagKey(item, "dppc_clickcancel") || NBT.hasTagKey(item, "dpvs_barrier")) {
                    e.setCancelled(true);
                    return;
                }
                if (inv.isValidChannel(2)) { // price edit with chat
                    p.closeInventory();
                    plugin.currentEdit.put(p.getUniqueId(), Tuple.of(e.getSlot(), inv));
                    p.sendMessage("채팅으로 가격을 설정 해주세요. : MIN-MAX");
                }
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
