package fr.olivier.projet.mqtt;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DevicePayload {
    
    private String name;

    private String model;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
