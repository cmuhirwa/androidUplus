package info.androidhive.uplus;

/**
 * Created by RwandaFab on 7/30/2017.
 */

public class SaveGroupContract {
    public static final String TABLE_NAME="groupinfo";
    public static final String ID="id";
    public static final String GROUP_NAME="groupName";
    public static final String TARGET_TYPE="targetType";
    public static final String TARGET_AMOUNT="targetAmount";
    public static final String PER_PERSON_TYPE="perPersonType";
    public static final String PER_PERSON="perPerson";
    public static final String ADMIN_ID="adminId";
    public static final String COLLECTION_TYPE="collectionType";
    public static final String COLLECTION_ACCOUNT="collectionAccount";
    public static final String SYNC_STATUS="status";
    public static final int SYNC_OK=0;
    public static final int SYNC_FAILED=1;
    public static final String UI_UPDATE_BROADCAST="info.androidhive.materialtabs.uiupdatebroadcast";
}
