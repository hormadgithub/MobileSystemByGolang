package pneumax.mobilesystembygolaung.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDoctype_Receive_Store;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDoctype_Receive_Store_Adapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class StoringActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    //parameter
    StaffLogin userLogin;
    String strDataBaseName;
    //parameter สำกรับส่งไป Execute
    String strServerAddress;
    String strTableName,strField,strCondition,strURL;
    ImageView mimgBackTop;
    View updateview;// above oncreate method
    ListView mlvListDoctypeStore;
    CircularImageView mbtnRefresh;
    String strSelectDoctype,strSelectDocdesc ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_storing);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility=new GlobalUtility();

        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
        createListViewDoctypeStore();
        mbtnRefresh.bringToFront();

    }

    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
    }


    private void BindWidgets() {
        mlvListDoctypeStore =(ListView) findViewById(R.id.lvListDoctypeStore);
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
        mbtnRefresh=(CircularImageView) findViewById(R.id.btnRefresh);
    }

    private void InitializeData() {
        mlvListDoctypeStore.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDoctypeStore.setAdapter(null);
    }

    private void SetEvent() {

        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

            }
        });

        mbtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListViewDoctypeStore();
            }
        });
    }



    private void createListViewDoctypeStore() {
        ListView listView = findViewById(R.id.lvListDoctypeStore);
        String tag = "6SepV2";
        try {
            ExecuteGetListDoctype_Receive_Store executeGetListDoctype_Receive_Store = new ExecuteGetListDoctype_Receive_Store(getApplicationContext());
            //C คือ VW_Mobile_ListDocNo_Checking  แสดงเอกสารที่รอ Confirm
            strURL=strServerAddress+ myConstant.urlMobile_ListDoctypeStore();
            executeGetListDoctype_Receive_Store.execute(strDataBaseName, strURL);
            String resultJSON = executeGetListDoctype_Receive_Store.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListDoctypeString = new String[jsonArray.length()];
            final String[] arrListDocdescString = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListDoctypeString[i] = jsonObject.getString("Doctype");
                arrListDocdescString[i] = jsonObject.getString("Docdesc");
                Log.d(tag, "Doctype [" + i + "] ==> " + arrListDoctypeString[i]);
            }//for

            ListDoctype_Receive_Store_Adapter listDoctype_Receive_Store_Adapter = new ListDoctype_Receive_Store_Adapter(getApplicationContext(), arrListDoctypeString, arrListDocdescString);
            listView.setAdapter(listDoctype_Receive_Store_Adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));

                    strSelectDoctype=arrListDoctypeString[i].trim();
                    strSelectDocdesc=arrListDocdescString[i].trim();
                    //Storing Parttube
                    if (strSelectDoctype.equals("SPT")) {
                        startStoringParttube(strSelectDoctype,strSelectDocdesc);
                    }
                    else {
                        startStoreDoctype(strSelectDoctype,strSelectDocdesc);
                    }
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView

    private void startStoreDoctype(String strDoctype,String strDocdesc) {
        Intent intent = new Intent(StoringActivity.this, StoreDoctypeActivity.class);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        intent.putExtra("SelectDoctype", strDoctype);
        intent.putExtra("SelectDocdesc", strDocdesc);
        startActivity(intent);
    }

    private void startStoringParttube(String strDoctype,String strDocdesc) {
        Intent intent = new Intent(StoringActivity.this, StoringParttubeActivity.class);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        intent.putExtra("SelectDoctype", strDoctype);
        intent.putExtra("SelectDocdesc", strDocdesc);
        startActivity(intent);
    }
}