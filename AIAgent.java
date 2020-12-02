import java.util.*;

public class AIAgent {
    Random rand;

    public AIAgent() {
        rand = new Random();
    }

    public Move randomMove(Stack possibilities) {

        int moveID = rand.nextInt(possibilities.size());
        //System.out.println("Agent randomly selected move : "+moveID);
        for (int i = 1; i < (possibilities.size() - (moveID)); i++) {
            possibilities.pop();
        }
        Move selectedMove = (Move) possibilities.pop();
        return selectedMove;
    }

    public Move nextBestMove(Stack possibilities) {
        Move newMove;
        Move selectedMove = new Move();
        Stack moves = new Stack();
        Stack backupPoss = (Stack)possibilities.clone();
        int points = 0;
        for (int i = 0; i < backupPoss.size(); i++) {
            newMove = (Move) possibilities.pop();
            Square s2 = (Square) newMove.getStart();
            Square s1 = (Square) newMove.getLanding();
            int x = s1.getXC();
            int y = s1.getYC();
            String tmp1 = s1.getName();
            if (tmp1.contains("Pawn") && points < 2) {
                if (points != 2) {
                    moves.clear();
                }
                points = 2;
                moves.add(newMove);
            }
            if (tmp1.contains("Bishop") && points < 3) {
                if (points != 3) {
                    moves.clear();
                }
                points = 3;
                moves.add(newMove);
            }
            if (tmp1.contains("King")) {
                if (points != 100) {
                    moves.clear();
                }
                points = 100;
                moves.add(newMove);
            }
            if (tmp1.contains("Queen") && points < 9) {
                if (points != 9) {
                    moves.clear();
                }
                points = 9;
                moves.add(newMove);
            }
            if (tmp1.contains("Rook") && points < 5) {
                if (points != 5) {
                    moves.clear();
                }
                points = 5;
                moves.add(newMove);
            }
            if (tmp1.contains("Knight") && points < 3) {
                if (points != 3) {
                    moves.clear();
                }
                points = 3;
                moves.add(newMove);
            }
            if (tmp1.isEmpty() && (y == 3 || y == 4) && (points < 1)) {
                points = 1;
                moves.add(newMove);
            }
        }
        if (!moves.empty()) {
            int moveID = rand.nextInt(moves.size());
            for (int i = 1; i < (moves.size() - (moveID)); i++) {
                moves.pop();
            }
            selectedMove = (Move) moves.pop();
            return selectedMove;
        } else {
            int moveID = rand.nextInt(backupPoss.size());
            for (int i = 1; i < (backupPoss.size() - (moveID)); i++) {
                backupPoss.pop();
            }
            selectedMove = (Move) backupPoss.pop();
            return selectedMove;
        }
    }

    public Move twoLevelsDeep(Stack possibilities) {
        Move selectedMove = new Move();
        return selectedMove;
    }
}
