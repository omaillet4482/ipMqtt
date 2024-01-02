package fr.olivier.projet.mqtt;


import java.util.UUID;

import org.jboss.logging.Logger;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import fr.olivier.projet.bo.NmapBo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped  
public class DomoticMqttService {
    
     private static final Logger LOG = Logger.getLogger(DomoticMqttService.class);
  

    public void sendMessage(NmapBo nmapBo, String value) {
        
        String topicCmd = 
        "ipmqtt/"+nmapBo.getName()+"_switch/switch_binary/endpoint_0/currentValue";


         Mqtt5BlockingClient client = Mqtt5Client.builder()
        .identifier(UUID.randomUUID().toString())
        .serverHost("192.168.1.150")
        .buildBlocking();

client.connect();
client.publishWith().topic(topicCmd).qos(MqttQos.AT_LEAST_ONCE).payload(value.getBytes()).send();
client.disconnect();   
        
    }

    public void sendMessageConfig(NmapBo nmapBo) {

      

         String topic = "homeassistant/switch/"+nmapBo.getName()
            +"/switch/config";

            Jsonb builder = JsonbBuilder.create();
            String json = builder.toJson(builderFromNmap(nmapBo));
            LOG.info(json);

           Mqtt5BlockingClient client = Mqtt5Client.builder()
        .identifier(UUID.randomUUID().toString())
        .serverHost("192.168.1.150")
        .buildBlocking();

client.connect();
client.publishWith().topic(topic).qos(MqttQos.AT_LEAST_ONCE).payload(json.getBytes()).send();
client.disconnect();     
    }

    protected ConfigPayload builderFromNmap(NmapBo nmapBo) {
        String topic = "ipmqtt/"+nmapBo.getName()+"_switch/switch_binary/endpoint_0/currentValue";
        ConfigPayload configMessage = new ConfigPayload();
        configMessage.setName(nmapBo.getName());
        configMessage.setUnique_id(nmapBo.getMac());
        configMessage.setState_topic(topic);
        configMessage.setCommand_topic(topic + "/set");
        DevicePayload device = new DevicePayload();
        device.setName(nmapBo.getName());
        device.setModel("SWITCH");
        configMessage.setDevice(device);

        return configMessage;
    }


}
