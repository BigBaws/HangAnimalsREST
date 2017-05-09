package hanganimals.gamelogic;

import hanganimals.models.MultiplayerGame;
import hanganimals.models.MultiplayerUser;

public class MultiplayerLogic {
    
    public static String guessLetter(MultiplayerGame game, String userid, String letter) throws Exception {
        MultiplayerUser user = game.getUser(userid);
        if (user.usedletters.contains(letter)) {
            return "You have already guessed on "+letter;
        }
        user.usedletters.add(letter);
        if (game.word.contains(letter)) {
            user.lastLetterCorrect = true;
            user.gamescore++;
        } else {
            user.lastLetterCorrect = false;
            user.wrongs = user.wrongs + 1;
            if (user.wrongs >= 6) {
                user.Lost = true;
            }
        }
        return updateVisibleWord(game, userid);
    }
    
    public static String updateVisibleWord(MultiplayerGame game, String userid) throws Exception {
        MultiplayerUser user = game.getUser(userid);
        user.userword = "";
        for (int n = 0; n < game.word.length(); n++) {
            String letter = game.word.substring(n, n + 1);
            if (user.usedletters.contains(letter)) {
                user.userword = user.userword + letter;
            } else {
                user.userword = user.userword + "*";
                user.gamescore--;
            }
        }
        if (!user.userword.contains("*")) {
            int users = game.getNumberOfFinishedUsers();
            
            if(users == 0) {
                user.gamescore += 10;
                game.nextRound();
            } else if(users == 1) {
                user.gamescore += 5;
            } else if(users == 2) {
                user.gamescore += 2;
            }
            
            user.Won = true;
            game.gameIsWon = true;
            game.winner = user.userid;
            game.gameIsActive();
        }
        return user.userword;
    }

}
