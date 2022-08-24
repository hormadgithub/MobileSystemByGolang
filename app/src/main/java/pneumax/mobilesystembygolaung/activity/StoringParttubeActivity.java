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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteEightParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument_Receive_Store;
import pneumax.mobilesystembygolaung.connected.ExecuteGetProductDesc;
import pneumax.mobilesystembygolaung.connected.ExecuteTenParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocument_Receive_Store_Adapter;
import pneumax.mobilesystembygolaung.manager.ListPartAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.PartTubeDetail;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.ReturnValue;
import pneumax.mobilesystembygolaung.object.StaffLogin;
import pneumax.mobilesystembygolaung.object.StorePartTubeDetail;

import static pneumax.mobilesystembygolaung.manager.GlobalVar.getDataBaseName;


public class StoringParttubeActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    Product clsProduct;
    StorePartTubeDetail clsStorePartTubeDetail;
    PartTubeDetail clsPartTubeDetail;

    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL;
    Integer intFullLength,intQtyIn;

    ReturnValue clsReturnValue;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;


    //From Layout
    Button mbtnSelectDocument,mbtnHold,mbtnReset;
    Button mbtnConfirmStorePartTube,mbtnCompleteStorePartTube;
    CircularImageView mbtnSearch;
    ImageView mimgBackTop;
    EditText metScanDocument,metScanLocation,metSelectDocno, metScanStorePart;
    TextView mtvShowDocSelected,mtxtShowStorePartno,mtvTitle;
    Group mgplvListDocument,mgpLocation,mgpScanPart;
    Boolean blnShowgplvListDocument,blnShowgpLocation,blnShowgpScanPart;

    // แสดงรายละเอียดการจัดสินค้า
    TextView mtxtShowStoreQty;
    TextView mtxtShowStoreQtyIn,mtxtShowStoreLocation,mtxtShowDigitno,mtxtInputFullLength,mtxtInputDamageLength;
    Spinner mspnLocation;



    ArrayAdapter<String> AdapterSpinnerLocation;
    private String[] arrLocationStrings;
    private String  strLocationStrings;


    Boolean blnListPartEmpty;
    View updateview;// above oncreate method
    ListView mlvListDocument,mlvListPart;

    //ตัวแปรสำหรับใช้ในการ Select ข้อมูล
    String strStoreDocType, strStoreTmpTime,strStoreDocno,strStoreLocation,strStorePartnid,strStorePackQty,strSelectDoctype,strSelectDocdesc;
    Boolean blnStorePartHaveSerialNo,blnStorePartHaveLabel,blnStorePartTube,blnStoreScanFast ;
    Boolean blnWaitInputLength=false,blnImportInputLength;
    //เก็บไว้เพื่อบอกว่าเป็นการเรียก Alert Warning จกที่ไหนเำื่อให้ทำงานได้ถูกต้อง
    Boolean blnWarning_Reset_Document, blnWarning_Hold_Documetn,blnWarning_Confirm,blnWarning_Complete=false;


    String strPartNid, strPartStatus, strDigitNo;

    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_storing_parttube);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility = new GlobalUtility();


        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
    }


    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
        strSelectDoctype = inboundIntent.getStringExtra("SelectDoctype");
        strSelectDocdesc = inboundIntent.getStringExtra("SelectDocdesc");
    }

    private void BindWidgets() {

        mtvTitle=(TextView) findViewById(R.id.tvTitle);

        metScanDocument=(EditText) findViewById(R.id.etScanDocument);
        metScanLocation=(EditText) findViewById(R.id.etScanLocation);
        mspnLocation = (Spinner) findViewById(R.id.spnLocation);


        metSelectDocno=(EditText) findViewById(R.id.etSelectDocno);
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        mtxtShowStorePartno=(TextView) findViewById(R.id.txtShowStorePartno);
        mtxtShowStoreLocation=(TextView) findViewById(R.id.txtShowStoreLocation);

        metScanStorePart =(EditText) findViewById(R.id.txtScanInputStorePart);
        mtxtInputFullLength =(EditText) findViewById(R.id.txtInputFullLength);
        mtxtInputDamageLength =(EditText) findViewById(R.id.txtInputDamageLength);

        mtxtShowStoreQty =(TextView) findViewById(R.id.txtShowStoreQty);
        mtxtShowStoreQtyIn =(TextView) findViewById(R.id.txtShowStoreQtyIn);

        mtxtShowDigitno   =(TextView) findViewById(R.id.txtShowDigitno);

        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpLocation=(Group) findViewById(R.id.gpLocation);
        mgpScanPart=(Group) findViewById(R.id.gpScanStorePart);

        mlvListPart =(ListView) findViewById(R.id.lvListPart);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);
        mbtnHold=(Button) findViewById(R.id.btnHold_Document);
        mbtnReset=(Button) findViewById(R.id.btnReset_Document);
        mbtnConfirmStorePartTube =(Button) findViewById(R.id.btnConfirmStorePartTube);
        mbtnCompleteStorePartTube =(Button) findViewById(R.id.btnCompleteStorePartTube);

        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }


    private void InitializeData() {
        mtvTitle.setText(strSelectDocdesc.substring(0, strSelectDocdesc.indexOf("(")));

        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDocument.setAdapter(null);

        mlvListPart.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListPart.setAdapter(null);

        mbtnSearch.setImageResource(R.drawable.ic_search_blue);
        metScanDocument.setEnabled(true);

        mbtnSearch.setVisibility(View.VISIBLE);
        mbtnSearch.setEnabled(true);

        mtxtInputFullLength.setText("");
        mtxtInputDamageLength.setText("");
        mbtnConfirmStorePartTube.setEnabled(false);
        mbtnCompleteStorePartTube.setEnabled(false);

        mtxtInputFullLength.setEnabled(false);
        mtxtInputDamageLength.setEnabled(false);

        Invisible_AllGroup();
        ClearData();
        metScanDocument.requestFocus();
    }


    private void  Invisible_AllGroup(){
        mgplvListDocument.setVisibility(View.GONE);
        mgpLocation.setVisibility(View.GONE);
        mgpScanPart.setVisibility(View.GONE);

        blnShowgplvListDocument=false;
        blnShowgpLocation=false;
        blnShowgpScanPart=false;
    }


    private void ClearData() {
        //ทำการกำหนดค่าใได้เป็นว่างก่อน
        strStoreTmpTime="";
        strStoreDocno="";
        strStoreLocation="";
        //เพื่อให้ทำการรอ Scan location ใหม่
        metScanLocation.setHint("Location");
        strStorePartnid="";

        metSelectDocno.setText("");
        metScanLocation.setText("");
        metScanStorePart.setText("");
        mtvShowDocSelected.setText("");
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

        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (! strStoreDocno.equals("")) {
//                    Clear_Tmp_RfcheckIn_Parttube(strStoreDocno,strStorePartnid);
//                }

               //if (!strStorePartnid.equals("")){
//                    Execute_Hold_Reset_Storing_Parttube(strStoreDocno,strStorePartnid,"H");
//                }
//                else {
//                    Execute_Hold_Reset_Storing_Parttube(strStoreDocno,"","R");
//                }
                if (! strStoreDocno.equals("")) {
                    Execute_Hold_Reset_Storing_Parttube(strStoreDocno, "", "H");
                }
                //ยังไม่ได้เลือก Document
                if (strStoreDocno.equals("") ||  blnShowgplvListDocument) {
                    finish();
                    overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

                }
                else if (blnShowgpLocation){
                    InitializeData();
                }
                else if (blnShowgpScanPart){
                    ShowSelectLocation(strStoreDocno);
                }

            }
        });

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


                if (metScanDocument.isEnabled()) {
                    if (!metScanDocument.getText().toString().isEmpty() && !metScanDocument.getText().toString().equals("")) {
                        metScanDocument.setText("");
                        blnManualInput = false;
                        metScanDocument.setHint(s.toString().trim());

                        if (s.toString().length() == 10) {
                            strScan = s.toString().trim();
                            metSelectDocno.setText(strScan);
                            strStoreDocno = strScan;

                            CheckDocumentFromScan(strScan);
                        } else {
                            ClearData();
                            strAlertMessage = "ไม่พบเอกสารที่คุณ Scan";
                            mtvShowDocSelected.setText(strAlertMessage);
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    }
                    //mbtnSearch.setText(getResources().getString(R.string.search));
                    mbtnSearch.setImageResource(R.drawable.ic_search_blue);
                    mgplvListDocument.setVisibility(View.GONE);
                }
                else {
                    if (!s.toString().equals("") ) {
                        if ( blnShowgplvListDocument) {
                            strAlertMessage = "กรุณาเลือกเอกสารที่ต้องการก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                        }

                        else  if ( blnShowgpLocation) {
                            strAlertMessage = "กรุณาเลือก Part ที่ต้องการก่อนนะครับ..";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanLocation.requestFocus();
                            SendKeyDown(2);
                        }

                        else  if ( blnShowgpScanPart) {
                            strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanStorePart.requestFocus();
                            SendKeyDown(2);
                        }
                    }
                }
            }
        });

        metScanLocation.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanLocation.setText(metScanLocation.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanLocation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strScan;
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                //01-03-2021 สโตร์ต้องการให้ป้อน Location ได้เหมือนเครื่องเดิม
//                if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                if (s.length()==1 || blnManualInput) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก

                if (blnShowgpLocation) {
                    if (!metScanLocation.getText().toString().isEmpty() && !metScanLocation.getText().toString().equals("")) {
                        metScanLocation.setText("");
                        blnManualInput = false;
                        metScanLocation.setHint(s.toString().trim());
                        strStoreLocation = metScanLocation.getHint().toString().trim();
                        mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(s.toString().trim()));
                        createListViewPart(strStoreDocno, strStoreLocation);
                        if (blnListPartEmpty) {
                            strAlertMessage = "ไม่พบรายการสินค้าที่ต้องจัด \n ใน Location ที่ Scan นะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    } else {
                        if (!s.toString().equals("")) {
                            if (blnShowgplvListDocument) {
                                strAlertMessage = "กรุณาเลือก Part ที่ต้องการ \n แล้วเลือก Select นะครับ.";
                                ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                                metScanLocation.setText("");
                            } else if (blnShowgpScanPart) {
                                strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
                                ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                                metScanLocation.setText("");
                                metScanStorePart.requestFocus();
                                SendKeyDown(2);
                            }
                        }
                    }
                }
            }
        });


        metScanStorePart.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanStorePart.setText(metScanStorePart.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanStorePart.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String[] arrGetDescFromScanBarcode;
                String strInputLengthByImport="";

                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                //30-04-2021 เนื่องจากอาจเปลี่ยนใจต้องการ Scan ท่อนใหม่ได้
//                if (blnWaitInputLength) {
//                    return;
//                }

                //ยอมให้ เอก กับ ลิน ป้อน Part ได้
                if((userLogin.DPCode.equals("MIS")) &&  (s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก

                if (metScanStorePart.isEnabled() && !metScanStorePart.getText().toString().isEmpty() && !metScanStorePart.getText().toString().equals("")) {
                    metScanStorePart.setText("");
                    blnManualInput = false;
                    metScanStorePart.setHint(s.toString().trim());

                    if (s.length() == 17 || s.length() == 18) {
                        arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                        strPartNid = arrGetDescFromScanBarcode[0];
                        strPartStatus = arrGetDescFromScanBarcode[1]; //0-สมบูรณ์ 1-ตัวไม่สามบูรณ์  2-ตัวเสีย 3-ตัวScrap
                        strDigitNo = arrGetDescFromScanBarcode[2];
                        mtxtShowDigitno.setText(strDigitNo);
                        if (strPartNid.equals(strStorePartnid)) {
                            if (!Check_ScanDuplicate(strStoreTmpTime, strStoreDocno, strPartNid, strDigitNo,strPartStatus)) {
                                 blnWaitInputLength = true;
                                //ทำการตรวจสอบว่ามีการป้อนความยาวท่อน Auto หรือเปล่า
                                if ( strStoreDocno.substring(0,3).equals("ORC") ) {
                                    strTableName="OPORCV";
                                    strField="InputLengthByImport";
                                    strCondition="RCVNO='" + strStoreDocno + "'";
                                    strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
                                    strInputLengthByImport = globalUtility.Find_ReturnValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
                                }
                                else if ( strStoreDocno.substring(0,3).equals("CPB") ) {
                                    strTableName="CPB";
                                    strField="ImportInputLength";
                                    strCondition="CPBNO='" + strStoreDocno + "'";
                                    strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
                                    strInputLengthByImport = globalUtility.Find_ReturnValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
                                }
                                //ใส่ความยาวท่อนให้ Auto
                                blnImportInputLength=false;
                                if (! strInputLengthByImport.equals("")) {
                                    blnImportInputLength=true;
                                    GetInputLenghtByImport(strPartNid,strDigitNo);

                                    if (! mtxtInputFullLength.getText().toString().isEmpty() &&! mtxtInputFullLength.getText().equals("")) {
                                        CheckInputLengthComplete();
                                    }
                                    else {
                                        blnWaitInputLength = false;
                                        strAlertMessage="ไม่พบท่อนที่คุณ Scan ที่จองไว้สำหรับเอกสารใบนี้ หรือมีการ Update ไปแล้วนะครับ" ;
                                        ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                                    }
                                }
                                else{
                                    mtxtInputFullLength.setText("");
                                    mtxtInputDamageLength.setText("");
                                    mtxtInputFullLength.setEnabled(blnWaitInputLength);
                                    mtxtInputDamageLength.setEnabled(blnWaitInputLength);
                                    mtxtInputFullLength.requestFocus();
                                }
                            }
                        }
                        else {
                            strAlertMessage = "สินค้าที่คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการจัดเก็บนะครับ";
                            globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                        }
                        metScanStorePart.setText("");
                    } else {
                        if (!s.toString().isEmpty()) {
                            strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                            globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                            metScanStorePart.setText("");
                        }
                    }
                }
                else{
                    if (!s.toString().isEmpty()) {
                        strAlertMessage = "คุณ Scan สินค้าครบตามต้องการแล้วนะครับ";
                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                        metScanStorePart.setText("");
                    }
                }
            }
        });

        mtxtInputDamageLength.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    mimgBackTop.requestFocus();
                    return true;
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    CheckInputLengthComplete();
                    return true;
                }
                return false;
            }
        });



        mbtnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSelectDocno();
            }
        });


        mbtnHold.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hold_Storing_Parttube(strStoreDocno,strStorePartnid);
            }
        });


        mbtnReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Storing_Parttube(strStoreDocno,strStorePartnid);
            }
        });


        mbtnConfirmStorePartTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intFullLength = Integer.parseInt(mtxtInputFullLength.getText().toString());
                intQtyIn = Integer.parseInt(mtxtShowStoreQtyIn.getText().toString());
                intQtyIn = intQtyIn+intFullLength  ;
                Integer intQty;
                intQty= Integer.parseInt(mtxtShowStoreQty.getText().toString());
                //ทำการแจ้งเตือนว่าจำนวนรวมเกินที่สามารถรับได้
                if  (intQtyIn > intQty ) {
                    strAlertMessage = "ความยาวรวมเกินจำนวนที่ต้องการรับนะครับ";
                    ShowAlertDialog(R.string.alertdialog_error, "Error.", strAlertMessage, R.drawable.alertdialog_ic_error);
                    mbtnConfirmStorePartTube.setEnabled(false);
                    mtxtInputFullLength.setText("");
                    mtxtInputDamageLength.setText("");
                    mtxtInputFullLength.setEnabled(true);
                    mtxtInputDamageLength.setEnabled(true);
                    mtxtInputFullLength.requestFocus();
                    return;
                }
                else {
                    Execute_Mobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube(strStoreTmpTime, strStoreDocno, strStorePartnid, strDigitNo);
                }
            }
        });

        mbtnCompleteStorePartTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Execute_Mobile_Upd_Tmp_RFCheckIn_PartTube(strStoreTmpTime,strStoreDocno,strStorePartnid);
             }
        });
    }

    private void ResetAllWarning() {
        blnWarning_Hold_Documetn =false;
        blnWarning_Reset_Document =false;
        blnWarning_Confirm=false;
    }

    private void CheckDocumentFromScan(String strDocno){
        strTableName="VW_Mobile_ListDocNo_Store_Parttube";
        strField="Docno";
        strCondition="Docno='" +strDocno + "'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue=globalUtility.Find_Record(getApplicationContext(), strDataBaseName,strTableName,strField,strCondition,strURL);
        if (!strReturnValue.trim().equals("")) {
            //ทำการตรวจสอบด้วยว่าเเอกสารพร้อมทำการ Storing Parttube หรือไม่ เช่น CPB Auto    ORC ที่รับเข้ามาทำการจัดเก็บเสร็จแล้วหรือไม่
            Execute_Mobile_INS_Tmp_RFCheckIn_Parttube(strDocno);
            ShowSelectLocation(strDocno);
        }
        else{
            mtvShowDocSelected.setText("Not Found Document");
            strStoreDocno = "";
            metSelectDocno.setText("");
        }
    }


    private void ShowSelectDocno() {
        mbtnSearch.setImageResource(R.drawable.ic_refresh_blue);
        Invisible_AllGroup();
        mgplvListDocument.setVisibility(View.VISIBLE);
        blnShowgplvListDocument=true;
        ClearData();
        createListViewDocument();
    }

    private void ShowSelectLocation(final String strDocno) {
        mbtnSearch.setVisibility(View.GONE);
        metScanDocument.setEnabled(false);

        Invisible_AllGroup();
        mgpLocation.setVisibility(View.VISIBLE);
        blnShowgpLocation= true;
        strStorePartnid="";
        createSpinnerLocation(strDocno);
        GetStoreDocType(strDocno);
        mtvShowDocSelected.setText(strDocno);

        metScanLocation.setText("");
        metScanLocation.requestFocus();
        SendKeyDown(2);
    }



    private void ShowScanPart(final String strStorePartnid) {
        Invisible_AllGroup();
        metScanStorePart.setText("");
        mtvShowDocSelected.setText(strStorePartnid);
        GetProductDetail();
        mgpScanPart.setVisibility(View.VISIBLE);
        GetStorePartTubeDetail();

        metScanStorePart.setHint("Scan Partid");
        metScanStorePart.setEnabled(true);
        blnShowgpScanPart=true;
        metScanStorePart.requestFocus();
        SendKeyDown(2);


        if (mtxtShowStoreQty.getText().toString().equals(mtxtShowStoreQtyIn.getText().toString()) ) {
            mbtnCompleteStorePartTube.setEnabled(true);
            metScanStorePart.setEnabled(false);
        }
    }



    private void createSpinnerLocation(final String strDocno) {
        try {
            strTableName="Tmp_RFCheckin_Parttube";
            strField="LCCode";
            strCondition = " Docno='" + strDocno +"' and PartTubeStock='Y' and StoreCheckin='Y' and RcvToPartTube<>'Y'";
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strTableName", strTableName,
                    "strField", strField,
                    "strCondition", strCondition,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrLocationStrings = new String[jsonArray.length()];
            //เช็คว่ายังเหลือ Part ให้จัดหรือไม่
            if (arrLocationStrings.length !=0) {
                for (int i = 0; i < jsonArray.length(); i += 1) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    arrLocationStrings[i] = jsonObject.getString("ResultReturn");
                }//for
                AdapterSpinnerLocation = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, arrLocationStrings);
                mspnLocation.setAdapter(AdapterSpinnerLocation);
                mspnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        strLocationStrings = arrLocationStrings[i];
                        //ถ้าเป็น MIS ให้แสดง Part ขึ้นมาเลย แต่ไม่สามารถป้อน Part ได้
                        if ((userLogin.DPCode.equals("MIS")|| !userLogin.MobileStoreInputManual.trim().equals(""))) {
                            strStoreLocation = strLocationStrings;
                        }
                        //ทำการ Run ใหม่เพื่อให้มีเฉพาะ part ที่เหลือ
                        if (! strStoreLocation.isEmpty() && ! strStoreLocation.equals("")){
                            createListViewPart(strStoreDocno,strStoreLocation);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        strLocationStrings = arrLocationStrings[0];
                    }
                });
            }
            else{
                //ถ้าก่อนห้ามีการเลือกเอกสารไว้
                InitializeData();

            }

        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Loation ==> " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }//createSpinnerlocation



    private void GetInputLenghtByImport(String strPickTubePartnid,String strDigitno) {
        String tag = "6SepV2";
        String strOnhand,strDMLength="";
        Double dblOnhand,dblDMLength=0.00;
        strTableName="Parttube";
        strField="Onhand";
        strCondition="Partnid='" + strPickTubePartnid + "' and Digitno='" + strDigitno + "' and RsvByDocno='" + strStoreDocno + "' and Storecheckin<>'Y'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strOnhand = globalUtility.Find_ReturnValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
        strField="DMLength";
        strDMLength = globalUtility.Find_ReturnValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
        if (strOnhand.equals("")) {
            mtxtInputFullLength.setText("");
            mtxtInputDamageLength.setText("");
        } else {
            dblOnhand=Double.parseDouble(strOnhand);
            dblDMLength=Double.parseDouble(strDMLength);

            mtxtInputFullLength.setText(String.valueOf(dblOnhand.intValue()));
            mtxtInputDamageLength.setText(String.valueOf(dblDMLength.intValue()));
        }
    }



    private void CheckInputLengthComplete() {
        Integer intInputFullLength;
        if (mtxtInputFullLength.getText().equals("")||mtxtInputFullLength.getText().toString().isEmpty()) {
            return;
        }

        if (mtxtInputDamageLength.getText().equals("")||mtxtInputDamageLength.getText().toString().isEmpty()) {
            return;
        }

        intInputFullLength= Integer.parseInt(mtxtInputFullLength.getText().toString());

        mbtnConfirmStorePartTube.setEnabled(false);
        if ( (blnWaitInputLength ) && (intInputFullLength != 0 ) ) {
            mbtnConfirmStorePartTube.setEnabled(true);
            metScanStorePart.setEnabled(false);
        }
    }


    private void CheckStorePartComplete() {
        mbtnCompleteStorePartTube.setEnabled(false);

        if ((blnWaitInputLength || blnImportInputLength ) && (intQtyIn != 0 ) &&  mtxtShowStoreQty.getText().toString().equals(mtxtShowStoreQtyIn.getText().toString()) ) {
                mbtnCompleteStorePartTube.setEnabled(true);
        }

        blnWaitInputLength=false;
        mtxtInputFullLength.setEnabled(blnWaitInputLength);
        mtxtInputDamageLength.setEnabled(blnWaitInputLength);
        mtxtInputFullLength.setText("");
        mtxtInputDamageLength.setText("");

       mbtnConfirmStorePartTube.setEnabled(false);
       metScanStorePart.setEnabled(true);
       metScanStorePart.requestFocus();
       SendKeyDown(2);
    }


    private void GetProductDetail() {
        String tag = "6SepV2";
        try {
            ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetProductDetail();
            getProductDesc.execute(getDataBaseName, strStorePartnid, strURL);

            String resultJSON = getProductDesc.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strAlertMessage="Not Found Data !!!";
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsProduct = gson.fromJson(resultJSON.toString(), Product.class);

                mtxtShowStorePartno.setText(clsProduct.PartNo+" "+clsProduct.PartDes);
                strStorePackQty=  clsProduct.PackQty;

                blnStorePartHaveLabel=clsProduct.PrintLabel.toUpperCase().equals("Y");
                blnStorePartHaveSerialNo=clsProduct.PartHaveSerialNo.toUpperCase().equals("Y");

                strTableName="PartTube";
                strField="PartNid";
                strCondition="PartNid='" + strStorePartnid + "'";
                strURL=strServerAddress+myConstant.urlMobile_CountRecord();;
                strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
                blnStorePartTube=!strReturnValue.equals("0");

            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void GetStorePartTubeDetail() {
        String tag = "6SepV2";
        String strSumStoreQtyIn="0";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetStorePartTubeDetail();
            executeThreeParameter.execute("strDataBaseName",strDataBaseName,
                    "strDocno",strStoreDocno,
                    "strPartnid", strStorePartnid,
                    strURL);

            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "";
                strAlertMessage="Not Found Data !!!" ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsStorePartTubeDetail = gson.fromJson(resultJSON.toString(), StorePartTubeDetail.class);

                strStoreTmpTime=clsStorePartTubeDetail.TmpTime;

                //กำหนดไม่ให้แสดงทศนิยม
                mtxtShowStoreQty.setText(String.format("%.0f",Double.parseDouble(clsStorePartTubeDetail.Qty)));
                mtxtShowStoreLocation.setText(clsStorePartTubeDetail.LCCode.toString());
               // mtxtShowStoreQtyIn.setText("0");

                strTableName="movement_PartTube";
                strField="MPTQty_In";
                strCondition = "MPTNO='" + strStoreDocno + "' and Partnid='" + strStorePartnid + "'";
                strURL=strServerAddress+myConstant.urlMobile_SumValue();;
                strSumStoreQtyIn = globalUtility.SumValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
                mtxtShowStoreQtyIn.setText(String.format("%.0f",Double.parseDouble(strSumStoreQtyIn)));


            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    public boolean Check_ScanDuplicate(String strTmpTime,String strDocno,String strPartNid,String strDigitNo,String strKind) {
        String tag = "6SepV2";
        Boolean resultBoolean = Boolean.FALSE;
        try {
            strTableName="Tmp_RFCheckIn_ChkDigit_Parttube";
            strField="Digit";
            strCondition = "Docno='" + strDocno + "' and PartNid='" + strPartNid + "' and Digit='" + strDigitNo + "'" ;
            strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);

            if (! strReturnValue.equals("")){
                resultBoolean=true;
                strAlertMessage = "คุณ Scan สินค้าซ้ำนะครับ.";
                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
            }
        } catch (Exception e) {
            Log.d(tag, "e Check_ScanDuplicate ==> " + e.toString());
        }
        return resultBoolean;
    }


    private void Execute_Mobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube(String strTmpTime,String strDocno, String strPartNid,String strDigitNo){
        String tag = "6SepV2";
        try {
            ExecuteTenParameter executeTenParameterParameter = new ExecuteTenParameter(getApplicationContext());
            strURL=strServerAddress+ myConstant.urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube();
            executeTenParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strRCVno",strDocno,
                    "strSTFCode",userLogin.STFcode.trim(),
                    "strPartnid",strPartNid,
                    "strPartTubeStock","Y",
                    "strKind",strPartStatus,
                    "strDigit",strDigitNo,
                    "strFullLength",mtxtInputFullLength.getText().toString().trim(),
                    "strDamageLength",mtxtInputDamageLength.getText().toString().trim(),
                    strURL);

            String resultJSON = executeTenParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();

                if (! strReturnValue.toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage; ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                    mbtnConfirmStorePartTube.setEnabled(false);
                    metScanStorePart.setText("");
                    metScanStorePart.setHint("Scan Partid");
                    mtxtInputFullLength.setText("");
                    mtxtInputDamageLength.setText("");
                    metScanStorePart.setEnabled(true);
                    metScanStorePart.requestFocus();
                }
                else {
                    intFullLength = Integer.parseInt(mtxtInputFullLength.getText().toString());
                    intQtyIn = Integer.parseInt(mtxtShowStoreQtyIn.getText().toString());
                    intQtyIn = intQtyIn+intFullLength  ;
                    mtxtShowStoreQtyIn.setText(String.valueOf(intQtyIn));
                    metScanStorePart.setText("");
                    metScanStorePart.setHint("Scan Partid");
                    CheckStorePartComplete();

                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    private void Execute_Mobile_Upd_Tmp_RFCheckIn_PartTube(String strTmpTime,String strDocno, String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteEightParameter executeEightParameterParameter = new ExecuteEightParameter(getApplicationContext());
            strURL=strServerAddress+ myConstant.urlMobile_Upd_Tmp_RFCheckIn_PartTube();
            executeEightParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strRCVno",strDocno,
                    "strSTFCode",userLogin.STFcode.trim(),
                    "strPartnid",strPartNid,
                    "strQtyIn",mtxtShowStoreQtyIn.getText().toString().trim(),
                    "strQtyDFIn","0",
                    "strQtyDmIn","0",
                    strURL);
            String resultJSON = executeEightParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Upd_Tmp_RFCheckIn_PartTube() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();

                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Upd_Tmp_RFCheckIn_PartTube() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
//                    strAlertMessage="";
//                    ShowAlertDialog(R.string.alertdialog_success, "Complete.", strAlertMessage, R.drawable.alertdialog_ic_success);
                    ShowSelectLocation(strStoreDocno);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    private void GetStoreDocType(String strDocno){
        strURL=strServerAddress+myConstant.urlMobile_GetDoctype();
        strReturnValue = globalUtility.ReturnValue_ExecuteTwoParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strDocno",strDocno,strURL);
        strStoreDocType=strReturnValue;

    }

    private void createListViewDocument() {
        //ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";

        try {
            ExecuteGetListDocument_Receive_Store executeGetListDocument_Receive_Store = new ExecuteGetListDocument_Receive_Store(getApplicationContext());
            //P คือ VW_Mobile_ListDocNo_Store_Parttubeing  แสดงเอกสารที่รอ จัดสินค้า
            strURL=strServerAddress+myConstant.urlMobile_ListDocument_Store_Parttube();
            executeGetListDocument_Receive_Store.execute(strDataBaseName, strSelectDoctype,userLogin.STFcode.trim(), strURL);
            String resultJSON = executeGetListDocument_Receive_Store.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListDocumentString = new String[jsonArray.length()];
            final String[] arrRefnoString = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListDocumentString[i] = jsonObject.getString("Docno");
                arrRefnoString[i] = jsonObject.getString("Refno");
                Log.d(tag, "Docno [" + i + "] ==> " + arrListDocumentString[i]);
            }//for

            ListDocument_Receive_Store_Adapter listDocument_Receive_Store_Adapter = new ListDocument_Receive_Store_Adapter(getApplicationContext(), arrListDocumentString, arrRefnoString);
            mlvListDocument.setAdapter(listDocument_Receive_Store_Adapter);

            mlvListDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                    metSelectDocno.setText(arrListDocumentString[i].toString());
                    mtvShowDocSelected.setText(arrRefnoString[i].toString());
                    strStoreDocno = metSelectDocno.getText().toString();
                    Execute_Mobile_INS_Tmp_RFCheckIn_Parttube(strStoreDocno.trim());
                    ShowSelectLocation(strStoreDocno);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView Document==> " + e.toString());
        }
    }//createListView


    private void Execute_Mobile_INS_Tmp_RFCheckIn_Parttube(String strDocno) {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+  myConstant.urlMobile_INS_Tmp_RFCheckIn_Parttube();
            executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strDocno,
                    "strStfCode", userLogin.STFcode.toString(),
                    strURL);
            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_INS_Tmp_RFCheckIn_Parttube() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (!strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage= clsResult.ResultMessage.toString() ;
                    ShowAlertDialog(R.string.alertdialog_error,"กรุณาตรวจสอบอีกครั้ง.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }

    private void createListViewPart(final String strDocno,final String strLocation) {
        strStorePartnid="";
        blnListPartEmpty=false;
        String tag = "6SepV2";
        strTableName=" tmp_rfcheckin_Parttube A inner join part B ON A.partnid=B.partnid ";
        strField="A.PartNid,rtrim(B.partno)+' '+B.partdes  as Description"  ;
        strCondition = "A.Docno='" + strDocno + "' and A.PartTubeStock='Y' and A.StoreCheckin='Y' and RcvToPartTube<>'Y' and B.LCCOde='" + strLocation +"'";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetListPart();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strTableName", strTableName,
                    "strField", strField,
                    "strCondition", strCondition,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListPartNidStrings = new String[jsonArray.length()];
            final String[] arrDescription = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป
            if (jsonArray.length()==0) {
                blnListPartEmpty=true;
            }

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListPartNidStrings[i] = jsonObject.getString("PartNid");
                arrDescription[i] = jsonObject.getString("Description");
                Log.d(tag, "PartNid [" + i + "] ==> " + arrListPartNidStrings[i]);
            }//for

            ListPartAdapter listPartAdapter = new ListPartAdapter(getApplicationContext(), arrListPartNidStrings, arrDescription);
            mlvListPart.setAdapter(listPartAdapter);

            mlvListPart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                    strStorePartnid=arrListPartNidStrings[i].toString();
                    ShowScanPart(strStorePartnid);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView

    private void Hold_Storing_Parttube(String strDocno, String strPartnid){
        ResetAllWarning();
        blnWarning_Hold_Documetn =true;
        strAlertMessage="คุณต้องการจัดเก็บสินค้าใหม่ \n ภายหลังใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }

    private void Reset_Storing_Parttube(String strDocno, String strPartnid){
        ResetAllWarning();
        blnWarning_Reset_Document =true;
        strAlertMessage="คุณต้องการจัดเก็บสินค้าใหม่ \n อีกกครั้งใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }



    private void Execute_Hold_Reset_Storing_Parttube(String strDocNo,String strPartnid,String strOption) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Store_Parttube_Hold_Reset();
            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strOption",strOption,
                    "strPartnid",strStorePartnid,
                    "strStfCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeFiveParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Store_Parttube_Hold_Reset();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage.toString();;
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
    }



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
                    //ทำการตรวจสอบด้วยว่าเป็นการเรียกใช้ตำแหน่งไหน
                    if (blnWarning_Reset_Document){
                        Execute_Hold_Reset_Storing_Parttube(strStoreDocno,"","R");
                        InitializeData();
                    }else if (blnWarning_Hold_Documetn) {
                        Execute_Hold_Reset_Storing_Parttube(strStoreDocno,"", "H");
                        InitializeData();
                    }
//                    }else if (blnWarning_Confirm){
//                        Execute_Mobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube(strStoreTmpTime,strStoreDocno,strStorePartnid,strDigitNo);
//                        //ห้ามเรียกใช้ InitializeData() เพราะจะทำการไม่สามารถจัด Part ที่ยังไม่่เสร็จได้
//                    }
//                    else if (blnWarning_Complete){
//                        Execute_Mobile_Upd_Tmp_RFCheckIn_PartTube(strStoreTmpTime,strStoreDocno,strStorePartnid);
//                        //ห้ามเรียกใช้ InitializeData() เพราะจะทำการไม่สามารถจัด Part ที่ยังไม่่เสร็จได้
//                    }
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