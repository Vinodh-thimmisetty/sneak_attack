package com.thoughtworks.sneak_peak.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * @author thimmv
 */
public class KillerInnocent {

    static class Player {
        private int id;
        private String role;

        public Player(final int id, final String role) {
            this.id = id;
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        public int getId() {
            return id;
        }
    }

    enum Role {
        KILLER, INNOCENT
    }

    // consider 1 as Killer and remaining as innocent
    private static Integer KILLER_ID = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int noOfPlayers = sc.nextInt();
        boolean isKillerFound = false;
        List<Player> players = new ArrayList<>();
        int killerIndex = noOfPlayers - 1; // assuming last player is killer

        for (int i = 0; i < noOfPlayers; i++) {
            final int tempIndex = generateRandom(noOfPlayers);
            if (tempIndex == KILLER_ID) {
                if (isKillerFound) {
                    players.add(new Player(i, Role.INNOCENT.toString()));
                } else {
                    isKillerFound = true;
                    killerIndex = i;
                    players.add(new Player(i, Role.KILLER.toString()));
                }
            } else {
                players.add(new Player(i, Role.INNOCENT.toString()));
            }
        }

        if (!isKillerFound) {
            players.remove(players.size() - 1);
            players.add(new Player(noOfPlayers - 1, Role.KILLER.toString()));
        }

        for (final Player player : players) {
            System.out.println("P" + player.getId() + " " + player.getRole());
        }

        System.out.println("=====================================");

        final int finalKillerIndex = killerIndex;
        int rounds = 1;
        while (players.size() - 1 > 0) {
            final Optional<Player> player = players.stream().filter(c -> c.getId() != finalKillerIndex).findFirst();
            if (player.isPresent()) {
                final Player temp = player.get();
                System.out.println("Round - " + rounds++);
                System.out.println("P" + finalKillerIndex + " KILLED P" + temp.getId());
                players.remove(temp);
            }
        }

    }

    private static int generateRandom(int size) {
        return new Random().nextInt(size);
    }

}
