package Class;

public class Bet {
    int eventID;
    String dateStart;
    String dateEnd;
    float betOdd;
    int  betStatus;
    String whoWon;

    public String getWhoWon() {
        return whoWon;
    }

    public void setWhoWon(String whoWon) {
        this.whoWon = whoWon;
    }

    public float getBetOdd() {
        return betOdd;
    }

    public void setBetOdd(float betOdd) {
        this.betOdd = betOdd;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getBetStatus() {
        return betStatus;
    }

    public void setBetStatus(int betStatus) {
        this.betStatus = betStatus;
    }
}
