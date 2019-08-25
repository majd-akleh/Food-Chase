/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import game.Direction;
import game.Game;
import java.util.Arrays;
import java.util.Random;
import neuralnetwork.NeuralNetworkInterface;

/**
 *
 * @author Marcil & Majd
 */
public class TrackingGame implements Game {

    private boolean guiEnabled;
    private int simulationLength;
    private int boardWidth, boardHeight;
    private int[][] board;
    private int[][] myMoves, foodMoves;

    public TrackingGame(int simulationLength, int boardWidth, int boardHeight) {
        this.simulationLength = simulationLength;
        this.boardHeight = boardHeight;
        this.boardWidth = boardHeight;
        board = new int[boardHeight][boardWidth];
        myMoves = new int[simulationLength][2];
        foodMoves = new int[simulationLength][2];
        steps = new Direction[simulationLength];
    }

    @Override
    public void setGuiEnabled(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    @Override
    public boolean isGuiEnabled() {
        return guiEnabled;
    }

    private boolean good(int r, int c) {
        return 0 <= r && r < boardHeight && 0 <= c && c < boardWidth && board[r][c] == 0;
    }

    public int[][] getMyMoves() {
        return myMoves;
    }

    public int[][] getFoodMoves() {
        return foodMoves;
    }
    long seed;

    public void setSeed(long s) {
        seed = s;
    }
    Direction[] steps;
    public Direction[] getSteps(){
        return steps;
    }
    @Override
    public double evaluate(NeuralNetworkInterface network) {
        double score = 0;
//        for (int i = 0; i < this.simulationLength; ++i)myMoves[i][0] = myMoves[i][1] = foodMoves[i][0] = foodMoves[i][1] = 0;steps[i] = 0;}
        int myRow = 0, myCol = 0;
        int mr[] = {0, -1, 0, 1};
        int mc[] = {-1, 0, 1, 0};
        int foodEaten = 0;
        Random random = new Random(seed);
        int foodRow = random.nextInt(boardHeight), foodCol = random.nextInt(boardWidth);
        int oldDist = Math.abs(myRow - foodRow) + Math.abs(myCol - foodCol);
        for (int i = 0; i < simulationLength; ++i) {
            network.setInput(new int[]{myRow, myCol, foodRow, foodCol});
            int res = network.getIntOutput();
            steps[i] = Direction.values()[res];
            if (good(myRow + mr[res], myCol + mc[res])) {
                myRow += mr[res];
                myCol += mc[res];
            } else {
                score -= 10;
            }
            myMoves[i][0] = myCol;
            myMoves[i][1] = myRow;
            foodMoves[i][0] = foodCol;
            foodMoves[i][1] = foodRow;
            int newDist = Math.abs(myRow - foodRow) + Math.abs(myCol - foodCol);
            if (newDist < oldDist) {
                score += 10;
            } else {
                score -= 100;
            }

            if (newDist == 0) {
                if(isGuiEnabled()){
                    
                }
                score += 100;
                foodEaten++;
                foodRow = random.nextInt(boardHeight);
                foodCol = random.nextInt(boardWidth);
                oldDist = Math.abs(myRow - foodRow) + Math.abs(myCol - foodCol);
            } else {
                oldDist = newDist;
            }

        }
        score += (int) 1e6;
//        if (score < 0) {
//            score = 0;
//        }
        if (isGuiEnabled()) {
            System.out.println("score = " + score + ", food eaten = " + foodEaten);
        }
        return score;
    }

    @Override
    public void setSightSize(int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTotalSightSize() {
        return 4;
    }

    @Override
    public double getMaxPossibleScore() {
        return Double.MAX_VALUE;//each step eat food
    }

}
