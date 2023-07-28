package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.PlatformCapacityExceededException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

import java.lang.reflect.Array;
import java.util.Arrays;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI,EscapeRoomPortalAPI{
    private Team[] teams;
    private int maxCapacity, indexToPutRoom=0;
    private EscapeRoom[] Rooms;
    private static final String room="room name is null, empty or blank",
            team="the team name is null, empty or blank";
    public EscapeRoomPlatform(Team[] teams, int maxCapacity){
        this.teams=teams;
        this.maxCapacity=maxCapacity;
        Rooms = new EscapeRoom[maxCapacity];
    }
    private void StringIsNullBlankEmpty(String str, String problem){
        if (str==null||str.isBlank()||str.isEmpty()){
            throw new IllegalArgumentException(problem);
        }
    }
    @Override
    public void addEscapeRoom(EscapeRoom room) throws RoomAlreadyExistsException{
        if (room == null){
            throw new IllegalArgumentException("This room is null");
        }else if (indexToPutRoom==maxCapacity){
            throw new PlatformCapacityExceededException("the maximum number of escape rooms has already been reached");
        }
        for (int i = 0; i < indexToPutRoom; i++) {
            if (Rooms[i].getName().equals(room.getName()))
                throw new RoomAlreadyExistsException("the specified room already exists in the platform");
        }
        Rooms[indexToPutRoom++]=room;
    }

    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException{
        StringIsNullBlankEmpty(roomName,room);
        boolean check=true;
        EscapeRoom []newrooms=new EscapeRoom[maxCapacity];
        for (int i = 0,z=0; i < indexToPutRoom; i++) {
            if (!Rooms[i].getName().equals(roomName)) {
                newrooms[z++]=Rooms[i];
//                Rooms[i] = Rooms[indexToPutRoom-1];
//                Rooms[indexToPutRoom-1]=null;
                //indexToPutRoom--;
            }else {
                check = false;
            }
        }
        if (check){
            throw new RoomNotFoundException();
        }
        indexToPutRoom--;
        Rooms=newrooms;
    }

    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime)
            throws RoomNotFoundException, TeamNotFoundException{
        StringIsNullBlankEmpty(roomName,room);
        StringIsNullBlankEmpty(teamName,team);
        int counter=0,bonus=0;
        boolean check=true;
        for (; counter < indexToPutRoom; counter++) {
            if (Rooms[counter].getName().equals(roomName)){
                if (escapeTime<=Rooms[counter].getMaxTimeToEscape()/2){
                    bonus=2;
                }else if (escapeTime<=(Rooms[counter].getMaxTimeToEscape()/4)*3){
                    bonus=1;
                }
                boolean checkForteams=true;
                for (Team value : teams) {
                    if (value.getName().equals(teamName)) {
                        value.updateRating(Rooms[counter].getDifficulty().getRank() + bonus);
                        checkForteams = false;
                        break;
                    }
                }
                check=false;
                if (checkForteams){
                    throw new TeamNotFoundException();// may not
                }
                break;
            }
        }
        if (check){
            throw new RoomNotFoundException();
        }
        if (escapeTime<=0||escapeTime>Rooms[counter].getMaxTimeToEscape()) {
            throw new IllegalArgumentException("the escape time is negative, zero or bigger than the maximum time " +
                    "to escape for the specified room");
        }
    }

    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException{
        StringIsNullBlankEmpty(roomName,room);
        for (int i = 0; i < indexToPutRoom; i++) {
            if (Rooms[i].getName().equals(roomName)){
                return Rooms[i];
            }
        }
        throw new RoomNotFoundException();
    }

    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException{
        if (review==null){
            throw new IllegalArgumentException("the review is null");
        }
        StringIsNullBlankEmpty(roomName,room);
        for (int i = 0; i < indexToPutRoom; i++) {
            if (Rooms[i].getName().equals(roomName)){
                Rooms[i].addReview(review);
                return;
            }
        }
        throw new RoomNotFoundException();
    }

    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException{
        StringIsNullBlankEmpty(roomName,room);
        for (int i = 0; i < indexToPutRoom; i++) {
            if (Rooms[i].getName().equals(roomName)){
                return Rooms[i].getReviews();
            }
        }
        throw new RoomNotFoundException();
    }

    @Override
    public Team getTopTeamByRating(){
        if (teams.length>0){
            int index=0;
            double max=teams[0].getRating();
            for (int i = 1; i < teams.length; i++) {
                if (teams[i].getRating()>max){
                    max=teams[i].getRating();
                    index=i;
                }
            }
            return teams[index];
        }else {
            return null;
        }
    }

    @Override
    public EscapeRoom[] getAllEscapeRooms(){
        return Arrays.copyOfRange(Rooms,0,indexToPutRoom);
    }
}
