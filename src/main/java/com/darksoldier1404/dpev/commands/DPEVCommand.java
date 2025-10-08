package com.darksoldier1404.dpev.commands;

import com.darksoldier1404.dpev.functions.DPEVFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.entity.Player;

import static com.darksoldier1404.dpev.Evaluation.plugin;

public class DPEVCommand {
    private final CommandBuilder builder;

    public DPEVCommand() {
        builder = new CommandBuilder(plugin);

        builder.addSubCommand("reload", "dpev.admin", "/dpev reload", false, (p, args) -> {
            if (args.length == 1) {
                plugin.reload();
                p.sendMessage(plugin.getPrefix() + "§a플러그인 설정이 리로드되었습니다.");
                return true;
            }
            return false;
        });

        builder.addSubCommand("items", "dpev.admin", "/dpev items", false, (p, args) -> {
            if (args.length == 1) {
                DPEVFunction.editItems(p);
                return true;
            }
            return false;
        });

        // price
        builder.addSubCommand("price", "dpev.admin", "/dpev price", true, (p, args) -> {
            if (args.length == 1) {
                DPEVFunction.editPrice(p);
                return true;
            }
            return false;
        });

        // open evaluation GUI
        builder.addSubCommand("evaluate", "/dpev evaluate", true, (p, args) -> {
            if (args.length == 1) {
                DPEVFunction.openEvaluationGUI((Player) p);
                return true;
            }
            return false;
        });

        // open sell
        builder.addSubCommand("sell", "/dpev sell", true, (p, args) -> {
            if (args.length == 1) {
                DPEVFunction.openSellGUI((Player) p);
                return true;
            }
            return false;
        });
    }

    public CommandBuilder getBuilder() {
        return builder;
    }
}
