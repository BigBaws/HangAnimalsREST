package hanganimals.validators;

import hanganimals.rest.LoginResource;
import hanganimals.User;

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
        boolean tokenAccepted = false;
        for(String key : LoginResource.users.keySet()) {
            User entry = LoginResource.users.get(key);
            if (entry.token.equals(token)) {
                if (entry.userid.equals(userid)) {
                    tokenAccepted = true;
                }
            }
        }
        return tokenAccepted;
    }
    
}
