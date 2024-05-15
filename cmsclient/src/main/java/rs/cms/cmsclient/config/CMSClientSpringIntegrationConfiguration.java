package rs.cms.cmsclient.config;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.core.Mqttv5ClientManager;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class CMSClientSpringIntegrationConfiguration {

    @Bean
    public ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager() {
        MqttConnectionOptions connectionOptions = new MqttConnectionOptions();
        connectionOptions.setServerURIs(new String[]{ "tcp://104.248.202.167:1883"});
        connectionOptions.setConnectionTimeout(30000);
        connectionOptions.setMaxReconnectDelay(1000);
        connectionOptions.setAutomaticReconnect(true);
        return new Mqttv5ClientManager(connectionOptions, "client1");
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttTransactionStatusTopic(ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager) {
        Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
                new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, "TS/#");

        messageProducer.setOutputChannel(mqttInputChannel());
        return messageProducer;
    }

    @Bean
    public MessageProducer mqttTransactionsTopic(ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager) {
        Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
                new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, "gm_meters/#");

        messageProducer.setOutputChannel(mqttInputChannel());
        return messageProducer;
    }

    @Bean
    public MessageProducer mqttMeterReportTopic(ClientManager<IMqttAsyncClient, MqttConnectionOptions> clientManager) {
        Mqttv5PahoMessageDrivenChannelAdapter messageProducer =
                new Mqttv5PahoMessageDrivenChannelAdapter(clientManager, "meters/#");

        messageProducer.setOutputChannel(mqttInputChannel());
        return messageProducer;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
            log.info("Message {} from topic {}", message, topic);
        };
    }
}
