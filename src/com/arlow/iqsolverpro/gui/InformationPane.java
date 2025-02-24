package com.arlow.iqsolverpro.gui;

import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.io.ResultsWriter;
import com.arlow.iqsolverpro.solver.SolveStats;

import javafx.scene.layout.AnchorPane;

public class InformationPane extends AnchorPane {
    BasePanel currentPanel;
    GameInstance game;

    public InformationPane() {
        currentPanel = new ConfigPanel();
        currentPanel.attach(this);

        this.setMaxSize(400, BASELINE_OFFSET_SAME_AS_HEIGHT);
        this.setMinSize(200, BASELINE_OFFSET_SAME_AS_HEIGHT);
    }
    
    public void UpdateGame(GameInstance game) {
        this.game = game;

        if (currentPanel != null) currentPanel.close();
        
        if (game == null) {
            currentPanel = new ConfigPanel();
        } else {
            currentPanel = new SetupPanel(game, this);
        }

        currentPanel.attach(this);
    }

    public void Solve() {
        if (currentPanel != null) currentPanel.close();

        if (game == null) {
            currentPanel = new ConfigPanel();
        } else {
            currentPanel = new RunningPanel(game, this);
        }

        currentPanel.attach(this);
    }

    public void ShowResults(SolveStats solveStats) {
        if (currentPanel != null) currentPanel.close();

        if (game == null) {
            currentPanel = new ConfigPanel();
        } else {
            currentPanel = new ResultsPanel(game, this, solveStats);
        }

        currentPanel.attach(this);
    }
}
