package com.example.chessapp;

import androidx.annotation.NonNull;

public class King implements Piece{
    private final boolean isWhite;
    private boolean kingMoved = false;

    public King(boolean isWhite){
        this.isWhite = isWhite;
    }
    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String getType() {
        return "K";
    }

    public void setKingMoved(boolean kingMoved){
        this.kingMoved = kingMoved;
    }

    public boolean notKingMoved(){
        return !kingMoved;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        if (Utils.outOfBounds(endRow, endCol)){return false;}

        if (Utils.sameSquareMove(startRow, endRow, startCol, endCol)){return false;}

        if (Utils.isMoveToSameColorPiece(endRow, endCol, isWhite(), board)){
            return false;
        }

        for (int r=0; r<=7; r++){
            for (int c=0; c<=7; c++){
                if (board[r][c] != null &&
                        !(board[r][c] instanceof King) &&
                        board[r][c].isWhite() != isWhite &&
                        board[r][c].isValidMove(r, c, endRow,endCol, board)){
                    return false;
                }

            }
        }

        // Castle KingSide
        if (notKingMoved() &&
                board[startRow][7] instanceof Rook &&
                ((Rook) board[startRow][7]).notRookMoved() &&
                startRow == endRow &&
                startCol == 4 &&
                endCol == 6 &&
                board[startRow][5] == null &&
                board[startRow][6] == null){
            return true;

        }

        // Castle QueenSide
        if (notKingMoved() &&
                board[startRow][0] instanceof Rook &&
                ((Rook) board[startRow][0]).notRookMoved() &&
                startRow == endRow &&
                startCol == 4 &&
                endCol == 2 &&
                board[startRow][3] == null &&
                board[startRow][2] == null &&
                board[startRow][1] == null){
            return true;

        }

        int RowDiff = Math.abs(endRow - startRow);
        int ColDiff = Math.abs(endCol - startCol);

        return RowDiff <= 1 && ColDiff <= 1;
    }

    @Override
    public int getDrawableResId() {return isWhite ? R.drawable.w_king : R.drawable.b_king;}

    @NonNull
    @Override
    public Piece clone() {
        return new King(this.isWhite);
    }
}

