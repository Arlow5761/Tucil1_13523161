package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameEventListener;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CustomBoardPanel {
    private GameInstance game;
    private Pane root;
    private GridPane grid;

    private GameEventListener piecePlacedListener = new GameEventListener(GameInstance.EventType.PiecePlaced) {
        public void Notify(Piece piece) {
            RerenderBoard();
        }
    };

    private GameEventListener pieceRemovedListener = new GameEventListener(GameInstance.EventType.PieceRemoved) {
        public void Notify(Piece piece) {
            RerenderBoard();
        }
    };

    public CustomBoardPanel(GameInstance game) {
        this.game = game;
    }

    public void attach(Pane root) {
        this.root = root;

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        root.getChildren().add(grid);

        RerenderBoard();

        game.AddListener(piecePlacedListener);
        game.AddListener(pieceRemovedListener);
    }

    public void close() {
        root.getChildren().remove(grid);

        game.RemoveListener(piecePlacedListener);
        game.RemoveListener(pieceRemovedListener);
    }

    public void RerenderBoard() {
        CustomBoard board = (CustomBoard) game.board;

        grid.getChildren().clear();

        for (int j = 0; j < board.positionMap.length; j++) {
            for (int i = 0; i < board.positionMap[j].length; i++) {
                if (board.positionMap[j][i] >= 0) {
                    Piece piece = board.GetPieceOnPosition(board.positionMap[j][i]);

                    Circle circle = new Circle();
                    circle.setRadius(20);
                    circle.setStroke(Color.WHITE);

                    if (piece != null) {
                        circle.setFill(PieceColoringService.GetColor(piece));
                    }
                
                    grid.add(circle, i, j);
                } 
            }
        }
    }
}
