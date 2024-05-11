package me.flamingkatana.mc.plugins.coloredanvils.item;

import me.flamingkatana.mc.plugins.coloredanvils.ColoredAnvils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NameFilter {

    private final List<String> illegalWords;
    private final Component filterMessage;

    public NameFilter() {
        boolean isFilterEnabled = ColoredAnvils.getPlugin().getConfig().getBoolean("Filter_Enabled");
        if (isFilterEnabled) {
            illegalWords = ColoredAnvils.getPlugin().getConfig().getStringList("Filter");
        } else {
            illegalWords = Collections.emptyList();
        }
        filterMessage = Component.text(ColoredAnvils.getPlugin().getConfig().getString("Filter_Message", ""));
    }

    public List<String> findIllegalWordsInName(ItemStack itemStack) {
        if (illegalWords.isEmpty() || itemStack == null) {
            return Collections.emptyList();
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Collections.emptyList();
        }
        Component itemName = itemMeta.displayName();

        if (itemName != null) {
            return findIllegalWords(PlainTextComponentSerializer.plainText().serialize(itemName));
        } else {
            return Collections.emptyList();
        }
    }

    public List<String> findIllegalWords(String name) {
        return illegalWords.stream()
                .filter(illegalWord -> containsIgnoreCase(name, illegalWord))
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String str, String check) {
        return str.toLowerCase().contains(check.toLowerCase());
    }

    public Component createFilterMessage(String word) {
        return Component.text()
                .color(NamedTextColor.RED)
                .append(this.filterMessage)
                .append(Component.text('\''))
                .append(Component.text().content(word).decorate(TextDecoration.BOLD))
                .append(Component.text("'."))
                .build();
    }
}
