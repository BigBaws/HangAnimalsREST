package hanganimals;

import com.example.UserResource;
import java.util.ArrayList;

public class User {

    public String name, userid, image, study, animal, animalcolor, token;
    public int currency, singleplayer, multiplayer;
    
    public User() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public User(String name, String userid, String image, String studeretning, int currency, int singleplayer, int multiplayer, String animal, String animalcolor, String token) {
        this.name = name;
        this.userid = userid;
        this.image = image;
        this.study = studeretning;
        this.currency = currency;
        this.singleplayer = singleplayer;
        this.multiplayer = multiplayer;
        this.animal = animal;
        this.animalcolor = animalcolor;
        this.token = token;
        UserResource.onlineUsers.add(this);
    }
    

}
