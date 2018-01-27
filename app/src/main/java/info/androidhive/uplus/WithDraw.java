package info.androidhive.uplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class WithDraw extends AppCompatActivity {
    Spinner spnWithDrawAccount;
    String Bankid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        spnWithDrawAccount=(Spinner)findViewById(R.id.spnWithdrawAccount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Collection, android.R.layout.simple_list_item_1);
        spnWithDrawAccount.setAdapter(adapter);
        spnWithDrawAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               int index=spnWithDrawAccount.getSelectedItemPosition();
                if(index==0)
                {
                    Bankid="1";
                }
                else if(index==1)
                {
                    Bankid="2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
