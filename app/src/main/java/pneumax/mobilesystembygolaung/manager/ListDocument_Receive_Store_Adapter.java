package pneumax.mobilesystembygolaung.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pneumax.mobilesystembygolaung.R;


public  class ListDocument_Receive_Store_Adapter extends BaseAdapter {
    private Context context;
    private String[] arrListDocument;
    private String[] arrListRefno;

    public ListDocument_Receive_Store_Adapter(Context context,
                                              String[] arrListDocument,
                                              String[] arrListRefno) {
        this.context = context;
        this.arrListDocument = arrListDocument;
        this.arrListRefno = arrListRefno;

    }

    @Override
    public int getCount() {
        return arrListDocument.length;
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
        View view1 = layoutInflater.inflate(R.layout.listdocument_receive_store, viewGroup, false);

        //Initial View
        TextView mColDocno = view1.findViewById(R.id.tvColDocno);
        TextView mColRefno = view1.findViewById(R.id.tvColRefno);

        //Show View
        mColDocno.setText(arrListDocument[i]);
        mColRefno.setText(arrListRefno[i]);

        return view1;
    }

}