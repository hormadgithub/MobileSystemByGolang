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
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument;
import pneumax.mobilesystembygolaung.connected.ExecuteGetProductDesc;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocumentAdapter;
import pneumax.mobilesystembygolaung.manager.ListPartAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.PickPartDetail;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.ReturnValue;
import pneumax.mobilesystembygolaung.object.StaffLogin;

import static pneumax.mobilesystembygolaung.manager.GlobalVar.getDataBaseName;


public class PickingActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    Product clsProduct;
    PickPartDetail clsPickPartDetail;

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
    Button mbtnIncPickQty, mbtnDecPickQty,mbtnConfirmPickPart;
    CircularImageView mbtnSearch;
    ImageView mimgBackTop;
    EditText metScanDocument,metScanLocation,metSelectDocno, metScanPickPart;
    TextView mtvShowDocSelected,mtxtShowPickPartno;
    Group mgplvListDocument,mgpLocation,mgpScanPart,mgpButtonIncDec;
    Boolean blnShowgplvListDocument,blnShowgpLocation,blnShowgpScanPart;

    // แสดงรายละเอียดการจัดสินค้า
    TextView mtxtShowPickQty,mtxtShowPickDF,mtxtShowPickSDM,mtxtShowPickDM,mtxtShowScanPickQty,mtxtShowPickLocation;
    TextView mtxtShowPickQtyOut,mtxtShowPickDFOut,mtxtShowPickSDMOut,mtxtShowPickDMOut;

    Spinner mspnLocation;
    ArrayAdapter<String> AdapterSpinnerLocation;
    private String[] arrLocationStrings;
    private String  strLocationStrings;

    Boolean blnListPartEmpty;
    View updateview;// above oncreate method
    ListView mlvListDocument,mlvListPart;

    //ตัวแปรสำหรับใช้ในการ Select ข้อมูล
    String strPickDocType, strPickTmpTime,strPickDocno,strPickLocation,strPickPartnid,strPickPackQty;
    Boolean blnPickPartHaveSerialNo,blnPickPartHaveLabel,blnPickPartTube,blnPickScanFast ;
    Boolean blnWaitScanSerialno;
    //เก็บไว้เพื่อบอกว่าเป็นการเรียก Alert Warning จกที่ไหนเำื่อให้ทำงานได้ถูกต้อง
    Boolean blnWarning_Reset_Document, blnWarning_Hold_Document,blnWarning_Reset_Part, blnWarning_Hold_Part,blnWarning_Confirm=false;

    Double dblPackQty, dblPickQtyOut, dblPickDFOut, dblPickSDMOut, dblPickDMOut;
    Integer intPackQty,intScanQty, intPickQty, intPickDF, intPickSDM, intPickDM;
    
    String strPartNid, strPartStatus, strDigitNo;
    Integer intCheckScanDuplicate=0; //ถ้า Scan ซ้ำัน 2 ครั้งแล้ว ครั้งที่ 3 จะทำว่า Digit ซ้ำกันจริงๆ ใช่หรือไม่

    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);
        setContentView(R.layout.activity_picking);

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
    }

    private void BindWidgets() {
        metScanDocument=(EditText) findViewById(R.id.etScanDocument);
        metScanLocation=(EditText) findViewById(R.id.etScanLocation);
        mspnLocation = (Spinner) findViewById(R.id.spnLocation);

        metSelectDocno=(EditText) findViewById(R.id.etSelectDocno);
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        mtxtShowPickPartno=(TextView) findViewById(R.id.txtShowPickPartno);
        mtxtShowPickLocation=(TextView) findViewById(R.id.txtShowPickLocation);


        metScanPickPart =(EditText) findViewById(R.id.txtScanInputPickPart);
        mtxtShowScanPickQty=(TextView) findViewById(R.id.txtShowScanPickQty);
        mtxtShowPickQty=(TextView) findViewById(R.id.txtShowPickQty);
        mtxtShowPickDF=(TextView) findViewById(R.id.txtShowPickDF);
        mtxtShowPickSDM=(TextView) findViewById(R.id.txtShowPickSDM);
        mtxtShowPickDM=(TextView) findViewById(R.id.txtShowPickDM);

        mtxtShowPickQtyOut=(TextView) findViewById(R.id.lblShowPickQtyOut);
        mtxtShowPickDFOut=(TextView) findViewById(R.id.txtShowPickDFOut);
        mtxtShowPickSDMOut=(TextView) findViewById(R.id.txtShowPickSDMOut);
        mtxtShowPickDMOut=(TextView) findViewById(R.id.txtShowPickDMOut);
        
        
        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpLocation=(Group) findViewById(R.id.gpLocation);
        mgpScanPart=(Group) findViewById(R.id.gpScanPickPart);
        mgpButtonIncDec=(Group) findViewById(R.id.gpButtonIncDec);

        mlvListPart =(ListView) findViewById(R.id.lvListPart);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);
        mbtnHold_Document =(Button) findViewById(R.id.btnHold_Document);
        mbtnReset_Document =(Button) findViewById(R.id.btnReset_Document);

        mbtnHold_Part =(Button) findViewById(R.id.btnHold_Part);
        mbtnReset_Part =(Button) findViewById(R.id.btnReset_Part);

        mbtnIncPickQty =(Button) findViewById(R.id.btnIncPickQty);
        mbtnDecPickQty =(Button) findViewById(R.id.btnDecPickQty);
        mbtnConfirmPickPart =(Button) findViewById(R.id.btnConfirmPickPart);

        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }


    private void InitializeData() {
        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDocument.setAdapter(null);

        mlvListPart.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListPart.setAdapter(null);

        mbtnSearch.setImageResource(R.drawable.ic_search_blue);
        metScanDocument.setEnabled(true);

        mbtnSearch.setVisibility(View.VISIBLE);
        mbtnSearch.setEnabled(true);

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
        strPickTmpTime="";
        strPickDocno="";
        strPickLocation="";
        //เพื่อให้ทำการรอ Scan location ใหม่
        metScanLocation.setHint("Location");

        strPickPartnid="";

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
                if (! strPickDocno.equals("")) {
                    //Clear_Tmp_Rfcheckout(strPickDocno,strPickPartnid);
                    if (!strPickPartnid.equals("")){
                        Execute_Hold_Reset_Picking(strPickDocno,strPickPartnid,"H");
                    }
                    else {
                        Execute_Hold_Reset_Picking(strPickDocno,"","R");
                    }
                }
               //ยังไม่ได้เลือก Document
               if (strPickDocno.equals("") ||  blnShowgplvListDocument) {
                   strPickLocation="";
                   finish();
                   //overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
                   overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_top);
               }
               else if (blnShowgpLocation){
                   InitializeData();
               }
               else if (blnShowgpScanPart){
                   ShowSelectLocation(strPickDocno);
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
                            strPickDocno = strScan;

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
                            strAlertMessage = "กรุณาเลือกเลือกเอกสารที่ต้องการ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                        }

                        else  if ( blnShowgpLocation) {
                            strAlertMessage = "กรุณาเลือกช่อง Scan Location ก่อนนะครับ..";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanLocation.requestFocus();
                        }

                        else  if ( blnShowgpScanPart) {
                            strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanPickPart.requestFocus();
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
                        strPickLocation = metScanLocation.getHint().toString().trim();
                        mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(s.toString().trim()));
                        createListViewPart(strPickDocno, strPickLocation);
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
                                metScanPickPart.requestFocus();
                            }
                        }
                    }
                }
            }
        });

        metScanPickPart.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanPickPart.setText(metScanPickPart.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });

        metScanPickPart.addTextChangedListener(new TextWatcher() {

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

                if (metScanPickPart.isEnabled() && !metScanPickPart.getText().toString().isEmpty() && !metScanPickPart.getText().toString().equals("")) {
                    metScanPickPart.setText("");
                    blnManualInput = false;
                    metScanPickPart.setHint(s.toString().trim());
                    //กำว่ากำลังรอใส่ Serialno อยู่หรือไม่
                    if (!blnWaitScanSerialno) {
                        if (s.length() == 17 || s.length() == 18) {
                            arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                            strPartNid = arrGetDescFromScanBarcode[0];
                            strPartStatus = arrGetDescFromScanBarcode[1]; //0-สมบูรณ์ 1-ตัวไม่สามบูรณ์  2-ตัวเสีย 3-ตัวScrap
                            strDigitNo = arrGetDescFromScanBarcode[2];

                            if (strPartNid.equals(strPickPartnid)) {
                                //ยอมให้ผ่านได้ถ้าตั้งใจยิงซ้ำ 3 ครั้ง
                                if (!Check_ScanDuplicate(strPickTmpTime, strPickDocno, s.toString(), strPartNid)) {
                                    //เคลียจำนวนการ Scan ซ้ำออกด้วย
                                    intCheckScanDuplicate = 0;
                                    //ได้มาจากตอน GetPartDetail
                                    dblPackQty = Double.parseDouble(strPickPackQty);
                                    dblPickQtyOut = Double.parseDouble(mtxtShowPickQtyOut.getText().toString());
                                    dblPickDFOut = Double.parseDouble(mtxtShowPickDFOut.getText().toString());
                                    dblPickSDMOut = Double.parseDouble(mtxtShowPickSDMOut.getText().toString());
                                    dblPickDMOut = Double.parseDouble(mtxtShowPickDMOut.getText().toString());

                                    intPackQty = dblPackQty.intValue();
                                    intPickQty = dblPickQtyOut.intValue();
                                    intPickDF = dblPickDFOut.intValue();
                                    intPickSDM = dblPickSDMOut.intValue();
                                    intPickDM = dblPickDMOut.intValue();


                                    if (strPartStatus.equals("0")) {
                                        mtxtShowScanPickQty.setText(String.valueOf(intPackQty));
                                        intPickQty = intPickQty + intPackQty;
                                        mtxtShowPickQtyOut.setText(String.valueOf(intPickQty));
                                    } else if (strPartStatus.equals("1")) {
                                        intPackQty = 1;
                                        mtxtShowScanPickQty.setText(String.valueOf(intPackQty));
                                        intPickDF = intPickDF + intPackQty;
                                        mtxtShowPickDFOut.setText(String.valueOf(intPickDF));
                                    } else if (strPartStatus.equals("2")) {
                                        intPackQty = 1;
                                        mtxtShowScanPickQty.setText(String.valueOf(intPackQty));
                                        intPickDM = intPickDM + intPackQty;
                                        mtxtShowPickDMOut.setText(String.valueOf(intPickDM));
                                    } else if (strPartStatus.equals("3")) {
                                        intPackQty = 1;
                                        mtxtShowScanPickQty.setText(String.valueOf(intPackQty));
                                        intPickSDM = intPickSDM + intPackQty;
                                        mtxtShowPickSDMOut.setText(String.valueOf(intPickSDM));
                                    } else {
                                        intPackQty = 0;
                                        mtxtShowScanPickQty.setText(String.valueOf(intPackQty));
                                        strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                    }
                                    //ตรวจสอบว่าเป็น Part ที่มี Serial หรือไม่
                                    if (blnPickPartHaveSerialNo) {
                                        blnWaitScanSerialno = true;
                                        mbtnConfirmPickPart.setEnabled(false); //ไม่อยมให้ทำการ Confirm ถ้ายังไม่ได้ Scan Serialno
                                        metScanPickPart.setHint("Scan Serialno");
                                        strAlertMessage = "กรุณาป้อน Serialno ด้วยนะครับ";
                                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                    }
                                }
                            } else {
                                intPackQty = 0;
                                mtxtShowScanPickQty.setText(String.valueOf(intPackQty));
                                strAlertMessage = "สินค้าที่คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการจัดนะครับ";
                                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                            }

                            mgpButtonIncDec.setVisibility(View.GONE);
                            if (intPackQty > 1) {
                                mgpButtonIncDec.setVisibility(View.VISIBLE);
                            }
                            metScanPickPart.setText("");
                            //ถ้าไม่ซ้าก็ให้เพิ่มจำนวน
                            if (intCheckScanDuplicate==0)  {
                                CheckPickComplete();
                            }
                        } else {
                            if (!s.toString().isEmpty()) {
                                strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                metScanPickPart.setText("");
                            }
                        }
                    } else {
                        if (!s.toString().isEmpty()) {
                            Execute_Insert_RFCheckOut_PartSerialNo(strPickDocno, strPickPartnid, s.toString());
                        }
                    }
                }
                else{
                    if (!s.toString().isEmpty()) {
                        strAlertMessage = "คุณ Scan สินค้าครบตามต้องการแล้วนะครับ";
                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                        metScanPickPart.setText("");
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
                Hold_Picking_Document();
            }
        });


        mbtnReset_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Picking_Document();
            }
        });


        mbtnHold_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hold_Picking_Part();
            }
        });


        mbtnReset_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Picking_Part();
            }
        });

        mbtnIncPickQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
                intPickQty = Integer.parseInt(mtxtShowPickQtyOut.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanPickQty.getText().toString());
                intScanQty = intScanQty + 1;
                if (intScanQty > intPackQty ) {
                    intScanQty = 0;
                    intPickQty = intPickQty - intPackQty; //ลดเลงให้มีค่าเท่ากับยังไม่ได้ Scan
                } else {
                    intPickQty = intPickQty + 1; //เพิ่มทีละ 1
                }
                mtxtShowScanPickQty.setText(String.valueOf(intScanQty));
                mtxtShowPickQtyOut.setText(String.valueOf(intPickQty));
                CheckPickComplete();
            }
        });

        mbtnDecPickQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intPickQty = Integer.parseInt(mtxtShowPickQtyOut.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanPickQty.getText().toString());

                intScanQty = intScanQty - 1;
                if (intScanQty < 0) {
                    intScanQty = intPackQty;
                    intPickQty = intPickQty + intPackQty; //ลดเลงให้มีค่าเท่ากับยังไม่ได้ Scan
                } else {
                    intPickQty = intPickQty - 1; //เพิ่มทีละ 1
                }
                mtxtShowScanPickQty.setText(String.valueOf(intScanQty));
                mtxtShowPickQtyOut.setText(String.valueOf(intPickQty));
                CheckPickComplete();
            }
        });

        mbtnConfirmPickPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ต้องการให้ทำงานไวขึ้นไม่ต้องถาม
                Execute_Mobile_Confirm_PickPart(strPickTmpTime,strPickDocno,strPickPartnid);
                //ทำการตรวจสอบว่าจัดครบทุกรายการแล้วหรือไม่
