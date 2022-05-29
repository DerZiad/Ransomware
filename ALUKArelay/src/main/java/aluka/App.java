package aluka;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Thread thread = new Thread(ServerRelay.getInstance());
        thread.start();
    }
}
