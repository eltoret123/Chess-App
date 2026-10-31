package com.example.chessapp;

import androidx.annotation.NonNull;

public class Bishop implements Piece{
    private final boolean isWhite;

    public Bishop(boolean isWhite){
        this.isWhite = isWhite;
    }
    @Override
    public boolean isWhite() {return isWhite;}

    @Override
    public String getType() {
        return "B";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        if (Utils.outOfBounds(endRow, endCol)){return false;}

        if (Utils.sameSquareMove(startRow, endRow, startCol, endCol)){return false;}

        if (Utils.isMoveToSameColorPiece(endRow, endCol, isWhite(), board)){return false;}

        if (Math.abs(endRow - startRow) != Math.abs(endCol-startCol)){return false;}

        int StepX = startRow < endRow ? 1:-1;
        int StepY = startCol < endCol ? 1:-1;

        int currentRow = startRow + StepX;
        int currentCol = startCol + StepY;

        while (currentRow != endRow && currentCol != endCol){
            if (board[currentRow][currentCol] != null){return false;}
            currentRow += StepX;
            currentCol += StepY;
        }

        return true;
    }

    @Override
    public int getDrawableResId() {return isWhite ? R.drawable.w_bishop : R.drawable.b_bishop;}

    @NonNull
    @Override
    public Piece clone() {
        return new Bishop(this.isWhite);
    }
}
