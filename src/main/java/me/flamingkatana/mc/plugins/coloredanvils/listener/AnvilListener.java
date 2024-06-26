package me.flamingkatana.mc.plugins.coloredanvils.listener;

import me.flamingkatana.mc.plugins.coloredanvils.ColoredAnvils;
import me.flamingkatana.mc.plugins.coloredanvils.constant.AnvilConstants;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AnvilListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        HumanEntity humanEntity = event.getView().getPlayer();

        ItemStack inputItem = anvilInventory.getItem(AnvilConstants.FIRST_INPUT_SLOT);
        if (inputItem == null) {
            return;
        }
        ItemStack outputItem = anvilInventory.getItem(AnvilConstants.OUTPUT_SLOT);
        if (outputItem == null) {
            return;
        }

        event.setResult(ColoredAnvils.itemColorTranslator()
                .translateOutputItemNameColorBasedOnInputItem(outputItem, inputItem, humanEntity));
    }

    @EventHandler
    public void onAnvilInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory instanceof AnvilInventory)) {
            return;
        }
        int clickedSlotIndex = event.getRawSlot();
        if (clickedSlotIndex != AnvilConstants.OUTPUT_SLOT) {
            return;
        }
        ItemStack outputItem = event.getCurrentItem();
        List<String> foundIllegalWords = ColoredAnvils.nameFilter().findIllegalWordsInName(outputItem);
        if (foundIllegalWords.isEmpty()) {
            return;
        }
        HumanEntity humanEntity = event.getWhoClicked();
        foundIllegalWords.forEach(illegalWord -> humanEntity.sendMessage(ColoredAnvils.nameFilter().createFilterMessage(illegalWord)));
        event.setCancelled(true);
        if (humanEntity instanceof Player player) {
            player.setExp(player.getExp()); // Cancelling the event will still visually take the player's EXP - this corrects it
        }
        inventory.setItem(AnvilConstants.OUTPUT_SLOT, new ItemStack(Material.AIR)); // Clients will not see the output slot clear without this
    }

}
