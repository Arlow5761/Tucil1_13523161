package com.arlow.iqsolverpro.gui;

import java.util.HashMap;
import java.util.Random;

import com.arlow.iqsolverpro.game.Piece;

import javafx.scene.paint.Color;

public class PieceColoringService {
    private static HashMap<Piece, Color> colorMap = new HashMap<Piece, Color>();

    public static Color GetColor(Piece piece) {
        Color color = colorMap.get(piece);

        if (color != null) return color;

        Random random = new Random();

        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        colorMap.put(piece, color);

        return color;
    }

    public static void Reset() {
        colorMap = new HashMap<Piece, Color>();
    }
}
