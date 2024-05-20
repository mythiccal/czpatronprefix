package mythiccal.uk.czpatronprefix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectCommand implements Listener {
    private final CZPatronPrefixPlugin plugin;
    private static final String INVENTORY_NAME = "Select Gradient";
    private static final int ITEMS_PER_PAGE = 27;
    private static final Pattern PAGE_PATTERN = Pattern.compile("Select Gradient - Page (\\d+) of (\\d+)");
    public SelectCommand(CZPatronPrefixPlugin plugin) {
        this.plugin = plugin;
    }
    public void openGradientSelectionGUI(Player player, int page) {
        Map<String, String> gradients = plugin.getGradients();
        List<String> gradientNames = new ArrayList<>(gradients.keySet());
        int totalPages = (int) Math.ceil((double) gradientNames.size() / ITEMS_PER_PAGE);
        Inventory gui = Bukkit.createInventory(null, 36, INVENTORY_NAME + " - Page " + (page + 1) + " of " + totalPages);
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, gradientNames.size());
        for (int i = start; i < end; i++) {
            String gradientName = gradientNames.get(i);
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(gradientName);
            item.setItemMeta(meta);
            gui.addItem(item);
        }
        if (page > 0) {
            ItemStack previousPage = new ItemStack(Material.ARROW);
            ItemMeta meta = previousPage.getItemMeta();
            meta.setDisplayName("Previous Page");
            previousPage.setItemMeta(meta);
            gui.setItem(27, previousPage);
        }
        if (page < totalPages - 1) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta meta = nextPage.getItemMeta();
            meta.setDisplayName("Next Page");
            nextPage.setItemMeta(meta);
            gui.setItem(35, nextPage);
        }
        player.openInventory(gui);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Matcher matcher = PAGE_PATTERN.matcher(title);
        if (matcher.matches()) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                int currentPage = Integer.parseInt(matcher.group(1)) - 1;
                if (itemName.equals("Previous Page")) {
                    openGradientSelectionGUI(player, currentPage - 1);
                    return;
                } else if (itemName.equals("Next Page")) {
                    openGradientSelectionGUI(player, currentPage + 1);
                    return;
                }
                if (plugin.getGradients().containsKey(itemName)) {
                    String selectedGradient = plugin.getGradients().get(itemName);
                    plugin.getConfig().set("gradientPrefix", selectedGradient);
                    plugin.setGradientPrefix(selectedGradient);
                    plugin.saveConfig();
                    player.sendMessage("Gradient updated to " + itemName);
                }
                player.closeInventory();
            }
        }
    }
}