/*
 * Copyright (C) 2017 Follpvosten
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package schneckenrennen;

import java.util.ResourceBundle;

/**
 * A class used to load and deliver localized Strings.
 * @author Follpvosten
 */
public final class Translations {
    
    private static ResourceBundle translationBundle = null;
    
    /**
     * Loads the Translations from the default ResourceBundle "CustomsBundle"
     */
    public static void loadTranslations() {
        translationBundle = ResourceBundle.getBundle("schneckenrennen/CustomsBundle");
    }
    
    /**
     * Returns a translation from the loaded Bundle.
     * @param key The key of the translation entry
     * @return The translated String
     */
    public static String get(String key) {
        return translationBundle.getString(key);
    }
    
    /**
     * Returns a formatted translation from the translations file.
     * @param key The key of the translation entry
     * @param args The formatting arguments
     * @return The translated, formatted String
     */
    public static String get(String key, Object... args) {
        return String.format(translationBundle.getString(key), args);
    }
}
