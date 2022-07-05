package discord.discord1;

import java.io.Serializable;

/**
 * The RequestCode enum some code that assign to a request in client and send to server.
 * server do something based on this code.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public enum RequestCode implements Serializable {
    CHECK_USERNAME_DUPLICATION,
    ADD_USER,
    FIND_USER_BY_USERNAME,
    ADD_REQUEST,
    ADD_FRIEND,
    ADD_SERVER,
    CHECK_SERVER_NAME_DUPLICATION,
    REMOVE_SERVER,
    UPDATE_CLIENT_USER,
    SEE_REQUEST,
    ACCEPT_REQUEST_FRIEND,
    SEND_REQUEST,
    SEE_FRIENDS,
    CHANGE_STATUS,
    BLOCK_FRIEND,
    SEE_BLOCKED_FRIEND,
    EXIT,
    LOG_OUT,
    LOG_IN,
    IS_EXISTS,
    OFFLINE_ACTIVE_USER,
    LOG_IN_ONCE,
    CHECK_PASSWORD,
    CHANGE_PASSWORD,
    UNBLOCK_FRIEND,
    SHOW_SERVERS,
    IS_SERVER_ADMIN,
    CHANGE_SERVER_NAME,
    CHECK_ROLE,
    ASSIGN_ROLE,
    ROLL_AND_PERMISSIONS,
    REMOVE_MEMBER,
    CHECK_CHANNEL_COUNT,
    IS_CHANNEL_EXIST,
    LIMIT_MEMBER,
    BAN_MEMBER,
    SEE_MEMBERS_LIST,
    CREATE_CHANNEL,
    REMOVE_CHANNEL,
    SEE_LIMITED_MEMBERS,
    SEE_BANNED_MEMBERS,
    ROLE_PERMISSIONS,
    CHANGE_PERMISSION_ADD,
    CHANGE_PERMISSION_REMOVE,
    GET_PERMISSIONS,
    ADD_MEMBER,
    SEE_CHANNELS,
    REMOVE_MEMBER2,
    SEE_PINNED_MESSAGE,
    SHOW_MESSAGES,
    CAN_PIN,
    PIN_MESSAGE,
    SEE_REACTIONS,
    SHOW_ALL_MESSAGES,
    CAN_REACT,
    REMOVE_OLD_REACT,
    ADD_REACT,
    SEE_PVs,
    SEE_PV,
    SEND_MESSAGE,
    CLOSE_CHAT,
    ENTER_CHANNEL,

    SEND_MESSAGE_IN_CHANNEL,

    CLOSE_CHANNEL,
    SEND_FILE_IN_CHANNEL,

    SEND_FILE_IN_CHAT,

    SEE_PV_TO_SEND_FILE,
    SEND_PICTURE,
    SEE_RECEIVED_FILES,


    SAVE_FILE,

    SEE_SAVED_FILES
}
