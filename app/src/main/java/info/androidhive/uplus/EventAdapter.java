
package info.androidhive.uplus;
        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.PopupMenu;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.amulyakhare.textdrawable.TextDrawable;
        import com.amulyakhare.textdrawable.util.ColorGenerator;
        import com.squareup.picasso.Picasso;

        import org.w3c.dom.Text;

        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.Locale;

        import info.androidhive.uplus.R;
        import info.androidhive.uplus.activity.SharedPrefManager;

        import static android.R.attr.data;

/**
 * Created by delaroy on 2/13/17.
 */
public class EventAdapter extends RecyclerView.Adapter<info.androidhive.uplus.EventAdapter.MyViewHolder> {
    private ArrayList<String> mDataset=new ArrayList<String>();
    private ArrayList<String> mPrice=new ArrayList<String>();
    private ArrayList<String> mTicketCode =new ArrayList<String>();
    private String mEventId, mEventName, mEventLocation;
    Context ctx;
    RecyclerView rec;
    Uri uri;

    public EventAdapter(ArrayList<String> mDataset,ArrayList<String> mPrice ,Context ctx,RecyclerView rec, ArrayList<String> mTicketCode, String mEventId, String mEventName,String mEventLocation){
        this.mDataset       = mDataset;
        this.mPrice         = mPrice;
        this.ctx            = ctx;
        this.rec            = rec;
        this.mTicketCode    = mTicketCode;
        this.mEventId       = mEventId;
        this.mEventName     = mEventName;
        this.mEventLocation = mEventLocation;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView, mTicketPrice;
        public Button mBtnBook;
        public RelativeLayout relativeLayout;// mCardView;
        public MyViewHolder(View v, final Context context,final ArrayList<String>eventName, final ArrayList<String>eventPrice,final RecyclerView rcc){
            super(v);
            mTextView       = (TextView) v.findViewById(R.id.ticketName1);
            mTicketPrice    = (TextView) v.findViewById(R.id.ticketamount);
            mBtnBook        = (Button)  v.findViewById(R.id.btnBook);
        }
    }

    @Override
    public info.androidhive.uplus.EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitem, parent, false);
        info.androidhive.uplus.EventAdapter.MyViewHolder vh = new info.androidhive.uplus.EventAdapter.MyViewHolder(v, this.ctx, this.mDataset, this.mPrice, this.rec);
        return vh;
    }

    @Override
    public void onBindViewHolder(final info.androidhive.uplus.EventAdapter.MyViewHolder holder, final int position){
        holder.mTextView.setText(mDataset.get(position));
        holder.mTicketPrice.setText(currencyConverter(mPrice.get(position)));
        holder.mBtnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.bookticket);
                dialog.show();
                final EditText editAmount, editAccount;
                final TextView ticketTitle;
                    final String Bankid;
                    final Button btnContribute;

                        editAmount  = (EditText)dialog.findViewById(R.id.editAmount);
                        ticketTitle = (TextView)dialog.findViewById(R.id.ticketTitle);
                        ticketTitle.setText(mDataset.get(position));
                        final Spinner spnAccount=(Spinner)dialog.findViewById(R.id.spnAccount);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),R.array.Collection, android.R.layout.simple_list_item_1);
                        spnAccount.setAdapter(adapter);
                        spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int index=spnAccount.getSelectedItemPosition();

//                                if(index==0)
//                                {
//                                    Bankid="1";
//                                }
//                                else
//                                {
//                                    Bankid="2";
//                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        btnContribute=(Button)dialog.findViewById(R.id.btnContribute);
                        editAccount=(EditText)dialog.findViewById(R.id.editAccount);
                        String number = SharedPrefManager.getInstance(v.getContext()).getPhone();
                        editAccount.setText(number);
                        editAmount.setText(mPrice.get(position));
                        editAmount.setFocusable(false);
                        btnContribute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String amount = editAmount.getText().toString();
                                String account = editAccount.getText().toString();
                                //validate data
                                if (amount.length() > 0 && Integer.parseInt(amount) >= 100) {
                                    if (account.length() == 10) {
                                        String pullNumber = account;
                                        String eventId = mEventId;
                                        String eventName = mEventName;
                                        String eventLocation = mEventLocation;
                                        String seatCode = mTicketCode.get(position);
                                        String method = "eventBook";
                                        String userId = SharedPrefManager.getInstance(v.getContext()).getUserId().toString();
                                        final ProgressDialog progress = new ProgressDialog(v.getContext());
                                        progress.setTitle("Booking");
                                        // progress.setCancelable(false);
                                        progress.setMessage("Please wait...");
                                        progress.setCancelable(false);
                                        dialog.dismiss();
                                        progress.show();
                                        EventBookBg eventBookBg = new EventBookBg(v.getContext(), progress, eventName, eventLocation);
                                        eventBookBg.execute(method, pullNumber, eventId, seatCode, userId);
                                    } else {
                                        Toast.makeText(v.getContext(), "Please enter a valid phone", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(v.getContext(), "Please enter a valid amount", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    public String currencyConverter(String data)
    {
        Locale locale = new Locale("RWF", "RWF");
        Double value=Double.parseDouble(data);
        DecimalFormat decimalFormat=new DecimalFormat("#,### RWF");
        return(decimalFormat.format(value));
    }

    public String textTobeConverted(String data)
    {
        String newData="D";
        int length=data.length();
        if(length>0)
        {
            newData=data.substring(0,1);
        }
        return newData;
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

}