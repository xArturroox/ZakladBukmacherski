package Class;

import java.util.ArrayList;
import java.util.List;

public class Coupon {


    float betOdd;
    float betMoney;
    float betWon;
    int couponStatus;

    public List<Bet> betList;

    public float getBetOdd() {
        return betOdd;
    }
    public Coupon()
    {
        betList = new ArrayList<Bet>();
    }

    public float getBetMoney() {
        return betMoney;
    }

    public void setBetMoney(float betMoney) {
        this.betMoney = betMoney;
    }

    public float getBetWon() {
        return betWon;
    }

    public void setBetWon(float betWon) {
        this.betWon = betWon;
    }

    public int getBetState() {
        return couponStatus;
    }

    public void setBetState(int couponStatus) {
        this.couponStatus = couponStatus;
    }


    public void setBetOdd(float betOdd) {
        this.betOdd = betOdd;
    }
}
