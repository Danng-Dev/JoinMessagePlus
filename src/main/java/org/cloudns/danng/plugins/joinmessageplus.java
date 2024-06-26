package org.cloudns.danng.plugins;

import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bstats.bukkit.Metrics;

public final class joinmessageplus extends JavaPlugin implements Listener {

    Player player ;

    FileConfiguration config = this.getConfig();

    Logger log = this.getLogger();

    boolean errorConfig = false;

    boolean papienabled = false;

    @Override
    public void onEnable() throws NullPointerException {
        // Plugin startup logic
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            log.info("You have Placeholder api enabled, you can now use placeholders");
            papienabled = true;
        }else{
            log.warning("You do not have placeholder api enabled, the plugin will still work, but you cannot use " +
                    "placeholders");
        }
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
        config.addDefault("messages.join", "&f(&2&l+&r&f)&6%player% &fjoined");
        config.addDefault("messages.leave", "&f(&4&l-&r&f)&6%player% &fLeft");
        config.addDefault("main.sendmessagetoplayer", false);
        config.addDefault("main.messageplayer.message", "&6 welcome to the server");
        config.addDefault("main.messageplayerleave", false);
        config.addDefault("main.messageplayer.leave", "&6You left the server");
        config.options().copyDefaults(true);
        saveConfig();
        log.info("Loaded config");
        log.info("Loading listener");
        getServer().getPluginManager().registerEvents(this, this);
        log.info("Registered Listener");
        log.info("Loading bstats");
        int ID = 22405;
        Metrics metrics = new Metrics(this, ID);
        log.info("Loaded bstats");
        log.info("Loading commands");
        try{
            if (getCommand("JMP") != null){
                getCommand("JMP").setExecutor(new Commands());
            }
        }catch (java.lang.NullPointerException e){
            log.severe("Caught NullPointerException");
            log.severe("" + e);
            log.severe("*****Do not report this, it i in progress*****");
        }

    }

    @Override
    public void onDisable() throws NullPointerException{
        // Plugin shutdown logic
        log.info("Disabling");
        if(errorConfig){
            log.warning("This plugin is shutting down due to an error with the config");
        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){
        replacer rep = new replacer();
        Player player = event.getPlayer();
        if (papienabled){
            String message;

            message = ChatColor.translateAlternateColorCodes('&', config.getString("messages.join"));

            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
            event.setJoinMessage(message);
        }else{
            CC cc = new CC();
            String msgjoin = cc.colour(config.getString("messages.join"));
            event.setJoinMessage(rep.replace(msgjoin, "%player%", player.getName()));
            if (config.getBoolean("main.sendtoplayer")){
                if (papienabled){
                    String thingy = cc.colour(config.getString("messages.join"));

                    thingy = PlaceholderAPI.setPlaceholders(event.getPlayer(), thingy);
                    player.sendMessage(thingy);
                }else{
                   String mes = rep.replace(cc.colour(config.getString("messages.join")), "%player%", player.getName());


                   player.sendMessage(mes);
                }
            }
        }
    }
    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event){

        player = event.getPlayer();

        CC cc = new CC();

        replacer rep = new replacer();
        if (papienabled){
            String bloob = cc.colour(config.getString("messages.leave"));

            bloob = PlaceholderAPI.setPlaceholders(event.getPlayer(), bloob);
            event.setQuitMessage(bloob);
        }else{
            String QM = cc.colour(rep.replace(config.getString("messages.leave"), "%player%", player.getName())
);
            event.setQuitMessage(QM);
            if (config.getBoolean("main.messageplayerleave")){
                String oramge = cc.colour(rep.replace(config.getString("main.messageplayer.leave"), "%player%",
                            player.getName()));

                    player.sendMessage(oramge);
                }
            }
        }

    }

