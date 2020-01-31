package com.thoughtworks.sneak_peak;

import com.thoughtworks.sneak_peak.domain.Player;
import com.thoughtworks.sneak_peak.service.LoggerService;
import com.thoughtworks.sneak_peak.service.LoggerServiceImpl;
import com.thoughtworks.sneak_peak.service.PlayerService;
import com.thoughtworks.sneak_peak.service.PlayerServiceImpl;

import java.util.List;
import java.util.Scanner;

import static java.lang.String.valueOf;

/**
 * @author thimmv
 */
public class Startup {

    public static void main(String[] args) {
        LoggerService ls = new LoggerServiceImpl();
        PlayerService ps = new PlayerServiceImpl(ls);
        Scanner sc = new Scanner(System.in);
        int noOfPlayers = sc.nextInt();
        final List<Player> players = ps.assignRoles(noOfPlayers);
        ls.log("========= Assigning Roles =====");
        players.forEach(x -> ls.log(String.join("", "P", valueOf(x.getPlayerId()), " ", x.getRole().toString())));
        ls.log("========= Attacking the innocents =====");
        ps.killPlayers(players);
        ls.log("========= Attacking && Suspecting =====");
        ps.suspectAndKillPlayers(players);
    }


}
