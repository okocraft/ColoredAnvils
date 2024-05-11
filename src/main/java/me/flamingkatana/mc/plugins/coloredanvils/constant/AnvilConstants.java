package me.flamingkatana.mc.plugins.coloredanvils.constant;

import java.util.regex.Pattern;

public class AnvilConstants {

    public static final int FIRST_INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 2;
    public static final char UNTRANSLATED_COLOR_CHAR = '&';
    public static final String COLOR_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    public static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]){6}");

}
