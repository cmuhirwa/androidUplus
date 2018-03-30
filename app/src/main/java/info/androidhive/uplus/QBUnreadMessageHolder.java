package info.androidhive.uplus;

import android.os.Bundle;

/**
 * Created by user on 16/03/2018.
 */

public class QBUnreadMessageHolder {
    private static QBUnreadMessageHolder instance;
    private Bundle bundle;

    public static synchronized QBUnreadMessageHolder getInstance(){
        QBUnreadMessageHolder qbUnreadMessageHolder;
        synchronized (QBUnreadMessageHolder.class)
        {
            if(instance == null)
                instance = new QBUnreadMessageHolder();
            qbUnreadMessageHolder = instance;

        }
        return qbUnreadMessageHolder;
    }

    private QBUnreadMessageHolder() { bundle= new Bundle(); }

    private void setBundle(Bundle bundle) { this.bundle = bundle; }

    public int getUnreadMessageByDialogId(String id) {   return this.bundle.getInt(id);  }


}
