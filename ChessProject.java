import javax.swing.*;
import javax.swing.border.Border;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    static JLayeredPane layeredPane;
    static JPanel chessBoard;
    static String aiType;
    static String boardSide;
    boolean agentwins = false;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
    int startX;
    int startY;
    int initialX;
    int initialY;
    static JPanel panels;
    static JLabel pieces;
    Boolean aiWhiteMoved = false;
    //all of these variables are needed in multiple methods while maintaining their values.
    Boolean validMove = false;
    static String pieceName;
    Boolean success;
    Boolean turn = true;
    Boolean whiteKingMoved = false;
    Boolean blackKingMoved = false;
    Boolean lWhiteRookMoved = false;
    Boolean lBlackRookMoved = false;
    Boolean rWhiteRookMoved = false;
    Boolean rBlackRookMoved = false;

    public ChessProject() {
        Dimension boardSize = new Dimension(600, 600);
        setTitle("Chess Project - Conor Bury 18111092 - AI Agent CA4");
        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a chess board to the Layered Pane 
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            chessBoard.add(square);

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground(i % 2 == 0 ? Color.white : Color.darkGray);
            else
                square.setBackground(i % 2 == 0 ? Color.darkGray : Color.white);
        }

        // Setting up the Initial Chess board.
        for (int i = 8; i < 16; i++) {
            pieces = new JLabel(new ImageIcon("resources/WhitePawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("resources/WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(0);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(1);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(6);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteBishop.png"));
        panels = (JPanel) chessBoard.getComponent(2);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteBishop.png"));
        panels = (JPanel) chessBoard.getComponent(5);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteKing.png"));
        panels = (JPanel) chessBoard.getComponent(3);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteQueen.png"));
        panels = (JPanel) chessBoard.getComponent(4);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(7);
        panels.add(pieces);
        for (int i = 48; i < 56; i++) {
            pieces = new JLabel(new ImageIcon("resources/BlackPawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("resources/BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(56);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(57);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(62);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackBishop.png"));
        panels = (JPanel) chessBoard.getComponent(58);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackBishop.png"));
        panels = (JPanel) chessBoard.getComponent(61);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackKing.png"));
        panels = (JPanel) chessBoard.getComponent(59);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
        panels = (JPanel) chessBoard.getComponent(60);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("resources/BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(63);
        panels.add(pieces);
    }//End ChessProject Method

    private static Boolean piecePresent(int x, int y) {
        Component c = chessBoard.findComponentAt(x, y);
        if (c instanceof JPanel) {
            return false;
        } else {
            return true;
        }
    }

    public static String findPiece(int x, int y) {
        x = x * 75;
        y = y * 75;
        if (chessBoard.findComponentAt(x, y) instanceof JLabel) {
            Component c1 = chessBoard.findComponentAt(x, y);
            JLabel awaitingPiece = (JLabel) c1;
            return awaitingPiece.getIcon().toString();
        } else {
            return "";
        }
    }

    private static Boolean checkWhiteOponent(int newX, int newY) {
        Boolean oponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("Black")))) {
            oponent = true;
        } else {
            oponent = false;
        }
        return oponent;
    }//End checkWhiteOpponent

    private static Boolean checkBlackOponent(int newX, int newY) {
        Boolean oponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("White")))) {
            oponent = true;
        } else {
            oponent = false;
        }
        return oponent;
    }//End checkWhiteOpponent

    public void mousePressed(MouseEvent e) {
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel)
            return;

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (JLabel) c;
        initialX = e.getX();
        initialY = e.getY();
        startX = (e.getX() / 75);
        startY = (e.getY() / 75);
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
    }//End mousePressed

    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;
        chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
    }//End mouseDragged

    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) return;

        chessPiece.setVisible(false);
        success = false;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());

        String tmp = chessPiece.getIcon().toString();
        String[] pieceTmp = tmp.split("/");
        pieceName = pieceTmp[1];
        pieceName = pieceName.substring(0, (pieceName.length() - 4));

        validMove = false;

        if (boardSide.equals("White")) {
            switch (pieceName) {
                case "WhitePawn" -> whitePawnMove(e);
                case "WhiteKnight" -> knightMove(e);
                case "WhiteKing" -> kingMove(e);
                case "WhiteBishop" -> bishopMove(e);
                case "WhiteRook" -> rookMove(e);
                case "WhiteQueen" -> {
                    rookMove(e);
                    if (!validMove) {
                        bishopMove(e);
                    }//If no valid Rook move was made check for valid Bishop moves
                }
            }//End switch statement
        } else if (boardSide.equals("Black")) {
            switch (pieceName) {
                case "BlackPawn" -> blackPawnMove(e);
                case "BlackKnight" -> knightMove(e);
                case "BlackKing" -> kingMove(e);
                case "BlackBishop" -> bishopMove(e);
                case "BlackRook" -> rookMove(e);
                case "BlackQueen" -> {
                    rookMove(e);
                    if (!validMove) {
                        bishopMove(e);
                    }//If no valid Rook move was made check for valid Bishop moves
                }
            }//End switch statement
        }//End If / Else

        //This if statement checks that a valid move was returned. if not it places the piece back where it originated. if so the piece is moved to the new location.
        //This also handles pawns being turned into respective queens when reaching enemy back line
        if (!validMove) {
            int location = 0;
            if (startY == 0) {
                location = startX;
            } else {
                location = (startY * 8) + startX;
            }
            String pieceLocation = "resources/" + pieceName + ".png";
            pieces = new JLabel(new ImageIcon(pieceLocation));
            panels = (JPanel) chessBoard.getComponent(location);
            panels.add(pieces);
            try {
                noMoveSound();
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        } else {
            if ((success) && (pieceName.contains("White"))) {
                int location = 56 + (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("resources/WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                    try {
                        queenMakeSound();
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("resources/WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                    try {
                        queenMakeSound();
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                }
            } else if ((success) && (pieceName.contains("Black"))) {
                int location = (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                    try {
                        queenMakeSound();
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                    try {
                        queenMakeSound();
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                }
            } else {
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add(chessPiece);
                    try {
                        validMoveSound();
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                } else {
                    Container parent = (Container) c;
                    parent.add(chessPiece);
                    try {
                        validMoveSound();
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                }
                chessPiece.setVisible(true);
            }
        }// End if(validMove) / Else
        if (validMove) {
            makeAIMove();
        }
    }//End mouseRelease

    public void noMoveSound() throws MalformedURLException {
        File file = new File("resources/no.wav");
        URL url = file.toURI().toURL();
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }//End noMove sound

    public void validMoveSound() throws MalformedURLException {
        File file = new File("resources/valid.wav");
        URL url = file.toURI().toURL();
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }//End validMoveSound

    public void queenMakeSound() throws MalformedURLException {
        File file = new File("resources/queen.wav");
        URL url = file.toURI().toURL();
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }//End queenMakeSound

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {

    }

    public void whitePawnMove(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if (startY == 1) {
            if (((startX == (e.getX() / 75)) && ((((e.getY() / 75) - startY) == 1) || ((e.getY() / 75) - startY) == 2)) || ((newX == startX + 1) && (newY == startY + 1) && (checkWhiteOponent(e.getX(), e.getY()))) || ((newX == startX - 1) && (newY == startY + 1) && (checkWhiteOponent(e.getX(), e.getY())))) {
                if ((((e.getY() / 75) - startY) == 2)) {
                    if ((!piecePresent(e.getX(), (e.getY()))) && (!piecePresent(e.getX(), (e.getY() - 75)))) {
                        validMove = true;
                    } else {
                        validMove = false;
                    }
                } else {
                    if ((!piecePresent(e.getX(), (e.getY())))) {
                        validMove = true;
                    } else if ((checkWhiteOponent(e.getX(), e.getY())) && (startX != newX)) {
                        validMove = true;
                    } else {
                        validMove = false;
                    }
                }
            } else {
                validMove = false;
            }
        } else {
            if ((startX - 1 >= 0) || (startX + 1 <= 7)) {
                if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7) && (newY == startY + 1))) || ((newX == (startX - 1)) && (startX - 1 >= 0) && (newY == startY + 1))))) {
                    if (checkWhiteOponent(e.getX(), e.getY())) {
                        validMove = true;
                        if (startY == 6) {
                            success = true;
                        }
                    } else {
                        validMove = false;
                    }
                } else {
                    if (!piecePresent(e.getX(), (e.getY()))) {
                        if ((startX == (e.getX() / 75)) && ((e.getY() / 75) - startY) == 1) {
                            if (startY == 6) {
                                success = true;
                            }
                            validMove = true;
                        } else {
                            validMove = false;
                        }
                    } else {
                        validMove = false;
                    }
                }
            } else {
                validMove = false;
            }
        }
    }//End WhitePawn move method

    public void blackPawnMove(MouseEvent e) {
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if ((startY == 6) && (pieceName.contains("Black"))) {
            if (((startX == (e.getX() / 75)) && ((((e.getY() / 75) - startY) == -1) || ((e.getY() / 75) - startY) == -2)) || ((newX == startX + 1) && (newY == startY - 1) && (!checkWhiteOponent(e.getX(), e.getY()))) || ((newX == startX - 1) && (newY == startY - 1) && (!checkWhiteOponent(e.getX(), e.getY())))) {
                if ((((e.getY() / 75) - startY) == -2)) {
                    if ((!piecePresent(e.getX(), (e.getY()))) && (!piecePresent(e.getX(), (e.getY() + 75)))) {
                        validMove = true;
                    } else {
                        validMove = false;
                    }
                } else {
                    if ((!piecePresent(e.getX(), (e.getY())))) {
                        validMove = true;
                    } else if ((!checkWhiteOponent(e.getX(), e.getY())) && (startX != newX)) {
                        validMove = true;
                    } else {
                        validMove = false;
                    }
                }
            } else {
                validMove = false;
            }
        } else {
            if ((startX - 1 >= 0) || (startX + 1 <= 7)) {
                if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7) && (newY == startY - 1))) || ((newX == (startX - 1)) && (startX - 1 >= 0) && (newY == startY - 1))))) {
                    if (!checkWhiteOponent(e.getX(), e.getY())) {
                        validMove = true;
                        if (startY == 1) {
                            success = true;
                        }
                    } else {
                        validMove = false;
                    }
                } else {
                    if (!piecePresent(e.getX(), (e.getY()))) {
                        if ((startX == (e.getX() / 75)) && ((e.getY() / 75) - startY) == -1) {
                            if (startY == 1) {
                                success = true;
                            }
                            validMove = true;
                        } else {
                            validMove = false;
                        }
                    } else {
                        validMove = false;
                    }
                }
            } else {
                validMove = false;
            }
        }
    }//End BlackPawn move method

    public void knightMove(MouseEvent e) {
        //Get position of where the user wants to move the knight
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        //If  statement to declare all possible movements for a knight
        if ((startX + 2 == newX && startY + 1 == newY)
                || (startX + 2 == newX && startY - 1 == newY)
                || (startX - 2 == newX && startY + 1 == newY)
                || (startX - 2 == newX && startY - 1 == newY)
                || (startX + 1 == newX && startY + 2 == newY)
                || (startX + 1 == newX && startY - 2 == newY)
                || (startX - 1 == newX && startY + 2 == newY)
                || (startX - 1 == newX && startY - 2 == newY)) {
            //If no piece is present in new position move is valid
            if (!piecePresent(e.getX(), (e.getY()))) {
                validMove = true;
            } else {
                //If an allied piece is in position. Move is invalid.
                //End else
                if ((!checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("White"))) {
                    validMove = false;
                } else if ((checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("Black"))) {
                    validMove = false;
                } else {
                    validMove = true;
                }
            }//End else
        } else {
            validMove = false;
        }//Else else
    }//End Knight move method

    public void kingMove(MouseEvent e) {
        //Get position of where the user wants to move the king
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        //If statement to declare all possible movements for a king
        if ((startX == newX && startY + 1 == newY)
                || (startX + 1 == newX && startY == newY)
                || (startX - 1 == newX && startY - 1 == newY)
                || (startX + 1 == newX && startY + 1 == newY)
                || (startX + 1 == newX && startY - 1 == newY)
                || (startX == newX && startY - 1 == newY)
                || (startX - 1 == newX && startY == newY)
                || (startX - 1 == newX && startY + 1 == newY)) {
            //If no piece is present in new position. valid move confirmed
            if (!piecePresent(e.getX(), (e.getY()))) {
                validMove = true;
                // If/Else to tell program king has moved. Blocks castling.
                if (pieceName.contains("White")) {
                    whiteKingMoved = true;
                } else {
                    blackKingMoved = true;
                }//End If/Else to block castling
            } else {
                //If/Else to ensure the king can only move to an open space. or to take an enemy piece
                if ((!checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("White"))) {
                    validMove = false;
                } else if ((checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("Black"))) {
                    validMove = false;
                } else {
                    validMove = true;
                    // If/Else to tell program king has moved. Blocks castling.
                    if (pieceName.contains("White")) {
                        whiteKingMoved = true;
                    } else {
                        blackKingMoved = true;
                    }//End If/Else to block castling
                }//End Else
            }//End Else
            //If none of the moves in the If are called. The move will be invalid.
        } else {
            validMove = false;
        }//End If/Else

        //This If/Else is used to allow castling by moving an unmoved king two spaces towards an unmoved rook
        //Starts by checking king has not moved and that it is not moving on y axis. Also check that piece is White. so that black pieces can be done in the else if
        if ((startY == newY) && (!whiteKingMoved) && (pieceName.contains("White"))) {
            //Another if/else is used for both left and right rooks. checks that spaces between rook and king are clear and that the king is moving to the correct spot. First if is for left rook
            if ((!lWhiteRookMoved) && (!piecePresent((75), (0))) && (!piecePresent((150), (0))) && (newX == 1)) {
                Component cmpnt = chessBoard.findComponentAt(0, 0);
                JLabel lookForLRook = (JLabel) cmpnt;
                String lookForLRookStr = lookForLRook.getIcon().toString();
                if (lookForLRookStr.contains("WhiteRook")) {
                    //Make the move valid
                    validMove = true;
                    //Remove the rook from the starting position and move it to the castled position
                    panels = (JPanel) chessBoard.getComponent(0);
                    panels.remove(0);
                    panels.updateUI();
                    pieces = new JLabel(new ImageIcon("resources/WhiteRook.png"));
                    panels = (JPanel) chessBoard.getComponent(2);
                    panels.add(pieces);
                }//End final if that moves pieces
            } else if ((!rWhiteRookMoved) && (!piecePresent(300, 0)) && (!piecePresent(375, 0)) && (!piecePresent(450, 0)) && (newX == 5)) {
                Component cmpnt = chessBoard.findComponentAt(525, 0);
                JLabel lookForLRook = (JLabel) cmpnt;
                String lookForLRookStr = lookForLRook.getIcon().toString();
                if (lookForLRookStr.contains("WhiteRook")) {
                    //Make the move valid
                    validMove = true;
                    //Remove the rook from the starting position and move it to the castled position
                    panels = (JPanel) chessBoard.getComponent(7);
                    panels.remove(0);
                    panels.updateUI();
                    pieces = new JLabel(new ImageIcon("resources/WhiteRook.png"));
                    panels = (JPanel) chessBoard.getComponent(4);
                    panels.add(pieces);
                }//End final if that moves pieces
            }//end else if for white castling
        } else if ((startY == newY) && (!blackKingMoved) && (pieceName.contains("Black"))) {
            //Another if/else is used for both left and right rooks. checks that spaces between rook and king are clear and that the king is moving to the correct spot. First if is for left rook
            if ((!lBlackRookMoved) && (!piecePresent((75), (525))) && (!piecePresent((150), (525))) && (newX == 1)) {
                Component cmpnt = chessBoard.findComponentAt(0, 525);
                JLabel lookForLRook = (JLabel) cmpnt;
                String lookForLRookStr = lookForLRook.getIcon().toString();
                //System.out.println(lookForLRookStr);
                if (lookForLRookStr.contains("BlackRook")) {
                    //Make the move valid
                    validMove = true;
                    //Remove the rook from the starting position and move it to the castled position
                    panels = (JPanel) chessBoard.getComponent(56);
                    panels.remove(0);
                    panels.updateUI();
                    pieces = new JLabel(new ImageIcon("resources/BlackRook.png"));
                    panels = (JPanel) chessBoard.getComponent(58);
                    panels.add(pieces);
                }//End final if that moves pieces
            } else if ((!rBlackRookMoved) && (!piecePresent(300, 525)) && (!piecePresent(375, 525)) && (!piecePresent(450, 525)) && (newX == 5)) {
                Component cmpnt = chessBoard.findComponentAt(525, 525);
                JLabel lookForLRook = (JLabel) cmpnt;
                String lookForLRookStr = lookForLRook.getIcon().toString();
                if (lookForLRookStr.contains("BlackRook")) {
                    //Make the move valid
                    validMove = true;
                    //Remove the rook from the starting position and move it to the castled position
                    panels = (JPanel) chessBoard.getComponent(63);
                    panels.remove(0);
                    panels.updateUI();
                    pieces = new JLabel(new ImageIcon("resources/BlackRook.png"));
                    panels = (JPanel) chessBoard.getComponent(60);
                    panels.add(pieces);
                }//End final if that moves pieces
            }//End else if for Black castling
        }//End else if for castling moves
    }//End King move method

    public void bishopMove(MouseEvent e) {
        //Get position of where the user wants to move the bishop
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        //Find how many spaces the bishop moved. Can be done with both X and Y axis. Due to bishop moving diagonal.
        int il = startX - newX;
        if (il < 0) {
            il = il * -1;
        }//If il is negative turn it into a positive integer. This shows how many spaces the bishop moved.

        //Turn co-ordinates into a single number. Modulus operations can be performed on it to get direction of bishop movement.
        //This is due to the fact that diagonal directions on this grid will always result in 9, -9, 11, -11 when co-ordinates are subtracted from one another
        int calc = Integer.parseInt(Integer.toString(newX) + Integer.toString(newY))
                - Integer.parseInt(Integer.toString(startX) + Integer.toString(startY));


        //For loop to detect if another piece is in the bishops path. as Bishop cannot 'jump over' any other piece
        for (int i = 1; i <= il; i++) {
            //Detects if bishop is attempting to move down and right
            if ((calc % 11 == 0) && (newY > startY)) {
                if (piecePresent((startX + i) * 75, (startY + i) * 75)) {
                    if ((i == il) && (checkWhiteOponent((startX + i) * 75, (startY + i) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        break;
                    } else if ((i == il) && (!checkWhiteOponent((startX + i) * 75, (startY + i) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                }
                //Detects if bishop is attempting to move up and left
            } else if ((calc % -11 == 0) && (newY < startY)) {
                if (piecePresent((startX - i) * 75, (startY - i) * 75)) {
                    if ((i == il) && (checkWhiteOponent((startX - i) * 75, (startY - i) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        break;
                    } else if ((i == il) && (!checkWhiteOponent((startX - i) * 75, (startY - i) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                }
                //Detects if bishop is attempting to move down and left
            } else if ((calc % -9 == 0) && (newY > startY)) {
                if (piecePresent((startX - i) * 75, (startY + i) * 75)) {
                    if ((i == il) && (checkWhiteOponent((startX - i) * 75, (startY + i) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        break;
                    } else if ((i == il) && (!checkWhiteOponent((startX - i) * 75, (startY + i) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                }
                //Detects if bishop is attempting to move up and right
            } else if ((calc % 9 == 0) && (newY < startY)) {
                if (piecePresent((startX + i) * 75, (startY - i) * 75)) {
                    if ((i == il) && (checkWhiteOponent((startX + i) * 75, (startY - i) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        break;
                    } else if ((i == il) && (!checkWhiteOponent((startX + i) * 75, (startY - i) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                }
                //If bishop makes any other move it will be invalid
            } else {
                validMove = false;
                break;
            }//End else
        }//End For Loop
    }//End Bishop move method

    public void rookMove(MouseEvent e) {
        //Get position of where the user wants to move the rook
        int newY = e.getY() / 75;
        int newX = e.getX() / 75;
        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        //System.out.println(newX+":x  y:"+ newY);
        //Find how many spaces the rook attempted to move. Store ily to show physical movement including negative
        //movement int is used to store positive int only for For loop
        int ily = (newX - startX) + (newY - startY);
        int movement;
        if (ily < 0) {
            movement = ily * -1;
        } else {
            movement = ily;
        }
        //For loop to detect if another piece is in the rooks path. as a rook cannot 'jump over' any other piece
        for (int i = 1; i <= movement; i++) {
            //Detects if rook is attempting to move right
            if ((ily > 0) && (newY == startY)) {
                if (piecePresent((startX + i) * 75, (startY) * 75)) {
                    if ((i == movement) && (checkWhiteOponent((startX + i) * 75, (startY) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("White")) && (startX == 0)) {
                            lWhiteRookMoved = true;
                        } else {
                            rWhiteRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    } else if ((i == movement) && (!checkWhiteOponent((startX + i) * 75, (startY) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("Black")) && (startX == 0)) {
                            lBlackRookMoved = true;
                        } else {
                            rBlackRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                    // If/Else used to show when rook has moved. This means that castling will not be allowed
                    if ((pieceName.contains("White")) && (startX == 0)) {
                        lWhiteRookMoved = true;
                    } else if ((pieceName.contains("White")) && (startX == 7)) {
                        rWhiteRookMoved = true;
                    } else if ((pieceName.contains("Black")) && (startX == 0)) {
                        lBlackRookMoved = true;
                    } else {
                        rBlackRookMoved = true;
                    }//End If/Else used for castling
                }
                //Detects if rook is attempting to move left
            } else if ((ily < 0) && (newY == startY)) {
                if (piecePresent(((startX - i) * 75), ((startY) * 75))) {
                    if ((i == movement) && (checkWhiteOponent(((startX - i) * 75), ((startY) * 75))) && (pieceName.contains("White"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("White")) && (startX == 0)) {
                            lWhiteRookMoved = true;
                        } else {
                            rWhiteRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    } else if ((i == movement) && (!checkWhiteOponent(((startX - i) * 75), ((startY) * 75))) && (pieceName.contains("Black"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("Black")) && (startX == 0)) {
                            lBlackRookMoved = true;
                        } else {
                            rBlackRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                    // If/Else used to show when rook has moved. This means that castling will not be allowed
                    if ((pieceName.contains("White")) && (startX == 0)) {
                        lWhiteRookMoved = true;
                    } else if ((pieceName.contains("White")) && (startX == 7)) {
                        rWhiteRookMoved = true;
                    } else if ((pieceName.contains("Black")) && (startX == 0)) {
                        lBlackRookMoved = true;
                    } else {
                        rBlackRookMoved = true;
                    }//End If/Else used for castling
                }
                //Detects if rook is attempting to move down
            } else if ((ily > 0) && (newX == startX)) {
                if (piecePresent((startX) * 75, (startY + i) * 75)) {
                    if ((i == movement) && (checkWhiteOponent((startX) * 75, (startY + i) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("White")) && (startX == 0)) {
                            lBlackRookMoved = true;
                        } else {
                            rBlackRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    } else if ((i == movement) && (!checkWhiteOponent((startX) * 75, (startY + i) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("Black")) && (startX == 0)) {
                            lBlackRookMoved = true;
                        } else {
                            rBlackRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                    // If/Else used to show when rook has moved. This means that castling will not be allowed
                    if ((pieceName.contains("White")) && (startX == 0)) {
                        lWhiteRookMoved = true;
                    } else if ((pieceName.contains("White")) && (startX == 7)) {
                        rWhiteRookMoved = true;
                    } else if ((pieceName.contains("Black")) && (startX == 0)) {
                        lBlackRookMoved = true;
                    } else {
                        rBlackRookMoved = true;
                    }//End If/Else used for castling
                }
                //Detects if rook is attempting to move up
            } else if ((ily < 0) && (newX == startX)) {
                if (piecePresent((startX) * 75, (startY - i) * 75)) {
                    if ((i == movement) && (checkWhiteOponent((startX) * 75, (startY - i) * 75)) && (pieceName.contains("White"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("White")) && (startX == 0)) {
                            lBlackRookMoved = true;
                        } else {
                            rBlackRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    } else if ((i == movement) && (!checkWhiteOponent((startX) * 75, (startY - i) * 75)) && (pieceName.contains("Black"))) {
                        validMove = true;
                        // If/Else used to show when rook has moved. This means that castling will not be allowed
                        if ((pieceName.contains("Black")) && (startX == 0)) {
                            lBlackRookMoved = true;
                        } else {
                            rBlackRookMoved = true;
                        }//End If/Else used for castling
                        break;
                    }
                    validMove = false;
                    break;
                } else {
                    validMove = true;
                    // If/Else used to show when rook has moved. This means that castling will not be allowed
                    if ((pieceName.contains("White")) && (startX == 0)) {
                        lWhiteRookMoved = true;
                    } else if ((pieceName.contains("White")) && (startX == 7)) {
                        rWhiteRookMoved = true;
                    } else if ((pieceName.contains("Black")) && (startX == 0)) {
                        lBlackRookMoved = true;
                    } else {
                        rBlackRookMoved = true;
                    }//End If/Else used for castling
                }
                //If rook makes any other move it will be invalid
            } else {
                validMove = false;
                break;
            }//End else
        }//End For Loop
    }//End rook move method

    public static Stack getWhitePawnSquares(int x, int y, String piece) {
        Stack moves = new Stack();
        Square startingSquare = new Square(x, y, piece);
        Move validM;
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y + 1;
        int tmpy2 = y + 2;
        //If white apwn is in starting position
        if (y == 1) {
            //if to check for piece in square one ahead
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20)) {
                Square tmp = new Square(x, tmpy1, findPiece(x, tmpy1));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }//end if to check for piece in square one ahead
            //if to check for piece two positions ahead and one position ahead. allows double jump on first move.
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && !piecePresent((x * 75) + 20, (tmpy2 * 75) + 20)) {
                Square tmp = new Square(x, tmpy2, findPiece(x, tmpy2));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }//End if to check for piece two positions ahead and one position ahead. allows double jump on first move.
            //Check for piece to right and one square ahead. allow to take this pawn
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkWhiteOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, findPiece(tmpx1, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }//End Check for piece to right and one square ahead. allow to take this pawn
            //Check for piece to left and one square ahead. allow to take this pawn
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkWhiteOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, findPiece(tmpx2, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }//End Check for piece to left and one square ahead. allow to take this pawn
        } else {
            //if to check for piece in square one ahead
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) {
                Square tmp = new Square(x, tmpy1, findPiece(x, tmpy1));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }//End if to check for piece in square one ahead
            //Check for piece to right and one square ahead. allow to take this pawn
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkWhiteOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, findPiece(tmpx1, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            } //End Check for piece to right and one square ahead. allow to take this pawn
            //Check for piece to left and one square ahead. allow to take this pawn
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkWhiteOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, findPiece(tmpx2, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            } //End Check for piece to left and one square ahead. allow to take this pawn
        }

        return moves;
    }//End getWhitePawnSquares

    public static Stack getBlackPawnSquares(int x, int y, String piece) {
        Stack moves = new Stack();
        Square startingSquare = new Square(x, y, piece);
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y - 1;
        int tmpy2 = y - 2;
        //If white apwn is in starting position
        if (y == 6) {
            Move validM;
            //if to check for piece in square one ahead
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) {
                Square tmp = new Square(x, tmpy1, findPiece(x, tmpy1));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }//End if to check for piece in square one ahead
            //if to check for piece two positions ahead and one position ahead. allows double jump on first move.
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && !piecePresent((x * 75) + 20, (tmpy2 * 75) + 20)) {
                Square tmp = new Square(x, tmpy2, findPiece(x, tmpy2));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }//End if to check for piece two positions ahead and one position ahead. allows double jump on first move.
            //Check for piece to right and one square ahead. allow to take this pawn
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkBlackOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, findPiece(tmpx1, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }//End Check for piece to right and one square ahead. allow to take this pawn
            //Check for piece to left and one square ahead. allow to take this pawn
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkBlackOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, findPiece(tmpx2, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }//End Check for piece to left and one square ahead. allow to take this pawn
        } else {
            Move validM;
            //Check for piece one square ahead.
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) {
                Square tmp = new Square(x, tmpy1, findPiece(x, tmpy1));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }//End Check for piece one square ahead.
            //Check for piece to right and one square ahead. allow to take this pawn
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (!checkWhiteOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, findPiece(tmpx1, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }//End Check for piece to right and one square ahead. allow to take this pawn
            //Check for piece to left and one square ahead. allow to take this pawn
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (!checkWhiteOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, findPiece(tmpx2, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }//End Check for piece to left and one square ahead. allow to take this pawn
        }//End else to check for positions other than in first position

        return moves;
    }//End getBlackPawnSquares

    public static Stack getKingSquares(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y + 1;
        int tmpy2 = y - 1;

        if (!((tmpx1 > 7))) {
            Square tmp = new Square(tmpx1, y, findPiece(tmpx1, y));
            Square tmp1 = new Square(tmpx1, tmpy1, findPiece(tmpx1, tmpy1));
            Square tmp2 = new Square(tmpx1, tmpy2, findPiece(tmpx1, tmpy2));
            if (checkSurroundingSquares(tmp)) {
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (checkWhiteOponent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM);
                    } else if (!checkWhiteOponent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM);
                    }
                }
            }
            if (!(tmpy1 > 7)) {
                if (checkSurroundingSquares(tmp1)) {
                    validM2 = new Move(startingSquare, tmp1);
                    if (!piecePresent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    } else {
                        if (checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                            moves.push(validM2);
                        } else if (!checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20))) && boardSide.equals("White")) {
                            moves.push(validM2);
                        }
                    }
                }
            }
            if (!(tmpy2 < 0)) {
                if (checkSurroundingSquares(tmp2)) {
                    validM3 = new Move(startingSquare, tmp2);
                    if (!piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    } else {
                        if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                            moves.push(validM3);
                        } else if (!checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20))) && boardSide.equals("White")) {
                            moves.push(validM3);
                        }
                    }
                }
            }
        }
        if (!((tmpx2 < 0))) {
            Square tmp3 = new Square(tmpx2, y, findPiece(tmpx2, y));
            Square tmp4 = new Square(tmpx2, tmpy1, findPiece(tmpx2, tmpy1));
            Square tmp5 = new Square(tmpx2, tmpy2, findPiece(tmpx2, tmpy2));
            if (checkSurroundingSquares(tmp3)) {
                validM = new Move(startingSquare, tmp3);
                if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (checkWhiteOponent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM);
                    } else if (!checkWhiteOponent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM);
                    }
                }
            }
            if (!(tmpy1 > 7)) {
                if (checkSurroundingSquares(tmp4)) {
                    validM2 = new Move(startingSquare, tmp4);
                    if (!piecePresent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    } else {
                        if (checkWhiteOponent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                            moves.push(validM2);
                        } else if (!checkWhiteOponent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20))) && boardSide.equals("White")) {
                            moves.push(validM2);
                        }
                    }
                }
            }
            if (!(tmpy2 < 0)) {
                if (checkSurroundingSquares(tmp5)) {
                    validM3 = new Move(startingSquare, tmp5);
                    if (!piecePresent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    } else {
                        if (checkWhiteOponent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                            moves.push(validM3);
                        } else if (!checkWhiteOponent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20))) && boardSide.equals("White")) {
                            moves.push(validM3);
                        }
                    }
                }
            }
        }
        Square tmp7 = new Square(x, tmpy1, findPiece(x, tmpy1));
        Square tmp8 = new Square(x, tmpy2, findPiece(x, tmpy2));
        if (!(tmpy1 > 7)) {
            if (checkSurroundingSquares(tmp7)) {
                validM2 = new Move(startingSquare, tmp7);
                if (!piecePresent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (checkWhiteOponent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM2);
                    } else if (!checkWhiteOponent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM2);
                    }
                }
            }
        }
        if (!(tmpy2 < 0)) {
            if (checkSurroundingSquares(tmp8)) {
                validM3 = new Move(startingSquare, tmp8);
                if (!piecePresent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (checkWhiteOponent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM3);
                    } else if (!checkWhiteOponent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM3);
                    }
                }
            }
        }
        return moves;
    } // end of the method getKingSquares()

    public static Stack getQueenMoves(int x, int y, String piece) {
        Stack completeMoves = new Stack();
        Stack tmpMoves = new Stack();
        Move tmp;

        tmpMoves = getRookMoves(x, y, piece);
        while (!tmpMoves.empty()) {
            tmp = (Move) tmpMoves.pop();
            completeMoves.push(tmp);
        }
        tmpMoves = getBishopMoves(x, y, piece);
        while (!tmpMoves.empty()) {
            tmp = (Move) tmpMoves.pop();
            completeMoves.push(tmp);
        }
        return completeMoves;
    }//End getQueenMoves

    public static Stack getRookMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
        for (int i = 1; i < 8; i++) {
            int tmpx = x + i;
            int tmpy = y;
            if (!(tmpx > 7 || tmpx < 0)) {
                Square tmp = new Square(tmpx, tmpy, findPiece(tmpx, tmpy));
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if ((checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM);
                        break;
                    } else if ((!checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int j = 1; j < 8; j++) {
            int tmpx1 = x - j;
            int tmpy1 = y;
            if (!(tmpx1 > 7 || tmpx1 < 0)) {
                Square tmp2 = new Square(tmpx1, tmpy1, findPiece(tmpx1, tmpy1));
                validM2 = new Move(startingSquare, tmp2);
                if (!piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if ((checkWhiteOponent(((tmp2.getXC() * 75) + 20), ((tmp2.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM2);
                        break;
                    } else if ((!checkWhiteOponent(((tmp2.getXC() * 75) + 20), ((tmp2.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int k = 1; k < 8; k++) {
            int tmpx3 = x;
            int tmpy3 = y + k;
            if (!(tmpy3 > 7 || tmpy3 < 0)) {
                Square tmp3 = new Square(tmpx3, tmpy3, findPiece(tmpx3, tmpy3));
                validM3 = new Move(startingSquare, tmp3);
                if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if ((checkWhiteOponent(((tmp3.getXC() * 75) + 20), ((tmp3.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM3);
                        break;
                    } else if ((!checkWhiteOponent(((tmp3.getXC() * 75) + 20), ((tmp3.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int l = 1; l < 8; l++) {
            int tmpx4 = x;
            int tmpy4 = y - l;
            if (!(tmpy4 > 7 || tmpy4 < 0)) {
                Square tmp4 = new Square(tmpx4, tmpy4, findPiece(tmpx4, tmpy4));
                validM4 = new Move(startingSquare, tmp4);
                if (!piecePresent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                    moves.push(validM4);
                } else {
                    if ((checkWhiteOponent(((tmp4.getXC() * 75) + 20), ((tmp4.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM4);
                        break;
                    } else if ((!checkWhiteOponent(((tmp4.getXC() * 75) + 20), ((tmp4.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        return moves;
    }//End of get Rook Moves.

    public static Stack getBishopMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
        for (int i = 1; i < 8; i++) {
            int tmpx = x + i;
            int tmpy = y + i;
            if (!(tmpx > 7 || tmpx < 0 || tmpy > 7 || tmpy < 0)) {
                Square tmp = new Square(tmpx, tmpy, findPiece(tmpx, tmpy));
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if ((checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM);
                        break;
                    } else if ((!checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } // end of the first for Loop
        for (int k = 1; k < 8; k++) {
            int tmpk = x + k;
            int tmpy2 = y - k;
            if (!(tmpk > 7 || tmpk < 0 || tmpy2 > 7 || tmpy2 < 0)) {
                Square tmpK1 = new Square(tmpk, tmpy2, findPiece(tmpk, tmpy2));
                validM2 = new Move(startingSquare, tmpK1);
                if (!piecePresent(((tmpK1.getXC() * 75) + 20), (((tmpK1.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if ((checkWhiteOponent(((tmpK1.getXC() * 75) + 20), ((tmpK1.getYC() * 75) + 20))) && boardSide.equals("Black")) {
                        moves.push(validM2);
                        break;
                    } else if ((!checkWhiteOponent(((tmpK1.getXC() * 75) + 20), ((tmpK1.getYC() * 75) + 20))) && boardSide.equals("White")) {
                        moves.push(validM2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } //end of second loop.
        for (int l = 1; l < 8; l++) {
            int tmpL2 = x - l;
            int tmpy3 = y + l;
            if (!(tmpL2 > 7 || tmpL2 < 0 || tmpy3 > 7 || tmpy3 < 0)) {
                Square tmpLMov2 = new Square(tmpL2, tmpy3, findPiece(tmpL2, tmpy3));
                validM3 = new Move(startingSquare, tmpLMov2);
                if (!piecePresent(((tmpLMov2.getXC() * 75) + 20), (((tmpLMov2.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (checkWhiteOponent(((tmpLMov2.getXC() * 75) + 20), ((tmpLMov2.getYC() * 75) + 20)) && boardSide.equals("Black")) {
                        moves.push(validM3);
                        break;
                    } else if (!checkWhiteOponent(((tmpLMov2.getXC() * 75) + 20), ((tmpLMov2.getYC() * 75) + 20)) && boardSide.equals("White")) {
                        moves.push(validM3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }// end of the third loop
        for (int n = 1; n < 8; n++) {
            int tmpN2 = x - n;
            int tmpy4 = y - n;
            if (!(tmpN2 > 7 || tmpN2 < 0 || tmpy4 > 7 || tmpy4 < 0)) {
                Square tmpNmov2 = new Square(tmpN2, tmpy4, findPiece(tmpN2, tmpy4));
                validM4 = new Move(startingSquare, tmpNmov2);
                if (!piecePresent(((tmpNmov2.getXC() * 75) + 20), (((tmpNmov2.getYC() * 75) + 20)))) {
                    moves.push(validM4);
                } else {
                    if (checkWhiteOponent(((tmpNmov2.getXC() * 75) + 20), ((tmpNmov2.getYC() * 75) + 20)) && boardSide.equals("Black")) {
                        moves.push(validM4);
                        break;
                    } else if (!checkWhiteOponent(((tmpNmov2.getXC() * 75) + 20), ((tmpNmov2.getYC() * 75) + 20)) && boardSide.equals("White")) {
                        moves.push(validM4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }// end of the last loop
        return moves;
    }//End getBishopMoves

    public static Stack getKnightMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Stack attackingMove = new Stack();
        Square s = new Square(x + 1, y + 2, findPiece(x + 1, y + 2));
        moves.push(s);
        Square s1 = new Square(x + 1, y - 2, findPiece(x + 1, y - 2));
        moves.push(s1);
        Square s2 = new Square(x - 1, y + 2, findPiece(x - 1, y + 2));
        moves.push(s2);
        Square s3 = new Square(x - 1, y - 2, findPiece(x - 1, y - 2));
        moves.push(s3);
        Square s4 = new Square(x + 2, y + 1, findPiece(x + 2, y + 1));
        moves.push(s4);
        Square s5 = new Square(x + 2, y - 1, findPiece(x + 2, y - 1));
        moves.push(s5);
        Square s6 = new Square(x - 2, y + 1, findPiece(x - 2, y + 1));
        moves.push(s6);
        Square s7 = new Square(x - 2, y - 1, findPiece(x - 2, y - 1));
        moves.push(s7);

        for (int i = 0; i < 8; i++) {
            Square tmp = (Square) moves.pop();
            Move tmpmove = new Move(startingSquare, tmp);
            if ((tmp.getXC() < 0) || (tmp.getXC() > 7) || (tmp.getYC() < 0) || (tmp.getYC() > 7)) {
            } else if (piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                if (piece.contains("White")) {
                    if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && boardSide.equals("Black")) {
                        attackingMove.push(tmpmove);
                    }
                } else if (piece.contains("Black")) {
                    if (!checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && boardSide.equals("White")) {
                        attackingMove.push(tmpmove);
                    }
                }
            } else {
                attackingMove.push(tmpmove);
            }
        }
        return attackingMove;
    }//End getKnightMoves

    public static String getPieceName(int x, int y) {
        return pieceName;
    }//End getPieceName

    private static Boolean checkSurroundingSquares(Square s) {
        Boolean possible = false;
        int x = s.getXC() * 75;
        int y = s.getYC() * 75;
        if (!((getPieceName((x + 75), y).contains("BlackKing")) || (getPieceName((x - 75), y).contains("BlackKing")) || (getPieceName(x, (y + 75)).contains("BlackKing")) || (getPieceName((x), (y - 75)).contains("BlackKing")) || (getPieceName((x + 75), (y + 75)).contains("BlackKing")) || (getPieceName((x - 75), (y + 75)).contains("BlackKing")) || (getPieceName((x + 75), (y - 75)).contains("BlackKing")) || (getPieceName((x - 75), (y - 75)).contains("BlackKing")))) {
            possible = true;
        }
        return possible;
    }//End checkSurroundingSquares

    private void colorSquares(Stack squares) {
        Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
        while (!squares.empty()) {
            Square s = (Square) squares.pop();
            int location = s.getXC() + ((s.getYC()) * 8);
            JPanel panel = (JPanel) chessBoard.getComponent(location);
            panel.setBorder(greenBorder);
        }
    }//End colorSquares

    private void getLandingSquares(Stack found) {
        Move tmp;
        Square landing;
        Stack squares = new Stack();
        while (!found.empty()) {
            tmp = (Move) found.pop();
            landing = (Square) tmp.getLanding();
            squares.push(landing);
        }
        colorSquares(squares);
    }//end getlanding squares

    private Stack findWhitePieces() {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        //Fist for loop to increment through the X axis
        for (int i = 0; i < 600; i += 75) {
            //Second for loop to increment through the Y axis
            for (int j = 0; j < 600; j += 75) {
                //divide j and i by 75 to get co-ords
                y = i / 75;
                x = j / 75;
                //Find component at the co-ords
                Component tmp = chessBoard.findComponentAt(j, i);
                //If the component found is an instance of a JLabel. Then a piece is present.
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    //If the name of the piece contains White. then it is a white piece. add to stack
                    if (pieceName.contains("White")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }//End if to find white pieces
                }//End if that checks if component is a JLabel
            }//End for loop to increment the Y Axis
        }//End for loop to increment the X axis
        return squares;
    }//End findWhitePieces method

    private Stack findBlackPieces() {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;
        //Fist for loop to increment through the X axis
        for (int i = 0; i < 600; i += 75) {
            //Second for loop to increment through the Y axis
            for (int j = 0; j < 600; j += 75) {
                //divide j and i by 75 to get co-ords
                y = i / 75;
                x = j / 75;
                //Find component at the co-ords
                Component tmp = chessBoard.findComponentAt(j, i);
                //If the component found is an instance of a JLabel. Then a piece is present.
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    //If the name of the piece contains Black. then it is a white piece. add to stack
                    if (pieceName.contains("Black")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }//End if to check is piece is black
                }//End if to see if component is JLabel
            }//End for loop to increment through Y axis
        }//End for loop to increment through X axis
        return squares;
    }//End findBlackPieces method

    private void resetBorders() {
        Border empty = BorderFactory.createEmptyBorder();
        for (int i = 0; i < 64; i++) {
            JPanel tmppanel = (JPanel) chessBoard.getComponent(i);
            tmppanel.setBorder(empty);
        }
    }//End reset borders method

    private void printStack(Stack input) {
        Move m;
        Square s, l;
        while (!input.empty()) {
            m = (Move) input.pop();
            s = (Square) m.getStart();
            l = (Square) m.getLanding();
            System.out.println("The possible move that was found is : (" + s.getXC() + " , " + s.getYC() + "), landing at (" + l.getXC() + " , " + l.getYC() + ")");
        }
    } //End printStack method

    public void makeAIMove() {
        //Create AI Agent object that will move the ai
        AIAgent agent = new AIAgent();
        resetBorders();
        layeredPane.validate();
        layeredPane.repaint();
        Stack white = findWhitePieces();
        Stack black = findBlackPieces();
        Stack aiMoves = new Stack();
        Stack completeMoves = new Stack();
        Stack temporary = new Stack();
        Move tmp;
        //Check which side of the board the user is. Return the opposite for the AIAgent to use
        if (boardSide.equals("White")) {
            aiMoves = black;
        } else if (boardSide.equals("Black")) {
            aiMoves = white;
        }//End if/else to check which side the user is on
        //While the available pieces is not empty. fetched from the last if/else
        while (!aiMoves.empty()) {
            //Remove the Square from the Stack of Squares. Get the name of the piece as a string.
            Square s = (Square) aiMoves.pop();
            String tmpString = s.getName();
            //Create a new stack of moves returned from the get methods. This will be pushed to another stack containing all moves available to aiAgent
            Stack tmpMoves = new Stack();
            //If/else the string contains ".." call method to search for possible moves for that piece.
            if (tmpString.contains("Knight")) {
                tmpMoves = getKnightMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Bishop")) {
                tmpMoves = getBishopMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("BlackPawn")) {
                //  System.out.println("GETTING BPawn MOVES");
                tmpMoves = getBlackPawnSquares(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("WhitePawn")) {
                // System.out.println("GETTING WPawn MOVES");
                tmpMoves = getWhitePawnSquares(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Rook")) {
                tmpMoves = getRookMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("Queen")) {
                tmpMoves = getQueenMoves(s.getXC(), s.getYC(), s.getName());
            } else if (tmpString.contains("King")) {
                tmpMoves = getKingSquares(s.getXC(), s.getYC(), s.getName());
            }//end if/else to find piece moves.
            //While the tmpMoves is not empty. Push the tmpMoves to completeMoves
            while (!tmpMoves.empty()) {
                tmp = (Move) tmpMoves.pop();
                //System.out.println(i+": ");
                //i++;
                //System.out.println("Name: "+tmp.getLanding().getName());
                //System.out.println("X coord: "+tmp.getLanding().getXC());
                //System.out.println("Y coord: "+tmp.getLanding().getYC());
                completeMoves.push(tmp);
            }//End While to add moves returned to Stack of all possible moves
        }//End while to search through stack of Square pieces

        temporary = (Stack) completeMoves.clone();
        getLandingSquares(temporary);
        //Create a new stack of black pieces. Flag used to identify when Black king is not found on board.
        Stack blackKingSearch = findBlackPieces();
        boolean blackFlag = false;
        //While loop to search stack for king piece
        while (!blackKingSearch.isEmpty()) {
            Square blackKing = (Square) blackKingSearch.pop();
            if (blackKing.getName().contains("King")) {
                blackFlag = true;
            }//If black king is found. turn flag true
        }//End While loop to search stack for king piece
        if (!blackFlag) {
            JOptionPane.showMessageDialog(null, "Congratulations,you have won!");
            System.exit(0);
        }//If BlackFlag is still false. Game is over. AIAgent has lost
        //Create a new stack of black pieces. Flag used to identify when White king is not found on board.
        Stack whiteKingSearch = findWhitePieces();
        boolean whiteFlag = false;
        //While loop to search stack for king piece
        while (!whiteKingSearch.isEmpty()) {
            Square whiteKing = (Square) whiteKingSearch.pop();
            if (whiteKing.getName().contains("King")) {
                whiteFlag = true;
            }//If whiteKing is found. turn flag true
        }//End While loop to search stack for king piece
        if (!whiteFlag) {
            JOptionPane.showMessageDialog(null, "Congratulations, you have won!");
            System.exit(0);
        }//If WhiteFlag is still false. Game is over. AIAgent has lost

        //If the AIAgent has not moves available. Stalemate is reached. Player wins.
        if (completeMoves.size() == 0) {
            JOptionPane.showMessageDialog(null, "Congratulations, you have placed the AI component in a Stale Mate Position");
            System.exit(0);
        } else {
            //Create stack to take moves from completeMoves. This is so that you can print the moves
            Stack testing = new Stack();
            while (!completeMoves.empty()) {
                Move tmpMove = (Move) completeMoves.pop();
                Square s1 = (Square) tmpMove.getStart();
                Square s2 = (Square) tmpMove.getLanding();
                //System.out.println("The " + s1.getName() + " can move from (" + s1.getXC() + ", " + s1.getYC() + ") to the following square: (" + s2.getXC() + ", " + s2.getYC() + ")");
                testing.push(tmpMove);
            }//End while loop to print the moves available.

            //Create red border to display over the move the AI component selects
            Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
            //Move created to take slected move from the AI component in question.
            Move selectedMove = new Move();
            //Switch to call the AI type that the user selected.
            switch (aiType) {
                case "random" -> selectedMove = agent.randomMove(testing);
                case "nextBest" -> selectedMove = agent.nextBestMove(testing);
                case "twoLevelsDeep" -> selectedMove = agent.twoLevelsDeep(testing);
            }//End switch to call AI type user selected
            //Get starting and landing square from selected move
            Square startingPoint = (Square) selectedMove.getStart();
            Square landingPoint = (Square) selectedMove.getLanding();
            //get co-ords from the squares selected.
            int startX1 = (startingPoint.getXC() * 75) + 20;
            int startY1 = (startingPoint.getYC() * 75) + 20;
            int landingX1 = (landingPoint.getXC() * 75) + 20;
            int landingY1 = (landingPoint.getYC() * 75) + 20;
            //Find the component that the AI wants to move.
            Component c = (JLabel) chessBoard.findComponentAt(startX1, startY1);
            Container parent = c.getParent();
            //remove the piece the Ai wants to move
            parent.remove(c);
            //Set the red border to the starting position of the AI
            int panelID = (startingPoint.getYC() * 8) + startingPoint.getXC();
            panels = (JPanel) chessBoard.getComponent(panelID);
            panels.setBorder(redBorder);
            parent.validate();
            //find the component at the landing position of the AI Move.
            Component l = chessBoard.findComponentAt(landingX1, landingY1);
            //If component is a JLabel.. OR is component is a piece since JLabels are used to show pieces.

            if (l instanceof JLabel) {
                Container parentlanding = l.getParent();
                JLabel awaitingName = (JLabel) l;
                String agentCaptured = awaitingName.getIcon().toString();
                //if piece captured is a king set falg to allow ai agent to win to true
                if (agentCaptured.contains("King")) {
                    agentwins = true;
                }//End if to allow the AI to win
                //remove the piece the AI is capturing
                parentlanding.remove(l);
                parentlanding.validate();
                //Add the piece the AI is using to the landing position.
                pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
                int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                //Add red border to the square to show where the piece moved to
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
                //if the agentwins is true. The AI Wins. end the game
                if (agentwins) {
                    JOptionPane.showMessageDialog(null, "The AI Agent has won!");
                    System.exit(0);
                }//End if agentwins is true AI wins
                Component z = chessBoard.findComponentAt(landingX1, landingY1);
                Container zlanding = z.getParent();
                JLabel comp = (JLabel)z;
                String aiPiece = comp.getIcon().toString();
                Boolean success = false;
                if(aiPiece.contains("Pawn") && (landingY1/75==7 || landingY1/75==0)){
                    success = true;
                }
                if(success){
                    System.out.println("Get here");
                    zlanding.remove(z);
                    if(boardSide.equals("White")) {
                        pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
                    }else{
                        pieces = new JLabel(new ImageIcon("resources/WhiteQueen.png"));
                    }
                    panels = (JPanel) chessBoard.getComponent(landingPanelID);
                    panels.add(pieces);
                }
            } else {
                //Add piece to the board that the Ai is moving.
                pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
                int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
                Component z = chessBoard.findComponentAt(landingX1, landingY1);
                Container zlanding = z.getParent();
                JLabel comp = (JLabel)z;
                String aiPiece = comp.getIcon().toString();
                Boolean success = false;
                if(aiPiece.contains("Pawn") && (landingY1/75==7 || landingY1/75==0)){
                    success = true;
                }
                if(success){
                    System.out.println("Get here");
                    zlanding.remove(z);
                    if(boardSide.equals("White")) {
                        pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
                    }else{
                        pieces = new JLabel(new ImageIcon("resources/WhiteQueen.png"));
                    }
                    panels = (JPanel) chessBoard.getComponent(landingPanelID);
                    panels.add(pieces);
                }
            }//End if/else to see if component is a JLabel
        }//End if/else to make sure the aiAgent has moves available
    }//End makeAIMove method

    public static void aiSelection() {
        //Method to allow the user to select which AI they would like to play against
        //Create JFrame window with 3 buttons. one for each of the AI types available
        JFrame aiOptions = new JFrame("Choose your AI Opponent");
        aiOptions.setLayout(new FlowLayout());
        Dimension aiSelectSize = new Dimension(600, 80);
        aiOptions.setSize(aiSelectSize);
        aiOptions.setResizable(false);
        aiOptions.setLocationRelativeTo(null);
        //Create the 3 buttons for each of the options
        JButton randomMove = new JButton("Random Move");
        randomMove.setBounds(50, 1, 60, 30);
        JButton nextBestMove = new JButton("Next Best Move");
        nextBestMove.setBounds(50, 300, 60, 30);
        JButton twoLevelsDeep = new JButton("Two Levels Deep");
        twoLevelsDeep.setBounds(50, 600, 60, 30);
        //Add the 3 buttons to the JFrame
        aiOptions.add(randomMove);
        aiOptions.add(nextBestMove);
        aiOptions.add(twoLevelsDeep);
        //Set the JFrame to be visible
        aiOptions.setVisible(true);
        //Add action listener. closes AIOptions. set the ai Type to Random and call the ChooseSide method
        randomMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                aiOptions.setVisible(false);
                aiType = "random";
                chooseSide();
            }
        });
        //Add action listener. closes AIOptions. set the ai Type to NextBest and call the ChooseSide method
        nextBestMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                aiOptions.setVisible(false);
                aiType = "nextBest";
                chooseSide();
            }
        });
        //Add action listener. closes AIOptions. set the ai Type to TwoLevelsDeep and call the ChooseSide method
        twoLevelsDeep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                aiOptions.setVisible(false);
                aiType = "twoLevelsDeep";
                chooseSide();
            }
        });
    }//End the chooseAI method

    public static void chooseSide() {
        //Create new JFrame that allows the user to select if they want to play as White or Black
        //Just create simple buttonsin the Frame that allow user to click Black or White.
        JFrame aiOptions = new JFrame("Choose your side of your board");
        aiOptions.setLayout(new FlowLayout());
        Dimension aiSelectSize = new Dimension(600, 80);
        aiOptions.setSize(aiSelectSize);
        aiOptions.setResizable(false);
        aiOptions.setLocationRelativeTo(null);
        //Create White button
        JButton white = new JButton("White");
        white.setBounds(50, 1, 60, 30);
        //Create Black button
        JButton black = new JButton("Black");
        black.setBounds(50, 300, 60, 30);
        //Add buttons to the Frame
        aiOptions.add(white);
        aiOptions.add(black);
        //Set the window to visible
        aiOptions.setVisible(true);
        //Create listener from the White button that sets the boardside to White and calls the displayBoard method. also closes the chooseSide window
        white.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                aiOptions.setVisible(false);
                boardSide = "White";
                displayBoard();
            }
        });
        //Create listener from the Black button that sets the boardside to Black and calls the displayBoard method. also closes the chooseSide window
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                aiOptions.setVisible(false);
                boardSide = "Black";
                displayBoard();
            }
        });

    }//end ChooseSide method

    public static void displayBoard() {
        //Create display board JFrame. instance of Chessproject. Displays the board to the user.
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//End display board class

    public static void main(String[] args) {
        //Call the aiSelection method. This allows the user to select the type of Ai they would like to play against.
        aiSelection();
    }//End Main

}//End class