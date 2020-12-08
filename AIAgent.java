import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Stack;

public class AIAgent extends JFrame {
    //Random variable created to get a random int to get random move
    Random rand;

    public AIAgent() {
        rand = new Random();
    }//End AIAgent

    public Move randomMove(Stack possibilities) {
        int moveID = rand.nextInt(possibilities.size());
        //System.out.println("Agent randomly selected move : "+moveID);
        for (int i = 0; i < (possibilities.size() - (moveID)); i++) {
            Move moved = (Move) possibilities.pop();
        }
        Move selectedMove = (Move) possibilities.pop();
        System.out.println("A random move was selected");
        return selectedMove;
    }//End randomMove method

    public Move nextBestMove(Stack possibilities) {
        //newMove is a move variable. it is used to take the Move from the stack to be processed.
        Move newMove;
        //selected move is the returned move that will be used.
        Move selectedMove = new Move();
        Stack moves = new Stack();
        //Create backup of moves. this allows a random move to be selected if no valuable moves are found
        Stack backupPoss = (Stack) possibilities.clone();
        //Create the points variable. set to 0 to begin with
        int points = 0;
        //while the stack of possibilities is not empty. continue the loop
        while (!possibilities.isEmpty()) {
            //Take move from stack and assign its values to variables. for instance name, x and y ect.
            newMove = (Move) possibilities.pop();
            Square s2 = (Square) newMove.getStart();
            Square s1 = (Square) newMove.getLanding();
            int x = s1.getXC();
            int y = s1.getYC();
            String tmp1 = s1.getName();
            //If/else chain to search for highest value moves
            //If points <= means that if it is equal, the move will also be added to the stack
            if (tmp1.contains("Pawn") && points <= 2) {
                //If the points value is not 2 clear the list as this means the stack contains less valuable moves
                if (points != 2) {
                    moves.clear();
                }//End if
                //Set the points value to 2 for taking a pawn
                points = 2;
                //Add the move to the stack of moves
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
            }//End if/else chain to search for moves of highest value
        }//End while to search through stack of moves
        //if moves is not empty return a move from the stack. else select a random move
        if (!moves.empty()) {
            //Create a random int in the range of the stack
            int moveID = rand.nextInt(moves.size());
            for (int i = 1; i < (moves.size() - (moveID)); i++) {
                moves.pop();
            }//Remove the moves until the move you wish to make
            //Set the selected move to the random valued move.
            selectedMove = (Move) moves.pop();
            System.out.println("The next best move was selected");
            //Return the move
            return selectedMove;
        } else {
            int moveID = rand.nextInt(backupPoss.size());
            for (int i = 0; i < (backupPoss.size() - (moveID)); i++) {
                backupPoss.pop();
            }
            selectedMove = (Move) backupPoss.pop();
            System.out.println("A random move was selected. no moves with value discovered");
            return selectedMove;
        }//End if/else
    }//End nextBestMove method

