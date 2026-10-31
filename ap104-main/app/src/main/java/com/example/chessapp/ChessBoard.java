package com.example.chessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChessBoard extends View {
    private static final int SIZE = 8;
    private Paint whitePaint;
    private Paint blackPaint;
    public Piece[][] board;
    private String lastMove = "";
    private TextView AI = null;

    public ChessBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeBoard();
        initDraw();
        invalidate();
    }
    public void setAI(TextView aiBox){
        AI=aiBox;
    }

    private void initializeBoard(){
        board = new Piece[8][8];
        // Black Pieces
        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][4] = new King(false);
        board[0][3] = new Queen(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);
        board[1][0] = new Pawn(false);
        board[1][1] = new Pawn(false);
        board[1][2] = new Pawn(false);
        board[1][3] = new Pawn(false);
        board[1][4] = new Pawn(false);
        board[1][5] = new Pawn(false);
        board[1][6] = new Pawn(false);
        board[1][7] = new Pawn(false);

        for (int x =2; x <=5; x++){
            for (int y=0;y<=7;y++){
                board[x][y] = null;
            }
        }

        // White Pieces
        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][4] = new King(true);
        board[7][3] = new Queen(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);
        board[6][0] = new Pawn(true);
        board[6][1] = new Pawn(true);
        board[6][2] = new Pawn(true);
        board[6][3] = new Pawn(true);
        board[6][4] = new Pawn(true);
        board[6][5] = new Pawn(true);
        board[6][6] = new Pawn(true);
        board[6][7] = new Pawn(true);
    }

    private void initDraw() {
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        blackPaint = new Paint();
        blackPaint.setColor(Color.CYAN);
    }

    private boolean flipped;

    public void setFlipped(boolean flipped){
        this.flipped = flipped;
    }

    public boolean flipped(){
        return flipped;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int tileSize = Math.min(width, height) / SIZE;
        canvas.translate(0,300);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                // flipped logic
                int drawRow = flipped() ? SIZE - row - 1: row;
                int drawCol = flipped() ? SIZE - col - 1: col;

                // Draw the board square
                Paint paint = (row + col) % 2 == 0 ? whitePaint : blackPaint;
                int left = drawCol * tileSize;
                int top = drawRow * tileSize;
                int right = left + tileSize;
                int bottom = top + tileSize;
                canvas.drawRect(left, top, right, bottom, paint);

                // Draw the piece if present
                if (board != null && board[row][col] != null) {
                    Drawable pieceDrawable = ContextCompat.getDrawable(getContext(), board[row][col].getDrawableResId());
                    if (pieceDrawable != null) {
                        pieceDrawable.setBounds(left, top, right, bottom);
                        pieceDrawable.draw(canvas);
                    }
                }
            }
        }
    }

    private boolean PieceSelected = false;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Piece selectedPiece = null;
    private RequestQueue requestQueue;

    public Piece[][] cloneBoard(Piece[][] board) {
        Piece[][] newBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    newBoard[i][j] = board[i][j].clone();
                }
            }
        }
        return newBoard;
    }

    public boolean isKingInCheck(boolean isWhite){
        int KingRow = -1;
        int KingCol = -1;

        // Find own King
        for (int i = 0; i < 8; i++){
            for (int j =0; j < 8; j++){
                if (board[i][j] != null && board[i][j].isWhite() == isWhite && board[i][j] instanceof King){
                    KingRow = i;
                    KingCol = j;
                }
            }
        }

        for (int i=0; i< 8; i++){
            for (int j=0; j<8;j++){
                if (board[i][j] != null && board[i][j].isWhite() != isWhite && board[i][j].isValidMove(i, j, KingRow, KingCol, board)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isKingInCheckOnBoard(boolean isWhite, Piece[][] testBoard) {
        // Same logic as isKingInCheck but uses testBoard (clone of current board state) instead of board
        int KingRow = -1;
        int KingCol = -1;

        // Find own King
        for (int i = 0; i < 8; i++){
            for (int j =0; j < 8; j++){
                if (testBoard[i][j] != null && testBoard[i][j].isWhite() == isWhite && testBoard[i][j] instanceof King){
                    KingRow = i;
                    KingCol = j;
                }
            }
        }

        for (int i=0; i< 8; i++){
            for (int j=0; j<8;j++){
                if (testBoard[i][j] != null && testBoard[i][j].isWhite() != isWhite && testBoard[i][j].isValidMove(i, j, KingRow, KingCol, testBoard)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckMate(boolean isWhite){
        if(!isKingInCheck(isWhite)){return false;}

        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece p = board[fromRow][fromCol];
                if (p != null) {
                    if (p.isWhite() == isWhite){
                        for (int toRow = 0; toRow < 8; toRow++) {
                            for (int toCol = 0; toCol < 8; toCol++) {
                                if (p.isValidMove(fromRow, fromCol, toRow, toCol, board)) {

                                    // Cloning the board before simulating the move
                                    Piece[][] testBoard = cloneBoard(board);

                                    testBoard[toRow][toCol] = testBoard[fromRow][fromCol];
                                    testBoard[fromRow][fromCol] = null;

                                    if (!isKingInCheckOnBoard(isWhite, testBoard)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        return true;
    }

    public boolean isMoveResultingInCheck(int fromRow, int fromCol, int toRow, int toCol, boolean isWhite) {
        Piece[][] testBoard = cloneBoard(board);

        // Simulate move
        testBoard[toRow][toCol] = testBoard[fromRow][fromCol];
        testBoard[fromRow][fromCol] = null;

        // Check if king is in check after move
        return isKingInCheckOnBoard(isWhite, testBoard);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tileSize = Math.min(getWidth(), getHeight()) / SIZE;
            int col = (int) (event.getX() / tileSize);
            int row = (int) ((event.getY() - 300) / tileSize);

            if (flipped) {
                row = SIZE - 1 - row;
                col = SIZE - 1 - col;
            }


            if (Utils.outOfBounds(row, col)) {
                return true;
            }

            if (!PieceSelected) {
                Piece piece = board[row][col];
                if (piece != null  ) {
                    selectedPiece = piece;
                    selectedCol = col;
                    selectedRow = row;
                    PieceSelected = true;
                }
            } else {
                if (selectedPiece != null && selectedPiece.isValidMove(selectedRow, selectedCol, row, col, board) && (GameSession.getInstance().getColour().equals("White") == selectedPiece.isWhite()) && GameSession.getInstance().getMyMove()) {

                    if (isMoveResultingInCheck(selectedRow, selectedCol, row, col, GameSession.getInstance().getColour().equals("White"))) {
                        // Illegal move: puts or leaves your king in check
                        return true;
                    }

                    if (selectedPiece instanceof Rook){ // Rook makes move --> castling not possible
                        ((Rook) selectedPiece).setRookMoved(true);
                        board[row][col] = selectedPiece;
                    }

                    if (selectedPiece instanceof King && (Math.abs(col - selectedCol) == 1 || Math.abs(row-selectedRow) == 1)){ // King makes normal move --> castling not possible
                        ((King) selectedPiece).setKingMoved(true);
                        board[row][col] = selectedPiece;
                    }


                    if (selectedPiece instanceof King && ((King) selectedPiece).notKingMoved() && Math.abs(col - selectedCol) == 2){
                        ((King) selectedPiece).setKingMoved(true);
                        if (col > selectedCol){ // Castle KingSide
                            board[row][6] = board[row][4];
                            board[row][5] = board[row][7];
                            board[row][4] = null;
                            board[row][7] = null;
                        }
                        else if (col < selectedCol){ // Castle QueenSide
                            board[row][2] = board[row][4];
                            board[row][3] = board[row][0];
                            board[row][4] = null;
                            board[row][0] = null;
                        }
                    }

                    if (selectedPiece instanceof Pawn && ((Pawn) selectedPiece).rowBoundary(row)) { // Pawn promotion
                        board[row][col] = new Queen(selectedPiece.isWhite());
                        String Move = "Q" + String.valueOf(row)+String.valueOf(col)+String.valueOf(selectedRow)+String.valueOf(selectedCol);
                        lastMove = Move;
                        MakeMove(Move,AI);
                    } else { // Normal Moves
                        board[row][col] = selectedPiece;
                        String Move = selectedPiece.getType() + String.valueOf(row)+String.valueOf(col)+String.valueOf(selectedRow)+String.valueOf(selectedCol);
                        lastMove = Move;
                        MakeMove(Move, AI);
                    }


                    board[selectedRow][selectedCol] = null;

                    // Reset selection after a valid move
                    PieceSelected = false;
                    selectedPiece = null;
                    selectedRow = -1;
                    selectedCol = -1;

                    invalidate();
                }
                // If move is invalid reset
                PieceSelected = false;
                selectedPiece = null;
                selectedRow = -1;
                selectedCol = -1;
            }
        }

            return true;
    }

    public void callInvalidate(){
        invalidate();
    }

    public void MakeMove(String Move, TextView AI) {
        requestQueue = Volley.newRequestQueue(getContext());
        ArrayList<String> responseString = new ArrayList<>();
        String requestURL = "https://studev.groept.be/api/a24pt104/MakeMove/"+Move+"/" + GameSession.getInstance().getID();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);

                        }
                        String LongMoves = GameSession.getInstance().getMovesString() + Move;
                        GameSession.getInstance().setMovesString(LongMoves);

                        // Use the ChatGPT reply here
                        ChatGPTRequest.sendMessage(getContext(),"You are given a series of chess moves in a custom format. The board uses 0-indexed coordinates where (0,0) is the top-left and (7,7) is the bottom-right. Each move is written as a 5-character string:The first character represents the piece:P = Pawn, R = Rook, N = Knight, B = Bishop, Q = Queen, K = King.The next two digits are the final position (row, column).The last two digits are the starting position (row, column).Example: \\\"P6242\\\" = Pawn moves from (4,2) to (6,2).Additional rules:White always moves first.A king castling involving two steps (e.g., \\\"K7674\\\"). The black queen starts at position (0,3).The white king starts at position (7,4).For each move, analyze its quality based on tactical soundness, positional value, and possible threats or blunders. Assume standard chess rules apply. Only analyse "+GameSession.getInstance().getColour()+" position in under 30 words. The first 5 char are white the next one are black. The first char in your response should be a number 1-9 depending on how good the move was.Only talk about the last move that this player made. Refer to the chess move with the standard chess notation where (0,0) is (a,8) and (7,7) is (h,1) do not use the characters I'm providing. " + GameSession.getInstance().getMovesString(), KEY HAS BEEN REMOVED FOR SECURITY REASONS,
                                AI::setText);
                        GameSession.getInstance().setMyMove(false);
                    } catch (JSONException e) {
                        Log.e("Database", e.getMessage(), e);
                    }
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

    }



    public void requestMove() {
        requestQueue = Volley.newRequestQueue(getContext());
        ArrayList<String> responseString = new ArrayList<>();
        String requestURL = "https://studev.groept.be/api/a24pt104/RetrieveMove/" + GameSession.getInstance().getID();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        for (int i = 0; i < 1; i++) {
                            JSONObject curObject = response.getJSONObject(i);
                            responseString.add(curObject.getString("Move"));
                        }
                        if (responseString.size()==1 && !lastMove.equals(responseString.get(0)))
                        {
                            responseStringToMoveMade(responseString);
                            lastMove= responseString.get(0);
                            GameSession.getInstance().setMyMove(true);
                            String move = GameSession.getInstance().getMovesString() + responseString;
                            GameSession.getInstance().setMovesString(move);
                            invalidate();
                        }


                    } catch (JSONException e) {
                        Log.e("Database", e.getMessage(), e);
                    }
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

    }

    private void responseStringToMoveMade(ArrayList<String> responseString) {
        char PiecefromString = responseString.get(0).charAt(0);

        char charFinishRow = responseString.get(0).charAt(1);
        int finishRow = Character.getNumericValue(charFinishRow);

        char charFinishCol = responseString.get(0).charAt(2);
        int finishCol = Character.getNumericValue(charFinishCol);

        char charStartRow = responseString.get(0).charAt(4);
        int startRow = Character.getNumericValue(responseString.get(0).charAt(3));


        board[startRow][Character.getNumericValue(charStartRow)] = null;
        if(PiecefromString == 'Q')
        {
            board[finishRow][finishCol] = new Queen(!GameSession.getInstance().getColour().equals("White"));
        } else if (PiecefromString == 'P') {
            board[finishRow][finishCol] = new Pawn(!GameSession.getInstance().getColour().equals("White"));
        } else if (PiecefromString == 'K') {
            if (charFinishCol == '6' && charStartRow == '4' && charFinishRow ==  '7')
            {
                board[finishRow][finishCol] = new King(!GameSession.getInstance().getColour().equals("White"));
                board[7][7] = null;
                board[7][5] = new Rook(!GameSession.getInstance().getColour().equals("White"));
            } else if (charFinishCol == '2' && charStartRow == '4' && charFinishRow ==  '7') {
                board[finishRow][finishCol] = new King(!GameSession.getInstance().getColour().equals("White"));
                board[7][0] = null;
                board[7][3] = new Rook(!GameSession.getInstance().getColour().equals("White"));
            } else if (charFinishCol == '6' && charStartRow == '4' && charFinishRow ==  '0')
            {
                board[finishRow][finishCol] = new King(!GameSession.getInstance().getColour().equals("White"));
                board[0][7] = null;
                board[0][5] = new Rook(!GameSession.getInstance().getColour().equals("White"));
            } else if (charFinishCol == '2' && charStartRow == '4' && charFinishRow ==  '0') {
                board[finishRow][finishCol] = new King(!GameSession.getInstance().getColour().equals("White"));
                board[0][0] = null;
                board[0][3] = new Rook(!GameSession.getInstance().getColour().equals("White"));
            }
            else {
                board[finishRow][finishCol] = new King(!GameSession.getInstance().getColour().equals("White"));
            }

        } else if (PiecefromString == 'R') {
            board[finishRow][finishCol] = new Rook(!GameSession.getInstance().getColour().equals("White"));
        } else if (PiecefromString == 'B') {
            board[finishRow][finishCol] = new Bishop(!GameSession.getInstance().getColour().equals("White"));
        } else if (PiecefromString == 'N') {
            board[finishRow][finishCol] = new Knight(!GameSession.getInstance().getColour().equals("White"));
        }
    }

    public void resetBoard() {
        initializeBoard();     // Refill the board with initial positions
        PieceSelected = false; // Clear any selection
        selectedRow = -1;
        selectedCol = -1;
        selectedPiece = null;
        invalidate();          // Redraw the board
    }



}

