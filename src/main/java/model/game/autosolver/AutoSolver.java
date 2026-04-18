package model.game.autosolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import model.game.AnswerNotFoundException;
import model.game.Direction;
import model.game.Map;

@SuppressWarnings("FieldMayBeFinal")
public class AutoSolver extends SwingWorker<ArrayList<Map>, Void> {
    final static private Direction[] DIRECTIONS = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
    private final ArrayList<MapStateNode> bfsList;
    private final Deque<Integer> bfsQueue;
    private final Set<Map> visited;

    public AutoSolver(Map map) {
        this.bfsList = new ArrayList<>(64);
        this.bfsQueue = new ArrayDeque<>();
        this.visited = new HashSet<>();

        bfsList.add(new MapStateNode(map, 0, -1));
        bfsQueue.addLast(0);
        visited.add(map);
    }

    private ArrayList<Map> solve() throws AnswerNotFoundException {
        while (!bfsQueue.isEmpty()) {
            int currIndex = bfsQueue.removeFirst();
            MapStateNode currNode = bfsList.get(currIndex);
            Map currMap = currNode.getMap();

            // check if victory has been reached
            if (Map.checkVictory(currMap)) {
                return buildPath(currIndex);
            }

            // add new nodes to the queue
            for (Direction dir : DIRECTIONS) {
                if (!Map.checkMove(currMap, dir)) {
                    continue;
                }

                Map newMap = Map.doMove(currMap, dir);
                if (visited.add(newMap)) {
                    bfsList.add(new MapStateNode(newMap, currNode.getDepth() + 1, currIndex));
                    bfsQueue.addLast(bfsList.size() - 1);
                }
            }
        }

        throw new AnswerNotFoundException();
    }

    private ArrayList<Map> buildPath(int targetIndex) {
        ArrayList<Map> path = new ArrayList<>();

        for (int next = targetIndex; next != -1; next = bfsList.get(next).getFather()) {
            path.add(bfsList.get(next).getMap());
        }

        Collections.reverse(path);
        return path;
    }

    @Override
    protected ArrayList<Map> doInBackground() throws Exception {
        try {
            return solve();
        } catch (AnswerNotFoundException e) {
            return null;
        }
    }

    @Override
    protected void done() {
        try {
            Map.produceHint(get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error in AutoSolver: " + e.getMessage());
        }
    }
}
