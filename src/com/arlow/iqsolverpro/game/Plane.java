package com.arlow.iqsolverpro.game;

public class Plane {
    public Board source;
    public int[][] positionMap;

    public boolean PieceFits(Piece piece) {
        int offsetX = -1;
        int offsetY = -1;

        for (int j = 0; j < positionMap.length; j++) {
            for (int i = 0; i < positionMap[j].length; i++) {
                if (positionMap[j][i] == piece.position) {
                    offsetX = i;
                    offsetY = j;
                }
            }
        }

        if (offsetX < 0 || offsetY < 0) {
            return false;
        }

        for (int j = 0; j < piece.shape.length; j++) {
            for (int i = 0; i < piece.shape[j].length; i++) {
                int posX = offsetX + i - piece.pivotX;
                int posY = offsetY + j - piece.pivotY;

                if (piece.shape[j][i]) {
                    if (posY >= positionMap.length || posY < 0 || posX >= positionMap[0].length || posX < 0) {
                        return false;
                    }

                    if (positionMap[posY][posX] < 0) {
                        return false;
                    }

                    if (source.GetPieceOnPosition(positionMap[posY][posX]) != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
