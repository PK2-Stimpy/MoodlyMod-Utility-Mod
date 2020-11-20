package us.np.moodlymod.util;

import static us.np.moodlymod.MoodlyMod.debug;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;

public class ConfigUtils {
    private String name;
    private String filePath;
    private String complexFilePath;
    private File file;
    private JSONObject jsonObject;

    public JSONObject getJSON() {return jsonObject;}
    public File getFile() {return file;}

    public ConfigUtils(String name, String subfolder) {
        debug(name, "Declare config");
        this.name = name;
        filePath =  "moodlymod\\" + ((subfolder != "") ? (subfolder + "\\") : "");
        complexFilePath = filePath + name + ".json";
        File pathF = new File(filePath);
        pathF.mkdirs();
        pathF = null;
        File file = new File(complexFilePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(complexFilePath);
                fileWriter.write("{}");
                fileWriter.close();
            }
        } catch(Exception e) { e.printStackTrace(); }

        try {
            InputStream is = new FileInputStream(complexFilePath);
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            jsonObject = new JSONObject(jsonTxt);
        } catch(Exception e) { e.printStackTrace(); }
        debug(name, "Done!");
    }

    public Object get(String key) {
        debug(key, "FETCH!");
        return jsonObject.get(key);
    }

    public void set(String key, Object value) {
        jsonObject.put(key, value);
        debug(name, "Set config '" + key + "' > '" + value + "'");
    }
    public void save() {
        try {
            PrintWriter printWriter = new PrintWriter(complexFilePath);
            printWriter.print(jsonObject.toString());
            printWriter.close();
        }catch (Exception e) { e.printStackTrace(); }
        debug(name, "Printed config file!");
    }
}