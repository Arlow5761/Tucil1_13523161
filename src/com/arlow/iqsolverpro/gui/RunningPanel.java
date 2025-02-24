package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameEventListener;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;
import com.arlow.iqsolverpro.game.RectangularBoard;
import com.arlow.iqsolverpro.solver.BruteforceSolver;
import com.arlow.iqsolverpro.solver.SolveStats;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class RunningPanel extends BasePanel {
    private InformationPane informationPane;
    private GameInstance game;
    private PiecesPanel piecesPanel;

    private GameEventListener piecePlacedListener = new GameEventListener(GameInstance.EventType.PiecePlaced) {
        public void Notify(Piece piece) {
            piecesPanel.RerenderPieces();
        }
    };

    private GameEventListener pieceRemovedListener = new GameEventListener(GameInstance.EventType.PieceRemoved) {
        public void Notify(Piece piece) {
            piecesPanel.RerenderPieces();
        }
    };

    public RunningPanel(GameInstance game, InformationPane informationPane) {
        super(new VBox());

        this.game = game;
        this.informationPane = informationPane;

        ((VBox) self).setSpacing(10);
        ((VBox) self).setAlignment(Pos.CENTER);

        Label pieceCountLabel = new Label("Running solver, please wait");
        pieceCountLabel.maxWidthProperty().bind(self.widthProperty());
        pieceCountLabel.setWrapText(true);
        pieceCountLabel.setTextAlignment(TextAlignment.CENTER);
        pieceCountLabel.setAlignment(Pos.CENTER);

        self.getChildren().add(pieceCountLabel);

        piecesPanel = new PiecesPanel(this.game);
        piecesPanel.setPrefHeight(300);

        self.getChildren().add(piecesPanel);
    }

    @Override
    public void attach(Pane root) {
        super.attach(root);
        piecesPanel.prefWidthProperty().bind(root.widthProperty());
        game.AddListener(piecePlacedListener);
        game.AddListener(pieceRemovedListener);

        BruteforceSolver solver = new BruteforceSolver(game, 0);
        SolveStats result = solver.Solve();
        informationPane.ShowResults(result);
    }

    @Override
    public void close() {
        super.close();
        piecesPanel.prefWidthProperty().unbind();
        game.RemoveListener(piecePlacedListener);
        game.RemoveListener(pieceRemovedListener);
    }
}
