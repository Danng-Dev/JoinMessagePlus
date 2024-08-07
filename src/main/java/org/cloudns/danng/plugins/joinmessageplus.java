package org.cloudns.danng.plugins;

import me.clip.placeholderapi.libs.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.logging.Logger;

import me.clip.placeholderapi.PlaceholderAPI;



import org.bstats.bukkit.Metrics;

import org.cloudns.danng.plugins.Lib.Tools.utils;
import org.cloudns.danng.plugins.Lib.Bob;
import org.cloudns.danng.plugins.Lib.Tools.*;

public final class joinmessageplus extends JavaPlugin implements Listener, CommandExecutor{

    Player player ;
    Configutils cf = new Configutils("config");

    FileConfiguration config = cf.getConfig();

    Logger log = this.getLogger();

    boolean errorConfig = false;

    boolean papienabled = false;

    utils ut = new utils();

    @Override
    public void onEnable() throws NullPointerException {
        // Plugin startup logic

        log.info("Join Message plus enabling");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            log.info("You have Placeholder api enabled, you can now use placeholders");
            papienabled = true;
        }else{
            log.warning("You do not have placeholder api enabled, the plugin will still work, but you cannot use " +
                    "placeholders");
        }
        log.info("Loading BobLib ver " + Bob.GetVer());
        Bob.setPlugin(this);
        ut.log("This is a BobLib Utils Test Message");
        ut.PluginAd();
        log.info("Loading Config");
        cf.getConfig();
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
                getCommand("JMP").setExecutor(this);
            }
        }catch (java.lang.NullPointerException e){
            log.severe("Caught NullPointerException");
            log.severe("" + e);
            log.severe("*****Do not report this, it is in progress*****");
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
    public void PlayerJoin(PlayerJoinEvent event) {
        CC cci = new CC();
        MiniMessage mm = MiniMessage.miniMessage();
        replacer rep = new replacer();
        Player player = event.getPlayer();
        if (player.hasPermission("JMP.silent")) {



            String before = "Hey, %player% you have silentjoin permission";

            String mssg = rep.replace(cci.colour(before) , "%player%", player.getName());

            Component msgg = mm.deserialize(mssg);

            event.setJoinMessage(null);
            player.sendMessage(before);
        } else {
            if (papienabled) {
                String message;

                CC cc = new CC();

                message = rep.replace(cc.colour(config.getString("messages.join")), "%player%", "%player_name%");

                message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
                event.setJoinMessage(message);
            } else {
                CC cc = new CC();
                String msgjoin = cc.colour(config.getString("messages.join"));
                event.setJoinMessage(rep.replace(msgjoin, "%player%", player.getName()));
                if (config.getBoolean("main.sendtoplayer")) {
                    if (papienabled) {
                        String thingy = cc.colour(config.getString("messages.join"));

                        thingy = PlaceholderAPI.setPlaceholders(event.getPlayer(), thingy);
                        player.sendMessage(thingy);
                    } else {
                        String mes = rep.replace(cc.colour(config.getString("messages.join")), "%player%", player.getName());


                        player.sendMessage(mes);
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event) throws IOException  {
        replacer rep = new replacer();
        player = event.getPlayer();



        CC cc = new CC();
        if(player.hasPermission("JMP.silent")){
            String before = "Hey, %player% you have silentjoin permission";

            String mssg = rep.replace(cc.colour(before) , "%player%", player.getName());

            event.setQuitMessage(null);
            player.sendMessage(mssg);
        }else{

            if (papienabled){
                String bloob = rep.replace(cc.colour(config.getString("messages.leave")), "%player%", "%player_name%");


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
        public void loadconfig() throws IOException{
                cf.save();
        }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        CC cc = new CC();

        replacer rep = new replacer();

        boolean success = false;
        try {
            loadconfig();
            success = true;
        }catch (java.io.IOException e){
            log.severe("Failed to reload plugin because of " + e);
        }
        if(success){
            commandSender.sendMessage(cc.colour("&6 you have successfully reloaded the config"));
        }
        return true;
    }

}

