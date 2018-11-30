package sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import run.Application;

import java.util.Scanner;

/**
 * A testing class to send a messages to the topic "spring-boot-application". This class is only
 * used to send testing messages to the topic created by the application. It isn't necessary for
 * the application itself.
 * */
@Component
public class Runner implements CommandLineRunner {
    // The object used for sending the message synchronously
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor used to give access to the necessary objects for sending the message and
     * for confirming its arrival
     *
     * @param rabbitTemplate
     * */
    public Runner(RabbitTemplate rabbitTemplate) {
        // The object used for sending the message synchronously
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * This method sends the testing message to the topic "spring-boot-application"
     *
     * @param args array of messages to be sent
     * */
    @Override
    public void run(String... args) throws Exception {
        String message = "";
        Scanner scan = new Scanner(System.in);

        while(scan.hasNext()) {
            System.out.println("Sending message...");

            rabbitTemplate.convertAndSend(Application.getTopicExchangeName(),
                    "foo.bar.baz",
                    scan.nextLine());
        }
    }
}
