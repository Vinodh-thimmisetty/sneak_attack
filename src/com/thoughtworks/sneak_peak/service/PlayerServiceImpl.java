package com.thoughtworks.sneak_peak.service;

import com.thoughtworks.sneak_peak.domain.Player;
import com.thoughtworks.sneak_peak.domain.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author thimmv
 */
public class PlayerServiceImpl implements PlayerService {

    private LoggerService loggerService;

    public PlayerServiceImpl(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public List<Player> assignRoles(final int noOfPlayers) {
        int KILLER_ID = 0; // assuming all random numbers with 0 are killers.
        boolean isKillerFound = false;
        List<Player> players = new ArrayList<>();
        for (int playerId = 0; playerId < noOfPlayers; playerId++) {
            final int tempID = generateRandom(noOfPlayers);
            if (tempID == KILLER_ID) {
                if (isKillerFound) {
                    players.add(new Player(playerId, Role.INNOCENT));
                } else {
                    isKillerFound = true;
                    players.add(new Player(playerId, Role.KILLER));
                }
            } else {
                players.add(new Player(playerId, Role.INNOCENT));
            }
        }

        // To make sure, at-least one killer exists;
        if (!isKillerFound) {
            players.remove(players.size() - 1);
            players.add(new Player(noOfPlayers - 1, Role.KILLER));
        }
        return players;
    }

    @Override
    public void killPlayers(final List<Player> players) {
        Player killer = findKiller(players);
        final List<Player> innocents = findInnocents(players);
        int rounds = 1;
        while (!innocents.isEmpty()) {
            Player innocent = getRandomPlayer(innocents);
            loggerService.log("Round - " + rounds++);
            loggerService.log("P" + killer.getPlayerId() + " KILLED P" + innocent.getPlayerId());
            innocents.remove(innocent);
            loggerService.log("--------------");
        }
    }

    @Override
    public void suspectAndKillPlayers(final List<Player> players) {
        Player killer = findKiller(players);
        final int noOfRoundMax = players.size() - 1;
        for (int eachRound = 1; eachRound <= noOfRoundMax; eachRound++) {
            loggerService.log("Round - " + eachRound);
            final Map<Player, Player> killsOrSuspects = groupByKillsAndSuspects(killer, players);
            final Player p_tobe_killed = killsOrSuspects.get(killer);
            killsOrSuspects.remove(killer);
            Map<Player, Integer> countMap = new HashMap<>();
            for (final Map.Entry<Player, Player> temp : killsOrSuspects.entrySet()) {
                Player tempPValue = temp.getValue();
                if (countMap.containsKey(tempPValue)) {
                    countMap.put(tempPValue, countMap.get(tempPValue) + 1);
                } else {
                    countMap.put(tempPValue, 1);
                }
            }
            if (isGameEnded(p_tobe_killed, players, countMap).isEmpty()) break;

            loggerService.log("--------------");
        }

    }

    private Player findKiller(List<Player> players) {
        return players.stream().filter(x -> x.getRole().equals(Role.KILLER)).findFirst().get();
    }

    private List<Player> findInnocents(final List<Player> players) {
        return players.stream().filter(x -> x.getRole().equals(Role.INNOCENT)).collect(Collectors.toList());
    }

    private List<Player> isGameEnded(final Player p_tobe_killed, List<Player> players, final Map<Player, Integer> countMap) {
        if (players.size() == 1) {
            loggerService.log("Killer won it !!");
            return Collections.emptyList();
        }

        Player killer = findKiller(players);
        if (players.size() == 2 && players.contains(killer)) {
            loggerService.log("Killer won it !!");
            return Collections.emptyList();
        }

        // find the largest in map
        int largest = 2;
        List<Player> playersToRemove = new ArrayList<>();
        for (final Map.Entry<Player, Integer> tempLoop : countMap.entrySet()) {
            final Integer value = tempLoop.getValue();
            if (value >= largest) {
                largest = value;
                playersToRemove.add(tempLoop.getKey()); // more than one suspects.
            }
        }

        loggerService.log("Killer P" + killer.getPlayerId() + " suspected P" + p_tobe_killed.getPlayerId() + " and  Killed");
        players.remove(p_tobe_killed);
        if (playersToRemove.contains(killer)) {
            loggerService.log("Killer P" + killer.getPlayerId() + " is Killed");
            return Collections.emptyList();
        } else {
            for (final Player player : playersToRemove) {
                loggerService.log("More than one Innocents identified wrong killer ---> So, co-ordinator killed P" + player.getPlayerId());
                players.remove(player);
            }
        }

        return players;
    }

    private Map<Player, Player> groupByKillsAndSuspects(final Player killer, final List<Player> players) {
        Map<Player, Player> kill_or_suspect_action_map = new HashMap<>();
        for (final Player player : players) {
            Player randomPlayer = getRandomPlayerExcludingOwn(new ArrayList<>(players), player);
            if (player.getPlayerId() == killer.getPlayerId()) {
                kill_or_suspect_action_map.put(player, randomPlayer);
                loggerService.log("P" + player.getPlayerId() + " Killed P" + randomPlayer.getPlayerId());
            } else {
                kill_or_suspect_action_map.put(player, randomPlayer);
                loggerService.log("P" + player.getPlayerId() + " suspects P" + randomPlayer.getPlayerId());
            }
            kill_or_suspect_action_map.put(player, randomPlayer);
        }
        return kill_or_suspect_action_map;
    }

    private Player getRandomPlayerExcludingOwn(final List<Player> players, final Player player) {
        players.remove(player);
        return players.get(new Random().nextInt(players.size()));
    }

    private Player getRandomPlayer(final List<Player> players) {
        return players.get(new Random().nextInt(players.size()));
    }

    private int generateRandom(final int totalPlayers) {
        return new Random().nextInt(totalPlayers);
    }
}
