package info.androidhive.uplus.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.uplus.Contact;
import info.androidhive.uplus.ContactFetcher;
import info.androidhive.uplus.ContactsAdapter;
import info.androidhive.uplus.R;
import info.androidhive.uplus.Transactions;
import info.androidhive.uplus.UserProfile;


public class TwoFragment extends Fragment implements SearchView.OnQueryTextListener {
    ArrayList<Contact> listContacts;
    ListView lvContacts;
    FloatingActionButton fabHist;
    ContactsAdapter adapterContacts;
    ArrayList<Contact> arrayList = new ArrayList<>();
    FloatingActionButton btn;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_two, container, false);
        setHasOptionsMenu(true);
        listContacts = new ContactFetcher(getActivity()).fetchAll();
        lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        fabHist = (FloatingActionButton) view.findViewById(R.id.histBtn);
        setAdapterContacts(listContacts);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent in1 = new Intent(getContext(), UserProfile.class);

                TextView c = (TextView) view.findViewById(R.id.tvMobile);
                TextView d = (TextView) view.findViewById(R.id.tvPhone);
                TextView e = (TextView) view.findViewById(R.id.tvName);
                String state = c.getText().toString();
                String name = e.getText().toString();
                String phone = d.getText().toString();
                in1.putExtra("user", name);
                in1.putExtra("state", state);
                in1.putExtra("phone", phone);
                startActivity(in1);
            }
        });

        showAlert(fabHist);

        return view;

    }

    public void showAlert(FloatingActionButton btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(getActivity(), Transactions.class);
                startActivity(t);
            }
        });

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();
        newText = newText.toLowerCase();
        int counter=0;
            ArrayList<Contact> newList = new ArrayList<>();
            for (Contact contact : listContacts) {
                String name="";
                if(contact.name!=null)
                {
                    name = contact.name.toLowerCase();
                }

                if (name.contains(newText)) {
                    newList.add(contact);
                    //Toast.makeText(getActivity(),"available",Toast.LENGTH_SHORT).show();
                }
                //counter++;
            }
            //
        //adapterContacts.searchFilter(newList);
        Collections.sort(newList, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact, Contact t1) {
                return 0;
            }
        });

        setAdapterContacts(newList);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuItem=menu.findItem(R.id.menu_item);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.menu_item);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    public void setAdapterContacts(ArrayList<Contact> listContact) {

        adapterContacts = new ContactsAdapter(getActivity(), listContact);


        lvContacts.setAdapter(adapterContacts);

        adapterContacts.notifyDataSetChanged();
    }


}


