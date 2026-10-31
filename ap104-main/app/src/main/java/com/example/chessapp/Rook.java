package com.example.chessapp;

import androidx.annotation.NonNull;

public class Rook implements Piece{

    private final boolean isWhite;
    private boolean RookMoved = false;


    public Rook(boolean isWhite){
        this.isWhite = isWhite;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String getType() {
        return "R";
    }

    public void setRookMoved(boolean RookMoved){
        this.RookMoved = RookMoved;
    }

    public boolean notRookMoved(){
        return !RookMoved;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        if (Utils.outOfBounds(endRow, endCol)){return false;}

        if (Utils.sameSquareMove(startRow, endRow, startCol, endCol)){return false;}

        if (Utils.isMoveToSameColorPiece(endRow, endCol, isWhite(), board)){
            return false;
        }

        if (startRow != endRow && startCol != endCol){
            return false;
        }

        int StepX = Integer.compare(endRow, startRow);
        int StepY = Integer.compare(endCol, startCol);

        int x = startRow + StepX;
        int y = startCol + StepY;

        if (startRow != endRow){
            while(x != endRow){
                if (board[x][startCol] != null){ return false;}
                x += StepX;
            }
        }

        if (startCol != endCol){
            while(y != endCol){
                if (board[startRow][y] != null){ return false;}
                y += StepY;
            }
        }

        return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite;
    }

    @Override
    public int getDrawableResId() {
        return isWhite ? R.drawable.w_rook : R.drawable.b_rook;
    }

    @NonNull
    @Override
    public Piece clone() {
        return new Rook(this.isWhite);
    }
}
