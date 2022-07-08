package discord.discord1;

import java.io.Serializable;

/**
 * The Permission enum contains 8 permissions.in server,admin can create roles and select some permissions.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */


public enum Permission implements Serializable {

    CREATE_CHANNEL,
    REMOVE_CHANNEL,
    REMOVE_MEMBER_FROM_SERVER,
    LIMIT_MEMBER_MESSAGE,
    CHANGING_SERVER_NAME,
    SEE_CHAT_HISTORY,
    PIN_MESSAGE,
    BAN_A_MEMBER
}
