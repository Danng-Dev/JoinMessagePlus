package org.cloudns.danng.plugins;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class joinmessageplus extends JavaPlugin implements Listener {

    Player player ;

    FileConfiguration config = this.getConfig();

    Logger log = this.getLogger();

    boolean errorConfig = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        log.info("Join Message plus enabling");
        log.info("Loading Config");
        try{
            this.saveDefaultConfig();
        }catch(java.lang.IllegalArgumentException e){
            log.severe("Caught illegal argument exception while enabling config");
            log.severe("" + e);
            log.severe("Shutting down plugin: broken config");
            errorConfig = true;
            getServer().getPluginManager().disablePlugin(this);
        }
        config.addDefault("messages.join", "&f(&2&l+&f)");
        config.addDefault("messages.leave", true);
        config.addDefault("main.sendmessagetoplayer", true);
        config.addDefault("main.messageplayer.message", true);
        config.addDefault("main.messageplayer.leave", true);
        config.addDefault("main.messageplayer.leave.message", true);
        config.options().copyDefaults(true);
        saveConfig();
        log.info("Loaded config");
        log.info("Loading listener");
        getServer().getPluginManager().registerEvents(this, this);
        log.info("Registered Listener");
        log.info("Loading commands");
        try{
            getCommand("JMP").setExecutor(new Commands());
        }catch (java.lang.NullPointerException e){
            log.severe("Caught NullPointerException");
            log.severe("" + e);
            log.severe("*****Do not report this, it i in progress*****");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info("Disabling");
        if(errorConfig){
            log.warning("This plugin is shutting down due to an error with the config");
        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        event.setJoinMessage(config.getString("messages.join"));
        if (config.getBoolean("main.sendtoplayer")){
            if (config.getString("main.sendmessagetoplayer.message") != null){
                player.sendMessage(config.getString("main.sendtoplayer.message"));
            }else{
                player.performCommand("");
            }

        }
    }
    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event){

        Player player = event.getPlayer();

        event.setQuitMessage(config.getString("messages.leave"));
        if (config.getBoolean("main.sendmessagetoplayer.leave")){
            if (config.getString("main.sendmessagetoplayer.leave.message") != null){
                player.sendMessage("main.sendmessagetoplayer.leave.message");
            }else{
                log.warning("main.sendmessagetoplayer.leave.message in config is null, please set it in the config for it to work");
            }
        }

    }
}
