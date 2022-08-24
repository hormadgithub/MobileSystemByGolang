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
import android.widget.SeekBar;
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
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument;
import pneumax.mobilesystembygolaung.connected.ExecuteGetPartTubeDetail;
import pneumax.mobilesystembygolaung.connected.ExecuteGetPickPart_JobtubeDetail;
import pneumax.mobilesystembygolaung.connected.ExecuteGetProductDesc;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocumentAdapter;
import pneumax.mobilesystembygolaung.manager.ListPartJobtubeAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.PartTubeDetail;
import pneumax.mobilesystembygolaung.object.PickPartJobtubeDetail;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.ReturnValue;
import pneumax.mobilesystembygolaung.object.StaffLogin;

import static pneumax.mobilesystembygolaung.manager.GlobalVar.getDataBaseName;


public class PickingJobtubeActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    Product clsProduct;
    PickPartJobtubeDetail clsPickPartJobtubeDetail;


    PartTubeDetail clsPartTubeDetail;


    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL,strSumRealCut;

    ReturnValue clsReturnValue;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;


    //From Layout
    Button mbtnHold_Document, mbtnReset_Document,mbtnHold_Part, mbtnReset_Part;
    Button mbtnConfirmCutLength,mbtnCompletePickTubePart;
    CircularImageView mbtnSearch;
    ImageView mimgBackTop;
    EditText metScanDocument,metScanLocation,metSelectDocno, metScanPickTubePart;
    TextView mtvShowDocSelected,mtxtShowPickTubePartno;
    Group mgplvListDocument,mgpLocation,mgpScanPart;
    Boolean blnShowgplvListDocument,blnShowgpLocation,blnShowgpScanPart;
    SeekBar msbRealCut;
    // แสดงรายละเอียดการจัดสินค้า
    TextView mtxtShowPickTubeQty,mtxtShowPickTubeDstb,mtxtShowPickTubeDigitno,mtxtShowPickTubeLength,mtxtShowPickTubeFixdigit,mtxtInputRealCutLength,mtxtShowPickTubeUnit;
    TextView mtxtShowPickTubeQtyOut,mtxtShowPickTubeDstbOut,mtxtShowPickTubeDigitnoOut,mtxtShowPickTubeRealCutOut;

    Spinner mspnLocation;
    ArrayAdapter<String> AdapterSpinnerLocation;
    private String[] arrLocationStrings;
    private String  strLocationStrings;



    Boolean blnListPartEmpty,blnManualInput=false;
    View updateview;// above oncreate method
    ListView mlvListDocument, mlvListPartJobtube;

    //ตัวแปรสำหรับใช้ในการ Select ข้อมูล
    String strPickTubeDocType, strPickTubeTmpTime,strPickTubeDocno,strPickTubeLocation,strPickTubePartnid,
            strPickTubeDigitno,strPickTubeQty,strPickTubeDstbQty,strPickTubePackQty, strPickTubeDigitNoOut;
    String strParttube_Digitno,strParttube_Onhand,strParttube_DMLength,strParttube_RealQty;
    Boolean blnPickTubePartHaveLabel,blnPickTubeFixDigitNo,blnPickTubeCutDamage,blnPickTubeSplit,blnRequestAllTube;
    Boolean blnWaitInputRealCutLength;
    //เก็บไว้เพื่อบอกว่าเป็นการเรียก Alert Warning จกที่ไหนเำื่อให้ทำงานได้ถูกต้อง
    Boolean blnWarning_Reset,blnWarning_Hold,blnWarning_Confirm,blnWarning_Complete=false;

    Double  dblPickTubeQtyOut,dblPickTubeRealCutOut, dblPickTubeDstbQtyOut;
    Integer intPackQty, intPickTubeQtyOut, intPickTubeRealCutOut, intPickTubeDstbQtyOut;

    String strPartNid, strPartStatus, strDigitNo;
    Integer intCheckScanDuplicate=0; //ถ้า Scan ซ้ำัน 2 ครั้งแล้ว ครั้งที่ 3 จะทำว่า Digit ซ้ำกันจริงๆ ใช่หรือไม่



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_picking_jobtube);

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
        metSelectDocno=(EditText) findViewById(R.id.etSelectDocno);
        metScanLocation=(EditText) findViewById(R.id.etScanLocation);
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        mtxtShowPickTubePartno=(TextView) findViewById(R.id.txtShowPickTubePartno);


        metScanPickTubePart =(EditText) findViewById(R.id.etScanPickTubePart);
        mtxtInputRealCutLength=(TextView) findViewById(R.id.txtInputRealCutLength);
        msbRealCut=(SeekBar)findViewById(R.id.sbRealCut);


        mtxtShowPickTubeQty=(TextView) findViewById(R.id.txtShowPickTubeQty);
        mtxtShowPickTubeDstb=(TextView) findViewById(R.id.txtShowPickTubeDstbQty);
        mtxtShowPickTubeDigitno=(TextView) findViewById(R.id.txtShowPickTubeDigitno);
        mtxtShowPickTubeLength=(TextView) findViewById(R.id.txtShowPickTubeLength);

        mtxtShowPickTubeQtyOut=(TextView) findViewById(R.id.txtShowPickTubeQtyOut);
        mtxtShowPickTubeDstbOut=(TextView) findViewById(R.id.txtShowPickTubeDstbQtyOut);
        mtxtShowPickTubeDigitnoOut=(TextView) findViewById(R.id.txtShowPickTubeDigitnoOut);
        mtxtShowPickTubeFixdigit=(TextView) findViewById(R.id.txtShowPickTubeFixdigit);
        mtxtShowPickTubeUnit=(TextView) findViewById(R.id.txtShowPickTubeUnit);

        mtxtShowPickTubeRealCutOut=(TextView) findViewById(R.id.txtShowPickTubeRealCutOut);


        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpLocation=(Group) findViewById(R.id.gpLocation);
        mspnLocation = (Spinner) findViewById(R.id.spnLocation);
        mgpScanPart=(Group) findViewById(R.id.gpScanPickTubePart);

        mlvListPartJobtube =(ListView) findViewById(R.id.lvListPart);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);
        mbtnHold_Document =(Button) findViewById(R.id.btnHold_Document);
        mbtnReset_Document =(Button) findViewById(R.id.btnReset_Document);
        mbtnHold_Part =(Button) findViewById(R.id.btnHold_Part);
        mbtnReset_Part =(Button) findViewById(R.id.btnReset_Part);
        
        mbtnConfirmCutLength=(Button) findViewById(R.id.btnConfirmCutLength);
        mbtnCompletePickTubePart =(Button) findViewById(R.id.btnCompletePickTubePart);

        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }


    private void InitializeData() {
        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDocument.setAdapter(null);
        mlvListPartJobtube.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListPartJobtube.setAdapter(null);

        mbtnSearch.setImageResource(R.drawable.ic_search_blue);
        metScanDocument.setEnabled(true);

        mbtnSearch.setVisibility(View.VISIBLE);
        mbtnSearch.setEnabled(true);
        mbtnCompletePickTubePart.setEnabled(false);
        mbtnConfirmCutLength.setEnabled(false);

        msbRealCut.setEnabled(false);
        SetSeekbarState();

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

        ResetAllWarning();
    }


    private void ClearData() {
        //ทำการกำหนดค่าใได้เป็นว่างก่อน
        strPickTubeTmpTime="";
        strPickTubeDocno="";
        strPickTubeLocation="";
        //เพื่อให้ทำการรอ Scan location ใหม่
        metScanLocation.setHint("Location");
        strPickTubePartnid="";
        strPickTubeDigitno="";
        strPickTubeQty="0";
        strPickTubeDstbQty="1";

        metSelectDocno.setText("");
        mtvShowDocSelected.setText("");
    }



    private void ResetAllWarning() {
        blnWarning_Hold=false;
        blnWarning_Reset=false;
        blnWarning_Confirm=false;
        blnWarning_Complete=false;
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

                if (! strPickTubeDocno.equals("")) {
                    //Clear_Tmp_Rfcheckout_Jobtube(strPickTubeDocno,strPickTubePartnid);
                    if (!strPickTubePartnid.equals("")){
                        Execute_Hold_Reset_Picking_Jobtube(strPickTubeDocno,strPickTubePartnid,"H");
                    }
                    else {
                        Execute_Hold_Reset_Picking_Jobtube(strPickTubeDocno,"","R");
                    }

                    mlvListPartJobtube.setAdapter(null);
                }
                //ยังไม่ได้เลือก Document
                if (strPickTubeDocno.equals("") ||  blnShowgplvListDocument) {
                    finish();
                    mlvListPartJobtube.setAdapter(null);
                    overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

                }
                else if (blnShowgpLocation){
                    InitializeData();
                }
                else if (blnShowgpScanPart){
                    ShowSelectLocation(strPickTubeDocno);
                }

            }
        });


        mtxtShowPickTubeDigitnoOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPartTubeDetail();
                strAlertMessage=("Onhand = "+ strParttube_Onhand + "\n DMLength = "+strParttube_DMLength+
                                "\n ตัดไปแล้ว = "+strSumRealCut+"\n ความยาวที่สามารขายได้ "+strParttube_RealQty);


                ShowAlertDialog(R.string.alertdialog_success,"Digitno. "+mtxtShowPickTubeDigitnoOut.getText().toString().trim(),strAlertMessage, R.drawable.alertdialog_ic_success);

            }
        });


        msbRealCut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //ทำการกำหนดค่ามากสุดที่สามารถเลื่อนได้
                //แผนก ATM มีค่าปลายเพลาเลือกได้ถึง 10 แผนกอื่นมีค่่าใบเลื่อยประมาณ 2
                if (!strPickTubePartnid.subSequence(0,1).equals("M") && progress>2){
                    progress=2;
                }
                mtxtInputRealCutLength.setText(String.valueOf(progress+Integer.parseInt(mtxtShowPickTubeLength.getText().toString())));
                //mbtnConfirmCutLength.setEnabled(blnWaitInputRealCutLength && (!mtxtShowPickTubeLength.getText().toString().equals(mtxtInputRealCutLength.getText().toString()) || blnPickTubeFixDigitNo ));
                //21-04-2021 ต้องการให้ Confirm ได้เลยไมต้องมีค่าใบเลื่อย เช่น ลูกค้าต้องการ 400 cm แล้วไม่ได้ Fix digit แล้วท่อนนั้นมีความยาวเต็มท่อน 400cm พอดี
                // หรือ ลูกค้าต้องการให้ตัด 59.5 cm  แต่ไม่สามารถป้อนได้ ต้องป้อนเป็น 60cm แต่ใช้ Remark บอกว่าให้ตัด 59.5 cm ทำให้ตัดจริงจะตัด 60 cm
                msbRealCut.setEnabled(blnWaitInputRealCutLength && !blnRequestAllTube);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                            strPickTubeDocno = strScan;

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
                            strAlertMessage = "กรุณาเลือก Part ที่ต้องการจัด \n แล้วเลือก Select นะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                        }

                        else  if ( blnShowgpLocation) {
                            strAlertMessage = "กรุณาทำการ Scan Location ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanLocation.requestFocus();
                        }

                        else  if ( blnShowgpScanPart) {
                            strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanPickTubePart.requestFocus();
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
                s = s.toString().toUpperCase().trim();

                if (blnShowgpLocation) {
                    if (!metScanLocation.getText().toString().isEmpty() && !metScanLocation.getText().toString().equals("")) {
                        metScanLocation.setText("");
                        blnManualInput = false;
                        metScanLocation.setHint(s.toString().trim());
                        strPickTubeLocation=metScanLocation.getHint().toString().trim();
                        mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(s.toString().trim()));
                        createListViewPart(strPickTubeDocno,strPickTubeLocation);

                        if (blnListPartEmpty){
                            strAlertMessage = "ไม่พบรายการสินค้าที่ต้องจัด \n ใน Location ที่ Scan นะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    }
                }
                else {
                    if (!s.toString().equals("") ) {
                        if ( blnShowgplvListDocument) {
                            strAlertMessage = "กรุณาเลือก Part ที่ต้องการ \n แล้วเลือก Select นะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanLocation.setText("");
                        }

                        else  if ( blnShowgpScanPart) {
                            strAlertMessage = "กรุณาเลือกช่อง Scan Part ก่อนนะครับ.";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanLocation.setText("");
                            metScanPickTubePart.requestFocus();
                        }
                    }
                }
            }
        });



        metScanPickTubePart.setOnKeyListener(new View.OnKeyListener() {
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
                        metScanPickTubePart.setText(metScanPickTubePart.getText().toString()+" ");
                        return true;
                    }
                    return false;
                }
            });

            metScanPickTubePart.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                //if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                //ระบบเดิมสามารถป้อนเลขที่เอกสารได้
                if((s.length()==1 || blnManualInput)) {
                        blnManualInput = true;
                        return;
                    }
                    s = s.toString().toUpperCase().trim();

                    //ทำให้สามารถรับค่าแบบ manual ได้
                    ProcessInputParttube(s.toString().trim());
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
                Hold_Picking_Jobtube_Document();
            }
        });


        mbtnReset_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Picking_Jobtube_Document();
            }
        });

        mbtnHold_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hold_Picking_Jobtube_Part();
            }
        });


        mbtnReset_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Picking_Jobtube_Part();
            }
        });

        
        mbtnConfirmCutLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ทำการตรวจสอบว่าป้อนข้อมูลถูกต้องตามเงือนไขหรือไม่
