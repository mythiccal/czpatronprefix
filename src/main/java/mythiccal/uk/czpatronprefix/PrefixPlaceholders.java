package mythiccal.uk.czpatronprefix;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PrefixPlaceholders extends PlaceholderExpansion {
    private final CZPatronPrefixPlugin plugin;



    //pov blindly copying papi github example
    public PrefixPlaceholders(CZPatronPrefixPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "czpatronprefix";
    }
    @Override
    public String getAuthor() {
        return "mythieuwu";
    }
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("prefix")) {
            MiniMessage miniMessage = MiniMessage.builder()
                    .tags(TagResolver.standard())
                    .build();
            String gradientPrefix = plugin.getGradientPrefix();
            String gradientValue = "<bold>PATRON</bold>++";
            Component parsedComponent = miniMessage.deserialize(applyGradient(gradientValue, gradientPrefix));
            return LegacyComponentSerializer.legacySection().serialize(parsedComponent);
        }
        return null;
    }
    private String applyGradient(String text, String gradientConfig) {
        return gradientConfig + text + "</gradient>";
    }
}