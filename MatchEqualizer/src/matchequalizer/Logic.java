/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchequalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author ignaciodeandreisdenis
 */
public class Logic {

    int matchMagic;

    Object[] players;  
    int k = 5;                             // sequence length   

    List<Object[]> subsets = new ArrayList<>();

    int[] s = new int[k];                  // here we'll keep indices 
    // pointing to elements in input array

    public Logic() {
    }

    public Logic(Object[] players) {
        this.players = players;
        matchMagic = getMagic(players);
    }

    public ArrayList<ArrayList<Player[]>> equalize() {
        if (k <= players.length) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++);
            subsets.add(getSubset(players, s));
            for (;;) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == players.length - k + i; i--);
                if (i < 0) {
                    break;
                } else {
                    s[i]++;                    // increment this item
                    for (++i; i < k; i++) {    // fill up remaining items
                        s[i] = s[i - 1] + 1;
                    }
                    subsets.add(getSubset(players, s));
                }
            }
        }

        ArrayList<Player[]> theMostEqualizedTeams = new ArrayList<Player[]>();

        int minimumDifference = getMinimumDifference();

        for (int i = 0; i < subsets.size(); i++) {

            Player[] team = (Player[]) subsets.get(i);

            int teamMagic = getMagic(team);

            Player[] teamAux = getTheOtherTeam(team);
            int team2Magic = getMagic(teamAux);

            System.out.println("Diferencia: " + Math.abs(teamMagic - team2Magic));

            if (Math.abs(teamMagic - team2Magic) <= minimumDifference) {
                minimumDifference = Math.abs(teamMagic - team2Magic);
                theMostEqualizedTeams.add(team);
            }

        }

        ArrayList<ArrayList<Player[]>> response = new ArrayList<>();

        for (int i = 0; i < theMostEqualizedTeams.size(); i++) {

            System.out.println("Team 1: " + Arrays.toString(theMostEqualizedTeams.get(i)));
            System.out.println(getMagic(theMostEqualizedTeams.get(i)));

            Player[] theOtherMostEqualizedTeam = getTheOtherTeam(theMostEqualizedTeams.get(i));

            System.out.println("Team 2: " + Arrays.asList(theOtherMostEqualizedTeam));
            System.out.println(getMagic(theOtherMostEqualizedTeam));

            if (!containsThisCombination(response, theMostEqualizedTeams.get(i))) {
                ArrayList<Player[]> teams = new ArrayList<>();
                teams.add(theMostEqualizedTeams.get(i));
                teams.add(theOtherMostEqualizedTeam);
                response.add(teams);
                System.out.println("Added");
            } else {
                System.out.println("Not Added");
            }

            System.out.println("------------------------------------------------------------------------------------");

        }

        return response;

    }

    private boolean containsThisCombination(ArrayList<ArrayList<Player[]>> response, Player[] theOtherMostEqualizedTeam) {

        boolean contains = false;

        for (int i = 0; i < response.size(); i++) {

            boolean needsToBreak = false;

            for (int j = 0; j < response.get(i).size(); j++) {

                if (getAsString(response.get(i).get(j)).equals(getAsString(theOtherMostEqualizedTeam))) {
                    contains = true;
                    System.out.println("contains");
                    needsToBreak = true;
                    break;

                }

            }

            if (needsToBreak) {
                break;
            }

        }

        return contains;

    }

    private String getAsString(Player[] team) {
        String response = "";
        for (int i = 0; i < team.length; i++) {
            response += team[i].getName();
        }
        return response;
    }

    private int getMinimumDifference() {

        int minimum = Integer.MAX_VALUE;

        for (int i = 0; i < subsets.size(); i++) {

            Player[] team = (Player[]) subsets.get(i);

            int teamMagic = getMagic(team);

            Player[] teamAux = getTheOtherTeam(team);
            int team2Magic = getMagic(teamAux);

            if (Math.abs(teamMagic - team2Magic) <= minimum) {
                minimum = Math.abs(teamMagic - team2Magic);
            }

        }

        return minimum;

    }

    private Player[] getTheOtherTeam(Player[] theMostEqualizedTeam) {

        Player[] theOtherMostEqualizedTeam = new Player[5];
        int index = 0;

        for (int a = 0; a < players.length; a++) {

            boolean contains = false;

            for (int b = 0; b < theMostEqualizedTeam.length; b++) {

                if (players[a].equals(theMostEqualizedTeam[b])) {
                    contains = true;
                }

            }

            if (!contains) {
                theOtherMostEqualizedTeam[index] = (Player) players[a];
                index++;
            }
        }

        return theOtherMostEqualizedTeam;
    }

    public int getMagic(Object[] team) {
        int teamMagic = 0;

        for (int j = 0; j < team.length; j++) {
            teamMagic += ((Player) team[j]).getLevel();
        }

        return teamMagic;
    }

// generate actual subset by index sequence
    Object[] getSubset(Object[] input, int[] subset) {
        Player[] result = new Player[subset.length];
        for (int i = 0; i < subset.length; i++) {
            result[i] = (Player) input[subset[i]];
        }
        return result;
    }

}
