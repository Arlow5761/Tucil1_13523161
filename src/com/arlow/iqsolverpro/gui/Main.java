package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.GameInstance;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application{
    private static GameInstance game;
    private static InformationPane informationPane;
    private static GraphicPane graphicPane;

    public static void main(String[] args) {
        launch(args);
    }

    public static void ReloadGame(GameInstance game) {
        Main.game = game;
        PieceColoringService.Reset();
        graphicPane.UpdateGame(Main.game);
        informationPane.UpdateGame(Main.game);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);

        informationPane = new InformationPane();
        graphicPane = new GraphicPane();
        splitPane.getItems().addAll(informationPane, graphicPane);

        splitPane.setDividerPositions(0.3);
        splitPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            splitPane.setDividerPositions(0.3);
        });
        
        Scene scene = new Scene(splitPane, 800, 400);

        stage.setTitle("IQ Solver Pro");
        stage.setScene(scene);
        stage.show();

        ReloadGame(null);
    }
}
