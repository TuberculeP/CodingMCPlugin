package me.felixlavieville.plugintest;

import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;


public final class PluginTest extends JavaPlugin{

    Server server = Bukkit.getServer();
    private static PluginTest plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        //envoyer "C'est parti pour la bagarre" dans la console
        server.getConsoleSender().sendMessage(ChatColor.GREEN + "C'est parti pour la bagarre");
        plugin = this;
        getServer().getPluginManager().registerEvents(new CommandBack(), this);
        getServer().getPluginManager().registerEvents(new ChatEvents(), this);
        getServer().getPluginManager().registerEvents(new GoGUI(), this);
        getServer().getPluginManager().registerEvents(new AdminStuff(), this);
        getServer().getPluginManager().registerEvents(new HubSpawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerHUD(), this);
        getServer().getPluginManager().registerEvents(new Food(), this);
    }

    public static PluginTest getPlugin() {
        return plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("back")) {
            //si le sender est un joueur
            if (sender instanceof Player) {
                //créer une instance de CommandBack
                CommandBack back = new CommandBack();
                //appeler la méthode Back
                back.back((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("sethub")){
            if(sender instanceof Player){
                if(sender.hasPermission("sethub.use")){
                    HubSpawn hub = new HubSpawn();
                    hub.setHub(((Player) sender).getLocation());
                    sender.sendMessage(ChatColor.GREEN + "Hub défini à : " + ((Player) sender).getLocation());
                }
            }
        }
        if(command.getName().equalsIgnoreCase("hub")){
            if(sender instanceof Player){
                HubSpawn hub = new HubSpawn();
                hub.hub((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("randomtp")){
            if(sender instanceof Player){
                RandomTP randomTP = new RandomTP();
                randomTP.randomTP((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("enderchest")){
            if(sender instanceof Player){
                EnderChest enderChest = new EnderChest();
                enderChest.enderChest((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("go")){
            if(sender instanceof Player){
                GoGUI goGUI = new GoGUI();
                goGUI.go((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("salaire")){
            if(sender instanceof Player){
                MoneySystem moneySystem = new MoneySystem();
                moneySystem.getSalaire((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("admin")){
            if(sender instanceof Player){
                //if is operator
                if(sender.isOp()){
                    AdminStuff adminStuff = new AdminStuff();
                    adminStuff.admin((Player) sender);
                }else{
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("gettag")){
            if(sender instanceof Player){
                if(args.length == 1){
                    GetTag getTag = new GetTag();
                    getTag.giveTag((Player) sender, args[0]);
                }else{
                    sender.sendMessage(ChatColor.RED + "Usage : /gettag <tag>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("ratio")){
            if(sender instanceof Player){
                ChatEvents chatEvents = new ChatEvents();
                chatEvents.ratio((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("flop")){
            if(sender instanceof Player){
                ChatEvents chatEvents = new ChatEvents();
                chatEvents.flop((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("tpa")) {
            if (sender instanceof Player) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        //vérifier si le joueur a déjà demandé un tp
                        if(getConfig().get("tpa." + sender.getName()) != null){
                            sender.sendMessage(ChatColor.RED + "Tu as déjà demandé un tp");
                        }else{
                            TPA tpa = new TPA();
                            try {
                                tpa.tpRequest((Player) sender, target);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage : /tpa <joueur>");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage : /tpa <joueur>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("tpaccept")) {
            if (sender instanceof Player) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    //vérifier si le joueur a une demande de tp dans la config
                    if (getConfig().get("tpa." + target.getName()) != null) {
                        TPA tpa = new TPA();
                        tpa.tpAccept(target, (Player) sender);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de tp de la part de " + target.getName());
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "Usage : /tpaccept <joueur>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("tpdeny")) {
            if (sender instanceof Player) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    //vérifier si le joueur a une demande de tp dans la config
                    if (getConfig().get("tpa." + target.getName()) != null) {
                        TPA tpa = new TPA();
                        tpa.tpDeny(target,(Player) sender);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Vous n'avez pas de demande de tp de la part de " + target.getName());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage : /tpdeny <joueur>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("money")) {
            if(args.length == 0){
                if(sender instanceof Player){
                    MoneySystem moneySystem = new MoneySystem();
                    moneySystem.getMoney((Player) sender);
                }
            }else{
                if(args[0].equals("give")) {
                    MoneySystem moneySystem = new MoneySystem();
                    if (sender instanceof Player) {
                        if (args.length == 3) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                if (args[2].matches("[0-9]+")) {
                                    moneySystem.transferMoney((Player) sender, target, Integer.parseInt(args[2]));
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage : /money give <joueur> <montant>");
                                }
                            } else {
                                sender.sendMessage("§6[§eJetBank§6]§r §a §cLe joueur " + args[1] + " n'est pas connecté ou n'existe pas");
                            }
                        }else{
                            sender.sendMessage(ChatColor.RED + "Usage : /money give <joueur> <montant>");
                        }
                    }else{
                        //si la commande est envoyée depuis la console, on vérifie que les arguments sont bien présents puis on utilise addMoney
                        if (args.length == 3) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                if (args[2].matches("[0-9]+")) {
                                    moneySystem.addMoney(target, Integer.parseInt(args[2]));
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage : /money give <joueur> <montant>");
                                }
                            } else {
                                sender.sendMessage("§6[§eJetBank§6]§r §a §cLe joueur " + args[1] + " n'est pas connecté ou n'existe pas");
                            }
                        }else{
                            sender.sendMessage(ChatColor.RED + "Usage : /money give <joueur> <montant>");
                        }
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Usage : /money give <joueur> <montant>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("skull")) {
            if (sender instanceof Player) {
                GetSkull getSkull = new GetSkull();
                if (args.length == 1) {
                    getSkull.getSkull((Player) sender, args[0]);
                } else {
                    getSkull.getSkull((Player) sender, sender.getName());
                }
            }
        }
        if(command.getName().equalsIgnoreCase("hubfirstpoint")) {
            if (sender instanceof Player) {
                if (sender.isOp()) {
                    HubSpawn hub = new HubSpawn();
                    hub.setBeginPoint(((Player) sender).getLocation());
                    sender.sendMessage("§6[§eHubSpawn§6]§r §a §aLe point de départ du hub a été défini");
                } else {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("hubsecondpoint")) {
            if (sender instanceof Player) {
                if (sender.isOp()) {
                    HubSpawn hub = new HubSpawn();
                    hub.setEndPoint(((Player) sender).getLocation());
                    sender.sendMessage("§6[§eHubSpawn§6]§r §a §aLe point d'arrivée du hub a été défini");
                } else {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("setroubaix")) {
            //impossible si non-op
            if(sender instanceof Player){
                if(sender.isOp()){
                    Roubaix roubaix = new Roubaix();
                    roubaix.setRoubaix((Player) sender);
                }else{
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("roubaix")) {
            if(sender instanceof Player){
                Roubaix roubaix = new Roubaix();
                roubaix.roubaix((Player) sender);
            }
        }
        if(command.getName().equalsIgnoreCase("feed")){
            Food food = new Food();
            if(sender instanceof Player){
                if(args.length == 0){
                    food.feed((Player) sender);
                }else{
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target != null){
                        food.feed(target);
                    }else{
                        sender.sendMessage(ChatColor.RED + "Usage : /feed <joueur>");
                    }
                }
            }
        }
        if(command.getName().equalsIgnoreCase("sethome")){
            if(sender instanceof Player){
                HomeSystem home = new HomeSystem();
                if(args.length == 1) {
                    String[] homes = home.getHomes((Player) sender);
                    if(homes.length < 3) {
                        if (!Arrays.asList(homes).contains(args[0])) {
                            home.setHome((Player) sender, args[0]);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Vous avez déjà un home avec ce nom");
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "Vous avez déjà 3 homes. Vous ne pouvez pas en créer plus. Vous pouvez en supprimer avec /delhome <nom>");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Usage : /sethome <nom>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("delhome")){
            if(sender instanceof Player){
                HomeSystem home = new HomeSystem();
                if(args.length == 1) {
                    String[] homes = home.getHomes((Player) sender);
                    if (Arrays.asList(homes).contains(args[0])) {
                        home.delHome((Player) sender, args[0]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Vous n'avez pas de home avec ce nom");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Usage : /delhome <nom>");
                }
            }
        }
        if(command.getName().equalsIgnoreCase("home")){
            if(sender instanceof Player){
                HomeSystem home = new HomeSystem();
                String[] homes = home.getHomes((Player) sender);
                if(args.length == 1) {
                    if (Arrays.asList(homes).contains(args[0])) {
                        home.home((Player) sender, args[0]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Vous n'avez pas de home avec ce nom");
                    }
                }else{
                    home.listHome((Player) sender);
                }
            }
        }

            //empêcher le nom de la commande de s'afficher dans le chat après l'exécution
        return true;
        //return super.onCommand(sender, command, label, args);
    }
}
