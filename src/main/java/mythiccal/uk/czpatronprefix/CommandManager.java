package mythiccal.uk.czpatronprefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final CZPatronPrefixPlugin plugin;
    private final SelectCommand selectCommand;
    public CommandManager(CZPatronPrefixPlugin plugin, SelectCommand selectCommand) {
        this.plugin = plugin;
        this.selectCommand = selectCommand;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /czpatronprefix <reload|selectgradient|resetgradient>");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("czpatronprefix.reload")) {
                plugin.reloadConfig();
                plugin.loadGradientsFromConfig();
                sender.sendMessage("Gradient configuration reloaded.");
                return true;
            } else {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("selectgradient")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("czpatronprefix.use")) {
                    selectCommand.openGradientSelectionGUI(player, 0); // Use the existing SelectCommand instance
                    return true;
                } else {
                    player.sendMessage("You don't have permission to use this command.");
                    return true;
                }
            } else {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("resetgradient")) {
            if (sender.hasPermission("czpatronprefix.use")) {
                plugin.resetGradientPrefix();
                plugin.getConfig().set("gradientPrefix", plugin.getGradientPrefix());
                plugin.saveConfig();
                sender.sendMessage("Gradient reset to default.");
                return true;
            } else {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }
        }
        sender.sendMessage("Unknown command. Usage: /czpatronprefix <reload|selectgradient|resetgradient>");
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "selectgradient", "resetgradient");
        }
        return new ArrayList<>();
    }
}