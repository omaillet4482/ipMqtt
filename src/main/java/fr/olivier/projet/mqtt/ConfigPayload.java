package fr.olivier.projet.mqtt;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection 
public class ConfigPayload {
    
    String name;


    String state_topic;

    String command_topic;

    DevicePayload device;

    String unique_id;

    String payload_off = "0";

    String payload_on = "1";

    String value_template = "{{ value_json.value }}";

    public String getName() {
        return name;
    }

    public String getValue_template() {
        return value_template;
    }

    public String getState_topic() {
        return state_topic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue_template(String value_template) {
        this.value_template = value_template;
    }

    public void setState_topic(String state_topic) {
        this.state_topic = state_topic;
    }

    public DevicePayload getDevice() {
        return device;
    }

    public void setDevice(DevicePayload device) {
        this.device = device;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getPayload_off() {
        return payload_off;
    }

    public void setPayload_off(String payload_off) {
        this.payload_off = payload_off;
    }

    public String getPayload_on() {
        return payload_on;
    }

    public void setPayload_on(String payload_on) {
        this.payload_on = payload_on;
    }

    public String getCommand_topic() {
        return command_topic;
    }

    public void setCommand_topic(String command_topic) {
        this.command_topic = command_topic;
    }

    
    
}
