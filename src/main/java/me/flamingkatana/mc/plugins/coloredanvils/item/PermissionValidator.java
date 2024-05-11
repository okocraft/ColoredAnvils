package me.flamingkatana.mc.plugins.coloredanvils.item;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMaps;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import me.flamingkatana.mc.plugins.coloredanvils.ColoredAnvils;
import me.flamingkatana.mc.plugins.coloredanvils.constant.AnvilConstants;
import org.bukkit.entity.HumanEntity;

public class PermissionValidator {

    private static final Char2ObjectMap<String> COLOR_CODE_PERMISSION_MAP;

    static {
        var map = new Char2ObjectOpenHashMap<String>();
        for (char c : AnvilConstants.COLOR_CODES.toCharArray()) {
            map.put(c, "coloredanvils.color." + c);
        }
        COLOR_CODE_PERMISSION_MAP = Char2ObjectMaps.unmodifiable(map);
    }

    private final boolean arePermissionsEnabled;

    public PermissionValidator() {
        arePermissionsEnabled = ColoredAnvils.getPlugin().getConfig().getBoolean("Use Permissions");
    }

    public String enforcePermissionsOnItemName(HumanEntity humanEntity, String name) {
        if (!arePermissionsEnabled) {
            return name;
        }

        String enforcedName;

        if (humanEntity.hasPermission("coloredanvils.color.hex")) {
            // If the player can use hex color, it is not necessary to modify anything.
            enforcedName = name;
        } else {
            // Remove hex color codes
            enforcedName = AnvilConstants.HEX_PATTERN.matcher(name).replaceAll("");
        }

        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0, l = enforcedName.length(); i < l; i++) {
            char c = enforcedName.charAt(i);
            if (c != AnvilConstants.UNTRANSLATED_COLOR_CHAR || i + 1 == l) {
                resultBuilder.append(c);
                continue;
            }
            char colorCode = enforcedName.charAt(i + 1);
            if (AnvilConstants.COLOR_CODES.indexOf(colorCode) == -1 || humanEntity.hasPermission(COLOR_CODE_PERMISSION_MAP.get(c))) {
                resultBuilder.append(c);
                continue;
            }

            i++; // To remove the color code completely, skip the next character as well.
        }

        return resultBuilder.toString();
    }
}
