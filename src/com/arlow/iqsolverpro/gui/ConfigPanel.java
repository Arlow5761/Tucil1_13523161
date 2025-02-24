package com.arlow.iqsolverpro.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.arlow.iqsolverpro.io.ConfigReader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

public class ConfigPanel extends BasePanel {
    public ConfigPanel() {
        super(new VBox());
        
        AnchorPane.setTopAnchor(self, 0.0d);
        AnchorPane.setLeftAnchor(self, 0.0d);
        AnchorPane.setBottomAnchor(self, 0.0d);
        AnchorPane.setRightAnchor(self, 0.0d);

        ((VBox) self).setAlignment(Pos.CENTER);
        ((VBox) self).setSpacing(10);
        ((VBox) self).setMinHeight(200);

        Label welcomeText = new Label("Welcome! Please load a config file to start the solver.");
        welcomeText.maxWidthProperty().bind(self.widthProperty());
        welcomeText.setWrapText(true);
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setAlignment(Pos.CENTER);

        self.getChildren().add(welcomeText);

        Button importButton = new Button("Import Config File");
        importButton.setOnAction(importButtonClickedHandler);
        
        self.getChildren().add(importButton);
    }

    private EventHandler<ActionEvent> importButtonClickedHandler = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select config file");

            File file = fileChooser.showOpenDialog(self.getScene().getWindow());

            if (file == null) return;
            
            try {
                InputStream fileStream = new FileInputStream(file);
                Main.ReloadGame(ConfigReader.GenerateGame(fileStream));
                fileStream.close();
            } catch (Exception exception) {
                String message = exception.getMessage();

                Alert alert = new Alert(AlertType.ERROR);

                if (message != null) {
                    alert.setContentText(message);
                }

                alert.show();
            }
        }
    };
}
