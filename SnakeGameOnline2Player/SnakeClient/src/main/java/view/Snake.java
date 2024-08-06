/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
//
//import static game.Direction.DOWN;
//import static game.Direction.LEFT;
//import static game.Direction.RIGHT;
//import static game.Direction.UP;
import controller.Client;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Snake {
    public static Snake instance;
    public LinkedList<Point> body;
    public LinkedList<Point> foodList;
    Direction direction;
    public boolean isAlive;
    public boolean startRun;
    int x,y, checkFoodPos;
    
    public Snake(int x, int y) {
        checkFoodPos = 0;
        this.x = x;
        this.y = y;
        body = new LinkedList<>();
        direction = Direction.RIGHT; // Initial direction
        isAlive = true;
        startRun = false;
        body.add(new Point(x,y));
          
    }
   
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
                
    public LinkedList<Point> getBody() {
        return body;
    }
    

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isAlive() {
        return isAlive;
    }
     public void setIsAlive(boolean x) {
        isAlive = x;
    }
    public void move() {
        if (!isAlive || !startRun) return;

        Point head = body.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.y--;
                y--;
                break;
            case DOWN:
                newHead.y++;
                y++;
                break;
            case LEFT:
                newHead.x--;
                x--;
                break;
            case RIGHT:
                newHead.x++;
                x++;
                break;
        }
        if(newHead.x < 0) {
            newHead.x = GamePanel.width/GamePanel.GRID_SIZE;
            x = GamePanel.width/GamePanel.GRID_SIZE;
        }
        if(newHead.y < 0) {
            newHead.y = GamePanel.height/GamePanel.GRID_SIZE;
            y = GamePanel.height/GamePanel.GRID_SIZE;
        }
        if(newHead.x > GamePanel.width/GamePanel.GRID_SIZE){
            newHead.x = 0;
            x = 0;
        }
        if(newHead.y > GamePanel.height/GamePanel.GRID_SIZE){
            newHead.y =  0;
            y = 0;
        }
      
            
        // Check collision with walls or itself
        if (body.contains(newHead)) {
            isAlive = false;
        } else {
            body.addFirst(newHead);
            if (newHead.equals(GamePanel.foodPosition)) {
                // Snake eats foodisFood
                checkFoodPos = 1;
                GamePanel.spawnFood();
            } else {
                checkFoodPos = 0;
                // Remove tail if not eating food
                body.removeLast();
            }
        }
        try{
            Client.socketHandle.write("snake-move," + x + ',' + y + ',' + checkFoodPos);
        }catch(IOException ex){}      
    }
    
    public void moveCpt1(int x, int y, int z){
        if (!isAlive) return;
        
        Point head = body.getFirst();
        Point newHead = new Point(head);
        newHead.x = x;      
        newHead.y = y;
        if (body.contains(newHead)) {
            isAlive = false;
        } else {
            System.out.println("z : " + z);
            body.addFirst(newHead);
            if (z == 1) {
                System.out.println("vao day ko");
            }else {
               // Remove tail if not eating food
               body.removeLast();
            }
        }
    }
}


enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}


