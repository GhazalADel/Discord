package discord;

import statics.RequestCode;

import java.util.HashMap;

public class ClientRequest {
    private RequestCode code;
    private HashMap<String,Object> data;

    public ClientRequest(RequestCode code) {
        this.code = code;
        data = new HashMap<>();
    }

    public Object getData(String key){
        return data.get(key);
    }

    public RequestCode getCode() {
        return code;
    }
    public void addData(String key , Object value){
        data.put(key,value);
    }
}