    public Move twoLevelsDeep(Stack possibilities) {
        Stack backUp = (Stack) possibilities.clone();
        //New stack created for player moves. Move will be added later
        Stack playerMoves = new Stack();
        //Final move score is used to store the AI's movement points
        int finalMoveScore;
        //This is used to store the points of the AI move and responce
        int savedMoveScore = -100000;
        //Moves to store individual moves.
        Move AIMove;
        Move PlayerMove;
        //Selected move is what the twoLevelsDeep agent returns
        Move selectedMove = new Move();
        //This stack is used to store the highest value moves
        Stack bestTwoLevelMoves = new Stack();
        //While loop to search through the possible AI moves
        while (!possibilities.isEmpty()) {
            //Take a move from the stack and get teh score for that move.
            AIMove = (Move) possibilities.pop();
            finalMoveScore = getMoveScore(AIMove.getLanding().getName(), AIMove.getLanding().getYC());
            //discover which side of teh board the player is using with IF/Else
            if (ChessProject.boardSide.contains("White")) {
                playerMoves = findWhitePieces();
            } else if (ChessProject.boardSide.contains("Black")) {
                playerMoves = findBlackPieces();
            }//End If/Else to find which side of the board the user is playing on
            //Reset bestplayerMove so that it will be 0 for each loop
            int bestPlayerMove = 0;
            //While loop to find the best move for the player
            while (!playerMoves.isEmpty()) {
                PlayerMove = (Move) playerMoves.pop();
                int tempo = getMoveScore(PlayerMove.getLanding().getName(), PlayerMove.getLanding().getYC());
                if (tempo > bestPlayerMove) {
                    bestPlayerMove = tempo;
                }
            }//End While loop to find best player move
            int tempo = finalMoveScore - bestPlayerMove;
            if (tempo >= savedMoveScore) {
                if (tempo != savedMoveScore && tempo != 0) {
                    bestTwoLevelMoves.clear();
                }
                savedMoveScore = tempo;
                bestTwoLevelMoves.add(AIMove);
            }
        }

        int moveID = rand.nextInt(bestTwoLevelMoves.size());
        for (int i = 1; i < (bestTwoLevelMoves.size() - (moveID)); i++) {
            bestTwoLevelMoves.pop();
        }
        selectedMove = (Move) bestTwoLevelMoves.pop();
        System.out.println("A two level deep move was selected");
        return selectedMove;

    }//end twoLevelsDeep method

    public int getMoveScore(String tmp1, int y) {
        //Add points int.  set to 0 by default
        int points = 0;
        //Multiple if's to check for certain pieces or locations. each gives different points values.
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
        //Return the points value of that move
        return points;
    }//End getMoveScore method

    private Stack findWhitePieces() {
        //Find white Pieces returns a Stack of possible moves
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        JLabel chessPiece;
        //For loop to increment through y axis
        for (int i = 0; i < 600; i += 75) {
            //For loop to increment through x axis
            for (int j = 0; j < 600; j += 75) {
                y = i / 75;
                x = j / 75;
                Component tmp = ChessProject.chessBoard.findComponentAt(j, i);
                //If to check if component is a JLabel
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    //If to check if piece is White
                    if (pieceName.contains("White")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }//end if to check if piece is White
                }//End if to check if component is JLabel
            }//End for loop to increment x axis
        }//End for loop to increment y axis
        Stack moves = new Stack();
        Stack completeMoves = new Stack();
        while (!squares.isEmpty()) {
            Square startOfMove = (Square) squares.pop();
            String movePieceName = startOfMove.getName();
            //If/else to search for possible pieceMoves
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
            }//End if/else to search for possible moves
            //While to add moves to completeMoves stack
            while (!moves.isEmpty()) {
                Move temp = (Move) moves.pop();
                completeMoves.push(temp);
            }//End while to add moves to completeMoves
        }//End while to search for moves
        //return all possible moves
        return completeMoves;
    }//end findWhitePieces method

    private Stack findBlackPieces() {
        //Find blackPieces returns a Stack of possible moves
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        JLabel chessPiece;
        //increment through the y axis
        for (int i = 0; i < 600; i += 75) {
            //Increment through the x axis
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
                    }//End if to see if piece is Black
                }//end if to see if component if JLabel
            }//End for loop to increment x axis
        }//End for loop to increment y axis
        //Create 2 stacks. moves to hold the values from getMove methods and CompleteMoves to hold all moves.
        Stack moves = new Stack();
        Stack completeMoves = new Stack();
        //While to search for moves
        while (!squares.isEmpty()) {
            Square startOfMove = (Square) squares.pop();
            String movePieceName = startOfMove.getName();
            //If/else to search for possible pieceMoves
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
            }//End if/else to search for possible moves
            //While to add moves to completeMoves
            while (!moves.isEmpty()) {
                Move temp = (Move) moves.pop();
                completeMoves.push(temp);
            }//end while to add moves to completedMoves
        }//End while to search for moves
        //return stack of all possibleMoves
        return completeMoves;
    }//End findBlackPieces method

}//End AIAgent class

