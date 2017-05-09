package hanganimals;

import com.example.UserResource;

public class User {

    public String name, userid, image, singleplayer, multiplayer, study, animal, animalcolor, token;
    public int currency;
    
    public User() {
        throw new UnsupportedOperationException("Not supported function.");
    }

    public User(String name, String userid, String image, String studeretning, int currency, String singleplayer, String multiplayer, String animal, String animalcolor, String token) {
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
    }

    public boolean validte(String token) {
        if (token.equals(this.token)) {
            return true;
        }
        return false;
    }

}