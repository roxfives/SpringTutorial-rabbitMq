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
 *
 * */
@SpringBootApplication(scanBasePackageClasses = {Receiver.class, Runner.class})
public class Application {
    static final String topicExchangeName = "spring-boot-application";
    static final String queueName = "spring-boot";

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

    public static String getTopicExchangeName() {
        return Application.topicExchangeName;
    }

    public static String getQueueName() {
        return Application.queueName;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        System.out.println("Message received from listener: routing to receiver... ");
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
