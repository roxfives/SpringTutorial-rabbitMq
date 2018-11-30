package run;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import receiver.Receiver;
import sender.Runner;

/**
 * The class with the entry point of the application
 * */
@SpringBootApplication(scanBasePackageClasses = {Receiver.class, Runner.class})
public class Application {
    // The topic to which the receiver will subscribe
    static final String topicExchangeName = "spring-boot-application";
    // The queue through which the messages will be sent
    static final String queueName = "spring-boot";

    // The entry point
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Getter for the name of the topic to which the message should be sent
     *
     * @return    the name of the topic to which the receiver subscribes
     * */
    public static String getTopicExchangeName() {
        return Application.topicExchangeName;
    }

    /**
     * Getter for the name of the queue that receives the messages from the terminal
     *
     * @return    the name of the queue used to buff the messages to Receiver
     * */
    public static String getQueueName() {
        return Application.queueName;
    }

    /**
     * Bean Spring uses to create the queue for the "spring-boot-application" topic
     *
     * @return the queue for the "spring-boot-application" topic
     * */
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /**
     * Bean Spring uses to create the topic to which the Recevier subscribes
     *
     * @return the "spring-boot-application" topic
     * */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    /**
     * Bean Spring uses to bind the queue "spring-boot" with the topic "spring-boot-application"
     *
     * @return     the Binding object with the information describing the binding of the
     *               queue "spring-boot" with the topic "spring-boot-application"
     * */
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    /**
     * Bean Spring uses to create the listener for messages to be put in the queue "spring-boot"
     *
     * @param connectionFactory the object used to create the connection dor the JMS provider
     * @param listenerAdapter the adapter used to deliver the message from the queue to the Receiver
     *
     * @return    the object container with the configurations for the message listener
     * */
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    /**
     * Bean method used for sending the message to the Receiver object
     *
     * @param receiver the instance of Receiver that will receive the messages
     *
     * @return    the adapter responsible for sending the message from the queue to the Receiver instance
     * */
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        System.out.println("Message received from listener: routing to receiver... ");
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
