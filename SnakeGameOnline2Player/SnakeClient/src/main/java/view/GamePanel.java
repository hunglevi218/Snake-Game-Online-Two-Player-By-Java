/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Win10
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import controller.Client;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {
    public static int width;
    public static int height;
    public static int GRID_SIZE = 20;
    public static int GRID_WIDTH = 30;
    public static int GRID_HEIGHT = 30;
    public int x1, x2, y1, y2;

    public Timer timer;
    public Snake snake;
    public Snake competitorSnake;
    public static int setPlayer;
    public static Point foodPosition;
    public GamePanel(int width, int height, int setPlayer) {
        this.setPlayer = setPlayer;
        
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);     
        x1 = 10;
        y1 = 10;
        x2 = 20;
        y2 = 20;
        foodPosition = new Point(15, 15);
        if(setPlayer < 0){
            snake = new Snake(x1,y1);
            competitorSnake = new Snake(x2, y2);
        }else{
            competitorSnake = new Snake(x1,y1);
            snake = new Snake(x2, y2);
        }
        System.out.println("setPlayer: " + setPlayer);
        
        timer = new Timer(100, this);
        timer.start();
        
        

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                Direction newDirection = null;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        newDirection = Direction.UP;
                        snake.startRun = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        newDirection = Direction.DOWN;
                        snake.startRun = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        newDirection = Direction.LEFT;
                        snake.startRun = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        snake.startRun = true;
                        newDirection = Direction.RIGHT;
                        break;
                }
                if (newDirection != null && newDirection != oppositeDirection(snake.getDirection())) {
                    snake.setDirection(newDirection);
                }

            }
        });
      
    }
    
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(setPlayer < 0){
            // Draw snake
            g.setColor(Color.GREEN);
            for (Point p : snake.getBody()) {
            g.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            snakeCollision();
            }
            
            // Draw competitorSnake
            g.setColor(Color.YELLOW);
            for (Point p : competitorSnake.getBody()) {
            g.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            snakeCollision();
            }
            
        }else{
             // Draw snake
            g.setColor(Color.YELLOW);
            for (Point p : snake.getBody()) {
            g.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            snakeCollision();
            }
            
            // Draw competitorSnake
            g.setColor(Color.GREEN);
            for (Point p : competitorSnake.getBody()) {
            g.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            snakeCollision();
            }
        }
        
            g.setColor(Color.RED);
            g.fillRect(foodPosition.x * GRID_SIZE, foodPosition.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
    }
    
    public void snakeCollision(){
        if(competitorSnake.getBody().getFirst().x == snake.getBody().getFirst().x && competitorSnake.getBody().getFirst().y == snake.getBody().getFirst().y){
            if(competitorSnake.getBody().size() < snake.getBody().size()){
                competitorSnake.setIsAlive(false);
                System.out.println("player win");
            }else if(competitorSnake.getBody().size() > snake.getBody().size()){
                snake.setIsAlive(false);
                System.out.println("cpt win");
            }else{
                Random random = new Random();
                int x = random.nextInt(2);
                if(x == 0){
                    snake.setIsAlive(false);
                    System.out.println("cpt win");
                }else{
                    competitorSnake.setIsAlive(false);
                    System.out.println("player win");
                }
            }
        } else{
            for(Point p : competitorSnake.getBody()){
                if(p.x == snake.getBody().getFirst().x && p.y == snake.getBody().getFirst().y){
                    snake.setIsAlive(false);
                }
            }
            for(Point p : snake.getBody()){
                if(p.x == competitorSnake.getBody().getFirst().x && p.y == competitorSnake.getBody().getFirst().y){
                    competitorSnake.setIsAlive(false);
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(snake.startRun ){
            snake.move();
        }
//        
        if (!snake.isAlive() || !competitorSnake.isAlive()) {
            if(!competitorSnake.isAlive()){
                Client.gameClientFrm.increaseWinMatchToUser();
            }else if(!snake.isAlive()){
                Client.gameClientFrm.increaseWinMatchToCompetitor();  
            }
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!");
        }
        repaint();
    }

    public Direction oppositeDirection(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return null;
        }
    }
     
    public static void spawnFood()  {
        Random random = new Random();
        int x = random.nextInt(width/GRID_SIZE);
        int y = random.nextInt(height/GRID_SIZE);
        foodPosition = new Point(x, y);
        try{
            Client.socketHandle.write("food," + x + ',' + y);
        }catch(IOException ex){}
    }
   
    public void spawnFood1(String x, String y){
        int xx = Integer.parseInt(x);
        int yy = Integer.parseInt(y);
        foodPosition = new Point(xx, yy);
    }
}

