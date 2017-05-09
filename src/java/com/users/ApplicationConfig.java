package com.users;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author BigBaws
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.example.AnimalResource.class);
        resources.add(com.example.ChatResource.class);
        resources.add(com.example.HighscoreResource.class);
        resources.add(com.example.LogicResource.class);
        resources.add(com.example.LoginResource.class);
        resources.add(com.example.MultiplayerLogicResource.class);
        resources.add(com.example.MultiplayerResource.class);
        resources.add(com.example.PaymentResource.class);
        resources.add(com.example.ServiceResource.class);
        resources.add(com.example.SingleplayerLogicResource.class);
        resources.add(com.example.SingleplayerResource.class);
        resources.add(com.example.UserResource.class);
        resources.add(com.users.ServiceResource.class);
    }
    
}
