package com.gmail.ak1cec0ld.plugins.Berries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CommandManager implements CommandExecutor {

    private Berries plugin;
    
    public CommandManager(Berries plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player)sender;
            if (args.length == 0){
                p.sendMessage("Pokemon Berries, attachable and edible items that give effects in Minecraft.");
                p.sendMessage("Author: 1cec0ld, Paradox1123, tenebraemaximus");
                p.sendMessage("Version: 3.0");
                p.sendMessage("See §a/berries help§r for commands");
            } else if (args.length == 1){
                if (args[0].equals("regions")){
                    List<?> l = plugin.getConfigManager().getBerryRegions();
                    for (Object o : l){
                        p.sendMessage(((String)o));
                    }
                } else if (args[0].equalsIgnoreCase("list")){
                    Set<String> berries = plugin.getConfigManager().getValidBerriesWithColors();
                    for (String s : berries){
                        p.sendMessage(s);
                    }
                } else if (args[0].equalsIgnoreCase("spawn")){
                    p.sendMessage("/berries spawn (water) [Uses]");
                    p.sendMessage("/berries spawn (hoe) [Material]");
                    p.sendMessage("/berries spawn {berryname} [Amount(max 25)]");
                } else if (args[0].equalsIgnoreCase("info")){
                    sendInfo(p);
                } else if (args[0].equalsIgnoreCase("help")){
                    p.sendMessage("Berry Commands:");
                    if (p.hasPermission("pokeitems.berries.commands.attach")){
                        p.sendMessage("    /berries attach");
                    }
                    if (p.hasPermission("pokeitems.berries.commands.detach")){
                        p.sendMessage("    /berries detach");
                    }
                    p.sendMessage("    /berries help");
                    p.sendMessage("    /berries info");
                    p.sendMessage("    /berries list");
                    p.sendMessage("    /berries regions");
                    if (p.hasPermission("pokeitems.berries.commands.spawn")){
                        p.sendMessage("    /berries spawn");
                    }
                } else if (args[0].equalsIgnoreCase("attach")){
                    p.sendMessage("/berries attach {berryname}");
                } else if (args[0].equalsIgnoreCase("detach")){
                    detachBerry(p);
                }
            } else if (args.length == 2){
                if (args[0].equalsIgnoreCase("spawn")){
                    if (args[1].equalsIgnoreCase("water") || args[1].equalsIgnoreCase("sprayduck")){
                        p.performCommand("berries spawn water 20");
                    } else if (args[1].equalsIgnoreCase("hoe") || args[1].equalsIgnoreCase("soiltiller")){
                        p.performCommand("berries spawn hoe iron");
                    } else {
                        for (String berryNamePlain : plugin.getConfigManager().getValidBerries()){
                            if (args[1].equalsIgnoreCase(berryNamePlain)){
                                p.performCommand("berries spawn "+berryNamePlain+" 1");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("attach")){
                    attachBerry(p,args[1]);
                }
            } else if (args.length == 3){
                if (args[0].equalsIgnoreCase("spawn")){
                    int amount;
                    try{
                        amount = Integer.parseInt(args[2]);
                        if (args[1].equalsIgnoreCase("water") || args[1].equalsIgnoreCase("sprayduck")){
                            ItemStack item = new ItemStack(Material.WATER_BUCKET,1);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setDisplayName("§9Sprayduck");
                            itemMeta.setLore(Arrays.asList("§eUses Left:","§e"+amount));
                            item.setItemMeta(itemMeta);
                            p.getWorld().dropItem(p.getLocation(), item);
                        } else {
                            for (String berryNamePlain : plugin.getConfigManager().getValidBerries()){
                                if (args[1].equalsIgnoreCase(berryNamePlain)){
                                    for (int i = 0; i < Math.min(amount, 25); i++){
                                        ItemStack item = new ItemStack(Material.APPLE,1);
                                        ItemMeta itemMeta = item.getItemMeta();
                                        itemMeta.setDisplayName("§aBerry");
                                        itemMeta.setLore(Arrays.asList("§"+plugin.getConfigManager().getBerryColor(berryNamePlain)+berryNamePlain));
                                        item.setItemMeta(itemMeta);
                                        p.getWorld().dropItem(p.getLocation(), item);
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e){
                        HashMap<String,Material> mats = new HashMap<String,Material>();
                        mats.put("wood",Material.WOOD_HOE);
                        mats.put("stone",Material.STONE_HOE);
                        mats.put("gold",Material.GOLD_HOE);
                        mats.put("iron",Material.IRON_HOE);
                        mats.put("diamond",Material.DIAMOND_HOE);
                        if (mats.keySet().contains(args[2].toLowerCase()) && (args[1].equalsIgnoreCase("hoe") || args[1].equalsIgnoreCase("soiltiller"))){
                            ItemStack item = new ItemStack(mats.get(args[2].toLowerCase()),1);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setDisplayName("§8SoilTiller");
                            item.setItemMeta(itemMeta);
                            p.getWorld().dropItem(p.getLocation(), item);
                        } else {
                            p.performCommand("berries spawn");
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void detachBerry(Player p) {
        if (p.getInventory().getItemInMainHand() != null){
            if (p.getInventory().getItemInMainHand().getItemMeta() != null){
                if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()){
                    if (plugin.isBerryName(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0))){
                        ItemStack item = new ItemStack(Material.APPLE,1);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName("§aBerry");
                        itemMeta.setLore(Arrays.asList());
                        item.setItemMeta(itemMeta);
                        p.getWorld().dropItem(p.getLocation(), item);
                        p.getInventory().getItemInMainHand().getItemMeta().setLore(null);
                    } else {
                        p.sendMessage("Your held item doesn't have a Berry attached to it!");
                    }
                } else {
                    p.sendMessage("Your held item doesn't have a Berry attached to it");
                }
            } else {
                p.sendMessage("Your held item doesn't have a Berry attached to it");
            }
        } else {
            p.sendMessage("You need to hold an item to detach the Berry from it!");
        }
    }

    private void attachBerry(Player p, String berryname) {
        if (plugin.isBerryName(berryname)){
            if(p.getInventory().getItemInMainHand()!=null){
                ItemStack main = p.getInventory().getItemInMainHand();
                if ((main.hasItemMeta() && !main.getItemMeta().hasLore()) || !main.hasItemMeta()){
                    ItemStack[] invlist = p.getInventory().getContents();
                    boolean foundBerry = false;
                    List<String> attachableList = plugin.getConfigManager().getAttachableItems();
                    if (attachableList.contains(main.getType().toString())){
                        for (ItemStack item : invlist){
                            if (!foundBerry && item.getItemMeta() != null && item.getItemMeta().getLore() != null && ChatColor.stripColor(item.getItemMeta().getLore().get(0).toLowerCase()).equals(berryname.toLowerCase())){
                                foundBerry = true;
                                item.setAmount(item.getAmount()-1);
                                ItemMeta meta = main.getItemMeta();
                                String newlore = "§"+plugin.getConfigManager().getBerryColor(berryname)+berryname;
                                meta.setLore(Arrays.asList(newlore));
                            }
                        }
                        if (!foundBerry){
                            p.sendMessage("No "+berryname+" Berry was found in your inventory!");
                        }
                    } else {
                        p.sendMessage("You can't attach a Berry to the "+main.getType().name().toLowerCase()+" you're holding!");
                    }
                } else {
                    p.sendMessage("Your "+main.getType().name().toLowerCase()+" can't be given a Berry!");
                }
            } else {
                p.sendMessage("You need to hold something to attach a Berry to it!");
            }
        } else {
            p.sendMessage(berryname+" isn't an actual Berry.");
        }
    }

    private void sendInfo(Player p) {
        if (p.getInventory().getItemInMainHand() != null){
            if (p.getInventory().getItemInMainHand().getItemMeta() != null && p.getInventory().getItemInMainHand().getItemMeta().getLore() != null){
                String itemLore = p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0);
                for (String names:plugin.getConfigManager().getValidBerriesWithColors()){
                    if (itemLore.equals(names)){
                        String uses = plugin.getConfigManager().getBerryUses(itemLore).toLowerCase();
                        if (uses.contains("consume")){
                            p.sendMessage("This Berry will give you its effect if you eat it.");
                        }
                        if (uses.contains("attack")){
                            p.sendMessage("This Berry will give you its effect if it is attached when you attack something.");
                        }
                        if (uses.contains("damage")){
                            p.sendMessage("This Berry will give you its effect if it is attached when you get hurt.");
                        }
                        if (uses.contains("lowhealth")){
                            p.sendMessage("This Berry only activates if your health is less than 3 hearts.");
                        }
                        if (uses.contains("middlehealth")){
                            p.sendMessage("This Berry only activates if your health is less than 4.5 hearts.");
                        }
                        if (uses.contains("highhealth")){
                            p.sendMessage("This Berry only activates if your health is less than 5.5 hearts.");
                        }
                        if (uses.contains("criticalhit")){
                            p.sendMessage("This Berry only activates on critical attacks.");
                        }
                        if (uses.contains("itemdurability")){
                            p.sendMessage("This Berry only activates when the item it is attached to has 1/2 or lower durability.");
                        }
                        if (uses.contains("physicaldamage")){
                            p.sendMessage("This Berry only activates if you are hit by a non-ranged attack.");
                        }
                        if (uses.contains("rangeddamage")){
                            p.sendMessage("This Berry only activates if you are hit by a ranged attack.");
                        }
                        String effects = plugin.getConfigManager().getBerryEffects(itemLore).toLowerCase();
                        if (effects.contains("smallheal")){
                            p.sendMessage("This Berry heals you for 2 hearts");
                        }
                        if (effects.contains("largeheal")){
                            p.sendMessage("This Berry heals you for 5 hearts");
                        }
                        if (effects.contains("confusionflavor")){
                            p.sendMessage("This Berry might Confuse you if you don't like its flavor!");
                        }
                        if (effects.contains("cureslow")){
                            p.sendMessage("This Berry will remove any Slowness effects you have");
                        }
                        if (effects.contains("curepoison")){
                            p.sendMessage("This Berry will instantly stop you from being Poisoned");
                        }
                        if (effects.contains("cureconfusion")){
                            p.sendMessage("This Berry will instantly stop you from feeling Confusion");
                        }
                        if (effects.contains("cureburn")){
                            p.sendMessage("This Berry will instantly stop you from Burning");
                        }
                        if (effects.contains("boostspeed")){
                            p.sendMessage("This Berry will make you run faster for 5 seconds");
                        }
                        if (effects.contains("boostdamage")){
                            p.sendMessage("This Berry will make an attack do an extra 2 hearts of damage");
                        }
                        if (effects.contains("fixitem")){
                            p.sendMessage("This Berry will return 1/4 of item durability to its holder");
                        }
                        if (effects.contains("boostmcmmoluck")){
                            p.sendMessage("This Berry will make your McMMO Skills stronger for 10 seconds");
                        }
                        if (effects.contains("quarterdamage")){
                            p.sendMessage("This Berry reduces incoming damage by 75%");
                        }
                        if (effects.contains("reducedamage")){
                            p.sendMessage("This Berry reduces incoming damage by 2.5 hearts");
                        }
                        if (effects.contains("addconfusion")){
                            p.sendMessage("This Berry will Confuse you when you eat it");
                        }
                        String flavor = plugin.getConfigManager().getBerryFlavor(itemLore).toLowerCase();
                        if (plugin.flavorConfuses(flavor, p.getUniqueId().toString())){
                            p.sendMessage("This Berry will Confuse you, you don't like "+flavor+" Berries!");
                        }
                    }
                }
            }
        } else if (p.getInventory().getItemInOffHand() != null){
            if (p.getInventory().getItemInOffHand().getItemMeta() != null && p.getInventory().getItemInOffHand().getItemMeta().getLore() != null){
                String itemLore = p.getInventory().getItemInOffHand().getItemMeta().getLore().get(0);
                for (String names:plugin.getConfigManager().getValidBerriesWithColors()){
                    if (itemLore.equals(names)){
                        String uses = plugin.getConfigManager().getBerryUses(itemLore).toLowerCase();
                        if (uses.contains("consume")){
                            p.sendMessage("This Berry will give you its effect if you eat it.");
                        }
                        if (uses.contains("attack")){
                            p.sendMessage("This Berry will give you its effect if it is attached when you attack something.");
                        }
                        if (uses.contains("damage")){
                            p.sendMessage("This Berry will give you its effect if it is attached when you get hurt.");
                        }
                        if (uses.contains("lowhealth")){
                            p.sendMessage("This Berry only activates if your health is less than 3 hearts.");
                        }
                        if (uses.contains("middlehealth")){
                            p.sendMessage("This Berry only activates if your health is less than 4.5 hearts.");
                        }
                        if (uses.contains("highhealth")){
                            p.sendMessage("This Berry only activates if your health is less than 5.5 hearts.");
                        }
                        if (uses.contains("criticalhit")){
                            p.sendMessage("This Berry only activates on critical attacks.");
                        }
                        if (uses.contains("itemdurability")){
                            p.sendMessage("This Berry only activates when the item it is attached to has 1/2 or lower durability.");
                        }
                        if (uses.contains("physicaldamage")){
                            p.sendMessage("This Berry only activates if you are hit by a non-ranged attack.");
                        }
                        if (uses.contains("rangeddamage")){
                            p.sendMessage("This Berry only activates if you are hit by a ranged attack.");
                        }
                        String effects = plugin.getConfigManager().getBerryEffects(itemLore).toLowerCase();
                        if (effects.contains("smallheal")){
                            p.sendMessage("This Berry heals you for 2 hearts");
                        }
                        if (effects.contains("largeheal")){
                            p.sendMessage("This Berry heals you for 5 hearts");
                        }
                        if (effects.contains("confusionflavor")){
                            p.sendMessage("This Berry might Confuse you if you don't like its flavor!");
                        }
                        if (effects.contains("cureslow")){
                            p.sendMessage("This Berry will remove any Slowness effects you have");
                        }
                        if (effects.contains("curepoison")){
                            p.sendMessage("This Berry will instantly stop you from being Poisoned");
                        }
                        if (effects.contains("cureconfusion")){
                            p.sendMessage("This Berry will instantly stop you from feeling Confusion");
                        }
                        if (effects.contains("cureburn")){
                            p.sendMessage("This Berry will instantly stop you from Burning");
                        }
                        if (effects.contains("boostspeed")){
                            p.sendMessage("This Berry will make you run faster for 5 seconds");
                        }
                        if (effects.contains("boostdamage")){
                            p.sendMessage("This Berry will make an attack do an extra 2 hearts of damage");
                        }
                        if (effects.contains("fixitem")){
                            p.sendMessage("This Berry will return 1/4 of item durability to its holder");
                        }
                        if (effects.contains("boostmcmmoluck")){
                            p.sendMessage("This Berry will make your McMMO Skills stronger for 10 seconds");
                        }
                        if (effects.contains("quarterdamage")){
                            p.sendMessage("This Berry reduces incoming damage by 75%");
                        }
                        if (effects.contains("reducedamage")){
                            p.sendMessage("This Berry reduces incoming damage by 2.5 hearts");
                        }
                        if (effects.contains("addconfusion")){
                            p.sendMessage("This Berry will Confuse you when you eat it");
                        }
                        String flavor = plugin.getConfigManager().getBerryFlavor(itemLore).toLowerCase();
                        if (plugin.flavorConfuses(flavor, p.getUniqueId().toString())){
                            p.sendMessage("This Berry will Confuse you, you don't like "+flavor+" Berries!");
                        }
                    }
                }
            }
        } else {
            p.sendMessage("Hold a Berry to get its Info!");
        }
    }
}
