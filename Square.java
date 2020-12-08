class Square {
    //Create variables required for a Square object. X, Y and pieceName
    public int xCoor;
    public int yCoor;
    public String pieceName;

    public Square(int x, int y, String name) {
        xCoor = x;
        yCoor = y;
        pieceName = name;
    }//End Square

    public Square(int x, int y) {
        xCoor = x;
        yCoor = y;
        pieceName = "";
    }//End Square

    public int getXC() {
        return xCoor;
    }//End getXC

    public int getYC() {
        return yCoor;
    }//End getYC

    public String getName() {
        return pieceName;
    }//End GetName

}//End Square class
