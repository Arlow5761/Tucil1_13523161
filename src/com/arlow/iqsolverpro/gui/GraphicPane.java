package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.RectangularBoard;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GraphicPane extends AnchorPane {
    private RectangularBoardPanel currentRectangularBoardPanel;
    private CustomBoardPanel currentCustomBoardPanel;
    
    public GraphicPane() {
        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void UpdateGame(GameInstance game) {
        if (currentRectangularBoardPanel != null) {
            currentRectangularBoardPanel.close();
            currentRectangularBoardPanel = null;
        }

        if (currentCustomBoardPanel != null) {
            currentCustomBoardPanel.close();
            currentCustomBoardPanel = null;
        }

        if (game == null) return;
        
        if (game.board instanceof CustomBoard) {
            currentCustomBoardPanel = new CustomBoardPanel(game);
            currentCustomBoardPanel.attach(this);
        } else if (game.board instanceof RectangularBoard) {
            currentRectangularBoardPanel = new RectangularBoardPanel(game);
            currentRectangularBoardPanel.attach(this);
        }

    }
}
