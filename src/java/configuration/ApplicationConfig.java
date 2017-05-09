package configuration;

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
        resources.add(configuration.ServiceResource.class);
        resources.add(hanganimals.rest.AnimalResource.class);
        resources.add(hanganimals.rest.ChatResource.class);
        resources.add(hanganimals.rest.HighscoreResource.class);
        resources.add(hanganimals.rest.LogicResource.class);
        resources.add(hanganimals.rest.LoginResource.class);
        resources.add(hanganimals.rest.MultiplayerLogicResource.class);
        resources.add(hanganimals.rest.MultiplayerResource.class);
        resources.add(hanganimals.rest.PaymentResource.class);
        resources.add(hanganimals.rest.ServiceResource.class);
        resources.add(hanganimals.rest.SingleplayerLogicResource.class);
        resources.add(hanganimals.rest.SingleplayerResource.class);
        resources.add(hanganimals.rest.UserResource.class);
    }
    
}