//                ResetAllWarning();
//                blnWarning_Confirm=true;
//                strAlertMessage="คุณจัดสินค้ารายการนี้ครบแล้วใช่หรือไม่" ;
//                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
                //ย้ายไปทำใน Alert  Dialog
                // Execute_Mobile_Confirm_PickPart(strPickTmpTime,strPickDocno,strPickPartnid);

            }
        });
    }



    private void ResetAllWarning() {
        blnWarning_Hold_Document =false;
        blnWarning_Reset_Document =false;
        blnWarning_Hold_Part =false;
        blnWarning_Reset_Part =false;

        blnWarning_Confirm=false;
    }

    private void CheckDocumentFromScan(String strDocno){
        strTableName="VW_Mobile_ListDocNo_Picking";
        strField="Docno";
        // strCondition="Docno='" +strDocno + "' and  (StoreCheck='' OR (StoreCheck='H' and HoldCode='"+ userLogin.STFcode.trim()+"'))";
        //คนอื่นสามารถจัดสินค้าที่ Hold ไว้ได้
        strCondition="Docno='" +strDocno + "'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue=globalUtility.Find_Record(getApplicationContext(), strDataBaseName,strTableName,strField,strCondition,strURL);
        if (!strReturnValue.trim().equals("")) {
            Execute_Mobile_Crt_Tmp_RFCheckOut_Picking(strDocno);
            ShowSelectLocation(strDocno);
        }
        else{
            mtvShowDocSelected.setText("Not Found Document");
            strPickDocno = "";
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
        //mgplvListDocument.setVisibility(View.GONE);
        mgpLocation.setVisibility(View.VISIBLE);
        blnShowgpLocation= true;
        strPickPartnid="";
        GetCustName(strPickDocno);
        createSpinnerLocation(strDocno);
        GetPickDocType(strDocno);

        metScanLocation.setText("");
        metScanLocation.requestFocus();
        SendKeyDown(2);

    }

//ไว้สำหรับทดสอบ
//    private void ShowSelectLocation1(final String strDocno) {
//        mbtnSearch.setVisibility(View.GONE);
//        metScanDocument.setEnabled(false);
//
//        Invisible_AllGroup();
//        //mgplvListDocument.setVisibility(View.GONE);
//        mgpLocation.setVisibility(View.VISIBLE);
//        blnShowgpLocation= true;
//        strPickPartnid="";
//        GetCustName(strPickDocno);
//        createSpinnerLocation1(strDocno);
//        GetPickDocType(strDocno);
//
//        metScanLocation.setText("");
//        metScanLocation.requestFocus();
//
//    }

    private void ShowScanPart(final String strPickPartnid) {
        Invisible_AllGroup();
        mtvShowDocSelected.setText(strPickPartnid);
        GetProductDetail();
        mgpScanPart.setVisibility(View.VISIBLE);
        GetPickPartDetail();
        metScanPickPart.setHint("Scan Partid");
        metScanPickPart.setEnabled(true);
        blnWaitScanSerialno=false;
        blnShowgpScanPart=true;
        CheckPickComplete();
        metScanPickPart.requestFocus();
        SendKeyDown(2);
    }


    private void CheckPickComplete() {
        Integer intQty,intQtyOut,intDFQty,intDFQtyOut,intSDMQty,intSDMQtyOut,intDMQty,intDMQtyOut;

            intQty = Integer.parseInt(mtxtShowPickQty.getText().toString());
            intQtyOut = Integer.parseInt(mtxtShowPickQtyOut.getText().toString());

            intDFQty = Integer.parseInt(mtxtShowPickDF.getText().toString());
            intDFQtyOut = Integer.parseInt(mtxtShowPickDFOut.getText().toString());

            intSDMQty = Integer.parseInt(mtxtShowPickSDM.getText().toString());
            intSDMQtyOut = Integer.parseInt(mtxtShowPickSDMOut.getText().toString());

            intDMQty = Integer.parseInt(mtxtShowPickDM.getText().toString());
            intDMQtyOut = Integer.parseInt(mtxtShowPickDMOut.getText().toString());


            //ถ้า Pack ไม่เท่ากับ 1 จะมีปุ่ม + - ให้กด
            if (intPackQty==1) {
                if (intQtyOut > intQty) {
                    mtxtShowPickQtyOut.setText(String.valueOf(intQty));
                    intQtyOut = Integer.parseInt(mtxtShowPickQtyOut.getText().toString());
                    if (intQty != 0) {
                        strAlertMessage = "คุณจัดสินค้าตัวดี\n เกินจำนวนที่ต้องการแล้วนะครับ";
                    } else {
                        strAlertMessage = "ในรายการจัดสินค้าไม่มีตัวดีที่ต้องจัดนะครับ";
                    }
                    ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                    //ให้ทำการ Scan partnid ต่อ
                    blnWaitScanSerialno = false;
                    metScanPickPart.setHint("Scan Partid");
                }
                if (intDFQtyOut > intDFQty) {
                    mtxtShowPickDFOut.setText(String.valueOf(intDFQty));
                    intDFQtyOut = Integer.parseInt(mtxtShowPickDFOut.getText().toString());
                    if (intDFQty != 0) {
                        strAlertMessage = "คุณจัดสินค้าตัวไม่สมบูรณ์\n เกินจำนวนที่ต้องการแล้วนะครับ";
                    } else {
                        strAlertMessage = "ในรายการจัดสินค้าไม่มีตัวไม่สมบูรณ์ที่ต้องจัดนะครับ";
                    }
                    ShowAlertDialog(R.string.alertdialog_error, "Error ", strAlertMessage, R.drawable.alertdialog_ic_error);

                    //ให้ทำการ Scan partnid ต่อ
                    blnWaitScanSerialno = false;
                    metScanPickPart.setHint("Scan Partid");
                }
                if (intSDMQtyOut > intSDMQty) {
                    mtxtShowPickSDMOut.setText(String.valueOf(intSDMQty));
                    intSDMQtyOut = Integer.parseInt(mtxtShowPickSDMOut.getText().toString());
                    if (intSDMQty != 0) {
                        strAlertMessage = "คุณจัดสินค้าตัว Scrap\n เกินจำนวนที่ต้องการแล้วนะครับ";
                    } else {
                        strAlertMessage = "ในรายการจัดสินค้าไม่มีตัว Scrap ที่ต้องจัดนะครับ";
                    }
                    ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                    //ให้ทำการ Scan partnid ต่อ
                    blnWaitScanSerialno = false;
                    metScanPickPart.setHint("Scan Partid");
                }
                if (intDMQtyOut > intDMQty) {
                    mtxtShowPickDMOut.setText(String.valueOf(intDMQty));
                    intDMQtyOut = Integer.parseInt(mtxtShowPickDMOut.getText().toString());
                    if (intDMQty != 0) {
                        strAlertMessage = "คุณจัดสินค้าตัวเสีย\n เกินจำนวนที่ต้องการแล้วนะครับ";
                    } else {
                        strAlertMessage = "ในรายการจัดสินค้าไม่มีตัวเสียที่ต้องจัดนะครับ";
                    }
                    ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                    //ให้ทำการ Scan partnid ต่อ
                    blnWaitScanSerialno = false;
                    metScanPickPart.setHint("Scan Partid");
                }
            }


            mbtnConfirmPickPart.setEnabled(false); //ไม่อยมให้ทำการ Confirm ถ้ายังไม่ได้ Scan Serialno
            mbtnHold_Part.setEnabled(false);
            mbtnReset_Part.setEnabled(false);

            //ทำการเพิ่มจำนวนทีจัดใน  Tmp_RFCheckOut
            if( ! blnWaitScanSerialno && ! mtxtShowScanPickQty.getText().toString().isEmpty() && ! mtxtShowScanPickQty.getText().toString().equals("") && ! mtxtShowScanPickQty.getText().toString().equals("0") ){
                //จำนวนที่ Out ต้องไม่มากกว่าจำนวนที่ต้องการจัด เนื่องจากตัวอื่นจะไม่มี Pack
                if (  !(intQtyOut > intQty) ) {
                    Execute_Mobile_Upd_Tmp_RFCheckOut(mtxtShowScanPickQty.getText().toString());
                }
            }

            if (! blnWaitScanSerialno && (intQtyOut != 0 || intDFQtyOut != 0 || intSDMQtyOut != 0 || intDMQtyOut != 0)) {

                if (  (intQtyOut <= intQty) &&  (intDFQtyOut <= intDFQty) &&  (intSDMQtyOut <= intSDMQty) &&  (intDMQtyOut <= intDMQty) ) {
                    //กรณี Hold แล้วเข้ามา
                    mbtnHold_Part.setEnabled(true);
                    mbtnReset_Part.setEnabled(true);
                }


                if (String.valueOf(intQty).trim().equals(String.valueOf(intQtyOut).trim()) &&
                        String.valueOf(intDFQty).trim().equals(String.valueOf(intDFQtyOut).trim()) &&
                        String.valueOf(intSDMQty).trim().equals(String.valueOf(intSDMQtyOut).trim()) &&
                        String.valueOf(intDMQty).trim().equals(String.valueOf(intDMQtyOut).trim())
                ) {
                    mbtnConfirmPickPart.setEnabled(true);
                    mbtnHold_Part.setEnabled(false);
                    mbtnReset_Part.setEnabled(false);
                    metScanPickPart.setEnabled(false);
                }
            }
    }



    private void GetProductDetail() {
            String tag = "6SepV2";
            try {
                ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
                strURL=strServerAddress+myConstant.urlMobile_GetProductDetail();
                getProductDesc.execute(getDataBaseName, strPickPartnid,strURL);

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

                     mtxtShowPickPartno.setText(clsProduct.PartNo+" "+clsProduct.PartDes);
                     mtxtShowPickLocation.setText(clsProduct.LCcode);
                     strPickPackQty=  clsProduct.PackQty;
                    //เอาไว้เปรียบเทียบว่าต้องแจ้งเตือนการ จัดสินค้าเกินหรือไม่
                    dblPackQty = Double.parseDouble(strPickPackQty);
                    intPackQty = dblPackQty.intValue();

                     blnPickPartHaveLabel=clsProduct.PrintLabel.toUpperCase().equals("Y");
                     blnPickPartHaveSerialNo=clsProduct.PartHaveSerialNo.toUpperCase().equals("Y");

                    strTableName="PartTube";
                    strField="PartNid";
                    strCondition="PartNid='" + strPickPartnid + "'";
                    strURL=strServerAddress+myConstant.urlMobile_CountRecord();
                    strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
                     blnPickPartTube=!strReturnValue.equals("0");

                }
            } catch (Exception e) {
                Log.d(tag, "e Create ListView ==> " + e.toString());
            }
        }




    private void GetPickPartDetail() {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetPickPartDetail();
            executeThreeParameter.execute("strDataBaseName",strDataBaseName,"strDocno",strPickDocno,"strPartnid", strPickPartnid,strURL);

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
                clsPickPartDetail = gson.fromJson(resultJSON.toString(), PickPartDetail.class);

                strPickTmpTime=clsPickPartDetail.TmpTime;

                //กำหนดไม่ให้แสดงทศนิยม
                mtxtShowPickQty.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.Qty)));
                mtxtShowPickDF.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyDf)));
                mtxtShowPickSDM.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyScrap)));
                mtxtShowPickDM.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyDM)));
                //ถ้าไม่ใช่ part ที่พิมพ์ label ที่ไม่ได้เลือกว่าจัดด่่วน หรือ เป็น Parttube ก็ให้จัดปกติ
                if  ((blnPickPartHaveLabel && ! blnPickScanFast && ! blnPickPartTube)){
                    mtxtShowPickQtyOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyOut)));
                    mtxtShowPickDFOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyDfOut)));
                    mtxtShowPickSDMOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyScrapOut)));
                    mtxtShowPickDMOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyDMOut)));
                }
                else {
                    mtxtShowPickQtyOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.Qty)));
                    mtxtShowPickDFOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyDf)));
                    mtxtShowPickSDMOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyScrap)));
                    mtxtShowPickDMOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartDetail.QtyDM)));
                }
                mtxtShowScanPickQty.setText("0");
            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    public boolean Check_ScanDuplicate(String strTmpTime,String strDocno,String strPartDigitNo,String strPartNid) {
        String tag = "6SepV2";
        Boolean resultBoolean = Boolean.FALSE;
        try {
            strTableName="Tmp_RFPartDigitNo";
            strField="PartDigitNo";
            strCondition = "TmpTime ='" + strTmpTime  + "' and Docno='" + strDocno + "' and PartDigitNo='" + strPartDigitNo + "' and PartNid='" + strPartNid + "'";
            strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);

            if (strReturnValue.equals("")){
                    //ทำการใส่ข้อมูลเข้าไปเพื่อไม่ให้ทำการ Scan ซ้ำื
                    Execute_Insert_Tmp_RFPartDigitNo(strTmpTime, strDocno, strPartDigitNo, strPartNid);
            }
            else{
                //เพิ่มจำนวนครั้งในการ Scan ซ้ำ
                intCheckScanDuplicate=intCheckScanDuplicate+1;
                //ถ้าตั้งใจ Scan ซ้ำถึง 2 ครั้ง ครั้งที่ 3 ยอมให้ผ่านแสดงว่า Barcodeซ้ำ
                if (intCheckScanDuplicate < 3) {
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


    private void Execute_Insert_RFCheckOut_PartSerialNo(String strDocNo,String strPartnid,String strSerialno) {
        String tag = "6SepV2";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Insert_RFCheckOut_PartSerialNo();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strPartnid",strPartnid,
                    "strSerialno",strSerialno,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Insert_RFCheckOut_PartSerialNo();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage.toString();;
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                    metScanPickPart.setText("");
                }
                else{
                    blnWaitScanSerialno = false;
                    metScanPickPart.setHint("Scan Partid");
                    metScanPickPart.setText("");
                    //เช็คว่าทำการ Scan ครับหรือยัง
                    CheckPickComplete();
//                    strAlertMessage=clsResult.ResultMessage.toString();
//                    ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage,R.drawable.alertdialog_ic_success);
                }

            }

        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
    }




    private void Execute_Mobile_Confirm_PickPart(String strTmpTime,String strDocno, String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameterParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Confirm_PickPart();
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
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_PickPart() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();

                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_PickPart() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
                    strAlertMessage=clsResult.ResultMessage;
                    //ไม่ต้องแจ้งเเตือนกันจัดครบ
//                    if (strAlertMessage.trim().toUpperCase().equals("COMPLETE")) {
//                        strAlertMessage="Complete Picking";
//                        ShowAlertDialog(R.string.alertdialog_success, "Complete.", strAlertMessage, R.drawable.alertdialog_ic_success);
//                    }
                    ShowSelectLocation(strPickDocno);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void Execute_Insert_Tmp_RFPartDigitNo(String strTmpTime,String strDocno, String strPartDigitNo, String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameterParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Insert_Tmp_RFPartDigitNo();
            executeFiveParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strDocNo",strDocno,
                    "strPartDigitNo",strPartDigitNo,
                    "strPartNid",strPartNid,
                    strURL);
            String resultJSON = executeFiveParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Insert_Tmp_RFPartDigitNo() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Insert_Tmp_RFPartDigitNo() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                };
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }




    private void GetCustName(String strDocno) {
        //strTableName="VW_Mobile_ListDocNo_Picking";
        strTableName="VW_CRO";
        //strField="CSName";
        strField="CSThiname";
        //strCondition="Docno='" + metSelectDocno +"'";
        strCondition="CRONO='" + strDocno +"'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        mtvShowDocSelected.setText(strReturnValue);
    }

    private void GetPickDocType(String strDocno){
        strURL=strServerAddress+myConstant.urlMobile_GetDoctype();
        strReturnValue = globalUtility.ReturnValue_ExecuteTwoParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strDocno",strDocno,strURL);
        strPickDocType=strReturnValue;

        strReturnValue="0";
        strURL=strServerAddress+myConstant.urlMobile_CountRecord();
        if (strPickDocType.equals("CRO")) {
            strTableName="CRO";
            strField="CRONO";
            strCondition="CRONO='"+strPickDocno.trim()+"' and rtrim(IssFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strPickDocType.equals("CMP")) {
            strTableName="COMPOS";
            strField="CMPNO";
            strCondition="CMPNO='"+strPickDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strPickDocType.equals("CPB")) {
            strTableName="CPB";
            strField="CPBNO";
            strCondition="CPBNO='"+strPickDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strPickDocType.equals("RLC")) {
            strTableName="RELOCATE";
            strField="RLCNO";
            strCondition="RLCNO='"+strPickDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        //ทำการตรวจสอบว่าเป็นการจัดด่วนหรือไม่่
        blnPickScanFast=!strReturnValue.equals("0");
    }




    private void createListViewDocument() {
        //ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";
        try {
            ExecuteGetListDocument executeGetListDocument = new ExecuteGetListDocument(getApplicationContext());
            //P คือ VW_Mobile_ListDocNo_Picking  แสดงเอกสารที่รอ จัดสินค้า
            strURL=strServerAddress+myConstant.urlMobile_ListDocument();
            executeGetListDocument.execute(strDataBaseName, "P",userLogin.STFcode.trim(), strURL);
            String resultJSON = executeGetListDocument.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListDocumentString = new String[jsonArray.length()];
            final String[] arrCustNameString = new String[jsonArray.length()];
            final String[] arrSortDateString = new String[jsonArray.length()];

            mtvShowDocSelected.setText("จำนวน "+ String.valueOf( jsonArray.length())+" ใบ");
            if (jsonArray.length()==0) {
                blnListPartEmpty=true;
            }

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListDocumentString[i] = jsonObject.getString("DocNo");
                arrCustNameString[i] = jsonObject.getString("CSName");
                arrSortDateString[i] = jsonObject.getString("SortDate");
                Log.d(tag, "Docno [" + i + "] ==> " + arrListDocumentString[i]);
            }//for

            ListDocumentAdapter listDocumentAdapter = new ListDocumentAdapter(getApplicationContext(), arrListDocumentString, arrCustNameString, arrSortDateString);

            mlvListDocument.setAdapter(listDocumentAdapter);

            mlvListDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                    metSelectDocno.setText(arrListDocumentString[i].toString());
                    mtvShowDocSelected.setText(arrCustNameString[i].toString());
                    strPickDocno = metSelectDocno.getText().toString();
                    Execute_Mobile_Crt_Tmp_RFCheckOut_Picking(strPickDocno.trim());
                    ShowSelectLocation(strPickDocno.trim());
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView Document==> " + e.toString());
        }
    }//createListView


    private void Execute_Mobile_Crt_Tmp_RFCheckOut_Picking(String strDocno) {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Crt_Tmp_RFCheckOut_Picking();
            executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strDocno,
                    "strSTfCode", userLogin.STFcode.toString(),
                    strURL);
            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Crt_Tmp_RFCheckOut_Picking() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON, Result.class);
                strReturnValue = clsResult.ResultID;
                if (!strReturnValue.toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ clsResult.ResultMessage ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    private void Execute_Mobile_Upd_Tmp_RFCheckOut(String strQty) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_UPD_Tmp_RFCheckOut() ;
            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strPickDocno,
                    "strPartnid", strPickPartnid,
                    "strKind", strPartStatus,
                    "strQty", strQty,
                    strURL);
            String resultJSON = executeFiveParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_UPD_Tmp_RFCheckOut() ;
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



    private void createSpinnerLocation(final String strDocno) {
        try {
            strTableName="Tmp_RFCheckOut";
            strField="LCCode";
            strCondition = " Docno='" + strDocno +"' and StoreCheck=''";
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_ReturnValue();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition, strURL);
            String resultJSON = executeFourParameter.get();

            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
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
                        //ถ้าเป็น MIS ให้แสดง Part ขึ้นมาเลย
                        if (userLogin.DPCode.equals("MIS"))
                            strPickLocation=strLocationStrings;
                        //ทำการ Run ใหม่เพื่อให้มีเฉพาะ part ที่เหลือ
                        if (! strPickLocation.isEmpty() && ! strPickLocation.equals("")){
                            createListViewPart(strPickDocno,strPickLocation);
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


    private void createListViewPart(final String strDocno,String strLocation) {
        strPickPartnid="";
        blnListPartEmpty=false;
        //mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(strLocation));
        //ListView listViewPart = findViewById(R.id.lvListPartnid);

        String tag = "6SepV2";
        strTableName=" tmp_rfcheckout A inner join part B ON A.partnid=B.partnid ";
        strField="A.PartNid,rtrim(B.partno)+' '+B.partdes  as Description"  ;
        strCondition = "A.Docno='" + strDocno + "' and A.LCCode ='" + strLocation + "' and StoreCheck=''";


        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetListPart();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition,strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListPartNidStrings = new String[jsonArray.length()];
            final String[] arrDescription = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป

            mtvShowDocSelected.setText("จำนวน "+ String.valueOf( jsonArray.length())+" ใบ");

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
                    strPickPartnid=arrListPartNidStrings[i].toString();
                    ShowScanPart(strPickPartnid);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView

    private void Hold_Picking_Document(){

        ResetAllWarning();
        blnWarning_Hold_Document =true;
        strAlertMessage="คุณต้องการจัดสินค้าใหม่ \n ภายหลังใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }

    private void Reset_Picking_Document(){
        ResetAllWarning();
        blnWarning_Reset_Document =true;
        strAlertMessage="คุณต้องการจัดสินค้าใหม่ \n อีกกครั้งใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }


    private void Hold_Picking_Part(){
        Execute_Hold_Reset_Picking(strPickDocno,strPickPartnid,"H");
        ShowSelectLocation(strPickDocno);
    }

    private void Reset_Picking_Part(){
        Execute_Hold_Reset_Picking(strPickDocno,strPickPartnid,"R");
        ShowSelectLocation(strPickDocno);
    }



    private void Execute_Hold_Reset_Picking(String strDocNo,String strPartnid,String strOption) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_Picking_Hold_Reset();
            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strPartnid",strPartnid,
                    "strOption",strOption,
                    "strStfCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeFiveParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Picking_Hold_Reset();
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
                        Execute_Hold_Reset_Picking(strPickDocno,"","R");
                        InitializeData();
                    }else if (blnWarning_Hold_Document){
                        Execute_Hold_Reset_Picking(strPickDocno,"","H");
                        InitializeData();
                    }
//                    if (blnWarning_Reset_Part){
//                        Execute_Hold_Reset_Picking(strPickDocno,strPickPartnid,"R");
//                        ShowSelectLocation(strPickDocno);
//                    }else if (blnWarning_Hold_Part){
//                        Execute_Hold_Reset_Picking(strPickDocno,strPickPartnid,"H");
//                        ShowSelectLocation(strPickDocno);
//                    }
//                    else if (blnWarning_Confirm){
//                        Execute_Mobile_Confirm_PickPart(strPickTmpTime,strPickDocno,strPickPartnid);
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