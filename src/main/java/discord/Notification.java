package discord;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Notification class used to creating notification with message and time of creation
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class Notification implements Serializable {

    //fields
    private String text;
    private LocalDateTime time;

    //constructor
    public Notification(String text, LocalDateTime time) {
        this.text = text;
        this.time = time;
    }

    /**
     * toString method overrided for creating specific format
     *@param -
     *@return String
     */
    @Override
    public String toString() {
        return text+" "+time;
    }
}
