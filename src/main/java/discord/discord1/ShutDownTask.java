package discord.discord1;

/**
 * when user terminate the program this thread will start.
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class ShutDownTask extends Thread {

    @Override
    public void run() {
      Client.offlineActiveUser();
    }
}
