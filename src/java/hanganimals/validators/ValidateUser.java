package hanganimals.validators;

import hanganimals.rest.LoginResource;
import hanganimals.models.User;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author BigBaws
 */
public class ValidateUser {
    
    public static boolean validateToken(String token) {
        boolean tokenAccepted = false;
        for(String key : LoginResource.users.keySet()) {
            User entry = LoginResource.users.get(key);
            if (entry.token.equals(token)) {
                tokenAccepted = true;
            }
        }
        return tokenAccepted;
    }
    
    public static boolean validateTokenUserid(String token, String userid) {
        User entry = LoginResource.users.get(userid);
        return entry.token.equals(token);
    }
    
    public static void checkToken(String token, String userid) {
        if(!ValidateUser.validateTokenUserid(token, userid))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
    
    public static void checkToken(String token) {
        if(!ValidateUser.validateToken(token))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
    
    
    
}
