package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class PiecesPanel extends ScrollPane {
    private GameInstance game;
    private FlowPane piecesPane;

    public PiecesPanel(GameInstance game) {
        this.game = game;

        piecesPane = new FlowPane(Orientation.HORIZONTAL);
        piecesPane.maxWidthProperty().bind(this.widthProperty());
        piecesPane.setVgap(10);
        piecesPane.setHgap(10);

        RerenderPieces();

        this.setContent(piecesPane);
    }

    public void RerenderPieces() {
        piecesPane.getChildren().clear();

        for (Piece piece : game.availablePieces) {
            PieceDiagram pieceDiagram = new PieceDiagram(piece, PieceColoringService.GetColor(piece));
            pieceDiagram.setPrefWidth(100);
            pieceDiagram.setPrefHeight(100);
            pieceDiagram.setAlignment(Pos.CENTER);

            piecesPane.getChildren().add(pieceDiagram);
        }
    }
}
