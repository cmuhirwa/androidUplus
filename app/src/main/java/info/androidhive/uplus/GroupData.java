package info.androidhive.uplus;

/**
 * Created by RwandaFab on 7/27/2017.
 */

public class GroupData {

    //create the constructore to get the data

    String groupName,groupTargetAmount;
    public GroupData(String groupName,String groupTargetAmount)
    {
        this.setGroupName(groupName);
        this.setGroupTargetAmount(groupTargetAmount);
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupTargetAmount() {
        return groupTargetAmount;
    }

    public void setGroupTargetAmount(String groupTargetAmount) { this.groupTargetAmount = groupTargetAmount; }
}
