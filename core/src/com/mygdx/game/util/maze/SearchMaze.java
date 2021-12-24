package com.mygdx.game.util.maze;

import java.util.ArrayList;
import java.util.Stack;

public class SearchMaze {
    private final Position start;// Начальная точка финала лабиринта
    private final Position end;// Конец лабиринта final
    private ArrayList<String> footPrint;// След
    private ArrayList<Position> test;
    private Stack<Position> stacks;// Пользовательский стек (вы также можете использовать стек Stack в java.util). Если вы хотите понять реализацию MyStack, вы можете обратиться к другому моему блогу
    private Position currentPosition;// Определяем текущую позицию
    public static int[][] map = new int[0][];

    public SearchMaze(int[][] map, int xS, int yS, int xE, int yE) {// Устанавливаем, инициализация стека работает
        start = new Position(xS, yS);
        end = new Position(xE, yE);
        currentPosition = start;
        stacks = new Stack<>();
        test = new ArrayList<>();
        SearchMaze.map = map;
    }

    public static void printMap() {// Распечатать карту
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == 1) System.out.print(" ■");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }

    public boolean moveTop() {// Двигаться вверх
        String s = currentPosition.getPx() + "" + (currentPosition.getPy() - 1);
        if ((map[currentPosition.getPx()][currentPosition.getPy() - 1] != 1) & !isArrived(s)) {
            footPrint.add(s);
            return true;
        }
        return false;
    }

    public boolean moveRight() {// Сдвиг вправо
        String s = (currentPosition.getPx() + 1) + "" + currentPosition.getPy();
        if (map[currentPosition.getPx() + 1][currentPosition.getPy()] != 1 & !isArrived(s)) {
            footPrint.add(s);
            return true;
        }
        return false;
    }

    public boolean moveBottom() {// Двигаться вниз
        String s = currentPosition.getPx() + "" + (currentPosition.getPy() + 1);
        if ((map[currentPosition.getPx()][currentPosition.getPy() + 1] != 1) & !isArrived(s)) {
            footPrint.add(s);
            return true;
        }
        return false;
    }

    public boolean moveLeft() {// Сдвиг влево
        String s = (currentPosition.getPx() - 1) + "" + currentPosition.getPy();
        if ((map[currentPosition.getPx() - 1][currentPosition.getPy()] != 1) & !isArrived(s)) {
            footPrint.add(s);
            return true;
        }
        return false;
    }

    public boolean isArrived(String position) {// Определяем, была ли достигнута текущая позиция
        return footPrint.contains(position);
    }

    public void move() {// Функция перемещения перемещается в четырех направлениях соответственно, а затем помещает возможный путь в стек
        if (moveRight()) {
            Position temp = new Position(currentPosition.getPx() + 1, currentPosition.getPy());
            test.add(temp);
            stacks.push(temp);
        } else if (moveBottom()) {
            Position temp = new Position(currentPosition.getPx(), currentPosition.getPy() + 1);
            test.add(temp);
            stacks.push(temp);
        } else if (moveTop()) {
            Position temp = new Position(currentPosition.getPx(), currentPosition.getPy() - 1);
            test.add(temp);
            stacks.push(temp);
        } else if (moveLeft()) {
            Position temp = new Position(currentPosition.getPx() - 1, currentPosition.getPy());
            test.add(temp);
            stacks.push(temp);
        } else {
            currentPosition = stacks.pop();// Если текущая позиция не может перемещаться во всех четырех направлениях, извлекаем текущую позицию из стека и продолжаем обход предыдущего узла
        }
    }

    public Stack<Position> startSearch(int startPos, int endPos){
        footPrint = new ArrayList<>();
        footPrint.add(String.valueOf(startPos) + String.valueOf(endPos));
        stacks.push(start);
        while (currentPosition.getPx() != startPos || currentPosition.getPy() != endPos) {
            move();
        }

        return  stacks;

    }

}
