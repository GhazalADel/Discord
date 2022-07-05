package discord.discord1;

import java.io.Serializable;

/**
 * The Status enum contains 5 status that used as users's status in the program
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */


public enum Status implements Serializable {
    ONLINE,
    OFFLINE,
    IDLE,
    DO_NOT_DISTURB,
    INVISIBLE
}
