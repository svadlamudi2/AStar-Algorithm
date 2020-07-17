import com.sun.scenario.effect.Merge;
import javafx.geometry.Pos;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Astar {
    private static final int MAX_INT = Integer.MAX_VALUE;
    String[][] array;
    Node[][] backArray;
    MergeSort mergeSort = new MergeSort();

    public Astar(int size, GUI gui) {
        array = new String[size][size];
        backArray = new Node[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                backArray[i][j] = new Node(new Position(i, j), null, MAX_INT, MAX_INT, false, false);
                backArray[i][j].fCost = MAX_INT;
                backArray[i][j].setBorder(new LineBorder(Color.BLACK));
                gui.panel.add(backArray[i][j]);
            }
        }
        gui.add(gui.panel);
        gui.setVisible(true);
        //set initial starting node to visited and the cost of getting to itself as 0.
    }



    public void findPath() throws InterruptedException {
        ArrayList<Position> positions = new ArrayList<>();
        ArrayList<Node> allNodes = new ArrayList<>(calculateFCosts(Node.startingNode.position.row, Node.startingNode.position.col));
        for(Node node : allNodes) {
            node.setOpaque(true);
            node.setBackground(Color.orange);
        }
        System.out.println();
        mergeSort.sort(allNodes, 0, allNodes.size() - 1);
        Node nextNode = allNodes.get(0);
        //remove the node being processed from the list of all nodes
        allNodes.remove(0);

        //Keep going until the target node has been reached
        while(!backArray[Node.targetNode.position.row][Node.targetNode.position.col].visited) {
            ArrayList<Node> newNodes = calculateFCosts(nextNode.position.row, nextNode.position.col);
            for(Node node : newNodes) {
                node.setOpaque(true);
                node.setBackground(Color.orange);
                //add the new node to the list of all nodes
                allNodes.add(node);
            }
            mergeSort.sort(allNodes, 0, allNodes.size() - 1);
            nextNode = allNodes.get(0);
            //remove the next node that is to be processed from the list of all nodes
            allNodes.remove(0);
        }
        //add the target positions to path list of positions
        Position currentPosition = backArray[Node.targetNode.position.row][Node.targetNode.position.col].prevPosition;
        positions.add(Node.targetNode.position);
        positions.add(currentPosition);
        //find the path taken
        while(!currentPosition.equals(Node.startingNode.position)) {
            currentPosition = backArray[currentPosition.row][currentPosition.col].prevPosition;
            positions.add(currentPosition);
        }
        //draw the path on the array with asteriks
        for(Position position : positions) {
            if(!position.equals(Node.startingNode.position)) {
                backArray[position.row][position.col].setBackground(Color.red);
                backArray[position.row][position.col].setOpaque(true);
            }
        }
    }

    public ArrayList<Node> calculateFCosts(int currentRow, int currentCol) {
        ArrayList<Node> neighborNodes = new ArrayList<>();
        Node currentNode = backArray[currentRow][currentCol];
        currentNode.visited = true;
        //if the current node is at the last row
        if(currentRow == array.length - 1) {
            for(int i = currentRow - 1; i < currentRow + 1; i++) {
                //if the current node is at the right most column
                if (currentCol == array.length - 1) {
                    for (int j = currentCol - 1; j < currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    array[i][j] = "|T||";
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }

                //if the current node is at the left most column
                else if(currentCol == 0) {
                    for (int j = currentCol; j <= currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }

                //if the current node is not at any of the bounds
                else {
                    for (int j = currentCol - 1; j <= currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }
            }
        }

        //if the current node is at the top
        else if(currentRow == 0) {
            for (int i = currentRow; i <= currentRow + 1; i++) {
                //if the current node is at the right most column
                if (currentCol == array.length - 1) {
                    for (int j = currentCol - 1; j < currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }

                //if the current node is at the left most column
                else if(currentCol == 0) {
                    for (int j = currentCol; j <= currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }

                //if the current node is not at any of the bounds
                else {
                    for (int j = currentCol - 1; j <= currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }
            }
        }

        //if there are no restrictions on the current node
        else {
            for(int i = currentRow - 1; i <= currentRow + 1; i++) {
                //if the current node is at the right most column
                if (currentCol == array.length - 1) {
                    for (int j = currentCol - 1; j < currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }

                //if the current node is at the left most column
                else if(currentCol == 0) {
                    for (int j = currentCol; j <= currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }

                //if the current node is not at any of the bounds
                else {
                    for (int j = currentCol - 1; j <= currentCol + 1; j++) {
                        //bottom or top row
                        if (i < currentRow || i > currentRow) {
                            //set the gcost of corner pieces to 14 more than the piece they are coming from
                            if (j < currentCol || j > currentCol) {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 14 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 14 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                            //set the gcost of adjacent to 10 more than the piece they are coming from
                            else {
                                if(!backArray[i][j].visited && backArray[i][j].gCost > 10 + currentNode.gCost && !backArray[i][j].isWall) {
                                    backArray[i][j].gCost = 10 + currentNode.gCost;
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }

                        //changing values in current row
                        else {
                            if(j < currentCol || j > currentCol) {
                                Node node = backArray[i][j];
                                if (!node.visited && node.gCost > currentNode.gCost + 10 && !node.isWall) {
                                    node.gCost = 10 + currentNode.gCost;
                                    node.hCost = calculateHCost(i, j);
                                    backArray[i][j].hCost = calculateHCost(i, j);
                                    backArray[i][j].setfCost();
                                    backArray[i][j].prevPosition = currentNode.position;
                                    neighborNodes.add(backArray[i][j]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return neighborNodes;
    }

    public int calculateHCost(int row, int col) {
        int base = Math.abs(Node.targetNode.position.row - row);
        int height = Math.abs(Node.targetNode.position.col - col);
        return (int) (Math.sqrt(Math.pow(base, 2) + Math.pow(height, 2)) * 10);
    }
}
