package com.thoughtworks.sneak_peak.domain;

/**
 * @author thimmv
 */
public class Player {

    private int playerId;
    private Role role;

    public Player(final int playerId, final Role role) {
        this.playerId = playerId;
        this.role = role;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final int playerId) {
        this.playerId = playerId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }
}
