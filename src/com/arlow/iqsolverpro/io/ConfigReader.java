package com.arlow.iqsolverpro.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arlow.iqsolverpro.game.Board;
import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;
import com.arlow.iqsolverpro.game.PyramidBoard;
import com.arlow.iqsolverpro.game.RectangularBoard;

public class ConfigReader {
    private static Pattern dimensionsPattern = Pattern.compile("([0-9]+) ([0-9]+) ([0-9]+)");

    public static GameInstance GenerateGame(InputStream configStream) throws IOException {
        Scanner configScanner = new Scanner(configStream);
        
        String dimensionsLine = configScanner.nextLine().stripTrailing();
        Matcher dimensionsMatcher = dimensionsPattern.matcher(dimensionsLine);

        if (!dimensionsMatcher.matches()) {
            configScanner.close();
            throw new IOException("config does not match expected format");
        }

        int height = Integer.parseInt(dimensionsMatcher.group(0));
        int width = Integer.parseInt(dimensionsMatcher.group(1));
        int pieceCount = Integer.parseInt(dimensionsMatcher.group(2));

        String boardType = configScanner.nextLine().stripTrailing();
        Board board;

        switch (boardType) {
            case "DEFAULT":
                board = new RectangularBoard(width, height);
            break;
            case "CUSTOM":
                boolean[][] boardPattern = new boolean[height][width];

                for (int i = 0; i < height; i++) {
                    if (!configScanner.hasNextLine()) {
                        configScanner.close();
                        throw new IOException("config does not match expected format");
                    }

                    String patternLine = configScanner.nextLine();

                    if (!patternLine.matches("[.X]{" + width + "}\r?\n")) {
                        configScanner.close();
                        throw new IOException("config does not match expected format");
                    }

                    for (int j = 0; j < width; j++) {
                        boardPattern[i][j] = patternLine.charAt(j) == 'X';
                    }
                }

                board = new CustomBoard(boardPattern);
            break;
            case "PYRAMID":
                if (width != height) {
                    configScanner.close();
                    throw new IOException("config does not match expected format");
                }

                board = new PyramidBoard(width);
            break;
            default:
                configScanner.close();
                throw new IOException("config does not match expected format"); 
        }

        Piece[] pieces = new Piece[pieceCount];

        for (int i = 0; i < pieceCount; i++) {
            pieces[i] = new Piece();
        }
        
        char currentChar = ' ';
        int currentPiece = -1;
        
        while (configScanner.hasNextLine() && currentPiece < pieceCount) {
            String currentLine = configScanner.nextLine().stripTrailing();
            boolean[] patternBuffer = new boolean[currentLine.length()];
            char firstChar = ' ';

            for (int i = 0; i < currentLine.length() - 1; i++) {
                if (currentLine.charAt(i) == ' ') {
                    patternBuffer[i] = false;
                } else if (firstChar == ' ') {
                    firstChar = currentLine.charAt(i);

                    if (currentChar != firstChar) {
                        currentChar = firstChar;
                        currentPiece++;
                    }

                    patternBuffer[i] = true;
                } else if (currentLine.charAt(i) == firstChar) {
                    patternBuffer[i] = true;
                } else {
                    configScanner.close();
                    throw new IOException("config does not match expected format"); 
                }
            }

            if (firstChar == ' ') {
                continue;
            }

            pieces[currentPiece].key = currentChar;

            if (pieces[currentPiece].shape.length <= 0) {
                pieces[currentPiece].shape = new boolean[][] {patternBuffer};

                pieces[currentPiece].pivotY = 0;
                for (int i = 0; i < patternBuffer.length; i++) {
                    if (patternBuffer[i]) {
                        pieces[currentPiece].pivotX = i;
                        break;
                    }
                }
            } else {
                int newHeight = pieces[currentPiece].shape.length + 1;
                int newWidth = Math.max(pieces[currentPiece].shape[0].length, patternBuffer.length);
                boolean[][] newShape = new boolean[newHeight][newWidth];

                for (int j = 0; j < pieces[currentPiece].shape.length; j++) {
                    for (int i = 0; i < newWidth; i++) {
                        if (i >= pieces[currentPiece].shape[0].length) {
                            newShape[j][i] = false;
                        } else {
                            newShape[j][i] = pieces[currentPiece].shape[j][i];
                        }
                    }
                }

                for (int i = 0; i < newWidth; i++) {
                    if (i >= patternBuffer.length) {
                        newShape[newHeight - 1][i] = false;
                    } else {
                        newShape[newHeight - 1][i] = patternBuffer[i];
                    }
                }

                pieces[currentPiece].shape = newShape;
            }
        }

        if (currentPiece < pieceCount - 1 || configScanner.hasNextLine()) {
            configScanner.close();
            throw new IOException("config does not match expected format"); 
        }

        configScanner.close();

        return new GameInstance(pieces, board);
    }
}
