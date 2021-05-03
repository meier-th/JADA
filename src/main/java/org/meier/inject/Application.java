package org.meier.inject;

public class Application {

    public static void run() {
        try {
            RunnerConfigurator config = new RunnerConfigurator();
            config.configureRunner();
        } catch (Exception error) {
            System.out.println("Dependency injection error");
            System.out.println(error.getMessage());
            throw new RuntimeException(error);
        }
    }

}
