package model.game.autosolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import model.game.AnswerNotFoundException;
import model.game.Direction;
import model.game.Map;

@SuppressWarnings("FieldMayBeFinal")
public class AutoSolver extends SwingWorker<ArrayList<Map>, Void> {
    final static private Direction[] DIRECTIONS = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
    private ArrayList<MapStateNode> bfsList;
    private Map currMap;

    public AutoSolver(Map map) {
        this.bfsList = new ArrayList<>(64);
        bfsList.add(new MapStateNode(map, 0, -1));
    }

    private ArrayList<Map> solve() throws AnswerNotFoundException {
        MapStateNode currNode;
        int cnt = 0;

        try {
            while (bfsList.get(cnt) != null) {
                currNode = bfsList.get(cnt);
                currMap = currNode.getMap();
                HashSet<Integer> visited = new HashSet<>();

                // check if victory has been reached
                if (Map.checkVictory(currMap)) {
                    int totSteps = bfsList.get(cnt).getDepth() + 1;

                    ArrayList<Map> ansList = new ArrayList<>(totSteps);

                    for (int next = cnt; next != -1; next = bfsList.get(next).getFather()) {
                        ansList.set(bfsList.get(next).getDepth(), bfsList.get(next).getMap());
                    }

                    return ansList;
                }

                // add new nodes to the queue
                for (Direction dir : DIRECTIONS) {
                    if (Map.checkMove(currMap, dir)) {
                        Map newMap = Map.doMove(currMap, dir);
                        if (visited.add(newMap.hashCode())) {
                            // System.out.println(newMap.hashCode());
                            bfsList.add(new MapStateNode(newMap, currNode.getDepth() + 1, cnt));
                        }
                    }
                }

                cnt++;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new AnswerNotFoundException();
        }

        return null;
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
