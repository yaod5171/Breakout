package Pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class Breakout extends Canvas implements KeyListener, Runnable {

    private Ball ball;
    private Paddle paddle;
    private boolean[] keys;
    private BufferedImage back;
    private int score;
    private List<Block> blocks;

    public Breakout() {
        //set up all variables related to the game
        reset();
        keys = new boolean[2];

        setBackground(Color.WHITE);
        setVisible(true);

        new Thread(this).start();
        addKeyListener(this);		//starts the key thread to log key strokes
    }

    public void reset() {
        paddle = new Paddle(760, 200, 10, 800, Color.orange, 2);
        ball = new Ball(700, 240, 10, 10, Color.blue, 2, 1);
        score = 0;
        blocks = new ArrayList();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                blocks.add(new Block(25 * j + 25, 100 * i + 25, 20, 90, Color.GREEN));
            }
        }
    }

    public void update(Graphics window) {
        paint(window);
    }

    public void paint(Graphics window) {
        Graphics2D twoDGraph = (Graphics2D) window;

        //take a snap shop of the current Frame and same it as an image
        //that is the exact same width and height as the current Frame
        if (back == null) {
            back = (BufferedImage) (createImage(getWidth(), getHeight()));
        }

        //create a graphics reference to the back ground image
        //we will draw all changes on the background image
        Graphics graphToBack = back.createGraphics();

        graphToBack.setColor(Color.BLACK);
        graphToBack.fillRect(0, 0, 800, 600);
        graphToBack.setColor(Color.WHITE);
        graphToBack.fillRect(20, 20, 760, 500);

        ball.moveAndDraw(graphToBack);
        paddle.draw(graphToBack);

        for (Block b : blocks) {
            b.draw(graphToBack);
        }

        //see if ball hits left wall or right wall
        if (!(ball.getxPos() <= 780)) {
            ball.setXSpeed(0);
            ball.setYSpeed(0);

            try {
                Thread.currentThread().sleep(950);
            } catch (Exception e) {
            }

            reset();
        }
        graphToBack.setColor(Color.GREEN);
        graphToBack.drawString("Derrick Yao", 50, 540);
        graphToBack.drawString("29 Apr 2019", 50, 560);
        graphToBack.drawString("P1 CA-SU-F106-26", 150, 540);
        graphToBack.drawString("Score = " + score, 700, 540);

        //see if ball hits top wall or bottom wall
        if (!(ball.getyPos() >= 20 && ball.getyPos() <= 510)) {
            ball.setYSpeed(-ball.getYSpeed());
        }
        //see if ball hits left wall
        if (!(ball.getxPos() >= 20)) {
            ball.setXSpeed(-ball.getXSpeed());
        }

        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            if (ball.didCollideLeft(b)
                    && (ball.didCollideTop(b) || ball.didCollideBottom(b))) {

                if (ball.getxPos() <= b.getxPos() + b.getWidth() - Math.abs(ball.getXSpeed())) {
                    ball.setYSpeed(-ball.getYSpeed());
                } else {
                    ball.setXSpeed(-ball.getXSpeed());
                }
                blocks.remove(b);
                score++;
            }/*
            if (ball.didCollideRight(b)
                    && (ball.didCollideTop(b) || ball.didCollideBottom(b))) {
                if (ball.getxPos() + ball.getWidth() >= b.getxPos() + Math.abs(ball.getXSpeed())) {
                    ball.setYSpeed(-ball.getYSpeed());
                } else {
                    ball.setXSpeed(-ball.getXSpeed());
                }
                blocks.remove(b);
                score++;
            }*/
        }

        if (ball.didCollideRight(paddle)
                && (ball.didCollideTop(paddle) || ball.didCollideBottom(paddle))) {
            if (ball.getxPos() + ball.getWidth() >= paddle.getxPos() + Math.abs(ball.getXSpeed())) {
                ball.setYSpeed(-ball.getYSpeed());
            } else {
                ball.setXSpeed(-ball.getXSpeed());
            }
        }

        //see if the paddles need to be moved
        if (keys[0] == true) {
            paddle.moveUpAndDraw(graphToBack);
        }
        if (keys[1] == true) {
            paddle.moveDownAndDraw(graphToBack);
        }

        twoDGraph.drawImage(back, null, 0, 0);
    }

    public void keyPressed(KeyEvent e) {
        switch (toUpperCase(e.getKeyChar())) {
            case 'W':
                keys[0] = true;
                break;
            case 'S':
                keys[1] = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (toUpperCase(e.getKeyChar())) {
            case 'W':
                keys[0] = false;
                break;
            case 'S':
                keys[1] = false;
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void run() {
        try {
            while (true) {
                Thread.currentThread().sleep(8);
                repaint();
            }
        } catch (Exception e) {
        }
    }
}
