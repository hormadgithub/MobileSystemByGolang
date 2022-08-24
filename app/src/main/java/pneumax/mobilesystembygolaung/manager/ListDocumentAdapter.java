package pneumax.mobilesystembygolaung.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pneumax.mobilesystembygolaung.R;


public  class ListDocumentAdapter extends BaseAdapter {
    private Context context;
    private String[] arrListDocument;
    private String[] arrCSName;
    private String[] arrSortDate;

    public ListDocumentAdapter(Context context,
                               String[] arrListDocument,
                               String[] arrCSName,
                               String[] arrSortDate){
        this.context = context;
        this.arrListDocument = arrListDocument;
        this.arrCSName = arrCSName;
        this.arrSortDate = arrSortDate;

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
        View view1 = layoutInflater.inflate(R.layout.listdocumentdata, viewGroup, false);

        //Initial View
        TextView mColDocno = view1.findViewById(R.id.tvColDocno);
        TextView mColCSName = view1.findViewById(R.id.tvColCustname);
        TextView mColSortDate = view1.findViewById(R.id.tvColSortDate);

        //Show View
        mColDocno.setText(arrListDocument[i]);
        mColCSName.setText(arrCSName[i]);
        mColSortDate.setText(arrSortDate[i]);

        return view1;
    }

}