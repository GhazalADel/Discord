package discord.discord1;

import java.io.Serializable;
import java.util.HashMap;

public class UIRequest {
    private UIRequestCode code;
    private HashMap<String, Object> data;

    public UIRequest(UIRequestCode code) {
        this.code = code;
        data = new HashMap<>();
    }

    public UIRequestCode getCode() {
        return code;
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }
}
