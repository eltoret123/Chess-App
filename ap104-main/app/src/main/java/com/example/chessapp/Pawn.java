package com.example.chessapp;

import androidx.annotation.NonNull;

public class Pawn implements Piece {

    private final boolean isWhite;

    public Pawn(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String getType() {
        return "P";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int direction = isWhite() ? -1 : 1;
        int expectedStartRow = isWhite() ? 6 : 1;

        if (Utils.outOfBounds(endRow, endCol)){return false;}

        if (Utils.sameSquareMove(startRow, endRow, startCol, endCol)){return false;}

        if (Utils.isMoveToSameColorPiece(endRow, endCol, isWhite(), board)){
            return false;
        }

        if (startCol == endCol && endRow == startRow + direction){ // check single move
            return board[endRow][endCol] == null;
        }

        if (startCol == endCol &&
            startRow == expectedStartRow &&
            endRow == startRow + 2* direction){ // Check double move
            return board[startRow + direction][endCol] == null &&
                    board[endRow][endCol] == null;
        }

        if (Math.abs(endCol-startCol) == 1 &&
            endRow == startRow + direction &&
            board[endRow][endCol] != null){ // Check normal capture
            return board[endRow][endCol].isWhite() != isWhite();
        }

        return false;
    }

    public boolean rowBoundary(int row){
        return row == 0 || row == 7;
    }

    @Override
    public int getDrawableResId() {
        return isWhite ? R.drawable.w_pawn : R.drawable.b_pawn;
    }

    @NonNull
    @Override
    public Piece clone() {
        return new Pawn(this.isWhite);
    }
}

