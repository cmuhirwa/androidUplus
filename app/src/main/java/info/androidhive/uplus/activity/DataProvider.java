package info.androidhive.uplus.activity;

/**
 * Created by RwandaFab on 7/27/2017.
 */

public class DataProvider {
    private String Member,Amount;

    public DataProvider(String Member,String Amount)
    {
        this.setMember(Member);
        this.setAmount(Amount);
    }
    public String getMember() {
        return Member;
    }

    public void setMember(String member) {
        Member = member;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
