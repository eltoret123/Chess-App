package com.example.chessapp;

public abstract class Utils {
    protected static boolean outOfBounds(int row, int col) {
        return row < 0 || row >= 8 || col < 0 || col >= 8;
    }

    protected static boolean sameSquareMove(int startRow, int endRow, int startCol, int endCol) {
        return startRow == endRow && startCol == endCol;
    }
    protected static boolean isMoveToSameColorPiece(int row, int col, boolean isWhite, Piece[][] board) {
        return board[row][col] != null && board[row][col].isWhite() == isWhite;
    }

}

