package receiver;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * The class that receives the message from tue queue
 * */
@Component
public class Receiver {
    /**
     * The method that should be called to deliver the message to an
     * instance of this class
     *
     * @param message the message to be delivered to the object
     * */
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
//        latch.countDown(); // A workaround to warn the application the message has been received
    }
}
