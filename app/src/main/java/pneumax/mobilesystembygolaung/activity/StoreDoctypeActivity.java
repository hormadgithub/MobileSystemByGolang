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
import pneumax.mobilesystembygolaung.connected.ExecuteSixParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocument_Receive_Store_Adapter;
import pneumax.mobilesystembygolaung.manager.ListPartAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.ReturnValue;
import pneumax.mobilesystembygolaung.object.StaffLogin;
import pneumax.mobilesystembygolaung.object.StorePartDetail;

import static pneumax.mobilesystembygolaung.manager.GlobalVar.getDataBaseName;


public class StoreDoctypeActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    Product clsProduct;
    StorePartDetail clsStorePartDetail;

    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter ????????????????????????????????? Execute
    String strTableName,strField,strCondition,strURL;

    ReturnValue clsReturnValue;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;


    //From Layout
    Button mbtnHold_Document, mbtnReset_Document,mbtnHold_Part, mbtnReset_Part;
    Button mbtnIncStoreQty, mbtnDecStoreQty,mbtnConfirmStorePart;
    CircularImageView mbtnSearch;
    ImageView mimgBackTop;
    EditText metScanDocument,metScanLocation,metSelectDocno, metScanStorePart;
    TextView mtvShowDocSelected,mtxtShowStorePartno,mtvTitle;
    Group mgplvListDocument,mgpLocation,mgpScanPart,mgpButtonIncDec;
    Boolean blnShowgplvListDocument,blnShowgpLocation,blnShowgpScanPart;

    // ??????????????????????????????????????????????????????????????????????????????
    TextView mtxtShowStoreQty,mtxtShowStoreDF,mtxtShowStoreSDM,mtxtShowStoreDM,mtxtShowScanStoreQty;
    TextView mtxtShowStoreQtyIn, mtxtShowStoreDFIn, mtxtShowStoreSDMIn, mtxtShowStoreDMIn,mtxtShowStoreLocation;
    Spinner mspnLocation;

    ArrayAdapter<String> AdapterSpinnerLocation;
    private String[] arrLocationStrings;
    private String  strLocationStrings;


    Boolean blnListPartEmpty;
    View updateview;// above oncreate method
    ListView mlvListDocument,mlvListPart;

    //???????????????????????????????????????????????????????????? Select ??????????????????
    String strStoreDocType, strStoreTmpTime,strStoreDocno,strStoreLocation,strStorePartnid,strStorePackQty,strSelectDoctype,strSelectDocdesc;
    Boolean blnStorePartHaveSerialNo,blnStorePartHaveLabel,blnStorePartTube,blnStoreScanFast ;
    Boolean blnWaitScanSerialno;
    //?????????????????????????????????????????????????????????????????????????????????????????? Alert Warning ?????????????????????????????????????????????????????????????????????????????????????????????
    Boolean blnWarning_Reset_Document=false, blnWarning_Hold_Document=false,blnWarning_Confirm=false;

    Double dblPackQty, dblStoreQtyOut, dblStoreDFOut, dblStoreSDMOut, dblStoreDMOut;
    Integer intPackQty,intScanQty, intStoreQty, intStoreDF, intStoreSDM, intStoreDM;

    String strPartNid, strPartStatus, strDigitNo;
    Integer intCheckScanDuplicate=0; //????????? Scan ??????????????? 2 ??????????????????????????? ???????????????????????? 3 ????????????????????? Digit ????????????????????????????????? ??????????????????????????????


    //????????????????????????????????????????????????????????????????????????????????? Scan ?????????
    Boolean blnManualInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_store_doctype);

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
        metScanLocation=(EditText) findViewById(R.id.etScanLocation);
        mspnLocation = (Spinner) findViewById(R.id.spnLocation);


        metSelectDocno=(EditText) findViewById(R.id.etSelectDocno);
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        mtxtShowStorePartno=(TextView) findViewById(R.id.txtShowStorePartno);
        mtxtShowStoreLocation=(TextView) findViewById(R.id.txtShowStoreLocation);

        metScanStorePart =(EditText) findViewById(R.id.txtScanInputStorePart);
        mtxtShowScanStoreQty=(TextView) findViewById(R.id.txtShowScanStoreQty);
        mtxtShowStoreQty=(TextView) findViewById(R.id.txtShowStoreQty);
        mtxtShowStoreDF=(TextView) findViewById(R.id.txtShowStoreDF);
        mtxtShowStoreSDM=(TextView) findViewById(R.id.txtShowStoreSDM);
        mtxtShowStoreDM=(TextView) findViewById(R.id.txtShowStoreDM);

        mtxtShowStoreQtyIn =(TextView) findViewById(R.id.txtShowStoreQtyIn);
        mtxtShowStoreDFIn =(TextView) findViewById(R.id.txtShowStoreDFIn);
        mtxtShowStoreSDMIn =(TextView) findViewById(R.id.txtShowStoreSDMIn);
        mtxtShowStoreDMIn =(TextView) findViewById(R.id.txtShowStoreDMIn);


        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpLocation=(Group) findViewById(R.id.gpLocation);
        mgpScanPart=(Group) findViewById(R.id.gpScanStorePart);
        mgpButtonIncDec=(Group) findViewById(R.id.gpButtonIncDec);

        mlvListPart =(ListView) findViewById(R.id.lvListPart);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);
        mbtnHold_Part =(Button) findViewById(R.id.btnHold_Part);
        mbtnReset_Part =(Button) findViewById(R.id.btnReset_Part);
        mbtnHold_Document =(Button) findViewById(R.id.btnHold_Document);
        mbtnReset_Document =(Button) findViewById(R.id.btnReset_Document);
        mbtnIncStoreQty =(Button) findViewById(R.id.btnIncStoreQty);
        mbtnDecStoreQty =(Button) findViewById(R.id.btnDecStoreQty);
        mbtnConfirmStorePart =(Button) findViewById(R.id.btnConfirmStorePart);

        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }


    private void InitializeData() {
        mtvTitle.setText("???????????????????????????????????????????????? "+ strSelectDocdesc.substring(0, strSelectDocdesc.indexOf("(")));

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
        //???????????????????????????????????????????????????????????????????????????????????????
        strStoreTmpTime="";
        strStoreDocno="";
        strStoreLocation="";
        //????????????????????????????????????????????? Scan location ????????????
        metScanLocation.setHint("Location");
        strStorePartnid="";

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
                if (! strStoreDocno.equals("")) {
                    if (!strStorePartnid.equals("")){
                        Execute_Hold_Reset_Storing(strStoreDocno,strStorePartnid,"H");
                    }
                    else {
                        Execute_Hold_Reset_Storing(strStoreDocno,"","R");
                    }
                }
                //?????????????????????????????????????????? Document
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
                    blnManualInput=false; //????????????????????? TextOnChange ???????????????
                    //???????????????" " ????????????????????????????????????????????????????????????????????? onTextChanged
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
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? manual
                //if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                if((s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????


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
                            strAlertMessage = "??????????????????????????????????????????????????? Scan";
                            mtvShowDocSelected.setText(strAlertMessage);
                            ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    }
                    //mbtnSearch.setText(getResources().getString(R.string.search));
                    mbtnSearch.setImageResource(R.drawable.ic_search_blue);
                    mgplvListDocument.setVisibility(View.GONE);
                }
                else {
                    if (!s.toString().equals("") ) {
                        if ( blnShowgplvListDocument) {
                            strAlertMessage = "????????????????????????????????????????????????????????????????????????????????????????????????????????????.";
                            ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                        }

                        else  if ( blnShowgpLocation) {
                            strAlertMessage = "?????????????????????????????? Part ????????????????????????????????????????????????????????????..";
                            ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
                            metScanDocument.setText("");
                            metScanLocation.requestFocus();
                            SendKeyDown(2);
                        }

                        else  if ( blnShowgpScanPart) {
                            strAlertMessage = "?????????????????????????????????????????? Scan Part ??????????????????????????????.";
                            ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
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
                    blnManualInput=false; //????????????????????? TextOnChange ???????????????
                    //???????????????" " ????????????????????????????????????????????????????????????????????? onTextChanged
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
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? manual
                //01-03-2021 ????????????????????????????????????????????????????????? Location ????????????????????????????????????????????????????????????
//                if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                if (s.length()==1 || blnManualInput) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????

                if (blnShowgpLocation) {
                    if (!metScanLocation.getText().toString().isEmpty() && !metScanLocation.getText().toString().equals("")) {
                        metScanLocation.setText("");
                        blnManualInput = false;
                        metScanLocation.setHint(s.toString().trim());
                        strStoreLocation = metScanLocation.getHint().toString().trim();
                        mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(s.toString().trim()));
                        createListViewPart(strStoreDocno, strStoreLocation);
                        if (blnListPartEmpty) {
                            strAlertMessage = "????????????????????????????????????????????????????????????????????????????????? \n ?????? Location ????????? Scan ??????????????????.";
                            ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    } else {
                        if (!s.toString().equals("")) {
                            if (blnShowgplvListDocument) {
                                strAlertMessage = "?????????????????????????????? Part ?????????????????????????????? \n ??????????????????????????? Select ??????????????????.";
                                ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
                                metScanLocation.setText("");
                            } else if (blnShowgpScanPart) {
                                strAlertMessage = "?????????????????????????????????????????? Scan Part ??????????????????????????????.";
                                ShowAlertDialog(R.string.alertdialog_error, "?????????????????????:", strAlertMessage, R.drawable.alertdialog_ic_error);
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
                    blnManualInput=false; //????????????????????? TextOnChange ???????????????
                    //???????????????" " ????????????????????????????????????????????????????????????????????? onTextChanged
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
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? manual
                //?????????????????? ????????? ????????? ????????? ?????????????????????
                if((userLogin.DPCode.equals("MIS")|| !userLogin.MobileStoreInputManual.trim().equals("")) &&  (s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                s = s.toString().toUpperCase().trim(); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????


                if (metScanStorePart.isEnabled() && !metScanStorePart.getText().toString().isEmpty() && !metScanStorePart.getText().toString().equals("")) {
                    metScanStorePart.setText("");
                    blnManualInput = false;
                    metScanStorePart.setHint(s.toString().trim());

                    if (s.length() == 17 || s.length() == 18) {
                        arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                        strPartNid = arrGetDescFromScanBarcode[0];
                        strPartStatus = arrGetDescFromScanBarcode[1]; //0-????????????????????? 1-??????????????????????????????????????????  2-????????????????????? 3-?????????Scrap
                        strDigitNo = arrGetDescFromScanBarcode[2];

                        if (strPartNid.equals(strStorePartnid)) {
                            //???????????????????????????????????????????????????????????????????????????????????? 3 ???????????????
                            if (!Check_ScanDuplicate(strStoreTmpTime, strStoreDocno, strPartNid, strDigitNo,strPartStatus)) {
                                //??????????????????????????????????????? Scan ??????????????????????????????
                                intCheckScanDuplicate = 0;
                                //????????????????????????????????? GetPartDetail
                                dblPackQty = Double.parseDouble(strStorePackQty.toString());
                                dblStoreQtyOut = Double.parseDouble(mtxtShowStoreQtyIn.getText().toString());
                                dblStoreDFOut = Double.parseDouble(mtxtShowStoreDFIn.getText().toString());
                                dblStoreSDMOut = Double.parseDouble(mtxtShowStoreSDMIn.getText().toString());
                                dblStoreDMOut = Double.parseDouble(mtxtShowStoreDMIn.getText().toString());

                                intPackQty = dblPackQty.intValue();
                                intStoreQty = dblStoreQtyOut.intValue();
                                intStoreDF = dblStoreDFOut.intValue();
                                intStoreSDM = dblStoreSDMOut.intValue();
                                intStoreDM = dblStoreDMOut.intValue();


                                if (strPartStatus.equals("0")) {
                                    mtxtShowScanStoreQty.setText(String.valueOf(intPackQty));
                                    intStoreQty = intStoreQty + intPackQty;
                                    mtxtShowStoreQtyIn.setText(String.valueOf(intStoreQty));
                                } else if (strPartStatus.equals("1")) {
                                    intPackQty = 1;
                                    mtxtShowScanStoreQty.setText(String.valueOf(intPackQty));
                                    intStoreDF = intStoreDF + intPackQty;
                                    mtxtShowStoreDFIn.setText(String.valueOf(intStoreDF));
                                } else if (strPartStatus.equals("2")) {
                                    intPackQty = 1;
                                    mtxtShowScanStoreQty.setText(String.valueOf(intPackQty));
                                    intStoreDM = intStoreDM + intPackQty;
                                    mtxtShowStoreDMIn.setText(String.valueOf(intStoreDM));
                                } else if (strPartStatus.equals("3")) {
                                    intPackQty = 1;
                                    mtxtShowScanStoreQty.setText(String.valueOf(intPackQty));
                                    intStoreSDM = intStoreSDM + intPackQty;
                                    mtxtShowStoreSDMIn.setText(String.valueOf(intStoreSDM));
                                } else {
                                    intPackQty = 0;
                                    mtxtShowScanStoreQty.setText(String.valueOf(intPackQty));
                                    strAlertMessage = "????????? Scan ????????????????????????????????????????????????";
                                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                                }
                            }
                        } else {
                            intPackQty = 0;
                            mtxtShowScanStoreQty.setText(String.valueOf(intPackQty));
                            strAlertMessage = "???????????????????????????????????? Scan ??????????????????????????? Part ??????????????????????????????????????????????????????????????????????????????";
                            globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                        }

                        mgpButtonIncDec.setVisibility(View.GONE);
                        if (intPackQty > 1) {
                            mgpButtonIncDec.setVisibility(View.VISIBLE);
                        }
                        metScanStorePart.setText("");
                        //????????????????????????????????????????????????????????????????????????
                        if (intCheckScanDuplicate==0)  {
                            CheckStoreComplete();
                        }
                    } else {
                        if (!s.toString().isEmpty()) {
                            strAlertMessage = "????????? Scan ????????????????????????????????????????????????";
                            globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                            metScanStorePart.setText("");
                        }
                    }
                }
                else{
                    if (!s.toString().isEmpty()) {
                        strAlertMessage = "????????? Scan ???????????????????????????????????????????????????????????????????????????????????????";
                        globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                        metScanStorePart.setText("");
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
                Hold_Storing_Document();
            }
        });


        mbtnReset_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Storing_Document();
            }
        });


        mbtnHold_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hold_Storing_Part();
            }
        });


        mbtnReset_Part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset_Storing_Part();
            }
        });


        mbtnIncStoreQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intStoreQty = Integer.parseInt(mtxtShowStoreQtyIn.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanStoreQty.getText().toString());
                intScanQty = intScanQty + 1;
                if (intScanQty > intPackQty ) {
                    intScanQty = 0;
                    intStoreQty = intStoreQty - intPackQty; //??????????????????????????????????????????????????????????????????????????????????????? Scan
                } else {
                    intStoreQty = intStoreQty + 1; //??????????????????????????? 1
                }
                mtxtShowScanStoreQty.setText(String.valueOf(intScanQty));
                mtxtShowStoreQtyIn.setText(String.valueOf(intStoreQty));
                CheckStoreComplete();
            }
        });

        mbtnDecStoreQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intStoreQty = Integer.parseInt(mtxtShowStoreQtyIn.getText().toString());
                intScanQty = Integer.parseInt(mtxtShowScanStoreQty.getText().toString());

                intScanQty = intScanQty - 1;
                if (intScanQty < 0) {
                    intScanQty = intPackQty;
                    intStoreQty = intStoreQty + intPackQty; //??????????????????????????????????????????????????????????????????????????????????????? Scan
                } else {
                    intStoreQty = intStoreQty - 1; //??????????????????????????? 1
                }
                mtxtShowScanStoreQty.setText(String.valueOf(intScanQty));
                mtxtShowStoreQtyIn.setText(String.valueOf(intStoreQty));
                CheckStoreComplete();
            }
        });

        mbtnConfirmStorePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                ResetAllWarning();
