package com.arlow.iqsolverpro.gui;

import javafx.scene.layout.Pane;

public class BasePanel {
    protected Pane self;
    protected Pane root;

    public BasePanel() {
        self = new Pane();
    }

    public BasePanel(Pane pane) {
        self = pane;
    }

    public void attach(Pane root) {
        this.root = root;
        root.getChildren().add(self);
    }

    public void close() {
        root.getChildren().remove(self);
        this.root = null;
    }

    protected void open(BasePanel newPanel) {
        Pane root = this.root;
        close();
        newPanel.attach(root);
    }
}
