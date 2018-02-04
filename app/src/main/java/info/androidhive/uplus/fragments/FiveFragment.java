package info.androidhive.uplus.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import info.androidhive.uplus.R;
import info.androidhive.uplus.activity.SharedPrefManager;


public class FiveFragment extends Fragment{

    Dialog dialog;
    String Bankid;

    public FiveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_five, container, false);

        TabHost tabChurch = (TabHost) view.findViewById(R.id.tabChurch);
        tabChurch.setup();

        TabHost.TabSpec spec1 = tabChurch.newTabSpec(" FEEDS ");
        spec1.setIndicator(" FEEDS ");
        spec1.setContent(R.id.Feeds);
        tabChurch.addTab(spec1);

        TabHost.TabSpec spec2 = tabChurch.newTabSpec(" GROUPS ");
        spec2.setIndicator(" GROUPS ");
        spec2.setContent(R.id.CGroups);
        tabChurch.addTab(spec2);

        TabHost.TabSpec spec3 = tabChurch.newTabSpec(" DONATE ");
        spec3.setIndicator(" DONATE ");
        spec3.setContent(R.id.Donate);
        tabChurch.addTab(spec3);

        return view;
    }

//    public void contributeNow()
//    {
//        try
//        {
//            dialog=new Dialog(getContext());
//            dialog.setCancelable(true);
//            dialog.setContentView(R.layout.layout_contribute);
//            dialog.show();
//
//            final Spinner spnAccount=(Spinner)dialog.findViewById(R.id.spnAccount);
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.Collection, android.R.layout.simple_list_item_1);
//            spnAccount.setAdapter(adapter);
//            spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    int index=spnAccount.getSelectedItemPosition();
//
//                    if(index==0)
//                    {
//                         Bankid="1";
//                    }
//                    else
//                    {
//                        Bankid="2";
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//            btnContribute=(Button)dialog.findViewById(R.id.btnContribute);
//            editAccount=(EditText)dialog.findViewById(R.id.editAccount);
//            String number = SharedPrefManager.getInstance(getApplicationContext()).getPhone();
//            editAccount.setText(number);
//            btnContribute.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String amount=editAmount.getText().toString();
//                    String account=editAccount.getText().toString();
//                    //validate data
//                    if(amount.length()>0 && Integer.parseInt(amount)>=100)
//                    {
//                        if(account.length()==10)
//                        {
//                            //call contribute Worker
//                            String method="contribute";
//                            String userId = SharedPrefManager.getInstance(getApplicationContext()).getUserId().toString();
//                            String finaldata="memberId:"+userId+"\n GroupId:"+Ids+"\n amount:"+amount+"\n fromPhone:"+account+"\n bankId:"+Bankid;
//                            //Toast.makeText(getApplicationContext(),finaldata,Toast.LENGTH_LONG).show();
//                            progress = new ProgressDialog(groupdetails.this);
//                            progress.setTitle("Contributing");
//                            progress.setCancelable(false);
//                            progress.setMessage("Please wait...");
//                            progress.setCancelable(false);
//                            dialog.dismiss();
//                            progress.show();
//                            SharedPreferences sharedPreferences=groupdetails.this.getSharedPreferences("info.androidhive.materialtabs", Context.MODE_PRIVATE);
//                            String userName=sharedPreferences.getString("userName",null);
//                            ContributeWorker contributeWorker=new ContributeWorker(getApplicationContext(),progress,Ids,Name,groupImage,Amount, groupBalance);
//
//                            contributeWorker.execute(method,userId,Ids,amount,account,Bankid);
//                        }
//                        else
//                        {
//                            Toast.makeText(getApplicationContext(),"Please enter a valid phone",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Please enter a valid amount",Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            });
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }


}
