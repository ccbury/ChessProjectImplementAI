import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AIAgent extends JFrame {
    Random rand;

    public AIAgent() {
        rand = new Random();
    }

    public Move randomMove(Stack possibilities) {
        int moveID = rand.nextInt(possibilities.size());
        //System.out.println("Agent randomly selected move : "+moveID);
        for (int i = 0; i < (possibilities.size() - (moveID)); i++) {
            Move moved = (Move) possibilities.pop();
        }
        Move selectedMove = (Move) possibilities.pop();
        System.out.println("A random move was selected");
        return selectedMove;
    }

    public Move nextBestMove(Stack possibilities) {
        Move newMove;
        Move selectedMove = new Move();
        Stack moves = new Stack();
        Stack backupPoss = (Stack) possibilities.clone();
        int points = 0;
        while (!possibilities.isEmpty()) {
            newMove = (Move) possibilities.pop();
            Square s2 = (Square) newMove.getStart();
            Square s1 = (Square) newMove.getLanding();
            int x = s1.getXC();
            int y = s1.getYC();
            String tmp1 = s1.getName();
            if (tmp1.contains("Pawn") && points <= 2) {
                if (points != 2) {
                    moves.clear();
                }
                points = 2;
                moves.add(newMove);
            } else if (tmp1.contains("Bishop") && points <= 3) {
                if (points != 3) {
                    moves.clear();
                }
                points = 3;
                moves.add(newMove);
            } else if (tmp1.contains("King")) {
                if (points != 100) {
                    moves.clear();
                }
                points = 100;
                moves.add(newMove);
            } else if (tmp1.contains("Queen") && points <= 9) {
                if (points != 9) {
                    moves.clear();
                }
                points = 9;
                moves.add(newMove);
            } else if (tmp1.contains("Rook") && points <= 5) {
                if (points != 5) {
                    moves.clear();
                }
                points = 5;
                moves.add(newMove);
            } else if (tmp1.contains("Knight") && points <= 3) {
                if (points != 3) {
                    moves.clear();
                }
                points = 3;
                moves.add(newMove);
            } else if (tmp1.isEmpty() && (y == 3 || y == 4) && (points <= 1)) {
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
            System.out.println("The next best move was selected");
            return selectedMove;
        } else {
            int moveID = rand.nextInt(backupPoss.size());
            for (int i = 0; i < (backupPoss.size() - (moveID)); i++) {
                backupPoss.pop();
            }
            selectedMove = (Move) backupPoss.pop();
            System.out.println("A random move was selected. no moves with value discovered");
            return selectedMove;
        }
    }

    public Move twoLevelsDeep(Stack possibilities) {
        Stack backUp = (Stack) possibilities.clone();
        Stack playerMoves = new Stack();
        int finalMoveScore;
        int savedMoveScore=0;
        Move AIMove;
        Move PlayerMove;
        Move selectedMove = new Move();
        Stack bestTwoLevelMoves = new Stack();

        while(!possibilities.isEmpty()){
            AIMove = (Move)possibilities.pop();
            finalMoveScore = getMoveScore(AIMove.getLanding().getName(), AIMove.getLanding().getYC());
            //System.out.println("AIMOVE SCORE: "+finalMoveScore);
            if(ChessProject.boardSide.contains("White")){
                playerMoves = findWhitePieces();
            }else if (ChessProject.boardSide.contains("Black")){
                playerMoves = findBlackPieces();
            }
            while(!playerMoves.isEmpty()){
                PlayerMove = (Move)playerMoves.pop();
                int tempo = finalMoveScore - getMoveScore(PlayerMove.getLanding().getName(), PlayerMove.getLanding().getYC());
                //System.out.println("AIMOVE SCORE MINUS PLAYER SCORE: "+finalMoveScore);
                if(tempo>=savedMoveScore){
                    if(tempo!=savedMoveScore && tempo!=0){
                        //System.out.println("Moves cleared");
                        bestTwoLevelMoves.clear();
                    }
                    savedMoveScore = finalMoveScore;
                    bestTwoLevelMoves.add(AIMove);
                   // System.out.println("Move added");
                }
            }
        }
        if (!bestTwoLevelMoves.empty()) {
            int moveID = rand.nextInt(bestTwoLevelMoves.size());
            for (int i = 1; i < (bestTwoLevelMoves.size() - (moveID)); i++) {
                bestTwoLevelMoves.pop();
            }
            selectedMove = (Move) bestTwoLevelMoves.pop();
            System.out.println("A two level deep move was selected");
            return selectedMove;
        } else {
            int moveID = rand.nextInt(backUp.size());
            Stack possibilitiesClone = backUp;
            for (int i = 0; i < (backUp.size() - (moveID)); i++) {
                possibilitiesClone.pop();
            }
            selectedMove = (Move) possibilitiesClone.pop();
            System.out.println("A random move was selected as no valuable moves were discovered");
            return selectedMove;
        }


    }


    public int getMoveScore(String tmp1, int y) {

        int points = 0;
        if (tmp1.contains("Pawn")) {
            points = 2;
        }
        if (tmp1.contains("Bishop")) {
            points = 3;
        }
        if (tmp1.contains("King")) {
            points = 100;
        }
        if (tmp1.contains("Queen")) {
            points = 9;
        }
        if (tmp1.contains("Rook")) {
            points = 5;
        }
        if (tmp1.contains("Knight")) {
            points = 3;
        }
        if (tmp1.isEmpty() && (y == 3 || y == 4)) {
            points = 1;
        }
        return points;
    }

    private Stack findWhitePieces() {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        JLabel chessPiece;
        for (int i = 0; i < 600; i += 75) {
            for (int j = 0; j < 600; j += 75) {
                y = i / 75;
                x = j / 75;
                Component tmp = ChessProject.chessBoard.findComponentAt(j, i);
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("White")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        Stack moves = new Stack();
        Stack completeMoves = new Stack();
        while(!squares.isEmpty()){
            Square startOfMove = (Square)squares.pop();
            String movePieceName = startOfMove.getName();
            if (movePieceName.contains("Knight")) {
                moves = ChessProject.getKnightMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("Bishop")) {
                moves = ChessProject.getBishopMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("BlackPawn")) {
                //  System.out.println("GETTING BPawn MOVES");
                moves = ChessProject.getBlackPawnSquares(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("WhitePawn")) {
                // System.out.println("GETTING WPawn MOVES");
                moves = ChessProject.getWhitePawnSquares(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("Rook")) {
                moves = ChessProject.getRookMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("Queen")) {
                moves = ChessProject.getQueenMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("King")) {
                moves = ChessProject.getKingSquares(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            }
            while(!moves.isEmpty()){
                Move temp = (Move) moves.pop();
                completeMoves.push(temp);
            }
        }
        return completeMoves;
    }

    private Stack findBlackPieces() {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        JLabel chessPiece;
        for (int i = 0; i < 600; i += 75) {
            for (int j = 0; j < 600; j += 75) {
                y = i / 75;
                x = j / 75;
                Component tmp = ChessProject.chessBoard.findComponentAt(j, i);
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("Black")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        Stack moves = new Stack();
        Stack completeMoves = new Stack();
        while(!squares.isEmpty()){
            Square startOfMove = (Square)squares.pop();
            String movePieceName = startOfMove.getName();
            if (movePieceName.contains("Knight")) {
                moves = ChessProject.getKnightMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("Bishop")) {
                moves = ChessProject.getBishopMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("BlackPawn")) {
                //  System.out.println("GETTING BPawn MOVES");
                moves = ChessProject.getBlackPawnSquares(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("WhitePawn")) {
                // System.out.println("GETTING WPawn MOVES");
                moves = ChessProject.getWhitePawnSquares(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("Rook")) {
                moves = ChessProject.getRookMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("Queen")) {
                moves = ChessProject.getQueenMoves(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            } else if (movePieceName.contains("King")) {
                moves = ChessProject.getKingSquares(startOfMove.getXC(), startOfMove.getYC(), startOfMove.getName());
            }
            while(!moves.isEmpty()){
                Move temp = (Move) moves.pop();
                completeMoves.push(temp);
            }
        }
        return completeMoves;
    }
}

