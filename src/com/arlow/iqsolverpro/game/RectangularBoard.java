package com.arlow.iqsolverpro.game;

import java.util.ArrayList;
import java.util.List;

public class RectangularBoard implements Board {
    public Piece[] slots;
    public int width;
    public int length;

    public RectangularBoard(int width, int length) {
        this.width = width;
        this.length = length;

        slots = new Piece[width * length];
    }

    @Override
    public int[] GetEmptyPositions() {
        List<Integer> emptySlots = new ArrayList<Integer>();

        for (int i = 0; i < slots.length; i++)
        {
            if (slots[i] == null) emptySlots.add(i);
        }

        int[] emptySlotsArray = new int[emptySlots.size()]; 
        
        for (int i = 0; i < emptySlots.size(); i++)
        {
            emptySlotsArray[i] = emptySlots.get(i);
        }

        return emptySlotsArray;
    }

    @Override
    public Plane[] GetPlanesOnPosition(int position) {
        Plane plane = new Plane();
        plane.source = this;

        plane.positionMap = new int[length][width];

        for (int i = 0; i < width * length; i++)
        {
            plane.positionMap[i / width][i % width] = i;
        }

        return new Plane[] {plane};
    }

    @Override
    public Piece GetPieceOnPosition(int position) {
        return slots[position];
    }

    @Override
    public boolean PlacePiece(Piece piece, Plane plane) {
        if (!plane.PieceFits(piece)) {
            return false;
        }

        int offsetX = -1;
        int offsetY = -1;

        for (int j = 0; j < plane.positionMap.length; j++) {
            for (int i = 0; i < plane.positionMap[j].length; i++) {
                if (plane.positionMap[j][i] == piece.position) {
                    offsetX = i;
                    offsetY = j;
                }
            }
        }

        for (int j = 0; j < piece.shape.length; j++) {
            for (int i = 0; i < piece.shape[j].length; i++) {
                int posX = offsetX + i - piece.pivotX;
                int posY = offsetY + j - piece.pivotY;

                if (piece.shape[j][i]) {
                    slots[plane.positionMap[posY][posX]] = piece;
                }
            }
        }

        return true;
    }

    @Override
    public boolean RemovePiece(Piece piece) {
        boolean pieceFound = false;

        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == piece) {
                slots[i] = null;
                pieceFound = true;
            }
        }

        return pieceFound;
    }
}
