package com.darksoldier1404.dpev;

import com.darksoldier1404.dpev.commands.DPEVCommand;
import com.darksoldier1404.dpev.events.DPEVEvent;
import com.darksoldier1404.dpev.functions.DPEVFunction;
import com.darksoldier1404.dppc.annotation.DPPCoreVersion;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.utils.PluginUtil;
import com.darksoldier1404.dppc.utils.Tuple;

import java.util.*;

@DPPCoreVersion(since = "5.3.0")
public class Evaluation extends DPlugin {
    public static Evaluation plugin;
    public static DInventory items;
    public static final Map<UUID, Tuple<Integer, DInventory>> currentEdit = new HashMap<>();

    public Evaluation() {
        super(false);
        plugin = this;
        init();
    }

    public static Evaluation getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        PluginUtil.addPlugin(plugin, 27936);
        items = new DInventory("감정 아이템 설정", 54, true, true, plugin).deserialize(plugin.config);
        getCommand("dpev").setExecutor(new DPEVCommand().getBuilder());
        getServer().getPluginManager().registerEvents(new DPEVEvent(), plugin);
    }

    @Override
    public void onDisable() {
        DPEVFunction.saveItems(items);
    }
}
