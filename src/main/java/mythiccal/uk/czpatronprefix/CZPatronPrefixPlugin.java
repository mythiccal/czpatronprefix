package mythiccal.uk.czpatronprefix;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class CZPatronPrefixPlugin extends JavaPlugin {
    //temporarily hardcoding this bc it wont behave, gradient is unnecessarily long bc i forgot the original patron gradient lol
    private static final String DEFAULT_GRADIENT = "<gradient:#a5f4ff:#85e7ff:#64daff:#44cdff:#45c2fb:#67b7f2:#89acea>";
    private String gradientPrefix;
    private Map<String, String> gradients;
    private SelectCommand selectCommand; // it was sending messages like 50 times without this
    @Override
        public void onEnable() {
            saveDefaultConfig();
            loadGradientsFromConfig();
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                new PrefixPlaceholders(this).register();
                getLogger().info("Successfully registered placeholders.");
            } else {
                getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            selectCommand = new SelectCommand(this);
            Bukkit.getPluginManager().registerEvents(selectCommand, this);
            CommandManager commandManager = new CommandManager(this, selectCommand);
            getCommand("czpatronprefix").setExecutor(commandManager);
            getCommand("czpatronprefix").setTabCompleter(commandManager);
        }
        @Override
        public void onDisable() {
        //i dont think i need to do anything here
        }
        public void loadGradientsFromConfig() {
            gradientPrefix = getConfig().getString("gradientPrefix", DEFAULT_GRADIENT);
            gradients = new HashMap<>();
            ConfigurationSection section = getConfig().getConfigurationSection("gradients");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    gradients.put(key, section.getString(key));
                }
            }
        }
        public void setGradientPrefix(String gradientPrefix) {
            this.gradientPrefix = gradientPrefix;
        }
        public void resetGradientPrefix() {
            this.gradientPrefix = DEFAULT_GRADIENT;
        }
        public String getGradientPrefix() {
            return gradientPrefix;
        }
        public Map<String, String> getGradients() {
            return gradients;
        }
        public String getGradientName(String gradientValue) {
            return gradients.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(gradientValue))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("Default");
        }
    }