package com.arlow.iqsolverpro.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import com.arlow.iqsolverpro.game.CustomBoard;
import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;
import com.arlow.iqsolverpro.game.PyramidBoard;
import com.arlow.iqsolverpro.game.RectangularBoard;
import com.arlow.iqsolverpro.solver.SolveStats;

public class ResultsWriter {
    public static void WriteResults(GameInstance game, SolveStats results, OutputStream outputStream) throws IOException {
        if (game.board instanceof RectangularBoard) {
            DrawRectangularBoard((RectangularBoard) game.board, outputStream);
        } else if (game.board instanceof CustomBoard) {
            DrawCustomBoard((CustomBoard) game.board, outputStream);
        } else if (game.board instanceof PyramidBoard) {
            DrawPyramidBoard((PyramidBoard) game.board, outputStream);
        } else {
            throw new IOException("Unknown board type");
        }

        if (results != null) {
            PrintResults(results, outputStream);
        }
    }

    private static void PrintResults(SolveStats results, OutputStream outputStream) throws IOException {
        try (Writer writer = new PrintWriter(outputStream)) {
            writer.write("Solving Results\n");

            writer.write("Solution found: ");
            if (results.solutionExists) {
                writer.write("Yes\n");
            } else {
                writer.write("No\n");
            }

            writer.write("Time taken: ");
            writer.write(Long.toString(results.timeTakenInMs));
            writer.write("ms\n");

            writer.write("Steps taken: ");
            writer.write(Integer.toString(results.stepsTaken));
            writer.write("\n");

            writer.flush();
        }
    }

    private static void DrawRectangularBoard(RectangularBoard board, OutputStream outputStream) throws IOException {
        try (Writer writer = new PrintWriter(outputStream)) {
            for (int j = 0; j < board.width; j++) {
                for (int i = 0; i < board.length; i++) {
                    Piece piece = board.GetPieceOnPosition(j * board.width + i);
                    if (piece != null) {
                        writer.append(piece.key);
                    } else {
                        writer.append('.');
                    }
                }

                writer.append('\n');
            }

            writer.flush();
        }
    }

    private static void DrawCustomBoard(CustomBoard board, OutputStream outputStream) throws IOException {
        try (Writer writer = new PrintWriter(outputStream)) {
            for (int j = 0; j < board.length; j++) {
                for (int i = 0; i < board.width; i++) {
                    if (board.positionMap[j][i] == -1) {
                        writer.append(' ');
                        continue;
                    }

                    Piece piece = board.GetPieceOnPosition(board.positionMap[j][i]);

                    if (piece != null) {
                        writer.append(piece.key);
                    } else {
                        writer.append('.');
                    }
                }

                writer.append('\n');
            }

            writer.flush();
        }
    }

    private static void DrawPyramidBoard(PyramidBoard board, OutputStream outputStream) throws IOException {
        try (Writer writer = new PrintWriter(outputStream)) {
            int counter = 0;
            for (int i = 0; i < board.size; i++) {
                for (int j = 0; j < board.size - i; j++) {
                    for (int k = 0; k < board.size - i; k++) {
                        Piece piece = board.GetPieceOnPosition(counter);

                        if (piece != null) {
                            writer.append(piece.key);
                        } else {
                            writer.append('.');
                        }
                    }

                    writer.append('\n');
                }

                writer.write("\n");
            }

            writer.flush();
        }
    }
}
