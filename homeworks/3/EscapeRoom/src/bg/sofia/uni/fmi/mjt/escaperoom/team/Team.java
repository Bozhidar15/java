package bg.sofia.uni.fmi.mjt.escaperoom.team;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class Team implements Ratable {

    String name;
    public TeamMember[] members;
    double rating=0;
    private Team(String name, TeamMember[] members){
        this.name=name;
        this.members=members;
    }
    public static Team of(String name, TeamMember[] members){//static
        return new Team(name, members);
    }
    /**
     * Updates the team rating by adding the specified points to it.
     *
     * @param points the points to be added to the team rating.
     * @throws IllegalArgumentException if the points are negative.
     */
    public void updateRating(int points) {
        if (points<0){
            throw new IllegalArgumentException("Invaid points");
        }else {
            rating += points;
        }
    }

    /**
     * Returns the team name.
     */
    public String getName() {
        return name;
    }

    public double getRating(){
        return rating;
    }
}
