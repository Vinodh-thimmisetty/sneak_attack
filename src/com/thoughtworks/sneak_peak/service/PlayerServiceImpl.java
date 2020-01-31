package com.thoughtworks.sneak_peak.service;

import com.thoughtworks.sneak_peak.domain.Player;
import com.thoughtworks.sneak_peak.domain.Role;

import java.util.ArrayList;
import java.util.List;
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
        Integer KILLER_ID = 0; // assuming all random numbers with 0 are killers.
        boolean isKillerFound = false;
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < noOfPlayers; i++) {
            final int tempIndex = generateRandom(noOfPlayers);
            if (tempIndex == KILLER_ID) {
                if (isKillerFound) {
                    players.add(new Player(i, Role.INNOCENT));
                } else {
                    isKillerFound = true;
                    players.add(new Player(i, Role.KILLER));
                }
            } else {
                players.add(new Player(i, Role.INNOCENT));
            }
        }

        if (!isKillerFound) {
            players.remove(players.size() - 1);
            players.add(new Player(noOfPlayers - 1, Role.KILLER));
        }
        return players;
    }

    @Override
    public void killPlayers(final List<Player> players) {
        Player killer = players.stream().filter(x -> x.getRole().equals(Role.KILLER)).findFirst().get();
        final List<Player> innocents = players.stream().filter(x -> x.getRole().equals(Role.INNOCENT)).collect(Collectors.toList());
        int rounds = 1;
        while (innocents.size() > 0) {
            Player innocent = getRandomPlayer(innocents, innocents.size());
            loggerService.log("Round - " + rounds++);
            loggerService.log("P" + killer.getPlayerId() + " KILLED P" + innocent.getPlayerId());
            innocents.remove(innocent);
            loggerService.log("--------------");
        }
    }

    private Player getRandomPlayer(final List<Player> players, final int listSize) {
        return players.get(new Random().nextInt(listSize));
    }

    private int generateRandom(final int totalPlayers) {
        return new Random().nextInt(totalPlayers);
    }
}
