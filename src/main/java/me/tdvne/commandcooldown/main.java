package me.tdvne.commandcooldown;

import me.tdvne.commandcooldown.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;
    FileConfiguration config;
    Main plugin = this;

    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        Bukkit.getServer().getConsoleSender().sendMessage("§a[CommandCooldown] Registering Listeners...");
        this.config = getConfig();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getConsoleSender().sendMessage("§aCommandCooldown has successfully registered listeners & loaded.");
    }

    public void onDisable() {
        saveConfig();
        Bukkit.getServer().getConsoleSender().sendMessage("§aCommandCooldown has successfully unloaded & disabled.");
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
            e.getPlayer().sendMessage(CC.translate(" &c&l┃ &fVersion: &cv" + plugin.getDescription().getVersion()));
            e.getPlayer().sendMessage(CC.translate("&4&m--*----------------*--"));
            e.setCancelled(true);
        }
    }
}
