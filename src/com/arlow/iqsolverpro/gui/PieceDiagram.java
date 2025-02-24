package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.Piece;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PieceDiagram extends GridPane {
    public Piece piece;

    public PieceDiagram(Piece piece, Color color) {
        this.piece = piece;

        for (int j = 0; j < piece.shape.length; j++) {
            for (int i = 0; i < piece.shape[j].length; i++) {
                if (!piece.shape[j][i]) continue;

                Circle circle = new Circle();
                circle.radiusProperty().bind(
                    Bindings.max(
                        Bindings.min(
                            Bindings.divide(prefWidthProperty(), 2 * piece.shape[j].length),
                            Bindings.divide(prefHeightProperty(), 2 * piece.shape.length)
                        ),
                        10
                    ));
                circle.setFill(color);

                this.add(circle, i, j);
            }
        }
    }
}
