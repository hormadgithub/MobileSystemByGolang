package pneumax.mobilesystembygolaung.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocumentAdapter;
import pneumax.mobilesystembygolaung.manager.ListSerialnoAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class DeliveryActivity extends AppCompatActivity {

    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL;

    String strforExecute;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;
    Boolean blnHavePartSerialNo=false;
    Boolean blnListPartEmpty;

    //From Layout
    ImageView mimgBackTop;
    Button   mbtnDelivery;
    CircularImageView mbtnSearch;

    EditText metScanDocument,metSelectDocno,metScanSerialno,metScanReceiveCode;
    TextView mtvShowDocSelected,metShowStaffDelivery,metShowStaffReceive;
    Group mgplvListDocument,mgpStaffDelivery,mgpSerialno,mgpStaffReceive;
    Boolean blnShowgplvListDocument,blnShowgpStaffDelivery,blnShowgpSerialno,blnShowgpStaffReceive;

    View updateview;// above oncreate method
    ListView mlvListDocument,mlvListSerialno;


    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_delivery);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility = new GlobalUtility();

        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
    }

    private void InitializeData() {
        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListSerialno.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mbtnSearch.setImageResource(R.drawable.ic_search_blue);

        mbtnSearch.setVisibility(View.VISIBLE);
        metScanDocument.setEnabled(true);

        Invisible_AllGroup();
        ClearData();
        mbtnDelivery.setEnabled(false);
        if( metScanDocument.isEnabled() ) {
            metScanDocument.requestFocus();
        }
        else{
            metScanReceiveCode.requestFocus();
        }
    }


    private void ClearData() {
        Invisible_AllGroup();

        metSelectDocno.setText("");
        mtvShowDocSelected.setText("");
        metShowStaffDelivery.setText("");
        metShowStaffReceive.setText("");

    }

    private void  Invisible_AllGroup(){
        mgplvListDocument.setVisibility(View.GONE);
        mgpStaffDelivery.setVisibility(View.GONE);
        mgpSerialno.setVisibility(View.GONE);
        mgpStaffReceive.setVisibility(View.GONE);

        blnShowgplvListDocument=false;
        blnShowgpStaffDelivery=false;
        blnShowgpSerialno=false;
        blnShowgpStaffReceive=false;
    }


    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
    }

    private void BindWidgets() {
        metScanDocument=(EditText) findViewById(R.id.etScanDocument);
        metScanSerialno=(EditText) findViewById(R.id.etScanSerialno);
        metScanReceiveCode=(EditText) findViewById(R.id.etScanReceiveCode);

        metSelectDocno=(EditText) findViewById(R.id.etSelectDocno);
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        metShowStaffDelivery=(EditText) findViewById(R.id.etShowStaffDelivery);
        metShowStaffReceive=(EditText) findViewById(R.id.etShowStaffReceive);

        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpStaffDelivery=(Group) findViewById(R.id.gpStaffDeliery);

        mlvListSerialno=(ListView) findViewById(R.id.lvListSerialno);
        mgpSerialno=(Group) findViewById(R.id.gpSerialno);
        mgpStaffReceive=(Group) findViewById(R.id.gpStaffReceive);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);
        mbtnDelivery =(Button) findViewById(R.id.btnDelivery);
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }



    private void SendKeyDown(Integer intrepeat){
        long now = SystemClock.uptimeMillis();
        BaseInputConnection mInputConnection = new BaseInputConnection(findViewById(R.id.etScanDocument), true);
        KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN, 0);
        for (int i = 0; i <intrepeat; i += 1) {
            mInputConnection.sendKeyEvent(down);
        }
    }


    private void SetEvent() {

        metScanDocument.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    mimgBackTop.requestFocus();
                    return true;
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
                    mbtnSearch.requestFocus();
                    return true;
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    blnManualInput=false; //เพือให้ TextOnChange ทำงาน
                    //เพิ่ม" " เข้าไปเพื่อไห้ไปทำงานใน onTextChanged
                    metScanDocument.setText(metScanDocument.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });

        metScanDocument.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String strScan;
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                //if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                //ระบบเดิมสามารถป้อนเลขที่เอกสารได้
                if((s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก

                if ( metScanDocument.isEnabled()) {
                    if (!metScanDocument.getText().toString().isEmpty() && !metScanDocument.getText().toString().equals("")) {
                        metScanDocument.setText("");
                        blnManualInput = false;
                        if (s.toString().length() == 10) {
                            strScan = s.toString().trim();
                            metSelectDocno.setText(strScan);
                            CheckPartHaveSerialno(strScan);
                            GetCustName(strScan);

                        } else {
                            ClearData();
                            strAlertMessage = "ไม่พบเอกสารที่คุณ Scan";
                            mtvShowDocSelected.setText(strAlertMessage);
                            //ShowAlertDialog(R.string.alertdialog_error,"คำเตือน:",strAlertMessage,R.drawable.alertdialog_ic_error);
                        }
                    }
                    mbtnSearch.setImageResource(R.drawable.ic_search_blue);
                    mgplvListDocument.setVisibility(View.GONE);
                }
              else {
                    if (!s.toString().equals("")) {
                        //เช็คว่าตอนนี้อยู่หน้าไหน
                        if (blnShowgpSerialno) {
                            strAlertMessage = "กรุณา Scan Serialno ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanSerialno.requestFocus();
                        }
                        if (blnShowgpStaffReceive) {
                            strAlertMessage = "กรุณา Scan คนรับสินค้าก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanReceiveCode.requestFocus();
                        }
                    }
                }
            }
        });


        metScanSerialno.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    mimgBackTop.requestFocus();
                    return true;
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    blnManualInput=false; //เพือให้ TextOnChange ทำงาน
                    //เพิ่ม" " เข้าไปเพื่อไห้ไปทำงานใน onTextChanged
                    metScanSerialno.setText(metScanSerialno.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanSerialno.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก


                if (blnShowgpSerialno) {
                    if (!s.toString().isEmpty() && !s.toString().equals("")) {
                        metScanSerialno.setText("");
                        blnManualInput = false;
                        if (s.toString().length() != 0) {
                            Execute_Update_RFCheckOut_PartSerialNo(metSelectDocno.getText().toString(), s.toString().trim());
                        } else {
                            strAlertMessage = "Not found serialno";
                            mtvShowDocSelected.setText(strAlertMessage);
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    }
                }
                else {
                    if (!s.toString().equals("")) {
                        if (blnShowgpStaffReceive) {
                            strAlertMessage = "กรุณา Scan คนรับสินค้าก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanReceiveCode.requestFocus();
                            SendKeyDown(2);
                        }
                    }
                }

            }
        });


        metScanReceiveCode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    mimgBackTop.requestFocus();
                    return true;
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    blnManualInput=false; //เพือให้ TextOnChange ทำงาน
                    //เพิ่ม" " เข้าไปเพื่อไห้ไปทำงานใน onTextChanged
                    metScanReceiveCode.setText(metScanReceiveCode.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanReceiveCode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String strScan;
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก

                if (!metScanReceiveCode.getText().toString().isEmpty() && !metScanReceiveCode.getText().toString().equals("")) {
                    metScanReceiveCode.setText("");
                    blnManualInput = false;
                    if (s.toString().length()==4 ) {
                        strScan = s.toString().trim();
                        metScanReceiveCode.setHint(strScan);
                        GetReceiveName(strScan);
                    }
                    else {
                        metShowStaffReceive.setText("");
                        strAlertMessage="คุณ Scan รหัสคนรับสินค้าไม่ถูกต้อง";
                        mtvShowDocSelected.setText(strAlertMessage);
                        ShowAlertDialog(R.string.alertdialog_error,"คำเตือน:",strAlertMessage, R.drawable.alertdialog_ic_error);
                    }
                    if (!metShowStaffReceive.getText().toString().isEmpty() && !metShowStaffReceive.getText().toString().equals("")){
                        mbtnDelivery.setEnabled(true);
                    }
                    else {
                        mbtnDelivery.setEnabled(false);
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "สินค้าที่คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการตรวจนับนะครับ", Toast.LENGTH_SHORT).show();
                }
                //mbtnSearch.setText(getResources().getString(R.string.search));
                mbtnSearch.setImageResource(R.drawable.ic_search_blue);
                mgplvListDocument.setVisibility(View.GONE);
            }
        });


        mbtnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListViewDocument();
            }
        });


        mbtnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ถ้าอยู่หน้ารับ Receive Code
                if (blnShowgpStaffReceive) {
                    if (!metShowStaffReceive.getText().toString().isEmpty() && !metShowStaffReceive.getText().toString().equals("")) {
// 01/03/2021 สโตร์ไม่ต้องการ CLick หลายครั้ง
//                        strAlertMessage = "คุณมั่นใจว่า สินค้าที่จ่ายไปถูกต้องแล้วใช่หรือไม่";
//                        ShowAlertDialog(R.string.alertdialog_warning, "ยืนยันความถูกต้อง.", strAlertMessage, R.drawable.alertdialog_ic_warning);
                        ExecuteDeliveryDocument(metSelectDocno.getText().toString());
                        InitializeData();
                    } else {
                        strAlertMessage = "กรุณา Scan ผู้รับสินค้าก่อนนะครับ";
                        ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        metScanReceiveCode.setCursorVisible(true);
                        metScanReceiveCode.requestFocus();
                    }
                }
            }
        });

        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (metScanDocument.isEnabled()) {
                    finish();
                    overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

                }
                else{
                    strforExecute="Update RFCheckOut_PartSerialNo set DlryCode='' where Docno='"+ metSelectDocno.getText().toString().trim() +"'";
                    strURL=strServerAddress+myConstant.urlspExecute();
                    strReturnValue= globalUtility.Execute(getApplicationContext(),strDataBaseName,strforExecute,strURL);

                    InitializeData();
                }
            }
        });

    }


    private void  ShowDelivery() {

        mbtnSearch.setVisibility(View.GONE);
        metScanDocument.setEnabled(false);

        String stfCode=userLogin.STFcode.trim();
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue = globalUtility.GetStaffName(getApplicationContext(),strDataBaseName,stfCode,strURL);
        metShowStaffDelivery.setText(stfCode+'-'+strReturnValue);

        Invisible_AllGroup();
        mgpStaffDelivery.setVisibility(View.VISIBLE);

        if(blnHavePartSerialNo){
            mgpSerialno.setVisibility(View.VISIBLE);
            blnShowgpSerialno=true;
            metScanSerialno.setHint("Scan Serialno");
            metScanSerialno.requestFocus();
            SendKeyDown(2);
        }
        else{
            mgpStaffReceive.setVisibility(View.VISIBLE);
            blnShowgpStaffReceive=true;
            metScanReceiveCode.setHint("รหัสคนรับสินค้า");
            metScanReceiveCode.requestFocus();
            SendKeyDown(2);

        }
    }



    private void  ShowStaffReceive() {
        metScanSerialno.setEnabled(false);
        mgpStaffReceive.setVisibility(View.VISIBLE);
        blnShowgpStaffReceive=true;
    }



    private void GetCustName(String strDocno) {
        if (CheckDeliveryDocument(metSelectDocno.getText().toString())) {
            //แสดง Cust Name
            strTableName = "VW_Mobile_ListDocNo_Delivery";
            strField = "CSName";
            strCondition = "Docno='" + strDocno.trim() + "'";
            strURL = strServerAddress + myConstant.urlMobile_ReturnValue();
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(), "strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition, strURL);
            mtvShowDocSelected.setText(strReturnValue);

            //เช็คว่ามีอยู่ในเอกสารรอตรวจสอบหรือไม่
            strField = "Docno";
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(), "strDataBaseName", strDataBaseName,
                    "strTableName", strTableName, "strField", strField,
                    "strCondition", strCondition, strURL);
            if (strReturnValue.equals("")) {
                strAlertMessage = "เอกสารที่คุณ Scan\n ไม่มีอยู่ในรายการที่รอจ่ายนะครับ ";
                ShowAlertDialog(R.string.alertdialog_error, "Error! ", strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                if (mtvShowDocSelected.getText().toString().equals(""))
                    mtvShowDocSelected.setText(strReturnValue);
                //ถ้าพบเอกสารที่รอจ่ายก็ให้ทำการแสดงการจ่ายสินค้า
                ShowDelivery();
            }
        }
    }

    private void CheckPartHaveSerialno(String strDocno) {
        strTableName="RFCheckOut_PartSerialNo";
        strField="Docno";
        strCondition="Docno='" + strDocno +"'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        blnHavePartSerialNo=(!strReturnValue.equals(""));

    }

    private void GetReceiveName(String strStfcode) {
        strURL=strServerAddress+ myConstant.urlMobile_ReturnValue();
        strReturnValue = globalUtility.GetStaffName(getApplicationContext(),strDataBaseName,strStfcode,strURL);
        metShowStaffReceive.setText(strStfcode.trim()+'-'+ strReturnValue);
    }



    private void Execute_Update_RFCheckOut_PartSerialNo(String strDocNo,String strSerialno) {
        String tag = "6SepV2";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Update_RFCheckOut_PartSerialNo();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strSerialno",strSerialno,
                    "strDeliveryCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Update_RFCheckOut_PartSerialNo();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
                    createListViewSerialno(strDocNo);
                    strAlertMessage=clsResult.ResultMessage;
                    if (strAlertMessage.toUpperCase().equals("COMPLETE")) {
                        ShowStaffReceive();
                        mgpStaffReceive.setVisibility(View.VISIBLE);
                        blnShowgpStaffReceive=true;
                        metScanReceiveCode.setHint("รหัสคนรับสินค้า");
                        metScanReceiveCode.requestFocus();
                        SendKeyDown(4);
                    }

                }

            }

        } catch (Exception e) {
            Log.d(tag, "e Delivery Document ==> " + e.toString());
        }
    }



    private boolean CheckDeliveryDocument(String strDocNo)  {
        String tag = "6SepV2";
        Boolean resultBoolean = Boolean.TRUE;
        try {
            String  strReveiveCode="";
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_DeliveryDocument();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strDeliveryCode",userLogin.STFcode.trim(),
                    "strReceiveCode",strReveiveCode,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_DeliveryDocument();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                resultBoolean = Boolean.FALSE;
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                    resultBoolean = Boolean.FALSE;
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Delivery Document ==> " + e.toString());
        }
        return resultBoolean;
    }



    private void ExecuteDeliveryDocument(String strDocNo) {
        String tag = "6SepV2";
        try {
            String  strReveiveCode=metShowStaffReceive.getText().toString().substring(0,4);
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_DeliveryDocument();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strDeliveryCode",userLogin.STFcode.trim(),
                    "strReceiveCode",strReveiveCode,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_DeliveryDocument();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Delivery Document ==> " + e.toString());
        }
    }



    private void createListViewDocument() {
        //mbtnSearch.setText(getResources().getString(R.string.refresh));

        ClearData();
        mbtnSearch.setImageResource(R.drawable.ic_refresh_blue);
        mgplvListDocument.setVisibility(View.VISIBLE);
        blnShowgplvListDocument=true;


        ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";
        try {
            ExecuteGetListDocument executeGetListDocument = new ExecuteGetListDocument(getApplicationContext());
            //D คือ VW_Mobile_ListDocNo_Delivery  แสดงเอกสารที่รอ รอจ่าย
            strURL=strServerAddress+myConstant.urlMobile_ListDocument();
            executeGetListDocument.execute(strDataBaseName, "D",userLogin.STFcode.trim(), strURL);
            String resultJSON = executeGetListDocument.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListDocumentString = new String[jsonArray.length()];
            final String[] arrCustNameString = new String[jsonArray.length()];
            final String[] arrSortDateString = new String[jsonArray.length()];

            mtvShowDocSelected.setText("จำนวน "+ String.valueOf( jsonArray.length())+" ใบ");

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListDocumentString[i] = jsonObject.getString("DocNo");
                arrCustNameString[i] = jsonObject.getString("CSName");
                arrSortDateString[i] = jsonObject.getString("SortDate");
                Log.d(tag, "Docno [" + i + "] ==> " + arrListDocumentString[i]);
            }//for

            ListDocumentAdapter listDocumentAdapter = new ListDocumentAdapter(getApplicationContext(), arrListDocumentString, arrCustNameString, arrSortDateString);

            listView.setAdapter(listDocumentAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));

                    metSelectDocno.setText(arrListDocumentString[i].toString());
                    mtvShowDocSelected.setText(arrCustNameString[i].toString());
                    CheckPartHaveSerialno(metSelectDocno.getText().toString());
                    ShowDelivery();
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView



    private void createListViewSerialno(String strDocno) {

        blnListPartEmpty=false;
        ListView listView = findViewById(R.id.lvListSerialno);
        String tag = "6SepV2";

        try {
            String strTableName="RFCheckOut_PartSerialNo";
            String strField="PartNid,SerialNo";
            String strCondition = "Docno='" + strDocno + "' and  SerialNo<>'' and  DlryCode<>'' ";

            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetSerialNo();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition, strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListPartNidStrings = new String[jsonArray.length()];
            final String[] arrSerialno = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป
            if (jsonArray.length()==0) {
                blnListPartEmpty=true;
            }

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListPartNidStrings[i] = jsonObject.getString("PartNid");
                arrSerialno[i] = jsonObject.getString("SerialNo");
                Log.d(tag, "PartNid [" + i + "] ==> " + arrListPartNidStrings[i]);
            }//for

            ListSerialnoAdapter listSerialnoAdapter = new ListSerialnoAdapter(getApplicationContext(), arrListPartNidStrings, arrSerialno);
            listView.setAdapter(listSerialnoAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView




    private void ShowAlertDialog(int intAlertType, String strAlertTitle, String strAlertMessage, int ic_AlertDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AlertDialog);

        if (intAlertType == R.string.alertdialog_success) {
            AlertDialogView = LayoutInflater.from(this).inflate(
                    R.layout.layout_success_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        if (intAlertType == R.string.alertdialog_warning) {
            AlertDialogView = LayoutInflater.from(this).inflate(
                    R.layout.layout_warning_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        if (intAlertType == R.string.alertdialog_error) {
            AlertDialogView = LayoutInflater.from(this).inflate(
                    R.layout.layout_error_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        builder.setView(AlertDialogView);
        ((TextView) AlertDialogView.findViewById(R.id.textTitle)).setText(strAlertTitle);
        ((TextView) AlertDialogView.findViewById(R.id.textMessage)).setText(strAlertMessage);
        ((ImageView) AlertDialogView.findViewById(R.id.imageIcon)).setImageResource(ic_AlertDialog);

        if (intAlertType == R.string.alertdialog_success || intAlertType == R.string.alertdialog_error) {
            ((Button) AlertDialogView.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.alertdialog_okay));
            final AlertDialog alertDialog = builder.create();
            AlertDialogView.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        if (intAlertType == R.string.alertdialog_warning ) {
            ((Button) AlertDialogView.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.yes));
            ((Button) AlertDialogView.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.no));
            final AlertDialog alertDialog = builder.create();

            AlertDialogView.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ExecuteDeliveryDocument(metSelectDocno.getText().toString());
//                    InitializeData();
                    alertDialog.dismiss();
                }
            });

            AlertDialogView.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

    }

}