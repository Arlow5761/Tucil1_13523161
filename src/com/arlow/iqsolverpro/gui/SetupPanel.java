package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameEventListener;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;
import com.arlow.iqsolverpro.game.RectangularBoard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class SetupPanel extends BasePanel {
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

    private EventHandler<ActionEvent> onSolveClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            informationPane.Solve();
        }
    };

    private EventHandler<ActionEvent> onCancelClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            Main.ReloadGame(null);
        }
    };

    public SetupPanel(GameInstance game, InformationPane informationPane) {
        super(new VBox());

        this.informationPane = informationPane;
        this.game = game;

        ((VBox) self).setSpacing(10);
        ((VBox) self).setAlignment(Pos.CENTER);

        String boardType;
        if (game.board instanceof RectangularBoard) {
            boardType = "DEFAULT";
        } else if (game.board instanceof CustomBoard) {
            boardType = "CUSTOM";
        } else {
            boardType = "UNKNOWN";
        }

        Label boardLabel = new Label("Board Type: " + boardType);
        boardLabel.maxWidthProperty().bind(self.widthProperty());
        boardLabel.setWrapText(true);
        boardLabel.setTextAlignment(TextAlignment.CENTER);
        boardLabel.setAlignment(Pos.CENTER);

        self.getChildren().add(boardLabel);

        Label dimensionLabel = new Label("Board Dimension: " + String.valueOf(((RectangularBoard) game.board).width) + "X" + String.valueOf(((RectangularBoard) game.board).length));
        dimensionLabel.maxWidthProperty().bind(self.widthProperty());
        dimensionLabel.setWrapText(true);
        dimensionLabel.setTextAlignment(TextAlignment.CENTER);
        dimensionLabel.setAlignment(Pos.CENTER);

        self.getChildren().add(dimensionLabel);

        Label pieceCountLabel = new Label("Pieces: " + String.valueOf(game.availablePieces.size()));
        pieceCountLabel.maxWidthProperty().bind(self.widthProperty());
        pieceCountLabel.setWrapText(true);
        pieceCountLabel.setTextAlignment(TextAlignment.CENTER);
        pieceCountLabel.setAlignment(Pos.CENTER);

        self.getChildren().add(pieceCountLabel);

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(onSolveClicked);
        solveButton.setAlignment(Pos.CENTER);

        self.getChildren().add(solveButton);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(onCancelClicked);
        cancelButton.setAlignment(Pos.CENTER);

        self.getChildren().add(cancelButton);

        piecesPanel = new PiecesPanel(this.game);
        piecesPanel.setMaxHeight(300);

        self.getChildren().add(piecesPanel);
    }

    @Override
    public void attach(Pane root) {
        super.attach(root);
        piecesPanel.prefWidthProperty().bind(root.widthProperty());
        game.AddListener(piecePlacedListener);
        game.AddListener(pieceRemovedListener);
    }

    @Override
    public void close() {
        super.close();
        piecesPanel.prefWidthProperty().unbind();
        game.RemoveListener(piecePlacedListener);
        game.RemoveListener(pieceRemovedListener);
    }
}
