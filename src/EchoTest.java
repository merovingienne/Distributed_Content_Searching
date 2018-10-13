import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chanuka on 10/12/18.
 */
public class EchoTest {
    public static void main(String[] args) {
        String input = "";
        boolean running = true;

        new EchoServer().start();
        EchoClient client = new EchoClient();

        System.out.println("Echo Test Program started.");
        System.out.println("Enter your message below.");

//        Console console = System.console();
//        if (console == null) {
//            System.out.println("No console: non-interactive mode!");
//            System.exit(0);
//        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (running){
            System.out.print(">>> ");
            try {
//            	input = console.readLine();
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (input.equalsIgnoreCase("end")){
                running = false;
                System.out.println("Shutting down test.");
            } else {
                String returnedMsg = client.sendEcho(input);
                System.out.println("Returned message: " + returnedMsg);
            }
        }

        client.sendEcho("end");
        client.close();
        System.out.println("Test shut down.");
        System.exit(0);
    }
}
