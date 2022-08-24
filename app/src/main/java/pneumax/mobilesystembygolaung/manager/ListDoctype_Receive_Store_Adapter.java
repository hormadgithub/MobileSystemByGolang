package pneumax.mobilesystembygolaung.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pneumax.mobilesystembygolaung.R;


public  class ListDoctype_Receive_Store_Adapter extends BaseAdapter {
    private Context context;
    private String[] arrListDoctype;
    private String[] arrListDocdesc;

    public ListDoctype_Receive_Store_Adapter(Context context,
                                             String[] arrListDoctype,
                                             String[] arrListDocdesc) {
        this.context = context;
        this.arrListDoctype = arrListDoctype;
        this.arrListDocdesc = arrListDocdesc;

    }

    @Override
    public int getCount() {
        return arrListDoctype.length;
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
        View view1 = layoutInflater.inflate(R.layout.listdoctype_receive_store, viewGroup, false);

        //Initial View
        TextView mColDoctype = view1.findViewById(R.id.tvColDoctype);
        TextView mColDocdesc = view1.findViewById(R.id.tvColDocdesc);

        //Show View
        mColDoctype.setText(arrListDoctype[i]);
        mColDocdesc.setText(arrListDocdesc[i]);

        return view1;
    }

}