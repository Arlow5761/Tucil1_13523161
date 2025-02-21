package com.arlow.iqsolverpro.game;

public abstract class GameEventListener {
    public GameInstance.EventType targetEventType;

    public abstract void Notify(Piece piece);
}
