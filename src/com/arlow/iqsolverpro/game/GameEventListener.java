package com.arlow.iqsolverpro.game;

public abstract class GameEventListener {
    public GameInstance.EventType targetEventType;

    public GameEventListener(GameInstance.EventType targetEvent) {
        targetEventType = targetEvent;
    }

    public abstract void Notify(Piece piece);
}
