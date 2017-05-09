package hanganimals.validators;

import hanganimals.rest.LoginResource;
import hanganimals.User;

/**
 *
 * @author BigBaws
 */
public class ValidateSingleplayer {
    
    public static boolean validateGame(String userid, String gameid) {
        boolean gameidAccepted = false;
        for(String key : LoginResource.users.keySet()) {
            User entry = LoginResource.users.get(key);
            if (entry.userid.equals(userid)) {
                // TODO: Validation
                gameidAccepted = true;
            }
        }
        return gameidAccepted;
    }

    public static boolean validateGuess(String letter) {
        boolean letterAccepted = false;
        if (letter != null && letter.length() == 1) {
            letterAccepted = true;
        }
        return letterAccepted;
    }
    
}
