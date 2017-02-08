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
 * It does basically absolutely nothing right now, except for being there.
 * Kinda like me. Huh.
 * @author Follpvosten
 */
public final class TranslationManager {
    
    private static ResourceBundle translationBundle = null;
    
    public static void loadTranslations() {
        translationBundle = ResourceBundle.getBundle("schneckenrennen/CustomsBundle");
    }
    
    public static String getTranslation(String key) {
        return translationBundle.getString(key);
    }
    
    public static String getTranslation(String key, Object... args) {
        return String.format(translationBundle.getString(key), args);
    }
}