//                ResetAllWarning();
//                blnWarning_Confirm=true;
//                strAlertMessage="คุณป้อนจำนวนความยาวที่ตัดถูกต้องแล้วใช่หรือไม่" ;
//                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
                //ลดการยืนยัน
                ConfirmCutLength();
            }
        });

        mbtnCompletePickTubePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetAllWarning();
                blnWarning_Complete=true;
                Execute_Mobile_Confirm_PickPart_Jobtube(strPickTubeTmpTime,strPickTubeDocno,strPickTubePartnid);
//                strAlertMessage="คุณจัดสินค้ารายการนี้ครบแล้วใช่หรือไม่" ;
//                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
            }
        });
    }

    private void ConfirmCutLength (){
        blnWaitInputRealCutLength=false;
        metScanPickTubePart.setEnabled(!blnWaitInputRealCutLength);
        mbtnConfirmCutLength.setEnabled(blnWaitInputRealCutLength);

        msbRealCut.setEnabled(blnWaitInputRealCutLength && !blnRequestAllTube);
        SetSeekbarState();
        Integer intSumRealCutLength= Integer.parseInt(mtxtShowPickTubeRealCutOut.getText().toString());
        Integer intInputRealCutLength= Integer.parseInt(mtxtInputRealCutLength.getText().toString());
        //เพิ่มความยาวที่ตัดจริง
        intSumRealCutLength =intSumRealCutLength+intInputRealCutLength;
        mtxtShowPickTubeRealCutOut.setText(String.valueOf(intSumRealCutLength));
        //ใส่ข้อมูลเข้าไปใน  Tmp_RFPartTube
        Execute_Insert_Tmp_RFPartTube(strPickTubeDocno,strPartNid, strPickTubeDigitno,strPickTubeQty,strPickTubeDstbQty, strPickTubeDigitNoOut,mtxtInputRealCutLength.getText().toString().trim());

        metScanPickTubePart.requestFocus();
        msbRealCut.setProgress(0);

        //ต้องเอาไว้ด้านล่าง Setprogress
        CheckPickTubeComplete();
    }


    private void CheckDocumentFromScan(String strDocno){
        strTableName="VW_Mobile_ListDocNo_Picking_Jobtube";
        strField="Docno";
        strCondition="Docno='" +strDocno + "'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue=globalUtility.Find_Record(getApplicationContext(), strDataBaseName,strTableName,strField,strCondition,strURL);
        if (!strReturnValue.trim().equals("")) {
            Execute_Mobile_Crt_Tmp_RFCheckOut_Picking(strDocno);
            ShowSelectLocation(strDocno);
        }
        else{
            mtvShowDocSelected.setText("Not Found Document");
            strPickTubeDocno ="";
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
//        metScanLocation.requestFocus();
//        SendKeyDown(2);
        blnShowgpLocation= true;
        strPickTubePartnid="";
        mtvShowDocSelected.setText(strPickTubeDocno);
        //เช็คว่าเป็นการตัดแยกท่อน หรือ ตัดส่วนที่เสียออก
        //ัดส่วนที่เสียออก
        strTableName="Jobtube";
        strCondition="JTNo='" + strPickTubeDocno + "' And SttCode='90'";
        strURL=strServerAddress+myConstant.urlMobile_CountRecord();
        blnPickTubeCutDamage=(! globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL).equals("0"));
        //แยกท่อน
        strCondition="JTNo='" + strPickTubeDocno +"' And  SACode ='STOCK ' and JTSplit='Y'";
        blnPickTubeSplit=(! globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL).equals("0"));
        createSpinnerLocation(strDocno);
        metScanLocation.requestFocus();
        SendKeyDown(2);
    }

    private void ShowScanPart(final String strPickTubePartnid,final String strPickTubeDigitno) {
        String strExecute="";
        Integer intRealCutOut;
        Invisible_AllGroup();
        mtvShowDocSelected.setText(strPickTubePartnid);
        GetProductDetail();
        mgpScanPart.setVisibility(View.VISIBLE);
//        metScanPickTubePart.requestFocus();
//        SendKeyDown(2);
        GetPickPart_JobtubeDetail();
        metScanPickTubePart.setHint("Scan Partid");
        metScanPickTubePart.setEnabled(true);
        blnWaitInputRealCutLength=false;
        mbtnConfirmCutLength.setEnabled(false);

        metScanPickTubePart.requestFocus();
        SendKeyDown(2);

        //ทำการหาจำนวนที่ทำการตัดแล้วใส่ให้ด้วย
        if(! mtxtShowPickTubeDstbOut.getText().toString().isEmpty() && ! mtxtShowPickTubeDstbOut.getText().toString().equals("0")){
            mbtnReset_Part.setEnabled(true);
            mbtnHold_Part.setEnabled(true);

            //หาจำนวนที่ตัดไปแล้วของ Part และ Digit ที่ Scan
            strTableName="Tmp_RFPartTube";
            strField="QtyOut";
            strCondition = "Docno='" + strPickTubeDocno + "' and Partnid='" + strPickTubePartnid + "' and DigitNo='" + mtxtShowPickTubeDigitno.getText().toString() +"'";
            strURL=strServerAddress+myConstant.urlMobile_SumValue();
            strSumRealCut = globalUtility.SumValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
            mtxtShowPickTubeRealCutOut.setText(String.format("%.0f",Double.parseDouble(strSumRealCut)));

            //ทำการ update tmpdate ใน Tmp_RFPartTube ให้ด้วยเพื่อจะได้รู้ว่าเข้ามาทำ Digit นี้ล่าสุด
            if (! mtxtShowPickTubeRealCutOut.getText().equals("0")){
                strCondition = "Docno='" + strPickTubeDocno + "' and Partnid='" + strPickTubePartnid + "' and DigitNo='" + mtxtShowPickTubeDigitno.getText().toString() +"'";
                strExecute="Update Tmp_RFPartTube set TmpDate=GETDATE() where " + strCondition;
                strURL=strServerAddress+myConstant.urlspExecute();
                strReturnValue=globalUtility.Execute(getApplicationContext(),strDataBaseName,strExecute,strURL);
            }


            //ถ้าเข้ามาเแล้วรับครบแล้วให้ Confirm ได้เลย
            CheckPickTubeComplete();

        }

        blnShowgpScanPart=true;
        msbRealCut.setEnabled(blnWaitInputRealCutLength && !blnRequestAllTube);
        SetSeekbarState();

        metScanPickTubePart.requestFocus();
    }



    private void CheckPickTubeComplete() {
        Integer intQty,intQtyOut,intRealCutOut,intDstb,intDstbOut;

        intQty = Integer.parseInt(mtxtShowPickTubeQty.getText().toString());
        intQtyOut = Integer.parseInt(mtxtShowPickTubeQtyOut.getText().toString());
        intRealCutOut = Integer.parseInt(mtxtShowPickTubeRealCutOut.getText().toString());

        intDstb = Integer.parseInt(mtxtShowPickTubeDstb.getText().toString());
        intDstbOut = Integer.parseInt(mtxtShowPickTubeDstbOut.getText().toString());

        //ถ้าจำนวนมากกว่า หรือ ว่าเป็นการยกท่อนก็ให้ทำการ Complete
        if (intQtyOut > intQty ) {
            mtxtShowPickTubeQtyOut.setText(String.valueOf(intQty));
            intQtyOut = Integer.parseInt(mtxtShowPickTubeQtyOut.getText().toString());
            strAlertMessage = "คุณตัด Job\n เกินจำนวนที่ต้องการแล้วนะครับ";
            ShowAlertDialog(R.string.alertdialog_error, "Error! จัดสินค้าเกิน.", strAlertMessage, R.drawable.alertdialog_ic_error);
            //ให้ทำการ Scan partnid ต่อ
            blnWaitInputRealCutLength = false;
            mbtnConfirmCutLength.setEnabled(blnWaitInputRealCutLength || blnRequestAllTube);
            msbRealCut.setEnabled(blnWaitInputRealCutLength && !blnRequestAllTube);
            SetSeekbarState();
            metScanPickTubePart.setHint("Scan Partid");
        }

        mbtnCompletePickTubePart.setEnabled(false);
        mbtnReset_Part.setEnabled(false);
        mbtnHold_Part.setEnabled(false);

        //ทำการเพิ่มจำนวนทีจัดใน  Tmp_RFCheckOut
        if( ! blnWaitInputRealCutLength && ! mtxtInputRealCutLength.getText().toString().isEmpty() && ! mtxtInputRealCutLength.getText().toString().equals("") && ! mtxtInputRealCutLength.getText().toString().equals("0") ){
            Execute_Mobile_UPD_Tmp_RFCheckOut_Jobtube(mtxtShowPickTubeLength.getText().toString());
        }

        if (! blnWaitInputRealCutLength && (intQtyOut != 0 )) {
            if (  (intQtyOut <= intQty) ) {
                //กรณี Hold แล้วเข้ามา
                mbtnHold_Part.setEnabled(true);
                mbtnReset_Part.setEnabled(true);
            }
            if (String.valueOf(intQty).trim().equals(String.valueOf(intQtyOut).trim()) &&
                    String.valueOf(intDstb).trim().equals(String.valueOf(intDstbOut).trim()) &&
                    (intRealCutOut>=intQtyOut || blnPickTubeFixDigitNo ||  (blnPickTubeSplit || blnPickTubeCutDamage ) && intQtyOut == intQty)) {
                mbtnCompletePickTubePart.setEnabled(true);
                mbtnReset_Part.setEnabled(false);
                mbtnHold_Part.setEnabled(false);
                metScanPickTubePart.setEnabled(false);
            }
        }
        //เนื่องจากมีการตรวจสอบค่าที่ป้อนก่อนหน้า
        if (! blnWaitInputRealCutLength) {
            mtxtInputRealCutLength.setText("0");
        }
    }

    private void SetSeekbarState() {
        if (msbRealCut.isEnabled()){
            msbRealCut.setThumb(getResources().getDrawable(R.drawable.custom_seekbar_backgroud));
        }
        else {
            msbRealCut.setThumb(getResources().getDrawable(R.drawable.custom_seekbar_backgroud_disable));
        }
    }


    private void GetProductDetail() {
        String tag = "6SepV2";
        try {
            ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetProductDetail();
            getProductDesc.execute(getDataBaseName, strPickTubePartnid,strURL);

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

                mtxtShowPickTubePartno.setText(clsProduct.PartNo+" "+clsProduct.PartDes);
                strPickTubePackQty=  clsProduct.PackQty;
                blnPickTubePartHaveLabel=clsProduct.PrintLabel.toUpperCase().equals("Y");
                msbRealCut.setProgress(0);
                mtxtInputRealCutLength.setText("0");
            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }

    private void GetPickPart_JobtubeDetail() {
        String tag = "6SepV2";
        try {
            ExecuteGetPickPart_JobtubeDetail executeGetPickPart_JobtubeDetail = new ExecuteGetPickPart_JobtubeDetail(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetPickPart_JobtubeDetail();
            executeGetPickPart_JobtubeDetail.execute(strDataBaseName,strPickTubeDocno, strPickTubePartnid, strPickTubeDigitno, strURL);

            String resultJSON = executeGetPickPart_JobtubeDetail.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "";
                strAlertMessage="Not Found Data !!!" ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsPickPartJobtubeDetail = gson.fromJson(resultJSON.toString(),PickPartJobtubeDetail.class);

                GetPickTubeDocType(strPickTubeDocno);

                strPickTubeTmpTime=clsPickPartJobtubeDetail.TmpTime;

                //กำหนดไม่ให้แสดงทศนิยม
                mtxtShowPickTubeQty.setText(String.format("%.0f",Double.parseDouble(clsPickPartJobtubeDetail.Qty)));
                mtxtShowPickTubeDstb.setText(String.format("%.0f",Double.parseDouble(clsPickPartJobtubeDetail.DstbQty)));
                mtxtShowPickTubeDigitno.setText(clsPickPartJobtubeDetail.DigitNo);
                //เก็บค่าไว้เพื่อทำาการเปรียบเทียบตอน Update
                strPickTubeQty=mtxtShowPickTubeQty.getText().toString().trim();
                strPickTubeDstbQty=mtxtShowPickTubeDstb.getText().toString().trim();
                

                mtxtShowPickTubeQtyOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartJobtubeDetail.QtyOut)));
                mtxtShowPickTubeDstbOut.setText(String.format("%.0f",Double.parseDouble(clsPickPartJobtubeDetail.DstbQtyOut)));
                mtxtShowPickTubeFixdigit.setText(clsPickPartJobtubeDetail.FixDigitNo.toUpperCase());

                //ต้องหาจำนวนความยาวต่อท่อนด้วย
               Integer intQty = Integer.parseInt(mtxtShowPickTubeQty.getText().toString());
               Integer intDstb = Integer.parseInt(mtxtShowPickTubeDstb.getText().toString());
               Integer intLength = (intQty/intDstb);
               //ถ้าเปลี่ยนเพลาหมุน สามารถป้อนความยาวมากว่าที่ต้องได้ถึง 10 cm เพราะมีค่าปล่ายเพลา
//               msbRealCut.setMax(10);
                //ทำการกำหนดค่ามากสุดที่สามารถเลื่อนได้
                //แผนก ATM มีค่าปลายเพลาเลือกได้ถึง 10 แผนกอื่นมีค่่าใบเลื่อยประมาณ 2
//                if (strPickTubePartnid.subSequence(0,1).equals("M")){
//                    msbRealCut.setMax(2);
//                }
//                else{
//                    msbRealCut.setMax(2);
//                }

               mtxtShowPickTubeLength.setText(intLength.toString());

                mtxtShowPickTubeRealCutOut.setText("0");
                mtxtShowPickTubeDigitnoOut.setText(""); //รอใส่ตอน Scan Part
                blnPickTubeFixDigitNo = mtxtShowPickTubeFixdigit.getText().toString().toUpperCase().equals("Y");

                strTableName="Part";
                strField="PartUnit";
                strCondition="partnid='" + strPickTubePartnid + "'";
                strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
                strReturnValue=globalUtility.Find_ReturnValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
                mtxtShowPickTubeUnit.setText(strReturnValue.trim());

                strTableName="Jobtubedt";
                strField="Request_All";
                strCondition= "JTNO = '" + strPickTubeDocno + "' and Partnid='" + strPickTubePartnid + "' and Digitno='" + strPickTubeDigitno + "'";
                strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
                strReturnValue=globalUtility.Find_ReturnValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);
                blnRequestAllTube=strReturnValue.equals("Y");

                CheckPickTubeComplete();
            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void GetPartTubeDetail() {
        String tag = "6SepV2";
        try {
            ExecuteGetPartTubeDetail executeGetPartTubeDetail = new ExecuteGetPartTubeDetail(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetPartTubeDetail();
            executeGetPartTubeDetail.execute(strDataBaseName, strPickTubePartnid,mtxtShowPickTubeDigitnoOut.getText().toString().trim(), strURL);

            String resultJSON = executeGetPartTubeDetail.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "";
                strParttube_Digitno="";
                strParttube_Onhand="0";
                strParttube_DMLength="0";
                strParttube_RealQty="0";
//                strAlertMessage="Not Found Data !!!" ;
//                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage,R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsPartTubeDetail = gson.fromJson(resultJSON.toString(), PartTubeDetail.class);

                strParttube_Digitno=clsPartTubeDetail.Digitno;
                strParttube_Onhand=String.format("%.0f",Double.parseDouble(clsPartTubeDetail.Onhand));
                strParttube_DMLength=String.format("%.0f",Double.parseDouble(clsPartTubeDetail.DMLength));

                //หาจำนวนที่ตัดไปแล้วของ Part และ Digit ที่ Scan
                strTableName="Tmp_RFPartTube";
                strField="QtyOut";
                strCondition = "Docno='" + strPickTubeDocno + "' and Partnid='" + strPickTubePartnid + "' and DigitNoCut='" + strParttube_Digitno +"'";
                strURL=strServerAddress+myConstant.urlMobile_SumValue();
                strSumRealCut = globalUtility.SumValue(getApplicationContext(),strDataBaseName,strTableName,strField,strCondition,strURL);

                //ถ้าเป็นการแยกทอน ฟรือ ตัดส่วนที่เสียออก ให้สามารถใช้ได้ทั้งหมด
                if (blnPickTubeCutDamage||blnPickTubeSplit){
                    strParttube_RealQty=String.format("%.0f",Double.parseDouble(clsPartTubeDetail.Onhand));
                }
                else{ //ทำการลบจำนวนที่ตัดแล้วออกด้วย
                    strParttube_RealQty=String.format("%.0f",Double.parseDouble(clsPartTubeDetail.RealQty)-Double.parseDouble(strSumRealCut));
                }


            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }

    private void Execute_Mobile_Confirm_PickPart_Jobtube(String strTmpTime, String strDocno, String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteEightParameter executeEightParameterParameter = new ExecuteEightParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Confirm_PickPart_Jobtube();
            executeEightParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strTmpTime",strTmpTime,
                    "strDocNo",strDocno,
                    "strPartNid",strPartNid,
                    "strDigitno",strPickTubeDigitno,
                    "strQty",strPickTubeQty,
                    "strDstbQty",strPickTubeDstbQty,
                    "strStfcode",userLogin.STFcode.trim(),
                    strURL);//ใช้ร่วมกันกับการจัดสินค้าปกติ
            String resultJSON = executeEightParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_PickPart_Jobtube() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_PickPart()+clsResult.ResultMessage ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
                    strAlertMessage=clsResult.ResultMessage;
                    //แจ้งเตือนเมื่อจัดครบทุกรายการแล้ว
//                    if (strAlertMessage.trim().toUpperCase().equals("COMPLETE")) {
//                        strAlertMessage="Complete Jobtube";
//                        ShowAlertDialog(R.string.alertdialog_success, "Complete.", strAlertMessage, R.drawable.alertdialog_ic_success);
//                    }
                    ShowSelectLocation(strPickTubeDocno);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    private void Execute_Insert_Tmp_RFPartTube(String strDocno, String strPartNid, String strPartDigitNo, String strQty, String strDstbQty, String strPartDigitNoCut,String strCutLength){
        String tag = "6SepV2";
        try {
            ExecuteEightParameter executeEightParameterParameter = new ExecuteEightParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Insert_Tmp_RFPartTube();
            executeEightParameterParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocno,
                    "strPartNid",strPartNid,
                    "strDigitNo",strPartDigitNo,
                    "strQty",strQty,
                    "strDstbQty",strDstbQty,
                    "strDigitNoCut",strPartDigitNoCut,
                    "strCutLength",strCutLength,
                    strURL);
            String resultJSON = executeEightParameterParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Insert_Tmp_RFPartTube()+clsResult.ResultMessage ; ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();
                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Insert_Tmp_RFPartTube() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                };
            }

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



    private void GetPickTubeDocType(String strDocno){
        strURL=myConstant.urlMobile_GetDoctype();
        strReturnValue = globalUtility.ReturnValue_ExecuteTwoParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strDocno",strDocno,strURL);
        strPickTubeDocType=strReturnValue;
    }




    private void createListViewDocument() {
        //ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";
        try {
            ExecuteGetListDocument executeGetListDocument = new ExecuteGetListDocument(getApplicationContext());
            //T คือ VW_Mobile_ListDocNo_Picking_Jobtube  แสดงเอกสารที่รอ จัดสินค้า
            strURL=strServerAddress+myConstant.urlMobile_ListDocument();
            executeGetListDocument.execute(strDataBaseName, "T",userLogin.STFcode.trim(), strURL);
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

            mlvListDocument.setAdapter(listDocumentAdapter);

            mlvListDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                    metSelectDocno.setText(arrListDocumentString[i].toString());
                    mtvShowDocSelected.setText(arrCustNameString[i].toString());
                    strPickTubeDocno = metSelectDocno.getText().toString();
                    Execute_Mobile_Crt_Tmp_RFCheckOut_Picking(strPickTubeDocno.trim());
                    ShowSelectLocation(strPickTubeDocno.trim());

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
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Crt_Tmp_RFCheckOut_Picking()+clsResult.ResultMessage ; ;
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
            strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
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
                        //ถ้าเป้น MIS ให้เลือก Location ได้ แค่ไม่สามารถ ป้อน part ได้
                        if ((userLogin.DPCode.equals("MIS")|| !userLogin.MobilePickInputManual.trim().equals(""))) {
                            strPickTubeLocation=strLocationStrings;
                        }

                        //ทำการ Run ใหม่เพื่อให้มีเฉพาะ part ที่เหลือ
                        if (! strPickTubeLocation.isEmpty() && ! strPickTubeLocation.equals("")){
                            createListViewPart(strPickTubeDocno,strPickTubeLocation);
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
        strPickTubePartnid="";
        strPickTubeDigitno="";
        strPickTubeQty="0";
        strPickTubeDstbQty="1";
        
        blnListPartEmpty=false;
        //ListView listViewPart = findViewById(R.id.lvListPartnid);
        String tag = "6SepV2";
        strTableName=" tmp_rfcheckout A inner join part B ON A.partnid=B.partnid ";
        strField="A.PartNid,A.Digitno,rtrim(B.partno)+' '+B.partdes  as Description"  ;
        strCondition = "A.Docno='" + strDocno + "' and A.LCCode ='" + strLocation + "' and StoreCheck=''";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetListPartTube();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition,strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            Log.d(tag, "JSON ==> " + resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListPartNidStrings = new String[jsonArray.length()];
            final String[] arrDigitnoStrings = new String[jsonArray.length()];
            final String[] arrDescriptionStrings = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป
            if (jsonArray.length()==0) {
                blnListPartEmpty=true;
            }

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListPartNidStrings[i] = jsonObject.getString("PartNid");
                arrDigitnoStrings[i] = jsonObject.getString("Digitno");
                arrDescriptionStrings[i] = jsonObject.getString("Description");
                Log.d(tag, "PartNid [" + i + "] ==> " + arrListPartNidStrings[i]);
            }//for

            ListPartJobtubeAdapter listPartJobtubeAdapter = new ListPartJobtubeAdapter(getApplicationContext(), arrListPartNidStrings, arrDigitnoStrings, arrDescriptionStrings);
            mlvListPartJobtube.setAdapter(listPartJobtubeAdapter);

            mlvListPartJobtube.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
                    updateview = view;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
                    strPickTubePartnid=arrListPartNidStrings[i];
                    strPickTubeDigitno=arrDigitnoStrings[i];

                    ShowScanPart(strPickTubePartnid,strPickTubeDigitno);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView

    private void Hold_Picking_Jobtube_Document(){
        ResetAllWarning();
        blnWarning_Hold=true;
        strAlertMessage="คุณต้องการจัดสินค้าใหม่ \n ภายหลังใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }

    private void Reset_Picking_Jobtube_Document(){
        ResetAllWarning();
        blnWarning_Reset=true;
        strAlertMessage="คุณต้องการจัดสินค้าใหม่ \n อีกกครั้งใช่หรือไม่" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);

    }

    private void Hold_Picking_Jobtube_Part(){
        Execute_Hold_Reset_Picking_Jobtube(strPickTubeDocno,strPickTubePartnid,"H");
        ShowSelectLocation(strPickTubeDocno);
    }

    private void Reset_Picking_Jobtube_Part(){
        Execute_Hold_Reset_Picking_Jobtube(strPickTubeDocno,strPickTubePartnid,"R");
        ShowSelectLocation(strPickTubeDocno);
    }


    private void Execute_Hold_Reset_Picking_Jobtube(String strDocNo,String strPartnid,String strOption) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Picking_Jobtube_Hold_Reset();
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
                strAlertMessage="Error Execute " + myConstant.urlMobile_Picking_Jobtube_Hold_Reset()+clsResult.ResultMessage ;;
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


    private void Execute_Mobile_UPD_Tmp_RFCheckOut_Jobtube(String strQty) {
        String tag = "6SepV2";
        try {
            ExecuteEightParameter executeEightParameter = new ExecuteEightParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_UPD_Tmp_RFCheckOut_Jobtube();
                    executeEightParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strPickTubeDocno,
                    "strPartnid", strPickTubePartnid,
                    "strDigitno",strPickTubeDigitno,
                    "strQty",strPickTubeQty,
                    "strDstbQty",strPickTubeDstbQty,                    
                    "strKind", strPartStatus,
                    "strQtyCut", strQty,
                    strURL);
            String resultJSON = executeEightParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue = "Error Execute";
                strAlertMessage="Error Execute "+ myConstant.urlMobile_UPD_Tmp_RFCheckOut_Jobtube() ;
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


    private  void ProcessInputParttube(String s){
        String[] arrGetDescFromScanBarcode;
               if (metScanPickTubePart.isEnabled() && !metScanPickTubePart.getText().toString().isEmpty() && !metScanPickTubePart.getText().toString().equals("")) {
                        metScanPickTubePart.setText("");
                        metScanPickTubePart.setHint(s.toString().trim());
                        //กำว่ากำลังรอใส่ ความยาวอยู่่หรือไม่ อยู่หรือไม่
                        if (!blnWaitInputRealCutLength) {
                            if (s.length() == 17 || s.length() == 18) {
                                arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                                strPartNid = arrGetDescFromScanBarcode[0];
                                strPartStatus = arrGetDescFromScanBarcode[1]; //0-สมบูรณ์ 1-ตัวไม่สามบูรณ์  2-ตัวเสีย 3-ตัวScrap
                                strDigitNo = arrGetDescFromScanBarcode[2];

                                strPickTubeDigitNoOut = strDigitNo;
                                mtxtShowPickTubeDigitnoOut.setText(strPickTubeDigitNoOut);
                                mbtnConfirmCutLength.setEnabled(false);
                                msbRealCut.setEnabled(false);
                                SetSeekbarState();
                                GetPartTubeDetail();

                                if (strPartNid.equals(strPickTubePartnid)) {
                                    //ถ้าไม่พบdigitที่ Scan ใน Parttube
                                    if (strParttube_Digitno.equals("")) {
                                        strAlertMessage = "ไม่พบท่อนที่คุณ Scan ใน แฟ้ม Parttube นะครับ" + mtxtShowPickTubeDigitnoOut.getText();
                                        ShowAlertDialog(R.string.alertdialog_error, "Error.", strAlertMessage, R.drawable.alertdialog_ic_error);

                                    }
                                    //ทำการตรวจสอบว่า มีการ Fix ท่อนไว้หรือไม่
                                    else {
                                        //ถ้ารถบุว่าตัดส่วนที่เสียออก หรือว่าแยกท่อน หรือ กำหนดท่อนที่จ้องการ
                                        if ((blnPickTubeFixDigitNo || blnPickTubeCutDamage || blnPickTubeSplit) && !strPickTubeDigitNoOut.equals(mtxtShowPickTubeDigitno.getText().toString().trim())) {

                                            if (blnPickTubeFixDigitNo) {
                                                strAlertMessage = "มีการกำหนดท่อนที่ต้องการให้ตัดไว้\n ท่อนที่ต้องการให้ตัดคือ" + mtxtShowPickTubeDigitno.getText();
                                            } else if (blnPickTubeCutDamage) {
                                                strAlertMessage = "การตัดส่วนที่เสียออกต้องเลือกให้ตรงท่อนด้วยนะครับ\n ท่อนที่ต้องการให้ตัดคือ" + mtxtShowPickTubeDigitno.getText();
                                            } else {
                                                strAlertMessage = "การตัดเเพื่อแยกท่นอต้องเลือกให้ตรงท่อนด้วยนะครับ\n ท่อนที่ต้องการให้ตัดคือ" + mtxtShowPickTubeDigitno.getText();
                                            }
                                            ShowAlertDialog(R.string.alertdialog_error, "Error.", strAlertMessage, R.drawable.alertdialog_ic_error);
                                        } else {
                                            //เคลียจำนวนการ Scan ซ้ำออกด้วย
                                            intCheckScanDuplicate = 0;
                                            //ได้มาจากตอน GetPartDetail
                                            dblPickTubeQtyOut = Double.parseDouble(mtxtShowPickTubeQtyOut.getText().toString());
                                            dblPickTubeRealCutOut = Double.parseDouble(mtxtShowPickTubeRealCutOut.getText().toString());
                                            dblPickTubeDstbQtyOut = Double.parseDouble(mtxtShowPickTubeDstbOut.getText().toString());


                                            intPickTubeQtyOut = dblPickTubeQtyOut.intValue();
                                            intPickTubeRealCutOut = dblPickTubeRealCutOut.intValue();
                                            intPickTubeDstbQtyOut = dblPickTubeDstbQtyOut.intValue();


                                            if (strPartStatus.equals("0")) {
                                                //กำหนดให้เท่ากับความยาวที่ต้องการให้ตัด
                                                intPackQty = Integer.parseInt(mtxtShowPickTubeLength.getText().toString());
                                                //กำหนดความยาวเรื่อมตั้เท่ากับความยาวที่ต้องการตัด
                                                mtxtInputRealCutLength.setText(String.valueOf(intPackQty));
                                                intPickTubeQtyOut = intPickTubeQtyOut + intPackQty;
                                                mtxtShowPickTubeQtyOut.setText(String.valueOf(intPickTubeQtyOut));

                                                intPickTubeDstbQtyOut = intPickTubeDstbQtyOut + 1;
                                                mtxtShowPickTubeDstbOut.setText(String.valueOf(intPickTubeDstbQtyOut));
                                                //ถ้าข้อมูลถูตต้องก็ Show ปุ่ม Confirm ในการตัดได้
                                                blnWaitInputRealCutLength = true;
                                                metScanPickTubePart.setEnabled(!blnWaitInputRealCutLength);
                                                //ต้องรอป้อนค่าใบเลื่อยก่อนถึงจะ Confirm การตัดได้
                                                //21-04-2021 ต้องการให้ Confirm ได้เลยไมต้องมีค่าใบเลื่อย เช่น ลูกค้าต้องการ 400 cm แล้วไม่ได้ Fix digit แล้วท่อนนั้นมีความยาวเต็มท่อน 400cm พอดี
                                                // หรือ ลูกค้าต้องการให้ตัด 59.5 cm  แต่ไม่สามารถป้อนได้ ต้องป้อนเป็น 60cm แต่ใช้ Remark บอกว่าให้ตัด 59.5 cm ทำให้ตัดจริงจะตัด 60 cm
                                                //ถ้ายกเท่อนไม่ต้องรอป้อนความยาวที่ตัดจริง
                                                mbtnConfirmCutLength.setEnabled(blnWaitInputRealCutLength || blnRequestAllTube);

                                                msbRealCut.setEnabled(blnWaitInputRealCutLength && !blnRequestAllTube);
                                                SetSeekbarState();

                                                //ทำการแจ้งเตือนความยาวที่เหลือว่าพอหรือไม่
                                                Integer intParttube_RealQty = Integer.parseInt(strParttube_RealQty);
                                                Integer intInputRealCutLength = Integer.parseInt(mtxtInputRealCutLength.getText().toString());

                                                if (intInputRealCutLength>intParttube_RealQty) {
                                                    strAlertMessage = ("Onhand = " + strParttube_Onhand + "\n DMLength = " + strParttube_DMLength +
                                                            "\n ตัดไปแล้ว = " + strSumRealCut + "\n ความยาวที่สามารขายได้ " + strParttube_RealQty+
                                                            "\n ความยาวที่คุณต้องการคือ " + mtxtInputRealCutLength.getText().toString());
                                                    ShowAlertDialog(R.string.alertdialog_success,"Digitno. "+mtxtShowPickTubeDigitnoOut.getText().toString().trim(),strAlertMessage, R.drawable.alertdialog_ic_success);
                                                }
                                            } else {
                                                intPackQty = 0;
                                                mtxtInputRealCutLength.setText(String.valueOf(intPackQty));
                                                strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                            }
                                        }
                                    }
                                } else {
                                    intPackQty = 0;
                                    mtxtInputRealCutLength.setText(String.valueOf(intPackQty));
                                    strAlertMessage = "สินค้าที่คุณ Scan ไม่ตรงกับที่ต้องการให้ตัดนะครับ";
                                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                }

                                metScanPickTubePart.setText("");

                            } else {
                                if (!s.toString().isEmpty()) {
                                    strAlertMessage = "คุณ Scan สินค้าไม่ถูกต้อง";
                                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                    metScanPickTubePart.setText("");
                                }
                            }
                        }
                    }   //ถ้าไม่ Enable แสดงว่าจัดครบแล้ว
                    else {
                        if (!s.toString().isEmpty()) {
                            strAlertMessage = "คุณ Scan สินค้าครบตามต้องการแล้วนะครับ";
                            globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                            metScanPickTubePart.setText("");
                        }
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
                    if (blnWarning_Reset){
                        Execute_Hold_Reset_Picking_Jobtube(strPickTubeDocno,"","R");
                        InitializeData();
                    }else if (blnWarning_Hold){
                        Execute_Hold_Reset_Picking_Jobtube(strPickTubeDocno,"","H");
                        InitializeData();
//                    }else if (blnWarning_Confirm){
//                        ConfirmCutLength();
//                    }else if (blnWarning_Complete){
//                        Execute_Mobile_Confirm_PickPart_Jobtube(strPickTubeTmpTime,strPickTubeDocno,strPickTubePartnid);
//                        //ห้ามเรียกใช้ InitializeData() เพราะจะทำการไม่สามารถจัด Part ที่ยังไม่่เสร็จได้
                    }
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