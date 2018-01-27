package info.androidhive.uplus;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    int colorCode;
    String character;
    ArrayList<Contact>arrayList;
    Context context;
    public ContactsAdapter(Context context, ArrayList<Contact> contacts)
    {
        super(context, 0, contacts);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvMobile = (TextView) view.findViewById(R.id.tvMobile);
        ImageView imgContactImage=(ImageView) view.findViewById(R.id.imgContactProfile);
        String tvEmail;

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        colorCode=color1;

        TextDrawable drawable = TextDrawable.builder().buildRound(textTobeConverted(contact.name), color1);
        imgContactImage.setImageDrawable(drawable);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvName.setText(contact.name);
        tvEmail = "";
        tvPhone.setText("");
        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            tvPhone.setText(contact.numbers.get(0).number);
            if(tvPhone.getText().toString().startsWith("+25078") || tvPhone.getText().toString().startsWith("078") || tvPhone.getText().toString().startsWith("+250 78") || tvPhone.getText().toString().startsWith("(078)")){
                tvMobile.setText("MTN");
            }else if(tvPhone.getText().toString().startsWith("+25072") || tvPhone.getText().toString().startsWith("072") || tvPhone.getText().toString().startsWith("+250 72") || tvPhone.getText().toString().startsWith("(072)")){
                tvMobile.setText("TIGO");
            }else if(tvPhone.getText().toString().startsWith("+25073") || tvPhone.getText().toString().startsWith("073") || tvPhone.getText().toString().startsWith("+250 73") || tvPhone.getText().toString().startsWith("(073)")){
                tvMobile.setText("AIRTEL");
            }else{
                tvMobile.setText("OTHER");
            }

        }
        return view;
    }

    public String textTobeConverted(String data)
    {
        String newData="D";
        int length = data.length();
        if(length>0)
        {
            newData=data.substring(0,1);
            character=newData;
        }
        return newData;
    }

    //method to search data
    public void searchFilter(ArrayList<Contact> contactss)
    {
        arrayList=new ArrayList<>();
        arrayList.addAll(contactss);
        notifyDataSetChanged();
        for(int i=0;i<contactss.size();i++)
        {
            Toast.makeText(context,contactss.get(i).toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
