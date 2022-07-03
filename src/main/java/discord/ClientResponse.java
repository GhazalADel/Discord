package discord;

import statics.ResponseCode;

import java.util.HashMap;

public class ClientResponse {

    private ResponseCode code;
    private HashMap<String,Object> data;

    public ClientResponse(ResponseCode code) {
        this.code = code;
        data=new HashMap<>();
    }

    public ResponseCode getCode() {
        return code;
    }

    public Object getData(String key){
        return data.get(key);
    }

    public void addData(String key , Object value){
        data.put(key,value);
    }
}
