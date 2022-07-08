package discord.discord1;



import java.io.Serializable;
import java.util.HashMap;

/**
 * ChannelMessage class represents a message in channel
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */
public class ChannelMessage extends Message implements Serializable {

    //fields

    private HashMap<Reaction,Integer> reactions;
    private HashMap<User,Reaction> userReaction;


    //constructor
    public ChannelMessage(String text, User sender) {
        super(text, sender);
        reactions=new HashMap<>();
        userReaction=new HashMap<>();
        reactions.put(Reaction.LIKE,0);
        reactions.put(Reaction.DISLIKE,0);
        reactions.put(Reaction.SMILE,0);
    }

    //getter methods

    /**
     * This method is used to access Hashmap<Reaction,Integer> and we can access count of reactions with that
     *@param -
     *@return HashMap<Reaction, Integer> This returns reactions
     */
    public HashMap<Reaction, Integer> getReactions() {
        return reactions;
    }

    /**
     * This method is used to access Hashmap<User,Reaction> and we can access users's reaction with that
     *@param -
     *@return HashMap<User, Reaction> This returns userReaction
     */
    public HashMap<User, Reaction> getUserReaction() {
        return userReaction;
    }

    //setter method

    /**
     * This method is used to add user's action into hashmap
     *@param userReaction
     *@return Nothing
     */
    public void setUserReaction(HashMap<User, Reaction> userReaction) {
        this.userReaction = userReaction;
    }
    /**
     * This method is used to get text of a message in channel
     *@param -
     *@return String
     */

    public String getText() {
        return super.getText();
    }
    /**
     * toString method overrided for creating specific format
     *@param -
     *@return String
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
