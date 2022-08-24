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
import pneumax.mobilesystembygolaung.connected.ExecuteEightParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument_Receive_Store;
import pneumax.mobilesystembygolaung.connected.ExecuteGetProductDesc;
import pneumax.mobilesystembygolaung.connected.ExecuteSixParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocument_Receive_Store_Adapter;
import pneumax.mobilesystembygolaung.manager.ListPartAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.ReceivePartDetail;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.ReturnValue;
import pneumax.mobilesystembygolaung.object.StaffLogin;

import static pneumax.mobilesystembygolaung.manager.GlobalVar.getDataBaseName;


public class ReceiveDoctypeActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    Product clsProduct;
    ReceivePartDetail clsReceivePartDetail;

    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL;

    ReturnValue clsReturnValue;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;


    //From Layout
    Button mbtnHold_Document, mbtnReset_Document,mbtnHold_Part, mbtnReset_Part;
    Button mbtnIncReceiveQty, mbtnDecReceiveQty,mbtnConfirmReceivePart;
    CircularImageView mbtnSearch;
    ImageView mimgBackTop;
    EditText metScanDocument,metSelectDocno, metScanReceivePart;
    TextView mtvShowDocSelected,mtxtShowReceivePartno,mtvTitle;
    Group mgplvListDocument,mgpLocation,mgpScanPart,mgpButtonIncDec;
    Boolean blnShowgplvListDocument,blnShowgpLocation,blnShowgpScanPart;

    // แสดงรายละเอียดการจัดสินค้า
    TextView mtxtShowReceiveQty,mtxtShowReceiveDF,mtxtShowReceiveSDM,mtxtShowReceiveDM,mtxtShowScanReceiveQty;
    TextView mtxtShowReceiveQtyIn, mtxtShowReceiveDFIn, mtxtShowReceiveSDMIn, mtxtShowReceiveDMIn,mtxtShowReceiveLocation;


    Boolean blnListPartEmpty;
    View updateview;// above oncreate method
    ListView mlvListDocument,mlvListPart;

    //ตัวแปรสำหรับใช้ในการ Select ข้อมูล
    String strReceiveDocType, strReceiveTmpTime,strReceiveDocno,strReceivePartnid,strReceivePackQty,strSelectDoctype,strSelectDocdesc;
    Boolean blnReceivePartHaveSerialNo,blnReceivePartHaveLabel,blnReceivePartTube,blnReceiveScanFast ;
    Boolean blnWaitScanSerialno;
    //เก็บไว้เพื่อบอกว่าเป็นการเรียก Alert Warning จกที่ไหนเำื่อให้ทำงานได้ถูกต้อง
    Boolean blnWarning_Reset,blnWarning_Hold,blnWarning_Confirm=false;

    Double dblPackQty, dblReceiveQtyOut, dblReceiveDFOut, dblReceiveSDMOut, dblReceiveDMOut;
    Integer intPackQty,intScanQty, intReceiveQty, intReceiveDF, intReceiveSDM, intReceiveDM;

    String strPartNid, strPartStatus, strDigitNo;
    Integer intCheckScanDuplicate=0; //ถ้า Scan ซ้ำัน 2 ครั้งแล้ว ครั้งที่ 3 จะทำว่า Digit ซ้ำกันจริงๆ ใช่หรือไม่


    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_receive_doctype);

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
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strSelectDoctype = inboundIntent.getStringExtra("SelectDoctype");
        strSelectDocdesc = inboundIntent.getStringExtra("SelectDocdesc");
    }

    private void BindWidgets() {

        mtvTitle=(TextView) findViewById(R.id.tvTitle);

        metScanDocument=(EditText) findViewById(R.id.etScanDocument);

        metSelectDocno=(EditText) findViewById(R.id.etSelectDocno);
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        mtxtShowReceivePartno=(TextView) findViewById(R.id.txtShowReceivePartno);
        mtxtShowReceiveLocation=(TextView) findViewById(R.id.txtShowReceiveLocation);

        metScanReceivePart =(EditText) findViewById(R.id.txtScanInputReceivePart);
        mtxtShowScanReceiveQty=(TextView) findViewById(R.id.txtShowScanReceiveQty);
        mtxtShowReceiveQty=(TextView) findViewById(R.id.txtShowReceiveQty);
        mtxtShowReceiveDF=(TextView) findViewById(R.id.txtShowReceiveDF);
        mtxtShowReceiveSDM=(TextView) findViewById(R.id.txtShowReceiveSDM);
        mtxtShowReceiveDM=(TextView) findViewById(R.id.txtShowReceiveDM);

        mtxtShowReceiveQtyIn =(TextView) findViewById(R.id.lblShowReceiveQtyIn);
        mtxtShowReceiveDFIn =(TextView) findViewById(R.id.txtShowReceiveDFIn);
        mtxtShowReceiveSDMIn =(TextView) findViewById(R.id.txtShowReceiveSDMIn);
        mtxtShowReceiveDMIn =(TextView) findViewById(R.id.txtShowReceiveDMIn);


        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpLocation=(Group) findViewById(R.id.gpLocation);
        mgpScanPart=(Group) findViewById(R.id.gpScanReceivePart);
        mgpButtonIncDec=(Group) findViewById(R.id.gpButtonIncDec);

        mlvListPart =(ListView) findViewById(R.id.lvListPart);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);

        mbtnHold_Document =(Button) findViewById(R.id.btnHold_Document);
        mbtnReset_Document =(Button) findViewById(R.id.btnReset_Document);

        mbtnHold_Part =(Button) findViewById(R.id.btnHold_Part);
        mbtnReset_Part =(Button) findViewById(R.id.btnReset_Part);

        mbtnIncReceiveQty =(Button) findViewById(R.id.btnIncReceiveQty);
        mbtnDecReceiveQty =(Button) findViewById(R.id.btnDecReceiveQty);
        mbtnConfirmReceivePart =(Button) findViewById(R.id.btnConfirmReceivePart);

        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }


    private void InitializeData() {
        //ตัด (จำนวน) ออก
        mtvTitle.setText("รับสินค้าจาก "+ strSelectDocdesc.substring(0, strSelectDocdesc.indexOf("(")));

        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDocument.setAdapter(null);

        mlvListPart.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListPart.setAdapter(null);

        mbtnSearch.setImageResource(R.drawable.ic_search_blue);
        metScanDocument.setEnabled(true);

        mbtnSearch.setVisibility(View.VISIBLE);
        mbtnSearch.setEnabled(true);
        mbtnConfirmReceivePart.setEnabled(false);
        Invisible_AllGroup();
        ClearData();
        metScanDocument.requestFocus();
    }


    private void  Invisible_AllGroup(){
        mgplvListDocument.setVisibility(View.GONE);
        mgpLocation.setVisibility(View.GONE);
        mgpScanPart.setVisibility(View.GONE);
        mgpButtonIncDec.setVisibility(View.GONE);

        blnShowgplvListDocument=false;
        blnShowgpLocation=false;
        blnShowgpScanPart=false;
    }


    private void ClearData() {
        //ทำการกำหนดค่าใได้เป็นว่างก่อน
        strReceiveTmpTime="";
        strReceiveDocno="";
        strReceivePartnid="";

        metSelectDocno.setText("");
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

                if (! strReceiveDocno.equals("")) {
                    //Clear_Tmp_RfcheckIn(strReceiveDocno,strReceivePartnid);
                    if (!strReceivePartnid.equals("")){
                        Execute_Hold_Reset_Receiving(strReceiveDocno,strReceivePartnid,"H");
                    }
                    else {
                        Execute_Hold_Reset_Receiving(strReceiveDocno,"","R");
                    }
                }
                //ยังไม่ได้เลือก Document
                if (strReceiveDocno.equals("") ||  blnShowgplvListDocument) {
                    finish();
                    overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

                }
                else if (blnShowgpLocation){
                    InitializeData();
                }
                else if (blnShowgpScanPart){
                    ShowSelectPart(strReceiveDocno);
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
                            strReceiveDocno = strScan;

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
                        }

                        else  if ( blnShowgpScanPart) {
                            strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanReceivePart.requestFocus();
                            SendKeyDown(2);
                        }
                    }
                }
            }
        });

        metScanReceivePart.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanReceivePart.setText(metScanReceivePart.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanReceivePart.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String[] arrGetDescFromScanBarcode;
              //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
              if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                  blnManualInput = true;
                  return;
              }
              s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก


                if (metScanReceivePart.isEnabled() && !metScanReceivePart.getText().toString().isEmpty() && !metScanReceivePart.getText().toString().equals("")) {
                    metScanReceivePart.setText("");
                    blnManualInput = false;
                    metScanReceivePart.setHint(s.toString().trim());

                        if (s.length() == 17 || s.length() == 18) {
                            arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                            strPartNid = arrGetDescFromScanBarcode[0];
                            strPartStatus = arrGetDescFromScanBarcode[1]; //0-สมบูรณ์ 1-ตัวไม่สามบูรณ์  2-ตัวเสีย 3-ตัวScrap
                            strDigitNo = arrGetDescFromScanBarcode[2];

                            if (strPartNid.equals(strReceivePartnid)) {
                                //ยอมให้ผ่านได้ถ้าตั้งใจยิงซ้ำ 3 ครั้ง
                                if (!Check_ScanDuplicate(strReceiveTmpTime, strReceiveDocno, strPartNid, strDigitNo,strPartStatus)) {
                                    //เคลียจำนวนการ Scan ซ้ำออกด้วย
                                    intCheckScanDuplicate = 0;
                                    //ได้มาจากตอน GetPartDetail
                                    dblPackQty = Double.parseDouble(strReceivePackQty);
                                    dblReceiveQtyOut = Double.parseDouble(mtxtShowReceiveQtyIn.getText().toString());
                                    dblReceiveDFOut = Double.parseDouble(mtxtShowReceiveDFIn.getText().toString());
                                    dblReceiveSDMOut = Double.parseDouble(mtxtShowReceiveSDMIn.getText().toString());
                                    dblReceiveDMOut = Double.parseDouble(mtxtShowReceiveDMIn.getText().toString());

                                    intPackQty = dblPackQty.intValue();
                                    intReceiveQty = dblReceiveQtyOut.intValue();
                                    intReceiveDF = dblReceiveDFOut.intValue();
                                    intReceiveSDM = dblReceiveSDMOut.intValue();
                                    intReceiveDM = dblReceiveDMOut.intValue();


                                    if (strPartStatus.equals("0")) {
                                        mtxtShowScanReceiveQty.setText(String.valueOf(intPackQty));
                                        intReceiveQty = intReceiveQty + intPackQty;
                                        mtxtShowReceiveQtyIn.setText(String.valueOf(intReceiveQty));
                                    } else if (strPartStatus.equals("1")) {
                                        intPackQty = 1;
                                        mtxtShowScanReceiveQty.setText(String.valueOf(intPackQty));
                                        intReceiveDF = intReceiveDF + intPackQty;
                                        mtxtShowReceiveDFIn.setText(String.valueOf(intReceiveDF));
                                    } else if (strPartStatus.equals("2")) {
                                        intPackQty = 1;
                                        mtxtShowScanReceiveQty.setText(String.valueOf(intPackQty));
                                        intReceiveDM = intReceiveDM + intPackQty;
                                        mtxtShowReceiveDMIn.setText(String.valueOf(intReceiveDM));
                                    } else if (strPartStatus.equals("3")) {
                                        intPackQty = 1;
                                        mtxtShowScanReceiveQty.setText(String.valueOf(intPackQty));
                                        intReceiveSDM = intReceiveSDM + intPackQty;
                                        mtxtShowReceiveSDMIn.setText(String.valueOf(intReceiveSDM));
                                    } else {
                                        intPackQty = 0;
                                        mtxtShowScanReceiveQty.setText(String.valueOf(intPackQty));
                                        strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                    }
                                }
                            } else {
                                intPackQty = 0;
                                mtxtShowScanReceiveQty.setText(String.valueOf(intPackQty));
                                strAlertMessage = "สินค้าที่คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการรับนะครับ";
                                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                            }

                            mgpButtonIncDec.setVisibility(View.GONE);
                            if (intPackQty > 1) {
                                mgpButtonIncDec.setVisibility(View.VISIBLE);
                            }
                            metScanReceivePart.setText("");
                            //ถ้าไม่ซ้าก็ให้เพิ่มจำนวน
                            if (intCheckScanDuplicate==0)  {
                                CheckReceiveComplete();
                            }
                        } else {
                            if (!s.toString().isEmpty()) {
                                strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                metScanReceivePart.setText("");
                            }
                        }
                }
                else{
                    if (!s.toString().isEmpty()) {
                        strAlertMessage = "คุณ Scan สินค้าครบตามต้องการแล้วนะครับ";
                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                        metScanReceivePart.setText("");
                    }
                }
            }
        });


        mbtnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSelectDocno();
            }
        });


        mbtnHold_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hold_Receiving_Document();
            }
        });


        mbtnReset_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Receiving_Document();
            }
        });

        mbtnHold_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hold_Receiving_Part();
            }
        });


        mbtnReset_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Receiving_Part();
            }
        });
        
        
        mbtnIncReceiveQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intReceiveQty = Integer.parseInt(mtxtShowReceiveQtyIn.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanReceiveQty.getText().toString());
                intScanQty = intScanQty + 1;
                if (intScanQty > intPackQty ) {
                    intScanQty = 0;
                    intReceiveQty = intReceiveQty - intPackQty; //ลดเลงให้มีค่าเท่ากับยังไม่ได้ Scan
                } else {
                    intReceiveQty = intReceiveQty + 1; //เพิ่มทีละ 1
                }
                mtxtShowScanReceiveQty.setText(String.valueOf(intScanQty));
                mtxtShowReceiveQtyIn.setText(String.valueOf(intReceiveQty));
                CheckReceiveComplete();
            }
        });

        mbtnDecReceiveQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intReceiveQty = Integer.parseInt(mtxtShowReceiveQtyIn.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanReceiveQty.getText().toString());

                intScanQty = intScanQty - 1;
                if (intScanQty < 0) {
                    intScanQty = intPackQty;
                    intReceiveQty = intReceiveQty + intPackQty; //ลดเลงให้มีค่าเท่ากับยังไม่ได้ Scan
                } else {
                    intReceiveQty = intReceiveQty - 1; //เพิ่มทีละ 1
                }
                mtxtShowScanReceiveQty.setText(String.valueOf(intScanQty));
                mtxtShowReceiveQtyIn.setText(String.valueOf(intReceiveQty));
                CheckReceiveComplete();
            }
        });

        mbtnConfirmReceivePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ทำการตรวจสอบว่าจัดครบทุกรายการแล้วหรือไม่
