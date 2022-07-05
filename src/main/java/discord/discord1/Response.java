package discord.discord1;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Response class represents a response that send form server to client
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class Response implements Serializable {

    //fields
    private ResponseCode code;
    private HashMap<String,Object> data;

    //constructor
    public Response(ResponseCode code) {
        this.code = code;
        data=new HashMap<>();
    }

    //getter methods
    /**
     * This method is used to access code of a response.
     *@param -
     *@return ResponseCode This returns code
     */
    public ResponseCode getCode() {
        return code;
    }
    /**
     * This method is used to access value of a specific key in data by receiving key.
     *@param key
     *@return Object
     */
    public Object getData(String key){
        return data.get(key);
    }

    /**
     * This method is used to add an element to data by giving key and value.
     *@param key
     *@param value
     *@return Nothing
     */
    public void addData(String key , Object value){
        data.put(key,value);
    }
}
