package com.thoughtworks.sneak_peak.service;

import com.thoughtworks.sneak_peak.domain.Player;

import java.util.List;

/**
 * @author thimmv
 */
public interface PlayerService {

    List<Player> assignRoles(int totalPlayers);

    void killPlayers(List<Player> players);
}
