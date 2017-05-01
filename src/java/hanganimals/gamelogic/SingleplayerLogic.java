package hanganimals.gamelogic;

import hanganimals.SingleplayerGame;

public class SingleplayerLogic {
    
    public static String guessLetter(SingleplayerGame game, String userid, String letter) {
        if (game.usedletters.contains(letter)) {
            return "You have already guessed on "+letter;
        }
        game.usedletters.add(letter);
        if (game.word.contains(letter)) {
            if (game.lastLetterCorrect == true) {
                game.combo++;
            }
            game.lastLetterCorrect = true;
            game.gamescore++;
        } else {
            game.lastLetterCorrect = false;
            game.combo = 0;
            game.wrongs = game.wrongs + 1;
            if (game.wrongs >= 6) {
                game.gameIsLost = true;
            }
        }
        return updateVisibleWord(game, userid);
    }
    
    public static String updateVisibleWord(SingleplayerGame game, String userid) {
        game.userword = "";
        for (int n = 0; n < game.word.length(); n++) {
            String letter = game.word.substring(n, n + 1);
            if (game.usedletters.contains(letter)) {
                game.userword = game.userword + letter;
            } else {
                game.userword = game.userword + "*";
            }
        }
        if (!game.userword.contains("*")) {
            game.gamescore += 10;
            game.gameIsWon = true;
        }
        return game.userword;
    }

}
