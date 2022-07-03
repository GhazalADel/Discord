package statics;

import java.io.Serializable;

/**
 * The Reaction enum contains 3 reactions that  assign to messages in channels.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public enum Reaction implements Serializable {
    LIKE,
    DISLIKE,
    SMILE
}
