package statics;

import java.io.Serializable;

/**
 * The ResponseCode enum some code that assign to a response in server and send to client.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */


public enum ResponseCode implements Serializable {
    Is_USERNAME_DUPLICATED,
    UPDATE_USER,
    SUCCESSFUL,
    SHOW_REQUEST,
    FRIEND_NOT_FOUND,
    BEFORE_FRIEND,
    BEFORE_REQUEST,
    REQUEST_OK,
    IS_EXISTS,
    SHOW_FRIENDS,
    OWN_REQUEST,
    SHOW_BLOCKED_FRIEND,
    REQUEST_AGAIN,
    SHOW_SERVERS,
    IS_SERVER_ADMIN,
    NOT_CHANGE,
    DUPLICATED,
    NAME_CHANGED,
    NOT_EXIST,
   BEFORE_IN_ROLE,
    ROLE_ASSIGNED,
    NOT_IN_SERVER,
    LIMIT_BEFORE,
    BANNED_BEFORE,
    LIMIT_MEMBER,
    BAN_MEMBER,
    SHOW_CHANNELS,
    ADMIN,
    SHOW_PINNED_MESSAGES,
    SHOW_MESSAGES,
    SHOW_REACTIONS,
    INVALID_USERNAME,
    YES,
    CLOSE_THREAD,
    MESSAGE_SENT,
    INVALID_INDEX,

    CANT_SEE_MESSAGES,
    CANT_SEND_MESSAGE,
    TRY_AGAIN,
    NO_FILE,

    SAVED_FILE,

    NO_SAVED_FILE

}
