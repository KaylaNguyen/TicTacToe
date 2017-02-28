package hu.ait.tictactoe.model;

/**
 * Created by khue on 2/23/17.
 */

public class TicTacToeModel {

    private static TicTacToeModel instance = null;

    private TicTacToeModel() {

    }

    public static TicTacToeModel getInstance() {
        if (instance == null) {
            instance = new TicTacToeModel();
        }

        return instance;
    }

    public static final short EMPTY = 0;
    public static final short CIRCLE = 1;
    public static final short CROSS = 2;

    private short[][] model = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    private short nextPlayer = CIRCLE;

    public short checkWinner() {
        short filledSquare = 0;
        for (int i = 0; i < model.length; i++) {
            if (model[i][0] == model[i][1] && model[i][0] == model[i][2] && model[i][0] != EMPTY){
                return model[i][0];
            }

            if (model[0][i] == model[1][i] && model[0][i] == model[2][i] && model[0][i] != EMPTY){
                return model[0][i];
            }

            for (int j = 0; j < model[i].length; j++) {
                if (model[i][j] != EMPTY){
                    filledSquare += 1;
                }
            }
        }

        if (model[0][0] == model[1][1] && model[0][0] == model[2][2]){
            return model[1][1];
        }
        if (model[2][0] == model[1][1] && model[2][0] == model[0][2]){
            return model[1][1];
        }

        if (filledSquare == model.length*model.length){
            return -1;
        }
        return EMPTY;
    }

    public void resetModel() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                model[i][j] = EMPTY;
            }
        }
        nextPlayer = CIRCLE;
    }

    public void makeMove(int x, int y, short player) {
        model[x][y] = player;
    }

    public short getFieldContent(int x, int y) {
        return model[x][y];
    }

    public short getNextPlayer() {
        return nextPlayer;
    }

    public void changeNextPlayer() {
        nextPlayer = (nextPlayer == CIRCLE) ? CROSS : CIRCLE;
    }
}
