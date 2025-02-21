package com.arlow.iqsolverpro.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInstance {
    enum EventType {
        PiecePlaced,
        PieceRemoved,
        BoardFull
    }

    public List<Piece> availablePieces;
    public Board board;

    private List<GameEventListener> listeners;
    private List<GameEventListener> listenerAddList;
    private List<GameEventListener> listenerRemoveList;

    public GameInstance(Piece[] pieces, Board board) {
        this.availablePieces = new ArrayList<Piece>(Arrays.asList(pieces));
        this.board = board;
    }

    public boolean PlacePiece(Piece piece, Plane plane) {
        if (board.PlacePiece(piece, plane)) {
            availablePieces.remove(piece);
            NotifyListeners(EventType.PiecePlaced, piece);

            if (board.GetEmptyPositions().length < 1) {
                NotifyListeners(EventType.BoardFull, null);
            }

            return true;
        }

        return false;
    }

    public boolean RemovePiece(Piece piece) {
        if (board.RemovePiece(piece)) {
            availablePieces.add(piece);
            NotifyListeners(EventType.PieceRemoved, piece);

            return true;
        }

        return false;
    }

    public boolean AddListener(GameEventListener listener) {
        if (listeners.contains(listener)) return false;

        listenerAddList.add(listener);

        return true;
    }

    public boolean RemoveListener(GameEventListener listener) {
        if (!listeners.contains(listener)) return false;

        listenerRemoveList.add(listener);

        return true;
    }

    private void NotifyListeners(EventType type, Piece piece) {
        for (int i = 0; i < listeners.size(); i++) {
            GameEventListener current = listeners.get(i);

            if (current.targetEventType == type) {
                current.Notify(piece);
            }
        }

        while (!listenerRemoveList.isEmpty()) {
            listeners.remove(listenerRemoveList.get(listenerRemoveList.size() - 1));
            listenerRemoveList.remove(listenerRemoveList.size() - 1);
        }

        while (!listenerAddList.isEmpty()) {
            listeners.add(listenerAddList.get(listenerAddList.size() - 1));
            listenerAddList.remove(listenerAddList.size() - 1);
        }
    }
}
