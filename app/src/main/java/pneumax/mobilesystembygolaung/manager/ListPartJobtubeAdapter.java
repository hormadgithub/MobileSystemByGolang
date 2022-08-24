package pneumax.mobilesystembygolaung.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pneumax.mobilesystembygolaung.R;

public class ListPartJobtubeAdapter extends BaseAdapter {

    private Context context;
    private String[] arrListPartNid;
    private String[] arrListDigitno;
    private String[] arrDescripiton;

    public ListPartJobtubeAdapter(Context context,
                                  String[] arrListPartNid,
                                  String[] arrListDigitno,
                                  String[] arrDescripiton) {
        this.context = context;
        this.arrListPartNid = arrListPartNid;
        this.arrListDigitno = arrListDigitno;
        this.arrDescripiton = arrDescripiton;

    }

    @Override
    public int getCount() {
        return arrListPartNid.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //LayoutInflater ป้องกันการ Error อาจเกิดจาก String + integer แล้ว error
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //ใส่ข้อมูลใน listview_appointment
        View view1 = layoutInflater.inflate(R.layout.listpartjobtubedata, viewGroup, false);

        //Initial View
        TextView mtvColPartNid = view1.findViewById(R.id.tvColPartNid);
        TextView mtvColDigitno = view1.findViewById(R.id.tvColDigitno);
        TextView mtvColDescription = view1.findViewById(R.id.tvColDescription);

        //Show View
        mtvColPartNid.setText(arrListPartNid[i]);
        mtvColDigitno.setText(arrListDigitno[i]);
        mtvColDescription.setText(arrDescripiton[i]);

        return view1;
    }
}