//                ResetAllWarning();
//                blnWarning_Confirm=true;
//                strAlertMessage="คุณตรวจสอบสินค้ารายการนี้ครบแล้วใช่หรือไม่" ;
//                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
                //ไม่ต้องถามต้องการทำงานให้เร็วขึ้น
                Execute_Mobile_Confirm_ReceivePart(strReceiveTmpTime,strReceiveDocno,strReceivePartnid);
            }
        });
    }

    private void ResetAllWarning() {
        blnWarning_Hold=false;
        blnWarning_Reset=false;
        blnWarning_Confirm=false;
    }

    private void CheckDocumentFromScan(String strDocno){
        strTableName="VW_Mobile_ListDocNo_Receive";
        strField="Docno";
        // strCondition="Docno='" +strDocno + "' and  (StoreCheck='' OR (StoreCheck='H' and HoldCode='"+ userLogin.STFcode.trim()+"'))";
        //คนอื่นสามารถจัดสินค้าที่ Hold ไว้ได้
        strCondition="Docno='" +strDocno + "'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue=globalUtility.Find_Record(getApplicationContext(), strDataBaseName,strTableName,strField,strCondition,strURL);
        if (!strReturnValue.trim().equals("")) {
            Execute_Mobile_INS_Tmp_RFCheckIn(strDocno);
            ShowSelectPart(strDocno);
        }
        else{
            mtvShowDocSelected.setText("Not Found Document");
            strReceiveDocno = "";
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

    private void ShowSelectPart(final String strDocno) {
        mbtnSearch.setVisibility(View.GONE);
        metScanDocument.setEnabled(false);

        Invisible_AllGroup();
        //mgplvListDocument.setVisibility(View.GONE);
        mgpLocation.setVisibility(View.VISIBLE);
        blnShowgpLocation= true;
        strReceivePartnid="";
        GetReceiveDocType(strDocno);
        createListViewPart(strReceiveDocno);
    }

    private void ShowScanPart(final String strReceivePartnid) {
        Invisible_AllGroup();
        mtvShowDocSelected.setText(strReceivePartnid);
        GetProductDetail();
        mgpScanPart.setVisibility(View.VISIBLE);
        GetReceivePartDetail();
        metScanReceivePart.setHint("Scan Partid");
        metScanReceivePart.setEnabled(true);
        blnWaitScanSerialno=false;
        blnShowgpScanPart=true;
        CheckReceiveComplete();
        metScanReceivePart.requestFocus();
        SendKeyDown(2);
    }





    private void CheckReceiveComplete() {
        Integer intQty, intQtyIn,intDFQty,intDFQtyIn,intSDMQty,intSDMQtyIn,intDMQty,intDMQtyIn;

        intQty = Integer.parseInt(mtxtShowReceiveQty.getText().toString());
        intQtyIn = Integer.parseInt(mtxtShowReceiveQtyIn.getText().toString());

        intDFQty = Integer.parseInt(mtxtShowReceiveDF.getText().toString());
        intDFQtyIn = Integer.parseInt(mtxtShowReceiveDFIn.getText().toString());

        intSDMQty = Integer.parseInt(mtxtShowReceiveSDM.getText().toString());
        intSDMQtyIn = Integer.parseInt(mtxtShowReceiveSDMIn.getText().toString());

        intDMQty = Integer.parseInt(mtxtShowReceiveDM.getText().toString());
        intDMQtyIn = Integer.parseInt(mtxtShowReceiveDMIn.getText().toString());

        //ถ้า Pack ไม่เท่ากับ 1 จะมีปุ่ม + - ให้กด
        if (intPackQty==1) {
            if (intQtyIn > intQty) {
                mtxtShowReceiveQtyIn.setText(String.valueOf(intQty));
                intQtyIn = Integer.parseInt(mtxtShowReceiveQtyIn.getText().toString());
                if (intQty != 0) {
                    strAlertMessage = "คุณรับสินค้าตัวดี\n เกินจำนวนแล้วนะครับ";
                } else {
                    strAlertMessage = "ในรายการรับสินค้าไม่มีตัวดีที่ต้องรับนะครับ";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //ให้ทำการ Scan partnid ต่อ
                blnWaitScanSerialno = false;
                metScanReceivePart.setHint("Scan Partid");
            }
            if (intDFQtyIn > intDFQty) {
                mtxtShowReceiveDFIn.setText(String.valueOf(intDFQty));
                intDFQtyIn = Integer.parseInt(mtxtShowReceiveDFIn.getText().toString());
                if (intDFQty != 0) {
                    strAlertMessage = "คุณรับสินค้าตัวไม่สมบูรณ์\n เกินจำนวนที่ต้องการแล้วนะครับ";
                } else {
                    strAlertMessage = "ในรายการรับสินค้าไม่มีตัวไม่สมบูรณ์ที่ต้องรับนะครับ";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //ให้ทำการ Scan partnid ต่อ
                blnWaitScanSerialno = false;
                metScanReceivePart.setHint("Scan Partid");
            }
            if (intSDMQtyIn > intSDMQty) {
                mtxtShowReceiveSDMIn.setText(String.valueOf(intSDMQty));
                intSDMQtyIn = Integer.parseInt(mtxtShowReceiveSDMIn.getText().toString());
                if (intSDMQty != 0) {
                    strAlertMessage = "คุณรับสินค้าตัว Scrap\n เกินจำนวนที่ต้องการแล้วนะครับ";
                } else {
                    strAlertMessage = "ในรายการรับสินค้าไม่มีตัว Scrap ที่ต้องรับนะครับ";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //ให้ทำการ Scan partnid ต่อ
                blnWaitScanSerialno = false;
                metScanReceivePart.setHint("Scan Partid");
            }
            if (intDMQtyIn > intDMQty) {
                mtxtShowReceiveDMIn.setText(String.valueOf(intDMQty));
                intDMQtyIn = Integer.parseInt(mtxtShowReceiveDMIn.getText().toString());
                if (intDMQty != 0) {
                    strAlertMessage = "คุณรับสินค้าตัวเสีย\n เกินจำนวนที่ในเอกสารแล้วนะครับ";
                } else {
                    strAlertMessage = "ในรายการรับสินค้าไม่มีตัวเสียที่ต้องรับนะครับ";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //ให้ทำการ Scan partnid ต่อ
                blnWaitScanSerialno = false;
                metScanReceivePart.setHint("Scan Partid");

            }
        }

        mbtnConfirmReceivePart.setEnabled(false);
        mbtnReset_Part.setEnabled(false);
        mbtnHold_Part.setEnabled(false);

        //ทำการ update จำนวนใน Tmp_RFCheckin ให้ด้วย
        if(! mtxtShowScanReceiveQty.getText().toString().isEmpty() && ! mtxtShowScanReceiveQty.getText().toString().equals("") && ! mtxtShowScanReceiveQty.getText().toString().equals("0") ){
            if ( !(intQtyIn > intQty)) {
                Execute_Mobile_Upd_Tmp_RFCheckIn(mtxtShowScanReceiveQty.getText().toString());
            }
        }


        if (! blnWaitScanSerialno && (intQtyIn != 0 || intDFQtyIn != 0 || intSDMQtyIn != 0 || intDMQtyIn != 0)) {
            if (  (intQtyIn <= intQty) &&  (intDFQtyIn <= intDFQty) &&  (intSDMQtyIn <= intSDMQty) &&  (intDMQtyIn <= intDMQty) ) {
                //กรณี Hold แล้วเข้ามา
                mbtnHold_Part.setEnabled(true);
                mbtnReset_Part.setEnabled(true);
            }
            if (String.valueOf(intQty).trim().equals(String.valueOf(intQtyIn).trim()) &&
                    String.valueOf(intDFQty).trim().equals(String.valueOf(intDFQtyIn).trim()) &&
                    String.valueOf(intSDMQty).trim().equals(String.valueOf(intSDMQtyIn).trim()) &&
                    String.valueOf(intDMQty).trim().equals(String.valueOf(intDMQtyIn).trim())
            ) {
                mbtnConfirmReceivePart.setEnabled(true);
                mbtnReset_Part.setEnabled(false);
                mbtnHold_Part.setEnabled(false);
                metScanReceivePart.setEnabled(false);
            }
        }
    }



    private void Execute_Mobile_Upd_Tmp_RFCheckIn(String strQty) {
        String tag = "6SepV2";
        try {
            ExecuteSixParameter executeSixParameter = new ExecuteSixParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_UPD_Tmp_RFCheckIn();
                    executeSixParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strReceiveDocno,
                    "strPartnid", strReceivePartnid,
                    "strKind", strPartStatus,
                    "strDigit", strDigitNo,
                    "strQty", strQty,
                    strURL);
            String resultJSON = executeSixParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_UPD_Tmp_RFCheckIn() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (!strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ clsResult.ResultMessage.toString() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                ;
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }




    private void GetProductDetail() {
        String tag = "6SepV2";
        try {
            ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
            strURL=strServerAddress +     myConstant.urlMobile_GetProductDetail();
            getProductDesc.execute(getDataBaseName, strReceivePartnid, strURL);

            String resultJSON = getProductDesc.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strAlertMessage="Not Found Data !!!";
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsProduct = gson.fromJson(resultJSON.toString(), Product.class);

                mtxtShowReceivePartno.setText(clsProduct.PartNo+" "+clsProduct.PartDes);
                strReceivePackQty=  clsProduct.PackQty;
                //เอาไว้เปรียบเทียบว่าต้องแจ้งเตือนการรับสินค้า
                dblPackQty = Double.parseDouble(strReceivePackQty);
                intPackQty = dblPackQty.intValue();

                blnReceivePartHaveLabel=clsProduct.PrintLabel.toUpperCase().equals("Y");
                blnReceivePartHaveSerialNo=clsProduct.PartHaveSerialNo.toUpperCase().equals("Y");

                strTableName="PartTube";
                strField="PartNid";
                strCondition="PartNid='" + strReceivePartnid + "'";
                strURL=strServerAddress+myConstant.urlMobile_CountRecord();
                strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
                blnReceivePartTube=!strReturnValue.equals("0");

            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }




    private void GetReceivePartDetail() {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetReceivePartDetail();
            executeThreeParameter.execute("strDataBaseName",strDataBaseName,"strDocno",strReceiveDocno,"strPartnid", strReceivePartnid, strURL);

            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "";
                strAlertMessage="Not Found Data !!!" ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsReceivePartDetail = gson.fromJson(resultJSON.toString(), ReceivePartDetail.class);

                strReceiveTmpTime=clsReceivePartDetail.TmpTime;

                //กำหนดไม่ให้แสดงทศนิยม
                mtxtShowReceiveQty.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.Qty)));
                mtxtShowReceiveDF.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyDf)));
                mtxtShowReceiveSDM.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyScrap)));
                mtxtShowReceiveDM.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyDM)));
                //ถ้าไม่ใช่ part ที่พิมพ์ label ที่ไม่ได้เลือกว่าจัดด่่วน หรือ เป็น Parttube ก็ให้จัดปกติ
                if  ((blnReceivePartHaveLabel && ! blnReceiveScanFast && ! blnReceivePartTube)){
                    mtxtShowReceiveQtyIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyIn)));
                    mtxtShowReceiveDFIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyDfIn)));
                    mtxtShowReceiveSDMIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyScrapIn)));
                    mtxtShowReceiveDMIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyDMIn)));
                }
                else {
                    mtxtShowReceiveQtyIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.Qty)));
                    mtxtShowReceiveDFIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyDf)));
                    mtxtShowReceiveSDMIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyScrap)));
                    mtxtShowReceiveDMIn.setText(String.format("%.0f",Double.parseDouble(clsReceivePartDetail.QtyDM)));
                }
                mtxtShowReceiveLocation.setText(clsReceivePartDetail.LCCode.toString());
                mtxtShowScanReceiveQty.setText("0");
            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    public boolean Check_ScanDuplicate(String strTmpTime,String strDocno,String strPartNid,String strDigitNo,String strKind) {
        String tag = "6SepV2";
        Boolean resultBoolean = Boolean.FALSE;
        try {
            strTableName="Tmp_RFCheckIn_ChkDigit";
            strField="Digit";
            strCondition = "Docno='" + strDocno + "' and PartNid='" + strPartNid + "' and Digit='" + strDigitNo + "'" ;
            strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);

            if (strReturnValue.equals("")){
                //ทำการใส่ข้อมูลเข้าไปเพื่อไม่ให้ทำการ Scan ซ้ำื
                Execute_Mobile_INS_Tmp_RFCheckIn_ChkDigit(strDocno, userLogin.STFcode.trim(), strPartNid,"N",strKind,strDigitNo,mtxtShowScanReceiveQty.getText().toString());
            }
            else{
                //เพิ่มจำนวนครั้งในการ Scan ซ้ำ
                intCheckScanDuplicate=intCheckScanDuplicate+1;
                //ถ้าตั้งใจ Scan ซ้ำถึง 2 ครั้ง ครั้งที่ 3 ยอมให้ผ่านแสดงว่า Barcodeซ้ำ
                if (intCheckScanDuplicate<3) {
                    resultBoolean = Boolean.TRUE;
                    strAlertMessage = "คุณ Scan สินค้าซ้ำนะครับ.";
                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Check_ScanDuplicate ==> " + e.toString());
        }
        return resultBoolean;
    }




    private void Execute_Mobile_Confirm_ReceivePart(String strTmpTime,String strDocno, String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameterParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Confirm_ReceivePart();
            executeFiveParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strDocNo",strDocno,
                    "strPartNid",strPartNid,
                    "strStfcode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeFiveParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_ReceivePart() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();

                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_ReceivePart() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
                    strAlertMessage=clsResult.ResultMessage;
//                    if (strAlertMessage.trim().toUpperCase().equals("COMPLETE")) {
//                        strAlertMessage="Receive Complete";
//                        ShowAlertDialog(R.string.alertdialog_success, "Complete.", strAlertMessage, R.drawable.alertdialog_ic_success);
//                    }
                    ShowSelectPart(strReceiveDocno);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void Execute_Mobile_INS_Tmp_RFCheckIn_ChkDigit(String strDocno, String strStfcode, String strPartNid,
                                                           String strParttubestock,String strKind, String strDigit, String strQty){
        String tag = "6SepV2";
        try {
            ExecuteEightParameter executeEightParameterParameter = new ExecuteEightParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_INS_Tmp_RFCheckIn_ChkDigit();
            executeEightParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno",strDocno,
                    "strStfcode",strStfcode,
                    "strPartnid",strPartNid,
                    "strParttubestock",strParttubestock,
                    "strKind",strKind,
                    "strDigit",strDigit,
                    "strQty",strQty,
                    strURL);
            String resultJSON = executeEightParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_INS_Tmp_RFCheckIn_ChkDigit() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_INS_Tmp_RFCheckIn_ChkDigit() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                };
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void GetReceiveDocType(String strDocno){
        strURL=strServerAddress+myConstant.urlMobile_GetDoctype();
        strReturnValue = globalUtility.ReturnValue_ExecuteTwoParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strDocno",strDocno,strURL);
        strReceiveDocType=strReturnValue;

        strReturnValue="0";
        strURL=strServerAddress+myConstant.urlMobile_CountRecord();
        if (strReceiveDocType.equals("CMP")) {
            strTableName="COMPOS";
            strField="CMPNO";
            strCondition="CMPNO='"+strReceiveDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strReceiveDocType.equals("CPB")) {
            strTableName="CPB";
            strField="CPBNO";
            strCondition="CPBNO='"+strReceiveDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strReceiveDocType.equals("RLC")) {
            strTableName="RELOCATE";
            strField="RLCNO";
            strCondition="RLCNO='"+strReceiveDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        //ทำการตรวจสอบว่าเป็นการจัดด่วนหรือไม่่
        blnReceiveScanFast=!strReturnValue.equals("0");
    }




    private void createListViewDocument() {
        //ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";
        try {
            ExecuteGetListDocument_Receive_Store executeGetListDocument_Receive_Store = new ExecuteGetListDocument_Receive_Store(getApplicationContext());
            //P คือ VW_Mobile_ListDocNo_Receiving  แสดงเอกสารที่รอ จัดสินค้า
            strURL=strServerAddress +  myConstant.urlMobile_ListDocument_Receive();
            executeGetListDocument_Receive_Store.execute(strDataBaseName, strSelectDoctype,userLogin.STFcode.trim(), strURL);
            String resultJSON = executeGetListDocument_Receive_Store.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

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
                    strReceiveDocno = metSelectDocno.getText().toString();
                    Execute_Mobile_INS_Tmp_RFCheckIn(strReceiveDocno.trim());
                    ShowSelectPart(strReceiveDocno.trim());
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView Document==> " + e.toString());
        }
    }//createListView


    private void Execute_Mobile_INS_Tmp_RFCheckIn(String strDocno) {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_INS_Tmp_RFCheckIn();
            executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strDocno,
                    "strStfCode", userLogin.STFcode.toString(),
                    strURL);
            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_INS_Tmp_RFCheckIn() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (!strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ clsResult.ResultMessage.toString() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                ;
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }




    private void createListViewPart(final String strDocno) {
        strReceivePartnid="";
        blnListPartEmpty=false;
        String tag = "6SepV2";
        strTableName=" tmp_rfcheckin A inner join part B ON A.partnid=B.partnid ";
        strField="A.PartNid,rtrim(B.partno)+' '+B.partdes  as Description"  ;
        strCondition = "A.Docno='" + strDocno + "' and StoreCheckin=''";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetListPart();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strTableName", strTableName,
                    "strField", strField,
                    "strCondition", strCondition,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListPartNidStrings = new String[jsonArray.length()];
            final String[] arrDescription = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป
            if (jsonArray.length()==0) {
                blnListPartEmpty=true;
                InitializeData();
                return;
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
                    strReceivePartnid=arrListPartNidStrings[i].toString();
                    ShowScanPart(strReceivePartnid);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView

    private void Hold_Receiving_Document(){

        ResetAllWarning();
        blnWarning_Hold=true;
        strAlertMessage="คุณต้องการรับสินค้าใหม่ \n ภายหลังใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }

    private void Reset_Receiving_Document(){
        ResetAllWarning();
        blnWarning_Reset=true;
        strAlertMessage="คุณต้องการรับสินค้าใหม่ \n อีกกครั้งใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }


    private void Hold_Receiving_Part(){
        Execute_Hold_Reset_Receiving(strReceiveDocno,strReceivePartnid,"H");
        ShowSelectPart(strReceiveDocno);
    }

    private void Reset_Receiving_Part(){
        Execute_Hold_Reset_Receiving(strReceiveDocno,strReceivePartnid,"R");
        ShowSelectPart(strReceiveDocno);
    }

    private void Execute_Hold_Reset_Receiving(String strDocNo,String strPartnid,String strOption) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress +myConstant.urlMobile_Receive_Hold_Reset();
                    executeFiveParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno",strDocNo,
                    "strPartnid",strPartnid,
                    "strOption",strOption,
                    "strStfCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeFiveParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Receive_Hold_Reset();
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
//                else{
//                    strAlertMessage=clsResult.ResultMessage.toString();
//                    ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage,R.drawable.alertdialog_ic_success);
//                }

            }
        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
    }


//
//    private void  Clear_Tmp_RfcheckIn(String strDocno,String  strPartnid){
//        String tag = "6SepV2";
//        try {
//            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
//            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
//                    "strStatus","RECEIVE",
//                    "strDocno",strDocno,
//                    "strPartnid",strPartnid,
//                    "strStfcode",userLogin.STFcode.trim(),
//                    myConstant.urlMobile_Clear_Tmp_RFCheckIn());
//            String resultJSON = executeFiveParameter.get();
//            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
//            //for Not User Pacel
//            if (resultJSON.equals("[]")) {
//                strResultReturnValue = "Error Execute";
//                strAlertMessage="Error Execute " + myConstant.urlMobile_Clear_Tmp_RFCheckIn();
//                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
//            } else {
//                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
//                Gson gson = new Gson();
//                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
//                strResultReturnValue = clsResult.ResultID.toString();
//                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
//                    strAlertMessage=clsResult.ResultMessage.toString();;
//                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
//                }
//                else{
//                    //strAlertMessage=clsResult.ResultMessage;
//                    // ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage,R.drawable.alertdialog_ic_success);
//                }
//
//            }
//
//        } catch (Exception e) {
//            Log.d(tag, "e Confirm Document ==> " + e.toString());
//        }
//    }


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
                    if (blnWarning_Reset){
                        Execute_Hold_Reset_Receiving(strReceiveDocno,"","R");
                        InitializeData();
                    }else if (blnWarning_Hold){
                        Execute_Hold_Reset_Receiving(strReceiveDocno,"","H");
                        InitializeData();
                    }
//                    else if (blnWarning_Confirm){
//                        Execute_Mobile_Confirm_ReceivePart(strReceiveTmpTime,strReceiveDocno,strReceivePartnid);
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