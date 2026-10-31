package com.example.chessapp;

import androidx.annotation.NonNull;

public interface Piece extends Cloneable{

    boolean isWhite();

    String getType();

    boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board);

    int getDrawableResId();

    @NonNull
    Piece clone();
}
