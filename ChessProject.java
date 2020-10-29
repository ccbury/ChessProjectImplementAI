import com.sun.tools.javac.Main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import javax.swing.*;


public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
    int startX;
    int startY;
    int initialX;
    int initialY;
    JPanel panels;
    JLabel pieces;

    //all of these variables are needed in multiple methods while maintaining their values.
    Boolean validMove = false;
    String pieceName;
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

    private Boolean piecePresent(int x, int y) {
        Component c = chessBoard.findComponentAt(x, y);
        if (c instanceof JPanel) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkWhiteOponent(int newX, int newY) {
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
        String[] pieceTmp = tmp.split( "/");
        pieceName = pieceTmp[1];
        pieceName = pieceName.substring(0, (pieceName.length() - 4));

        validMove = false;

        if (turn) {
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
            if (validMove) {
                turn = !turn;
            }//End if. used to change from Whites turn to move to Blacks
        } else {
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
            if (validMove) {
                turn = !turn;
            }//End if. used to change from Blacks turn to move to Whites
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
            String pieceLocation = "resources/"+pieceName+".png";
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
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("resources/WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
            } else if ((success) && (pieceName.contains("Black"))) {
                int location = (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("resources/BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
            } else {
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add(chessPiece);
                } else {
                    Container parent = (Container) c;
                    parent.add(chessPiece);
                }
                chessPiece.setVisible(true);
            }
        }// End if(validMove) / Else

    }//End mouseRelease

    public void noMoveSound() throws MalformedURLException {
        File file = new File("resources/no.wav");
        URL url = file.toURI().toURL();
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }
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
                System.out.println(lookForLRookStr);
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

    public static void main(String[] args) {
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//End Main

}//End class