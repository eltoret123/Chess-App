package com.example.chessapp;

import androidx.annotation.NonNull;

public class Knight implements  Piece{
    private final boolean isWhite;

    public Knight(boolean isWhite){
        this.isWhite = isWhite;
    }
    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String getType() {
        return "N";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        if (Utils.outOfBounds(endRow, endCol)){return false;}

        if (Utils.sameSquareMove(startRow, endRow, startCol, endCol)){return false;}

        if (Utils.isMoveToSameColorPiece(endRow, endCol, isWhite(), board)){
            return false;
        }

        if (board[endRow][endCol] != null &&
                board[endRow][endCol].isWhite() == isWhite){
            return false;
        } // same color

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    @Override
    public int getDrawableResId() {return isWhite ? R.drawable.w_knight : R.drawable.b_knight;}

    @NonNull
    @Override
    public Piece clone() {
        return new Knight(this.isWhite);
    }
}

