package fr.olivier.projet.mqtt;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import fr.olivier.projet.bo.NmapBo;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped  
public class DomoticMqttService {
    

    @Inject
    @Channel("out-topic")
    Emitter<ConfigPayload> emitter;

        @Inject
    @Channel("out-topic")
    Emitter<String> emitterState;

    public void sendMessage(String topic, String message) {

        emitterState.send(MqttMessage.of(topic, message));
    }

    public void sendMessageConfig(NmapBo nmapBo) {


        emitter.send(
            MqttMessage.of("ipmqtt/switch/"+nmapBo.getName()
            +"/switch/config", 
            builderFromNmap(nmapBo),
            MqttQoS.AT_LEAST_ONCE)
            );
    }

    protected ConfigPayload builderFromNmap(NmapBo nmapBo) {
        ConfigPayload configMessage = new ConfigPayload();
        configMessage.setName(nmapBo.getName());
        configMessage.setUnique_id(nmapBo.getMac());
        configMessage.setState_topic("ipmqtt/"+nmapBo.getName()+"_switch/switch_binary/endpoint_0/currentValue");

        DevicePayload device = new DevicePayload();
        device.setName(nmapBo.getName());
        device.setModel("SWITCH");
        configMessage.setDevice(device);

        return configMessage;
    }


}
