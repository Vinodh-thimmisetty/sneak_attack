package com.thoughtworks.sneak_peak;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author thimmv
 */
public class KillerInnocent {

    static class Player {
        private String role;

        public Player(final String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

    }

    enum Role {
        KILLER, INNOCENT
    }

    private static Integer KILLER_ID = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int noOfPlayers = sc.nextInt();
        boolean isKillerFound = false;
        List<Player> players = new ArrayList<>();

        // consider 1 as Killer and remaining as innocent

        for (int i = 0; i < noOfPlayers; i++) {
            int xx = generateRandom(noOfPlayers);
            if (xx == KILLER_ID) {
                if (isKillerFound) {
                    players.add(new Player(Role.INNOCENT.toString()));
                } else {
                    isKillerFound = true;
                    players.add(new Player(Role.KILLER.toString()));
                }
            } else {
                players.add(new Player(Role.INNOCENT.toString()));
            }
        }

        if (!isKillerFound) {
            players.remove(players.size() - 1);
            players.add(new Player(Role.KILLER.toString()));
        }

        for (final Player player : players) {
            System.out.println(player.getRole());
        }


    }

    private static int generateRandom(int size) {
        return new Random().nextInt(size);
    }

}
