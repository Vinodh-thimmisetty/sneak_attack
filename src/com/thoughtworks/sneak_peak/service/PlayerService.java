package com.thoughtworks.sneak_peak.service;

import com.thoughtworks.sneak_peak.domain.Player;

import java.util.List;

/**
 * @author thimmv
 */
public interface PlayerService {

    int generateRandom(int totalPlayers);

    List<Player> assignRoles(int totalPlayers);

    void killPlayers(List<Player> players);

    Player getRandomPlayer(List<Player> players, int listSize);
}
