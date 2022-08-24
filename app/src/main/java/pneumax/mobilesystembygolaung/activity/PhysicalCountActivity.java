package pneumax.mobilesystembygolaung.activity;

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

import org.json.JSONArray;
import org.json.JSONObject;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteElevenParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteGetProductDesc;
import pneumax.mobilesystembygolaung.connected.ExecuteSevenParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListPartPhysicalCountAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;
import pneumax.mobilesystembygolaung.object.Tmp_RFPhysicalCount;

public class PhysicalCountActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;


    Tmp_RFPhysicalCount clsTmp_RFPhysicalCount;
    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL;

    View AlertDialogView;
    String strAlertMessage;
    String strReturnValue;

    Product clsProduct;
    double dblPackQty, dblScanQty, dblCountQty, dblCountDF, dblCountSDM, dblCountDM;

    String strPartNid, strPartStatus, strDigitNo;
    Result clsResult;
    Boolean blnListPartEmpty=false;
    Integer intCheckScanDuplicate=0; //ถ้า Scan ซ้ำัน 2 ครั้งแล้ว ครั้งที่ 3 จะทำว่า Digit ซ้ำกันจริงๆ ใช่หรือไม่
    public String strTmpTime;


    static final int RESULT_OK = -1;

    ImageView mimgBackTop;
    Button  mbtnSelectPart, mbtnIncQty, mbtnDecQty,mbtnHold_Part;
    TextView mtxtGenno, mtxtWeekno, mtxtIssuePeriod, mtxtLocation, mtxtListPart;
    TextView mlblShowProductDetail, mtxtShowPartnid, mtxtShowPartno, mtxtShowPartdesc, mtxtShowWHCode, mtxtShowLCCode, mtxtShowPackQty;

    EditText metScanCountPartnid,metScanLocation;
    TextView mtxtShowScanQty, mtxtShowCountQty, mtxtShowCountDF, mtxtShowCountSDM, mtxtShowCountDM;
    EditText mtxtInputBincardQty, mtxtInputBincardDF, mtxtInputBincardSDM, mtxtInputBincardDM;

    Spinner mspnGenno, mspnWeekno, mspnIssuePeriod, mspnLocation;


    //LinearLayout mToast_Layout;

    //การใช้ Group ต้องอยู่ contrant layout  เดี่ยวเกันท่านั้น
    Group mgpSelectLocation, mgpSelectPart, mgpCountPart,mgpInputBincard,mgpButtonIncDec;
    Boolean blnShowgpSelectLocation,blnShowgpSelectPart,blnShowgpCountPart,blnShowgpInputBincard;


    private String[] arrGenNoStrings, arrWeekNoStrings, arrIssuePeriodStrings, arrLocationStrings;
    private String strGenNoStrings, strWeekNoStrings, strIssuePeriodStrings, strLocationStrings;

    ArrayAdapter<String> AdapterSpinnerLocation;



    View updateview;// above oncreate method
    ListView mlvlvPartnid;
    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_physical_count);
        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility = new GlobalUtility();
        //Get Value from Intent
        getValueFromIntent();

        //bindWidgets
        bindWidgets();

        InitializeData();
        createSpinnerGenNo();
        SetEvent();
    }

    private void getValueFromIntent() {
        Bundle bundle = getIntent().getExtras();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = bundle.getString(globalVar.getServerAddress);
        strDataBaseName =bundle.getString(GlobalVar.getInstance().getDataBaseName);

    }//getValueFromIntent


    public void bindWidgets(){
        //เชื่มตัวแปรกัยหน้า Layout
        mimgBackTop = (ImageView) findViewById(R.id.imgBackTop);
        mbtnSelectPart = (Button) findViewById(R.id.btnSelectPart);
        mbtnHold_Part = (Button) findViewById(R.id.btnHold_Part);

        mtxtGenno = (TextView) findViewById(R.id.txtGenno);
        mtxtWeekno = (TextView) findViewById(R.id.txtWeekno);
        mtxtIssuePeriod = (TextView) findViewById(R.id.txtIssuePeriod);
        mtxtLocation = (TextView) findViewById(R.id.txtLocation);
        mtxtListPart = (TextView) findViewById(R.id.txtListPart);
        metScanLocation=(EditText) findViewById(R.id.etScanLocation);


       // mlblShowProductDetail = (TextView) findViewById(R.id.lblShowProductDetail);
        mtxtShowPartnid = (TextView) findViewById(R.id.txtShowPartnid);
        mtxtShowPartno = (TextView) findViewById(R.id.txtShowPartno);
        mtxtShowPartdesc = (TextView) findViewById(R.id.txtShowPartdesc);
        mtxtShowWHCode = (TextView) findViewById(R.id.txtShowWHCode);
        mtxtShowLCCode = (TextView) findViewById(R.id.txtShowLCCode);
        mtxtShowPackQty = (TextView) findViewById(R.id.txtShowPackQty);

        metScanCountPartnid = (EditText) findViewById(R.id.etScanCountPartnid);
        mtxtShowScanQty = (TextView) findViewById(R.id.txtShowScanQty);
        mbtnIncQty = (Button) findViewById(R.id.btnIncQty);
        mbtnDecQty = (Button) findViewById(R.id.btnDecQty);
        mtxtShowCountQty = (TextView) findViewById(R.id.txtShowCountQty);
        mtxtShowCountDF = (TextView) findViewById(R.id.txtShowCountDF);
        mtxtShowCountSDM = (TextView) findViewById(R.id.txtShowCountSDM);
        mtxtShowCountDM = (TextView) findViewById(R.id.txtShowCountDM);

        mtxtInputBincardQty = (EditText) findViewById(R.id.txtInputBincardQty);
        mtxtInputBincardDF = (EditText) findViewById(R.id.txtInputBincardDF);
        mtxtInputBincardSDM = (EditText) findViewById(R.id.txtInputBincardSDM);
        mtxtInputBincardDM = (EditText) findViewById(R.id.txtInputBincardDM);

        mspnGenno = (Spinner) findViewById(R.id.spnGenno);
        mspnWeekno = (Spinner) findViewById(R.id.spnWeekno);
        mspnIssuePeriod = (Spinner) findViewById(R.id.spnIssuePeriod);
        mspnLocation = (Spinner) findViewById(R.id.spnLocation);
        mlvlvPartnid = (ListView) findViewById(R.id.lvPartnid);

        mgpSelectLocation = (Group) findViewById(R.id.gpSelectLocation);
        mgpSelectPart = (Group) findViewById(R.id.gpSelectPart);
        mgpCountPart = (Group) findViewById(R.id.gpCountPart);
        mgpInputBincard = (Group) findViewById(R.id.gpInputBincard);
        mgpButtonIncDec = (Group) findViewById(R.id.gpButtonIncDec);


    }

    private void InitializeData() {
        //เนื่องจกออกจากการเลือก Part จึงจะทำการเอาข้อมูลออกจาก tmp_RFPhysicalcount
        strTmpTime = globalVar.GetTempTime(userLogin.STFcode);

        Invisibale_AllGroup();
        mgpSelectLocation.setVisibility(View.VISIBLE);
        blnShowgpSelectLocation=true;

        mbtnSelectPart.setText("SELECT PART");
        mbtnSelectPart.setVisibility(View.GONE);
        mtxtListPart.setText("Select Part");

        metScanLocation.setEnabled(true);
        metScanLocation.requestFocus();

    }

    private void Invisibale_AllGroup(){
        mgpSelectLocation.setVisibility(View.GONE);
        mgpSelectPart.setVisibility(View.GONE);
        mgpCountPart.setVisibility(View.GONE);
        mgpInputBincard.setVisibility(View.GONE);
        mgpButtonIncDec.setVisibility(View.GONE);

        blnShowgpSelectLocation=false;
        blnShowgpSelectPart=false;
        blnShowgpCountPart=false;
        blnShowgpInputBincard=false;


        //เป็นปุ่มที่ใช้ร่วมกันทั้ง 4 หน้า
        mbtnSelectPart.setVisibility(View.GONE);
        metScanLocation.setEnabled(false );
        metScanCountPartnid.setEnabled(false );

    }

    private void SendKeyDown(Integer intrepeat){
        long now = SystemClock.uptimeMillis();
        BaseInputConnection mInputConnection = new BaseInputConnection(findViewById(R.id.etScanLocation), true);
        KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN, 0);
        for (int i = 0; i <intrepeat; i += 1) {
            mInputConnection.sendKeyEvent(down);
        }
    }


    private void ShowSelectLocation() {
        Invisibale_AllGroup();
        mgpSelectLocation.setVisibility(View.VISIBLE);
        blnShowgpSelectLocation=true;
        metScanLocation.setEnabled(true );
        metScanLocation.setHint("Location");
        mbtnSelectPart.setText("SELECT PART");
        mtxtListPart.setText("Select Part");
        createSpinnerLocation(strGenNoStrings, strWeekNoStrings, strIssuePeriodStrings);
        metScanLocation.requestFocus();
    }

    private void ShowSelectPart() {
        Invisibale_AllGroup();
        mgpSelectPart.setVisibility(View.VISIBLE);
        blnShowgpSelectPart=true;
        metScanLocation.setEnabled(false);
        metScanCountPartnid.setHint("Scan Part");
        mbtnSelectPart.setText("COUNT PART");
        mtxtListPart.setText("Select Part");
        createListViewPart(strGenNoStrings, strWeekNoStrings, strIssuePeriodStrings, strLocationStrings);
        if (blnListPartEmpty ) {
            //strAlertMessage="ไม่พบสินค้าที่ต้องตรวจนับใน Lacation " + strLocationStrings + " นี้นะครับ" ;
            //ShowAlertDialog(R.string.alertdialog_error,getResources().getString(R.string.alertdialog_error),strAlertMessage,R.drawable.alertdialog_ic_error);

            ShowSelectLocation();
            metScanLocation.requestFocus();
        }
    }

    private void ShowCountPart() {
        Boolean blnBackFromBincard;
        blnBackFromBincard=blnShowgpInputBincard;
        Invisibale_AllGroup();
        mgpCountPart.setVisibility(View.VISIBLE);
        blnShowgpCountPart=true;
        metScanCountPartnid.setEnabled(true);
        mbtnSelectPart.setVisibility(View.VISIBLE);


        mbtnSelectPart.setText("BINCARD"); //เปลี่ยนเป็น Bincard แทน
        mtxtShowScanQty.setText("0");
        //ถ้าไม่ได้กลับมาจากหน้า Input Bincard ก็ให้เคลียค่าก่อน
        if(!blnBackFromBincard) {
        //ทำการดึงจำนวนจาก Tmp_RFPhysicalcount
              Execute_Get_Tmp_RFPhysicalCount();
//            mtxtShowCountQty.setText("0");
//            mtxtShowCountDF.setText("0");
//            mtxtShowCountSDM.setText("0");
//            mtxtShowCountDM.setText("0");
        }
        metScanCountPartnid.requestFocus();
        SendKeyDown(2);
    }

    private void ShowInputBincard() {
        Invisibale_AllGroup();
        mgpInputBincard.setVisibility(View.VISIBLE);
        blnShowgpInputBincard=true;
        mbtnSelectPart.setVisibility(View.VISIBLE);
        mbtnSelectPart.setText("CONFIRM"); //เปลี่ยนเป็น Confirm แทน
        mtxtInputBincardQty.setText("");
        mtxtInputBincardDF.setText("");
        mtxtInputBincardSDM.setText("");
        mtxtInputBincardDM.setText("");

        mtxtInputBincardQty.requestFocus();
        SendKeyDown(2);
    }


    private void SetEvent() {
        //ทำการตรวจสอบว่าอยู่หน้าไหนด้วย
        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blnShowgpSelectLocation) {
                    finish();
                    overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

                } else if (blnShowgpSelectPart) {
                    //ทำการลบที่ Hold ไว้ด้วย
                    Delete_Tmp_RFPhysicalCount("ALL");
                    Delete_Tmp_RFPartDigitNo("ALL");
                    ShowSelectLocation();
                    mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(strLocationStrings.trim()));
                } else if (blnShowgpCountPart) {
                    //Reset เพื่อนับใหม่
                    Delete_Tmp_RFPhysicalCount("PART");
                    Delete_Tmp_RFPartDigitNo("PART");
                    ShowSelectPart();
                } else {
                    ShowCountPart();
                }
            }
        });

        mbtnHold_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSelectPart();
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
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                if(s.length()==1 || blnManualInput) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //เพื่อจัดช่วงว่างด้านหลังที่เพิ่มมาออก

                metScanLocation.requestFocus();
                if (metScanLocation.isEnabled()) {
                    if (!metScanLocation.getText().toString().isEmpty() && !metScanLocation.getText().toString().equals("")) {
                        metScanLocation.setText("");
                        blnManualInput = false;
                        metScanLocation.setHint(s.toString().trim());

                        mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(s.toString().trim()),false);
                        strLocationStrings =s.toString().trim();
                        ShowListPart();
                    }
                }
                else {
                    if (!s.toString().equals("") ) {
                        if (blnShowgpCountPart){
//                            strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
//                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanLocation.setText("");
                            metScanCountPartnid.requestFocus();
                            SendKeyDown(2);
                        }
                    }
                }
            }
        });



        metScanCountPartnid.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanCountPartnid.setText(metScanCountPartnid.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanCountPartnid.addTextChangedListener(new TextWatcher() {

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

                if (metScanCountPartnid.isEnabled()) {
                    if (!metScanCountPartnid.getText().toString().isEmpty() && !metScanCountPartnid.getText().toString().equals("")) {
                        metScanCountPartnid.setText("");
                        blnManualInput = false;
                        metScanCountPartnid.setHint(s.toString());

                        if (s.length() == 17 || s.length() == 18) {
                            arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                            strPartNid = arrGetDescFromScanBarcode[0];
                            strPartStatus = arrGetDescFromScanBarcode[1]; //0-สมบูรณ์ 1-ตัวไม่สามบูรณ์  2-ตัวเสีย 3-ตัวScrap
                            strDigitNo = arrGetDescFromScanBarcode[2];

                            if (strPartNid.equals(mtxtShowPartnid.getText().toString())) {
                                //ยอมให้ผ่านได้ถ้าตั้งใจยิงซ้ำ 3 ครั้ง
                                if (!Check_ScanDuplicate(s.toString(), strPartNid)) {
                                    //เคลียจำนวนการ Scan ซ้ำออกด้วย
                                    intCheckScanDuplicate = 0;
                                    dblPackQty = Double.parseDouble(mtxtShowPackQty.getText().toString());
                                    dblCountQty = Double.parseDouble(mtxtShowCountQty.getText().toString());
                                    dblCountDF = Double.parseDouble(mtxtShowCountDF.getText().toString());
                                    dblCountSDM = Double.parseDouble(mtxtShowCountSDM.getText().toString());
                                    dblCountDM = Double.parseDouble(mtxtShowCountDM.getText().toString());

                                    //ต้องแปลง Double เป็น Integer

                                    if (strPartStatus.equals("0")) {
                                        mtxtShowScanQty.setText(String.valueOf(dblPackQty));
                                        dblCountQty = dblCountQty + dblPackQty;
                                        mtxtShowCountQty.setText(String.valueOf(dblCountQty));
                                    } else if (strPartStatus.equals("1")) {
                                        dblPackQty = 1;
                                        mtxtShowScanQty.setText(String.valueOf(dblPackQty));
                                        dblCountDF = dblCountDF + dblPackQty;
                                        mtxtShowCountDF.setText(String.valueOf(dblCountDF));
                                    } else if (strPartStatus.equals("2")) {
                                        dblPackQty = 1;
                                        mtxtShowScanQty.setText(String.valueOf(dblPackQty));
                                        dblCountDM = dblCountDM + dblPackQty;
                                        mtxtShowCountDM.setText(String.valueOf(dblCountDM));
                                    } else if (strPartStatus.equals("3")) {
                                        dblPackQty = 1;
                                        mtxtShowScanQty.setText(String.valueOf(dblPackQty));
                                        dblCountSDM = dblCountSDM + dblPackQty;
                                        mtxtShowCountSDM.setText(String.valueOf(dblCountSDM));
                                    } else {
                                        dblPackQty = 0;
                                        mtxtShowScanQty.setText(String.valueOf(dblPackQty));
                                        strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                    }
                                    //ทำการเพิ่มจำนวนใน Temp
                                    Execute_Update_Tmp_RFPhysicalCount();
                                    //ไม่ตจ้องแสดงทศนิยม
                                    mtxtShowPackQty.setText(String.format("%.0f",Double.parseDouble(mtxtShowPackQty.getText().toString())));
                                    mtxtShowScanQty.setText(String.format("%.0f",Double.parseDouble(mtxtShowScanQty.getText().toString())));
                                    mtxtShowCountQty.setText(String.format("%.0f",Double.parseDouble(mtxtShowCountQty.getText().toString())));
                                    mtxtShowCountDF.setText(String.format("%.0f",Double.parseDouble(mtxtShowCountDF.getText().toString())));
                                    mtxtShowCountDM.setText(String.format("%.0f",Double.parseDouble(mtxtShowCountDM.getText().toString())));
                                    mtxtShowCountSDM.setText(String.format("%.0f",Double.parseDouble(mtxtShowCountSDM.getText().toString())));
                                }
                            }
                            else {
                                    dblPackQty = 0;
                                    mtxtShowScanQty.setText(String.valueOf(dblPackQty));
                                    strAlertMessage = "คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการตรวจนับนะครับ";
                                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                }
                                mgpButtonIncDec.setVisibility(View.GONE);
                                if (dblPackQty > 1.0) {
                                    mgpButtonIncDec.setVisibility(View.VISIBLE);
                                }
                            }
                        else {
                                if (!s.toString().isEmpty()) {
                                    strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                    metScanCountPartnid.setText("");
                                }
                            }
                        }
                    }
                else{
                    if (!s.toString().equals("") ) {
                        if ( blnShowgpInputBincard) {
                            mtxtInputBincardQty.requestFocus();
                        }
                    }
                }
            }
        });



        mbtnIncQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int   intScanQty,intPackQty,intCountQty  ;
                intCountQty = Integer.parseInt(mtxtShowCountQty.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanQty.getText().toString());
                intPackQty = Integer.parseInt(mtxtShowPackQty.getText().toString());

                intScanQty = intScanQty + 1;
                if (intScanQty > intPackQty) {
                    intScanQty = 0;
                    intCountQty = intCountQty - intPackQty; //ลดเลงให้มีค่าเท่ากับยังไม่ได้ Scan
                } else {
                    intCountQty = intCountQty + 1; //เพิ่มทีละ 1
                }
                mtxtShowScanQty.setText(String.valueOf(intScanQty));
                mtxtShowCountQty.setText(String.valueOf(intCountQty));

            }
        });

        mbtnDecQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int   intScanQty ,intCountQty,intPackQty ;
                intCountQty = Integer.parseInt(mtxtShowCountQty.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanQty.getText().toString());
                intPackQty = Integer.parseInt(mtxtShowPackQty.getText().toString());

                intScanQty = intScanQty - 1;
                if (intScanQty < 0) {
                    intScanQty =  intPackQty;
                    intCountQty = intCountQty + intPackQty; //ลดเลงให้มีค่าเท่ากับยังไม่ได้ Scan
                } else {
                    intCountQty = intCountQty - 1; //เพิ่มทีละ 1
                }
                mtxtShowScanQty.setText(String.valueOf(intScanQty));
                mtxtShowCountQty.setText(String.valueOf(intCountQty));

            }
        });

        mbtnSelectPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowListPart();
            }
        });

    }

    private void ShowListPart() {
        if (mbtnSelectPart.getText().toString().trim().equals("SELECT PART")) {
            ShowSelectPart();
        } else if (mbtnSelectPart.getText().toString().trim().equals("BINCARD")) {
            ShowInputBincard();
        }else if (mbtnSelectPart.getText().toString().trim().equals("CONFIRM")) {
            strAlertMessage="คุณมั่นว่าป้อนข้อมูลตรวจนับ\n ถูกต้องแล้วใช่หรือไม่" ;
            ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
        } else {
            //Count Part
            if (mtxtListPart.getText().toString().trim().toUpperCase().equals("SELECT PART")) {
                strAlertMessage="กรุณาทำการเลือก Part ที่ต้องการตรวจนับก่อนนะครับ";
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                metScanLocation.requestFocus();
            } else {
                GetProductDetail(mtxtListPart.getText().toString().trim());
                ShowCountPart();
            }
        }
    }



    private void createSpinnerGenNo() {
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetPhysicalCount();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", "VW_Mobile_PhysicalGenNo", "strField", "GenNo as ResultReturn", "strCondition", "GenNo<>''", strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrGenNoStrings = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrGenNoStrings[i] = jsonObject.getString("ResultReturn");
            }//for

            Spinner nameSpinner = (Spinner) findViewById(R.id.spnGenno);
            ArrayAdapter<String> nameArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrGenNoStrings);
            nameSpinner.setAdapter(nameArrayAdapter);
            nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strGenNoStrings = arrGenNoStrings[i];
                    createSpinnerWeekNo(strGenNoStrings);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strGenNoStrings = arrGenNoStrings[0];
                    createSpinnerWeekNo(strGenNoStrings);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Gen No ==> " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//createSpinnerGenNo


    private void createSpinnerWeekNo(final String strGenNo) {

        String strCondition = "GenNo='" + strGenNo + "'";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_GetPhysicalCount();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", "VW_Mobile_PhysicalCount", "strField", "WeekNo as ResultReturn", "strCondition", strCondition,strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrWeekNoStrings = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrWeekNoStrings[i] = jsonObject.getString("ResultReturn");
            }//for

            Spinner nameSpinner = (Spinner) findViewById(R.id.spnWeekno);
            ArrayAdapter<String> nameArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrWeekNoStrings);
            nameSpinner.setAdapter(nameArrayAdapter);
            nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strWeekNoStrings = arrWeekNoStrings[i];
                    createSpinnerIssuePeriod(strGenNo, strWeekNoStrings);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strWeekNoStrings = arrWeekNoStrings[0];
                    createSpinnerIssuePeriod(strGenNo, strWeekNoStrings);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner WeekNo ==> " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//createSpinnerWeekno


    private void createSpinnerIssuePeriod(final String strGenNo, final String strWeekNo) {

        String strCondition = "GenNo='" + strGenNo + "' and WeekNo='" + strWeekNo + "'";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_GetPhysicalCount();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", "VW_Mobile_PhysicalCount", "strField", "IssTimePeriod as ResultReturn", "strCondition", strCondition, strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrIssuePeriodStrings = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrIssuePeriodStrings[i] = jsonObject.getString("ResultReturn");
            }//for

            Spinner nameSpinner = (Spinner) findViewById(R.id.spnIssuePeriod);
            ArrayAdapter<String> nameArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrIssuePeriodStrings);
            nameSpinner.setAdapter(nameArrayAdapter);
            nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strIssuePeriodStrings = arrIssuePeriodStrings[i];
                    createSpinnerLocation(strGenNo, strWeekNo, strIssuePeriodStrings);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strWeekNoStrings = arrWeekNoStrings[0];
                    createSpinnerLocation(strGenNo, strWeekNo, strIssuePeriodStrings);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Issue Period ==> " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//createSpinnerIssuePeriod


    private void createSpinnerLocation(final String strGenNo, final String strWeekNo, final String strIssTimePeriod) {

        String strCondition = "GenNo='" + strGenNo + "' and WeekNo='" + strWeekNo + "' and IssTimePeriod='" + strIssTimePeriod + "'";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+ myConstant.urlMobile_GetPhysicalCount();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", "VW_Mobile_PhysicalCount", "strField", "LCCode as ResultReturn", "strCondition", strCondition, strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrLocationStrings = new String[jsonArray.length()];

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
                     //ให้ใช่ค่าตอน Scan แทน
                    // strLocationStrings = arrLocationStrings[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strLocationStrings = arrLocationStrings[0];
                }

            });
        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Loation ==> " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }//createSpinnerWeekno


    private void createListViewPart(final String strGenNo, final String strWeekNo, String strIssTimePeriod, String strLocation) {

        blnListPartEmpty=false;
        ListView listView = findViewById(R.id.lvPartnid);
        String tag = "6SepV2";

        strTableName= " VW_Mobile_PhysicalCount ";
        strField="PartNid,Description"  ;
        strCondition = "GenNo='" + strGenNo + "' and WeekNo=" + strWeekNo + " and IssTimePeriod= " + strIssTimePeriod + " and LCCode ='" + strLocation + "'";

        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+ myConstant.urlMobile_GetListPart();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName",strTableName, "strField", strField, "strCondition", strCondition, strURL);
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
            }

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListPartNidStrings[i] = jsonObject.getString("PartNid");
                arrDescription[i] = jsonObject.getString("Description");
                Log.d(tag, "PartNid [" + i + "] ==> " + arrListPartNidStrings[i]);
            }//for

            ListPartPhysicalCountAdapter listPartPhysicalCountAdapter = new ListPartPhysicalCountAdapter(getApplicationContext(), arrListPartNidStrings, arrDescription);
            listView.setAdapter(listPartPhysicalCountAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                    //เป็นตัวบอกว่าได้ทำการเลือก Part ที่ต้องการตรวจนับแล้ว
                    mtxtListPart.setText(arrListPartNidStrings[i].toString());
                    ShowListPart();

                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView


    private void GetProductDetail(String strPartNid) {
        String tag = "6SepV2";
        String strCondition;
        try {
            ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_GetProductDetail();
            getProductDesc.execute(strDataBaseName, strPartNid, strURL);

            String resultJSON = getProductDesc.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "";
                strAlertMessage="Not Found Data !!!" ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsProduct = gson.fromJson(resultJSON.toString(), Product.class);
                mtxtShowPartnid.setText(clsProduct.PartNID);
                mtxtShowPartno.setText(clsProduct.PartNo);
                mtxtShowPartdesc.setText(clsProduct.PartDes);

                mtxtShowPackQty.setText(clsProduct.PackQty);
                mtxtShowPackQty.setText(String.format("%.0f",Double.parseDouble(mtxtShowPackQty.getText().toString())));
                mtxtShowWHCode.setText(clsProduct.WHcode);
                mtxtShowLCCode.setText(clsProduct.LCcode);


                //ใส่ข้อมูลเข้าใน Tmp_RFPhysicalCount เพื่อไม่ให้คนอื่นเอาไปตรวจนับได้
                Execute_Insert_Tmp_RFPhysicalCount();

            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void Execute_Insert_Tmp_RFPhysicalCount() {
        String tag = "6SepV2";

        //ShowToast(strTmpTime);
        try {
            ExecuteSevenParameter executeSevenParameter = new ExecuteSevenParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_Insert_Tmp_RFPhysicalCount();
            executeSevenParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime", strTmpTime,
                    "strSTfCode", userLogin.STFcode.toString(),
                    "strGenNo", strGenNoStrings,
                    "strWeekNo", strWeekNoStrings,
                    "strLCCode", strLocationStrings,
                    "strPartNid", mtxtShowPartnid.getText().toString(),
                    strURL);
            String resultJSON = executeSevenParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Insert_Tmp_RFPhysicalCount() ;
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


    private void Execute_Get_Tmp_RFPhysicalCount() {
        String tag = "6SepV2";

        //ShowToast(strTmpTime);
        try {
            ExecuteSevenParameter executeSevenParameter = new ExecuteSevenParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_Get_Tmp_RFPhysicalCount();
            executeSevenParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime", strTmpTime,
                    "strSTfCode", userLogin.STFcode.toString(),
                    "strGenNo", strGenNoStrings,
                    "strWeekNo", strWeekNoStrings,
                    "strLCCode", strLocationStrings,
                    "strPartNid", mtxtShowPartnid.getText().toString(),
                    strURL);
            String resultJSON = executeSevenParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "";
                strAlertMessage="Not Found Data !!!" ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsTmp_RFPhysicalCount = gson.fromJson(resultJSON.toString(), Tmp_RFPhysicalCount.class);
                mtxtShowCountQty.setText(String.format("%.0f",Double.parseDouble(clsTmp_RFPhysicalCount.Qty)));
                mtxtShowCountDF.setText(String.format("%.0f",Double.parseDouble(clsTmp_RFPhysicalCount.Defect)));
                mtxtShowCountSDM.setText(String.format("%.0f",Double.parseDouble(clsTmp_RFPhysicalCount.Scrap)));
                mtxtShowCountDM.setText(String.format("%.0f",Double.parseDouble(clsTmp_RFPhysicalCount.Damage)));
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }
    
    

    public boolean Check_ScanDuplicate(String strPartDigitNo,  String strPartNid) {
        String tag = "6SepV2";
        Boolean resultBoolean = Boolean.FALSE;
        try {
            strTableName="Tmp_RFPartDigitNo";
            strField="PartDigitNo";
            strCondition = "TmpTime ='" + strTmpTime + "' and PartDigitNo='" + strPartDigitNo + "' and PartNid='" + strPartNid + "'";
            strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);

            if (strReturnValue.equals("")){
                //ทำการใส่ข้อมูลเข้าไปเพื่อไม่ให้ทำการ Scan ซ้ำื
                Execute_Insert_Tmp_RFPartDigitNo( strPartDigitNo, strPartNid);
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



    private void Execute_Insert_Tmp_RFPartDigitNo(final String strPartDigitNo,final String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameterParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Insert_Tmp_RFPartDigitNo();
            executeFiveParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strDocNo","PhyCnt",
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


    private void Delete_Tmp_RFPhysicalCount(String strOption ){
        String tag = "6SepV2";
        strTableName="Tmp_RFPhysicalCount";
        if (strOption.equals("ALL")) {
            strCondition = "TmpTime='" + strTmpTime + "' ";
        }
        else{ // เคลียเฉพาะ Part เนื่องจากอาจมีการ Hold ไว้หลาย Part
            strCondition = "TmpTime='" + strTmpTime + "' And PartNid='" + strPartNid +"'";
        }

         try {
             strURL=strServerAddress+ myConstant.urlMobile_DeleteRecord();
             strReturnValue = globalUtility.Delete_Record(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
            //for Not User Pacel
            if (strReturnValue.equals("")||(! strReturnValue.toString().toUpperCase().equals("SUCCESS"))) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+myConstant.urlMobile_DeleteRecord() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+myConstant.urlMobile_DeleteRecord() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void Delete_Tmp_RFPartDigitNo(String strOption ){
        String tag = "6SepV2";
        strTableName="Tmp_RFPartDigitNo";
        if (strOption.equals("ALL")) {
            strCondition = "TmpTime='" + strTmpTime + "' ";
        }
        else{ // เคลียเฉพาะ Part เนื่องจากอาจมีการ Hold ไว้หลาย Part
            strCondition = "TmpTime='" + strTmpTime + "' And PartNid='" + strPartNid +"'";
        }
        strURL=strServerAddress+ myConstant.urlMobile_DeleteRecord();

        try {
            strReturnValue = globalUtility.Delete_Record(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
            //for Not User Pacel
            if (strReturnValue.equals("")||(! strReturnValue.toString().toUpperCase().equals("SUCCESS"))) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+myConstant.urlMobile_DeleteRecord() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+myConstant.urlMobile_DeleteRecord() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    private void Execute_Update_Tmp_RFPhysicalCount(){
        String tag = "6SepV2";
        try {
            //ไม่สามารถส่งค่าว่างไปได้
            if (mtxtInputBincardQty.getText().equals("")||mtxtInputBincardQty.getText().toString().isEmpty()) {
                mtxtInputBincardQty.setText("0");
            }
            if (mtxtInputBincardDF.getText().equals("")||mtxtInputBincardDF.getText().toString().isEmpty()) {
                mtxtInputBincardDF.setText("0");
            }
            if (mtxtInputBincardSDM.getText().equals("")||mtxtInputBincardSDM.getText().toString().isEmpty()) {
                mtxtInputBincardSDM.setText("0");
            }
            if (mtxtInputBincardDM.getText().equals("")||mtxtInputBincardDM.getText().toString().isEmpty()) {
                mtxtInputBincardDM.setText("0");
            }


            ExecuteElevenParameter executeElevenParameter = new ExecuteElevenParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Update_Tmp_RFPhysicalCount();
            executeElevenParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strPartNid",mtxtShowPartnid.getText().toString(),
                    "strQty",mtxtShowCountQty.getText().toString(),
                    "strDF",mtxtShowCountDF.getText().toString(),
                    "strSDM",mtxtShowCountSDM.getText().toString(),
                    "strDM",mtxtShowCountDM.getText().toString(),
                    "strCardQty",mtxtInputBincardQty.getText().toString(),
                    "strCardDF",mtxtInputBincardDF.getText().toString(),
                    "strCardSDM",mtxtInputBincardSDM.getText().toString(),
                    "strCardDM",mtxtInputBincardDM.getText().toString(),
                    strURL);
            String resultJSON = executeElevenParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+myConstant.urlMobile_Update_Tmp_RFPhysicalCount() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (!strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+  myConstant.urlMobile_Update_Tmp_RFPhysicalCount() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }





    private void Execute_Update_PhysicalCount(){
        String tag = "6SepV2";
        try {
            //ไม่สามารถส่งค่าว่างไปได้
            if (mtxtInputBincardQty.getText().equals("")||mtxtInputBincardQty.getText().toString().isEmpty()) {
                mtxtInputBincardQty.setText("0");
            }
            if (mtxtInputBincardDF.getText().equals("")||mtxtInputBincardDF.getText().toString().isEmpty()) {
                mtxtInputBincardDF.setText("0");
            }
            if (mtxtInputBincardSDM.getText().equals("")||mtxtInputBincardSDM.getText().toString().isEmpty()) {
                mtxtInputBincardSDM.setText("0");
            }
            if (mtxtInputBincardDM.getText().equals("")||mtxtInputBincardDM.getText().toString().isEmpty()) {
                mtxtInputBincardDM.setText("0");
            }


            ExecuteElevenParameter executeElevenParameter = new ExecuteElevenParameter(getApplicationContext());
            strURL=strServerAddress+ myConstant.urlMobile_Update_PhysicalCount();
            executeElevenParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strPartNid",mtxtShowPartnid.getText().toString(),
                    "strQty",mtxtShowCountQty.getText().toString(),
                    "strDF",mtxtShowCountDF.getText().toString(),
                    "strSDM",mtxtShowCountSDM.getText().toString(),
                    "strDM",mtxtShowCountDM.getText().toString(),
                    "strCardQty",mtxtInputBincardQty.getText().toString(),
                    "strCardDF",mtxtInputBincardDF.getText().toString(),
                    "strCardSDM",mtxtInputBincardSDM.getText().toString(),
                    "strCardDM",mtxtInputBincardDM.getText().toString(),
                   strURL);
            String resultJSON = executeElevenParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+myConstant.urlMobile_Update_PhysicalCount() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+  myConstant.urlMobile_Update_PhysicalCount() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
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
                    //ย้ายไปทำใน Alert Dialog
                    Execute_Update_PhysicalCount();
                    ShowSelectPart();
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
