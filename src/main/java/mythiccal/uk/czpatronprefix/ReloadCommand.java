package mythiccal.uk.czpatronprefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final CZPatronPrefixPlugin plugin;
    public ReloadCommand(CZPatronPrefixPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("prefixselector.reload")) {
            plugin.reloadConfig();
            plugin.loadGradientsFromConfig();
            sender.sendMessage("Gradient configuration reloaded.");
            return true;
        } else {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }
    }
}