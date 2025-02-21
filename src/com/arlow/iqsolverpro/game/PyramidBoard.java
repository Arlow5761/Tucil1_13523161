package com.arlow.iqsolverpro.game;

import java.util.ArrayList;
import java.util.List;

public class PyramidBoard implements Board {
    public Piece[] slots;
    public int[] layerSeparators;
    public int size;

    public PyramidBoard(int size) {
        this.size = size;
        this.layerSeparators = new int[size];

        int counter = 0;
        for (int i = size; i > 0; i++) {
            counter += i * i;
            layerSeparators[size - i] = counter;
        }

        slots = new Piece[counter];
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
        int layer = GetLayer(position);

        int[] layerCoords = ToLayerCoords(position, layer);
        int layerX = layerCoords[0];
        int layerY = layerCoords[1];

        Plane flatPlane =  new Plane();
        flatPlane.source = this;
        flatPlane.positionMap = new int[size - layer][size - layer];

        int normalOffset = 4 - (layerSeparators[layer - 1] % (size - layer));
        int normalBase = layerSeparators[layer - 1] - (layerSeparators[layer - 1] % (size - layer));
        for (int pos = layerSeparators[layer - 1]; pos < layerSeparators[layer]; pos++) {
            int y = (pos - normalBase) / (size - layer);
            int x = (pos + normalOffset) % (size - layer);
            flatPlane.positionMap[y][x] = pos;
        }

        Plane diagonalPlane1 = new Plane();
        diagonalPlane1.source = this;

        int diagonalConstant1 = layerX + layerY + layer;
        int diagonalSize1 = size - Math.abs(diagonalConstant1 + 1 - size);

        diagonalPlane1.positionMap = new int[diagonalSize1][diagonalSize1];

        for (int j = 0; j < diagonalSize1; j++) {
            for (int i = 0; i < diagonalSize1; i++) {
                if (i > j) {
                    diagonalPlane1.positionMap[j][i] = -1;
                    continue;
                }

                int currentLayer = diagonalSize1 - 1 - i - j;
            
                if (diagonalConstant1 >= size) {
                    diagonalPlane1.positionMap[j][i] = ToSlotIndex(currentLayer - i, currentLayer - j, currentLayer);
                } else {
                    diagonalPlane1.positionMap[j][i] = ToSlotIndex(i, j, currentLayer);
                }
            }
        }

        Plane diagonalPlane2 = new Plane();
        diagonalPlane2.source = this;
        
        int diagonalConstant2 = layerY - layerX;
        int diagonalSize2 = size - Math.abs(diagonalConstant2);

        diagonalPlane2.positionMap = new int[diagonalSize2][diagonalSize2];

        for (int j = 0; j < diagonalSize2; j++) {
            for (int i = 0; i < diagonalSize2; i++) {
                if (i < j) {
                    diagonalPlane2.positionMap[j][i] = -1;
                    continue;
                }

                int currentLayer = i - j;
            
                if (diagonalConstant2 > 0) {
                    diagonalPlane2.positionMap[j][i] = ToSlotIndex(diagonalSize2 - 1 - i, diagonalSize2 - 1 - j - currentLayer, currentLayer);
                } else {
                    diagonalPlane2.positionMap[j][i] = ToSlotIndex(i - currentLayer, j, currentLayer);
                }
            }
        }

        return new Plane[] {flatPlane, diagonalPlane1, diagonalPlane2};
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
    
    private int[] ToLayerCoords(int slotIndex) {
        return ToLayerCoords(slotIndex, GetLayer(slotIndex));
    }

    private int[] ToLayerCoords(int slotIndex, int layer) {
        int[] retval = new int[2];

        if (layer == 0) {
            retval[0] = slotIndex % (size - layer);
            retval[0] = slotIndex / (size - layer);
        } else {
            retval[0] = (slotIndex - layerSeparators[layer - 1]) % (size - layer);
            retval[1] = (slotIndex - layerSeparators[layer - 1]) / (size - layer);
        }

        return retval;
    }

    private int ToSlotIndex(int layerX, int layerY, int layer) {
        int base = 0;
        if (layer > 0) {
            base = layerSeparators[layer - 1];
        }

        return base + layerY * size + layerX;
    }

    private int GetLayer(int slotIndex) {
        for (int layer = 0; layer < size; layer++) {
            if (slotIndex < layerSeparators[layer]) {
                return layer;
            }
        }

        return size - 1;
    }
}
