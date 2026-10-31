package com.example.chessapp;

import androidx.annotation.NonNull;

public class Queen implements Piece{
    private final boolean isWhite;
    private final Rook RookMoves;
    private final Bishop BishopMoves;


    public Queen(boolean isWhite){
        this.BishopMoves = new Bishop(isWhite);
        this.RookMoves = new Rook(isWhite);
        this.isWhite = isWhite;
    }
    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String getType() {
        return "Q";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        return RookMoves.isValidMove(startRow, startCol, endRow, endCol, board) ||
                BishopMoves.isValidMove(startRow, startCol, endRow, endCol, board);
    }

    @Override
    public int getDrawableResId() {return isWhite ? R.drawable.w_queen : R.drawable.b_queen;}

    @NonNull
    @Override
    public Piece clone() {
        return new Queen(this.isWhite);
    }
}
