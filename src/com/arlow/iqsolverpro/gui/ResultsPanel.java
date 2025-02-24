package com.arlow.iqsolverpro.gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameEventListener;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;
import com.arlow.iqsolverpro.game.RectangularBoard;
import com.arlow.iqsolverpro.io.ResultsWriter;
import com.arlow.iqsolverpro.solver.SolveStats;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

public class ResultsPanel extends BasePanel {
    private SolveStats solveStats;
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

    private EventHandler<ActionEvent> onSaveClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save As");

            File file = fileChooser.showSaveDialog(self.getScene().getWindow());

            if (file == null) return;

            try {
                OutputStream outputStream = new FileOutputStream(file);
                ResultsWriter.WriteResults(game, solveStats, outputStream);
                outputStream.close();
            } catch(Exception e) {}
        }
    };

    private EventHandler<ActionEvent> onCancelClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
            Main.ReloadGame(null);
        }
    };

    public ResultsPanel(GameInstance game, InformationPane informationPane, SolveStats solveStats) {
        super(new VBox());

        this.solveStats = solveStats;
        this.informationPane = informationPane;
        this.game = game;

        ((VBox) self).setSpacing(10);
        ((VBox) self).setAlignment(Pos.CENTER);

        ByteArrayOutputStream resultsOutput = new ByteArrayOutputStream();
        try {
            ResultsWriter.PrintResults(solveStats, resultsOutput);
        } catch (Exception e) {}

        String resultsString = resultsOutput.toString();

        Label resultsLabel = new Label(resultsString);
        resultsLabel.maxWidthProperty().bind(self.widthProperty());
        resultsLabel.setWrapText(true);
        resultsLabel.setTextAlignment(TextAlignment.CENTER);
        resultsLabel.setAlignment(Pos.CENTER);

        self.getChildren().add(resultsLabel);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(onSaveClicked);
        saveButton.setAlignment(Pos.CENTER);

        self.getChildren().add(saveButton);
        
        Button cancelButton = new Button("Back");
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
