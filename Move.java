class Move {
    //Create variable required for a Move object. Consists of two Square objects
    Square start;
    Square landing;

    public Move(Square x, Square y) {
        start = x;
        landing = y;
    }//End move

    public Move() {
    }//End move

    public Square getStart() {
        return start;
    }//End getStart

    public Square getLanding() {
        return landing;
    }//End getLanding

}//End Move class
