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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages saving and loading various types of data to files.
 * @author Follpvosten
 */
public class Saves {
    
    public static Rennen loadRace(String filename) throws IOException, JSONException {
	File file = new File(filename);
	FileInputStream fis = new FileInputStream(file);
	byte[] data = new byte[(int) file.length()];
	fis.read(data);
	fis.close();
	return Rennen.fromJSON(new JSONObject(new String(data, "UTF-8")));
    }
    
    public static void saveRace(Rennen race, String filename) throws IOException {
	File file = new File(filename);
	FileWriter fw = new FileWriter(file, false);
	fw.write(race.toJSON().toString());
	fw.flush();
	fw.close();
    }
    
}
