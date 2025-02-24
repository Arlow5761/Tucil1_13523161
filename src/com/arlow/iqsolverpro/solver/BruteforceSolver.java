package com.arlow.iqsolverpro.solver;

import com.arlow.iqsolverpro.game.GameInstance;
import com.arlow.iqsolverpro.game.Piece;
import com.arlow.iqsolverpro.game.Plane;

public class BruteforceSolver implements Solver {
    private GameInstance game;
    private Piece[] pieces;
    private int delay;
    private SolveStats result;

    public BruteforceSolver(GameInstance game, int delay) {
        this.game = game;
        this.delay = delay;
        this.result = new SolveStats();

        this.pieces = new Piece[game.availablePieces.size()];
        for (int i = 0; i < game.availablePieces.size(); i++) {
            this.pieces[i] = game.availablePieces.get(i);
        }
    }

    public SolveStats Solve() {
        long startTime = System.currentTimeMillis();

        result.solutionExists = RecursiveSolve(0) && game.board.GetEmptyPositions().length <= 0;
        result.timeTakenInMs = System.currentTimeMillis() - startTime;
        
        return result;
    }

    private boolean RecursiveSolve(int index) {
        if (index >= pieces.length) {
            return true;
        }

        int[] emptySlots = game.board.GetEmptyPositions();
        
        for (boolean flipped = false; !flipped; flipped = true) {
            for (int rotations = 0; rotations < 4; rotations++) {
                for (int i = 0; i < emptySlots.length; i++) {
                    pieces[index].position = emptySlots[i];

                    for (Plane plane : game.board.GetPlanesOnPosition(emptySlots[i])) {
                        if (game.PlacePiece(pieces[index], plane)) {
                            result.stepsTaken++;
                            
                            if (delay > 0) {
                                try {
                                    Thread.sleep(delay);
                                } catch (Exception e) {
                                    System.out.println("[WARNING]: solver thread interrupted during sleep");
                                }
                            }

                            if (RecursiveSolve(index + 1)) {
                                return true;
                            }

                            game.RemovePiece(pieces[index]);
                        }
                    }
                }

                pieces[index].RotateClockwise();
            }

            pieces[index].FlipHorizontal();
        }

        return false;
    }
}
