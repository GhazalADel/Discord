package discord.discord1;

import java.util.HashMap;

public class UIResponse {

    private UIResponseCode code;
    private HashMap<String, Object> data;

    public UIResponse (UIResponseCode code) {
        this.code = code;
        data=new HashMap<>();
    }

    public UIResponseCode getCode() {
        return code;
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