//                blnWarning_Confirm=true;
//                strAlertMessage="??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????" ;
//                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                Execute_Mobile_Confirm_StorePart(strStoreTmpTime,strStoreDocno,strStorePartnid);

            }
        });
    }

    private void ResetAllWarning() {
        blnWarning_Hold_Document =false;
        blnWarning_Reset_Document =false;
        blnWarning_Confirm=false;
    }

    private void CheckDocumentFromScan(String strDocno){
        strTableName="VW_Mobile_ListDocNo_Store";
        strField="Docno";
        strCondition="Docno='" +strDocno + "'";
        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue=globalUtility.Find_Record(getApplicationContext(), strDataBaseName,strTableName,strField,strCondition,strURL);
        if (!strReturnValue.trim().equals("")) {
            Execute_Mobile_INS_Tmp_RFCheckIn(strDocno);
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
        //mgplvListDocument.setVisibility(View.GONE);
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
        mtvShowDocSelected.setText(strStorePartnid);
        GetProductDetail();
        mgpScanPart.setVisibility(View.VISIBLE);
        GetStorePartDetail();
        metScanStorePart.setHint("Scan Partid");
        metScanStorePart.setEnabled(true);
        blnWaitScanSerialno=false;
        blnShowgpScanPart=true;
        CheckStoreComplete();
        metScanStorePart.requestFocus();
        SendKeyDown(2);
    }



    private void createSpinnerLocation(final String strDocno) {
        try {
            strTableName="Tmp_RFCheckin";
            strField="LCCode";
            strCondition = " Docno='" + strDocno +"' and StoreCheckin<>'Y'";
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+   myConstant.urlMobile_ReturnValue();
            executeFourParameter.execute("strDataBaseName", strDataBaseName,
                    "strTableName", strTableName,
                    "strField", strField,
                    "strCondition",strCondition,
                    strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //??????????????????????????????????????????
            arrLocationStrings = new String[jsonArray.length()];
            //????????????????????????????????????????????? Part ???????????????????????????????????????
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
                        //????????????????????? MIS ????????????????????? Part ???????????????????????????
                        if ((userLogin.DPCode.equals("MIS")|| !userLogin.MobileStoreInputManual.trim().equals("")))
                            strStoreLocation=strLocationStrings;
                        //??????????????? Run ????????????????????????????????????????????????????????? part ????????????????????????
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
                //???????????????????????????????????????????????????????????????????????????????????????
                InitializeData();

            }

        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Loation ==> " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }//createSpinnerlocation


    private void CheckStoreComplete() {

        Integer intQty, intQtyIn, intDFQty, intDFQtyIn, intSDMQty, intSDMQtyIn, intDMQty, intDMQtyIn;

        intQty = Integer.parseInt(mtxtShowStoreQty.getText().toString());
        intQtyIn = Integer.parseInt(mtxtShowStoreQtyIn.getText().toString());

        intDFQty = Integer.parseInt(mtxtShowStoreDF.getText().toString());
        intDFQtyIn = Integer.parseInt(mtxtShowStoreDFIn.getText().toString());

        intSDMQty = Integer.parseInt(mtxtShowStoreSDM.getText().toString());
        intSDMQtyIn = Integer.parseInt(mtxtShowStoreSDMIn.getText().toString());

        intDMQty = Integer.parseInt(mtxtShowStoreDM.getText().toString());
        intDMQtyIn = Integer.parseInt(mtxtShowStoreDMIn.getText().toString());

        //????????? Pack ?????????????????????????????? 1 ???????????????????????? + - ???????????????
        if (intPackQty==1) {
            if (intQtyIn > intQty) {
                mtxtShowStoreQtyIn.setText(String.valueOf(intQty));
                intQtyIn = Integer.parseInt(mtxtShowStoreQtyIn.getText().toString());
                if (intQty != 0) {
                    strAlertMessage = "?????????????????????????????????????????????\n ?????????????????????????????????????????????????????????";
                } else {
                    strAlertMessage = "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //???????????????????????? Scan partnid ?????????
                blnWaitScanSerialno = false;
                metScanStorePart.setHint("Scan Partid");
            }
            if (intDFQtyIn > intDFQty) {
                mtxtShowStoreDFIn.setText(String.valueOf(intDFQty));
                intDFQtyIn = Integer.parseInt(mtxtShowStoreDFIn.getText().toString());
                if (intDFQty != 0) {
                    strAlertMessage = "?????????????????????????????????????????????????????????????????????\n ?????????????????????????????????????????????????????????";
                } else {
                    strAlertMessage = "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //???????????????????????? Scan partnid ?????????
                blnWaitScanSerialno = false;
                metScanStorePart.setHint("Scan Partid");
            }
            if (intSDMQtyIn > intSDMQty) {
                mtxtShowStoreSDMIn.setText(String.valueOf(intSDMQty));
                intSDMQtyIn = Integer.parseInt(mtxtShowStoreSDMIn.getText().toString());
                if (intSDMQty != 0) {
                    strAlertMessage = "??????????????????????????????????????? Scrap\n ?????????????????????????????????????????????????????????";
                } else {
                    strAlertMessage = "?????????????????????????????????????????????????????????????????? Scrap ????????????????????????????????????????????????????????????";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //???????????????????????? Scan partnid ?????????
                blnWaitScanSerialno = false;
                metScanStorePart.setHint("Scan Partid");
            }
            if (intDMQtyIn > intDMQty) {
                mtxtShowStoreDMIn.setText(String.valueOf(intDMQty));
                intDMQtyIn = Integer.parseInt(mtxtShowStoreDMIn.getText().toString());
                if (intDMQty != 0) {
                    strAlertMessage = "???????????????????????????????????????????????????\n ?????????????????????????????????????????????????????????";
                } else {
                    strAlertMessage = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                }
                ShowAlertDialog(R.string.alertdialog_error, "Error", strAlertMessage, R.drawable.alertdialog_ic_error);
                //???????????????????????? Scan partnid ?????????
                blnWaitScanSerialno = false;
                metScanStorePart.setHint("Scan Partid");
            }
        }

        mbtnConfirmStorePart.setEnabled(false);
        mbtnReset_Part.setEnabled(false);
        mbtnHold_Part.setEnabled(false);

        //??????????????? update ????????????????????? Tmp_RFCheckin ?????????????????????
        if(! mtxtShowScanStoreQty.getText().toString().isEmpty() && ! mtxtShowScanStoreQty.getText().toString().equals("") && ! mtxtShowScanStoreQty.getText().toString().equals("0") ){
            if ( !(intQtyIn > intQty)) {
                Execute_Mobile_Upd_Tmp_RFCheckIn(mtxtShowScanStoreQty.getText().toString());
            }
        }


        if (! blnWaitScanSerialno && (intQtyIn != 0 || intDFQtyIn != 0 || intSDMQtyIn != 0 || intDMQtyIn != 0)) {

            if (  (intQtyIn <= intQty) &&  (intDFQtyIn <= intDFQty) &&  (intSDMQtyIn <= intSDMQty) &&  (intDMQtyIn <= intDMQty) ) {
                //???????????? Hold ??????????????????????????????
                mbtnHold_Part.setEnabled(true);
                mbtnReset_Part.setEnabled(true);
            }
            if (String.valueOf(intQty).trim().equals(String.valueOf(intQtyIn).trim()) &&
                    String.valueOf(intDFQty).trim().equals(String.valueOf(intDFQtyIn).trim()) &&
                    String.valueOf(intSDMQty).trim().equals(String.valueOf(intSDMQtyIn).trim()) &&
                    String.valueOf(intDMQty).trim().equals(String.valueOf(intDMQtyIn).trim())
            ) {
                mbtnConfirmStorePart.setEnabled(true);
                mbtnReset_Part.setEnabled(false);
                mbtnHold_Part.setEnabled(false);
                metScanStorePart.setEnabled(false);
            }
        }
    }

    private void GetProductDetail() {
        String tag = "6SepV2";
        try {
            ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetProductDetail();
            getProductDesc.execute(getDataBaseName, strStorePartnid, strURL);

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

                mtxtShowStorePartno.setText(clsProduct.PartNo+" "+clsProduct.PartDes);
                strStorePackQty=  clsProduct.PackQty;
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                dblPackQty = Double.parseDouble(strStorePackQty);
                intPackQty = dblPackQty.intValue();

                blnStorePartHaveLabel=clsProduct.PrintLabel.toUpperCase().equals("Y");
                blnStorePartHaveSerialNo=clsProduct.PartHaveSerialNo.toUpperCase().equals("Y");

                strTableName="PartTube";
                strField="PartNid";
                strCondition="PartNid='" + strStorePartnid + "'";
                strURL=strServerAddress+myConstant.urlMobile_CountRecord();
                strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
                blnStorePartTube=!strReturnValue.equals("0");

            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }




    private void GetStorePartDetail() {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetStorePartDetail();
            executeThreeParameter.execute("strDataBaseName",strDataBaseName,
                    "strDocno",strStoreDocno,
                    "strPartnid", strStorePartnid,
                    strURL);

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
                clsStorePartDetail = gson.fromJson(resultJSON.toString(), StorePartDetail.class);

                strStoreTmpTime=clsStorePartDetail.TmpTime;

                //???????????????????????????????????????????????????????????????
                mtxtShowStoreQty.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.Qty)));
                mtxtShowStoreDF.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyDf)));
                mtxtShowStoreSDM.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyScrap)));
                mtxtShowStoreDM.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyDM)));
                //??????????????????????????? part ???????????????????????? label ??????????????????????????????????????????????????????????????????????????? ???????????? ???????????? Parttube ????????????????????????????????????
                if  ((blnStorePartHaveLabel && ! blnStoreScanFast && ! blnStorePartTube)){
                    mtxtShowStoreQtyIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyIn)));
                    mtxtShowStoreDFIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyDfIn)));
                    mtxtShowStoreSDMIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyScrapIn)));
                    mtxtShowStoreDMIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyDMIn)));
                }
                else {
                    mtxtShowStoreQtyIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.Qty)));
                    mtxtShowStoreDFIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyDf)));
                    mtxtShowStoreSDMIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyScrap)));
                    mtxtShowStoreDMIn.setText(String.format("%.0f",Double.parseDouble(clsStorePartDetail.QtyDM)));
                }
                mtxtShowStoreLocation.setText(clsStorePartDetail.LCCode.toString());
                mtxtShowScanStoreQty.setText("0");
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
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????? Scan ????????????
                Execute_Mobile_INS_Tmp_RFCheckIn_ChkDigit(strDocno, userLogin.STFcode.trim(), strPartNid,"N",strKind,strDigitNo,mtxtShowScanStoreQty.getText().toString());
            }
            else{
                //???????????????????????????????????????????????????????????? Scan ?????????
                intCheckScanDuplicate=intCheckScanDuplicate+1;
                //??????????????????????????? Scan ?????????????????? 2 ??????????????? ???????????????????????? 3 ??????????????????????????????????????????????????? Barcode?????????
                if (intCheckScanDuplicate<3) {
                    resultBoolean = Boolean.TRUE;
                    strAlertMessage = "????????? Scan ?????????????????????????????????????????????.";
                    globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Check_ScanDuplicate ==> " + e.toString());
        }
        return resultBoolean;
    }



    private void Execute_Mobile_Confirm_StorePart(String strTmpTime,String strDocno, String strPartNid){
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameterParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Confirm_StorePart();
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
                strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_StorePart() ;
                ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);

            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult = gson.fromJson(resultJSON.toString(), Result.class);
                strReturnValue = clsResult.ResultID.toString();

                if (! strReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage="Error Execute "+ myConstant.urlMobile_Confirm_StorePart() ;
                    ShowAlertDialog(R.string.alertdialog_error,"Error.",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
                    //???????????????????????????????????????????????????????????????????????????????????????
                    strAlertMessage=clsResult.ResultMessage;
//                    if (strAlertMessage.trim().toUpperCase().equals("COMPLETE")) {
//                        strAlertMessage="Complete Storing";
//                        ShowAlertDialog(R.string.alertdialog_success, "Complete.", strAlertMessage, R.drawable.alertdialog_ic_success);
//                    }
                    ShowSelectLocation(strStoreDocno);
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


    private void GetStoreDocType(String strDocno){
        strURL=strServerAddress+myConstant.urlMobile_GetDoctype();
        strReturnValue = globalUtility.ReturnValue_ExecuteTwoParameter(getApplicationContext(),"strDataBaseName",strDataBaseName,"strDocno",strDocno,strURL);
        strStoreDocType=strReturnValue;

        strReturnValue="0";
        strURL=strServerAddress+myConstant.urlMobile_CountRecord();
        if (strStoreDocType.equals("CRO")) {
            strTableName="CRO";
            strField="CRONO";
            strCondition="CRONO='"+strStoreDocType.trim()+"' and rtrim(IssFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strStoreDocType.equals("CMP")) {
            strTableName="COMPOS";
            strField="CMPNO";
            strCondition="CMPNO='"+strStoreDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strStoreDocType.equals("CPB")) {
            strTableName="CPB";
            strField="CPBNO";
            strCondition="CPBNO='"+strStoreDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strStoreDocType.equals("RLC")) {
            strTableName="RELOCATE";
            strField="RLCNO";
            strCondition="RLCNO='"+strStoreDocno.trim()+"' and rtrim(ScanFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }
        else if (strStoreDocType.equals("ORC")) {
            strTableName="OPORCV";
            strField="Rcvno";
            strCondition="Rcvno='"+strStoreDocno.trim()+"' and rtrim(RcvFastCode)<>''";
            strReturnValue=globalUtility.CountRecord(getApplicationContext(),strDataBaseName,strTableName,strCondition,strURL);
        }

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        blnStoreScanFast=!strReturnValue.equals("0");
    }




    private void createListViewDocument() {
        //ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";

        try {
            ExecuteGetListDocument_Receive_Store executeGetListDocument_Receive_Store = new ExecuteGetListDocument_Receive_Store(getApplicationContext());
            //P ????????? VW_Mobile_ListDocNo_Storeing  ????????????????????????????????????????????? ???????????????????????????
            strURL=strServerAddress+myConstant.urlMobile_ListDocument_Store();
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
                    strStoreDocno = metSelectDocno.getText().toString();
                    Execute_Mobile_INS_Tmp_RFCheckIn(strStoreDocno.trim());
                    ShowSelectLocation(strStoreDocno);
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



    private void Execute_Mobile_Upd_Tmp_RFCheckIn(String strQty) {
        String tag = "6SepV2";
        try {
            ExecuteSixParameter executeSixParameter = new ExecuteSixParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_UPD_Tmp_RFCheckIn();
            executeSixParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno", strStoreDocno,
                    "strPartnid", strStorePartnid,
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
    
    

    private void createListViewPart(final String strDocno,final String strLocation) {
        strStorePartnid="";
        blnListPartEmpty=false;
        String tag = "6SepV2";
        strTableName=" tmp_rfcheckin A inner join part B ON A.partnid=B.partnid ";
        strField="A.PartNid,rtrim(B.partno)+' '+B.partdes  as Description"  ;
        strCondition = "A.Docno='" + strDocno + "' and StoreCheckin<>'' and StoreCheckin<>'Y' and B.LCCOde='" + strLocation +"'";
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
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? Location ???????????????
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

    private void Hold_Storing_Document(){
        ResetAllWarning();
        blnWarning_Hold_Document =true;
        strAlertMessage="????????????????????????????????????????????????????????????????????????????????? \n ???????????????????????????????????????????????????" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }

    private void Reset_Storing_Document(){
        ResetAllWarning();
        blnWarning_Reset_Document =true;
        strAlertMessage="????????????????????????????????????????????????????????????????????????????????? \n ?????????????????????????????????????????????????????????" ;
        ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
    }


    private void Hold_Storing_Part(){
        Execute_Hold_Reset_Storing(strStoreDocno,strStorePartnid,"H");
        ShowSelectLocation(strStoreDocno);
    }

    private void Reset_Storing_Part(){
        Execute_Hold_Reset_Storing(strStoreDocno,strStorePartnid,"R");
        ShowSelectLocation(strStoreDocno);
    }


    private void Execute_Hold_Reset_Storing(String strDocNo,String strPartnid,String strOption) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_Store_Hold_Reset();
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
                strAlertMessage="Error Execute " + myConstant.urlMobile_Store_Hold_Reset();
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
//
//    private void Execute_Hold_Reset_Storing1(String strDocNo,String strPartnid,String strOption) {
//        String tag = "6SepV2";
//        try {
//            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
//            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
//                    "strDocNo",strDocNo,
//                    "strPartnid",strPartnid,
//                    "strOption",strOption,
//                    "strStfCode",userLogin.STFcode.trim(),
//                    myConstant.urlMobile_Store_Hold_Reset());
//            String resultJSON = executeFiveParameter.get();
//            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
//            //for Not User Pacel
//            if (resultJSON.equals("[]")) {
//                strResultReturnValue = "Error Execute";
//                strAlertMessage="Error Execute " + myConstant.urlMobile_Store_Hold_Reset();
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
////                else{
////                    strAlertMessage=clsResult.ResultMessage.toString();
////                    ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage,R.drawable.alertdialog_ic_success);
////                }
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
                    //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (blnWarning_Reset_Document){
                        Execute_Hold_Reset_Storing(strStoreDocno,"","R");
                        InitializeData();
                    }else if (blnWarning_Hold_Document){
                        Execute_Hold_Reset_Storing(strStoreDocno,"","H");
                        InitializeData();
                    }
//                    else if (blnWarning_Confirm){
//                        Execute_Mobile_Confirm_StorePart(strStoreTmpTime,strStoreDocno,strStorePartnid);
//                        //???????????????????????????????????? InitializeData() ???????????????????????????????????????????????????????????????????????? Part ??????????????????????????????????????????????????????
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