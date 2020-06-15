public class Position {
    int row;
    int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(){};

    public boolean equals(Position position) {
        return position.row == this.row && position.col == this.col;
    }
}
