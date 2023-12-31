package fr.olivier.projet.mqtt;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection 
public class ConfigPayload {
    
    String name;

    String value_template; 

    String state_topic;

    DevicePayload device;

    String unique_id;

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

    
    
}
