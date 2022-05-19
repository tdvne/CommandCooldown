package me.tdvne.commandcooldown;

import me.tdvne.commandcooldown.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
    FileConfiguration config;
    private static main instance;

    public static main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.config = getConfig();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, (Plugin) this);
        System.out.println("[CommandCooldown] This plugin has enabled successfully");
    }

    public void onDisable() {
        saveConfig();
        System.out.println("[CommandCooldown] This plugin has disabled successfully");
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent cooldown) {
        String command = cooldown.getMessage().split(" ", 2)[0].substring(1).toLowerCase();
        Player p = cooldown.getPlayer();
        if (!getConfig().isInt("cooldowns." + command))
            return;
        if (this.config.getLong("players." + p.getUniqueId().toString() + "." + command) + (getConfig().getInt("cooldowns." + command) * 1000L) > System.currentTimeMillis()) {
            cooldown.setCancelled(true);
            String time = ((this.config.getLong("players." + p.getUniqueId().toString() + "." + command) + (getConfig().getInt("cooldowns." + command) * 1000L) - System.currentTimeMillis()) / 1000L) + "";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Message").replace("<time>", time)));
            return;
        }
        this.config.set("players." + p.getUniqueId().toString() + "." + command, System.currentTimeMillis());
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().trim().equalsIgnoreCase("/commandcooldown")) {
            e.getPlayer().sendMessage(CC.translate("&4&m--*----------------*--"));
            e.getPlayer().sendMessage(CC.translate("&4&lCommand Cooldown"));
            e.getPlayer().sendMessage(CC.translate(" &c&l┃ &fAuthor: &ctdvne"));
            e.getPlayer().sendMessage(CC.translate(" &c&l┃ &fDiscord: &ctdvne#0001"));
            e.getPlayer().sendMessage(CC.translate(" &c&l┃ &fVersion: &cv1.0"));
            e.getPlayer().sendMessage(CC.translate("&4&m--*----------------*--"));
            e.setCancelled(true);
        }
    }
}
