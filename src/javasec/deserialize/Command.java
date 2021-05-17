package javasec.deserialize;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public class Command implements Runnable, Serializable {

    private String command;

    public Command(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            String result = scanner.hasNext()?scanner.next():"";
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
