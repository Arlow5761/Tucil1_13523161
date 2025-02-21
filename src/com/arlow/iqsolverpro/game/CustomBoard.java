package com.arlow.iqsolverpro.game;

public class CustomBoard extends RectangularBoard {
    public int[][] positionMap;

    public CustomBoard(boolean[][] pattern) {
        super(pattern[0].length, pattern.length);

        positionMap = new int[length][width];

        int counter = 0;

        for (int j = 0; j < pattern.length; j++) {
            for (int i = 0; i < pattern[j].length; i++) {
                if (pattern[j][i]) {
                    positionMap[j][i] = counter++;
                } else {
                    positionMap[j][i] = -1;
                }
            }
        }

        slots = new Piece[counter];
    }

    @Override
    public Plane[] GetPlanesOnPosition(int position) {
        Plane plane = new Plane();
        plane.source = this;
        plane.positionMap = positionMap;

        return new Plane[] {plane};
    }
}
