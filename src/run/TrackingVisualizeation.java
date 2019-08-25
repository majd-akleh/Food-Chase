/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import game.Direction;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author Marcil & Majd
 */
public class TrackingVisualizeation extends JFrame {

    PaintSurface surface;
    int n, m;

    public TrackingVisualizeation(int scale, int board[][], int[][] myMoves, int[][] foodMoves, Direction[] steps) throws HeadlessException {
        super();
        surface = new PaintSurface(scale, board, myMoves, foodMoves, steps);
        this.add(surface);
        this.setVisible(true);
        this.setSize(surface.getCols() + 5 * scale, surface.getRows() + 5 * scale);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public TrackingVisualizeation(int scale, long foodSeed, int board[][], NeuralNetwork network, TrackingGame game) {
        super();
        game.setSeed(foodSeed);
        game.evaluate(network);
        surface = new PaintSurface(scale, board, game.getMyMoves(), game.getFoodMoves(), game.getSteps());
        this.add(surface);
        this.setVisible(true);
        this.setSize(surface.getCols() + 5 * scale, surface.getRows() + 5 * scale);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public PaintSurface getPaintSurface() {
        return surface;
    }

    void simulate(long delay) {
        while (surface.done() == false) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(GATrackingTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class PaintSurface extends JComponent {

        int scale;
        int board[][];
        int myMoves[][];
        int foodMoves[][];
        Direction steps[];
        int time;
        int simulationTime;

        public PaintSurface(int scale, int[][] board, int myMovoes[][], int foodMoves[][], Direction[] steps) {
            super();
            this.scale = scale;
            this.board = board;
            this.myMoves = myMovoes;
            this.foodMoves = foodMoves;
            this.simulationTime = myMovoes.length;
            time = -1;
            this.steps = steps;
            setSize(board.length * scale + 4 * scale, board[0].length * scale + scale * 4);
            setVisible(true);
        }

        int getRows() {
            return board.length * scale;
        }

        int getCols() {
            return board[0].length * scale;
        }

        public boolean done() {
            if (time + 1 < simulationTime) {
                time++;
                repaint();
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawString("time = " + time + " direction = " + steps[time], getCols() + scale, 10);
            g2.drawString("my row = " + myMoves[time][1] + ", my col = " + myMoves[time][0], getCols() + scale, 20);
            g2.drawString("food row = " + foodMoves[time][1] + ", my col = " + foodMoves[time][0], getRows() + scale, 30);

            for (int i = 0; i < getRows() + scale; i += scale) {
                g2.drawLine(1, i, getCols(), i);
            }
            for (int i = 0; i < getCols() + scale; i += scale) {
                g2.drawLine(i, 1, i, getRows());
            }
            Color temp = g2.getColor();
            g2.setColor(Color.blue);
            g2.fillRect(myMoves[time][0] * scale, myMoves[time][1] * scale, scale, scale);
            g2.setColor(Color.red);
            g2.fillRect(foodMoves[time][0] * scale, foodMoves[time][1] * scale, scale, scale);
            g2.setColor(temp);
        }
    }
}
