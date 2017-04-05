/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author vich
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
        resources.add(com.example.MultiPlayerLogicResource.class);
        resources.add(com.example.PaymentResource.class);
        resources.add(com.example.SinglePlayerLogicResource.class);
        resources.add(com.example.UserResource.class);
    }
    
}
