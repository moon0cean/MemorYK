package org.yk.games.ascii;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Score extends HashMap<Player, Integer> {

    public Score(List<Player> players) {
//        buildScore(players);
        players.forEach(p -> put(p, 0));
    }

    private Score buildScore(List<Player> players) {
        return (Score) Map.copyOf(this);
    }

    public void addPoint(Player player) {
        put(player, get(player) + 1);
    }

}
