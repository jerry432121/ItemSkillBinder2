package com.example.itemskillbinder;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    private Map<String, Object> skillBindings;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        skillBindings = getConfig().getConfigurationSection("items").getValues(false);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ItemSkillBinder 插件已啟動！");
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemSkillBinder 插件已卸載！");
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String displayName = ChatColor.stripColor(meta.getDisplayName());
        if (skillBindings.containsKey(displayName)) {
            String skillName = skillBindings.get(displayName).toString();
            MythicBukkit.inst().getAPIHelper().castSkill(player, skillName);
            player.sendMessage(ChatColor.GREEN + "已觸發技能：" + skillName);
        }
    }
}
