package com.arlow.iqsolverpro.game;

public interface Board {
    public int[] GetEmptyPositions();

    public Plane[] GetPlanesOnPosition(int position);

    public Piece GetPieceOnPosition(int position);

    public boolean PlacePiece(Piece piece, Plane plane);

    public boolean RemovePiece(Piece piece);
}
