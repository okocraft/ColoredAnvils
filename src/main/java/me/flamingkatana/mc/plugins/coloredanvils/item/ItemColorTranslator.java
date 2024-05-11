package me.flamingkatana.mc.plugins.coloredanvils.item;

import me.flamingkatana.mc.plugins.coloredanvils.ColoredAnvils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemColorTranslator {

    private static final Style NON_ITALIC = Style.style(TextDecoration.ITALIC.withState(TextDecoration.State.FALSE));

    public ItemColorTranslator() {
    }

    public ItemStack translateOutputItemNameColorBasedOnInputItem(ItemStack outputItem, ItemStack inputItem, HumanEntity humanEntity) {
        ItemMeta outputItemMeta = outputItem.getItemMeta();
        if (outputItemMeta == null) {
            return outputItem;
        }

        ItemMeta inputItemMeta = inputItem.getItemMeta();
        if (inputItemMeta == null) {
            return outputItem;
        }

        Component outputName = outputItemMeta.displayName();
        Component inputName = inputItemMeta.displayName();

        if (inputName == null && outputName != null) {
            // Newly named
            return rename(outputItem, humanEntity, outputName, outputItemMeta);
        } else if (inputName != null && outputName != null) {
            return inputName.equals(outputName) ?
                    outputItem : // Not renamed
                    rename(outputItem, humanEntity, outputName, outputItemMeta);
        }

        // Custom name removed
        return outputItem;
    }

    private static @NotNull ItemStack rename(ItemStack outputItem, HumanEntity humanEntity, Component outputName, ItemMeta outputItemMeta) {
        String plainOutputName = PlainTextComponentSerializer.plainText().serialize(outputName);
        String enforcedName = ColoredAnvils.permissionValidator().enforcePermissionsOnItemName(humanEntity, plainOutputName);
        Component colored = LegacyComponentSerializer.legacyAmpersand().deserialize(enforcedName).applyFallbackStyle(NON_ITALIC);
        outputItemMeta.displayName(colored);
        outputItem.setItemMeta(outputItemMeta);
        return outputItem;
    }
}
