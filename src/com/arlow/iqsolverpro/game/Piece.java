package com.arlow.iqsolverpro.game;

public class Piece {
    public boolean[][] shape;
    public int position;
    public int pivotX;
    public int pivotY;

    public Piece Clone() {
        Piece clone = new Piece();
        clone.shape = new boolean[shape.length][shape[0].length];
        clone.position = position;
        clone.pivotX = pivotX;
        clone.pivotY = pivotY;

        return clone;
    }

    public void RotateClockwise() {
        boolean[][] newShape = new boolean[shape[0].length][shape.length];

        for (int j = 0; j < shape.length; j++) {
            for (int i = 0; i < shape[j].length; i++) {
                newShape[i][shape.length - 1 - j] = shape[j][i];
            }
        }

        for (int i = 0; i < shape[0].length; i++) {
            if (shape[0][i]) {
                pivotX = 0;
                pivotY = i;
            }
        }

        shape = newShape;
    }

    public void FlipHorizontal() {
        boolean[][] newShape = new boolean[shape.length][shape[0].length];

        for (int i = 0; i < shape.length / 2; i++) {
            newShape[i] = shape[shape.length - 1 - i];
        }

        for (int i = 0; i < shape[0].length; i++) {
            if (shape[0][i]) {
                pivotX = 0;
                pivotY = i;
            }
        }

        shape = newShape;
    }
}
