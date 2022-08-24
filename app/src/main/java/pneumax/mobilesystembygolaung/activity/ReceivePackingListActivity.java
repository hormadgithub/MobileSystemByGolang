
package pneumax.mobilesystembygolaung.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument_Receive_Store;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocument_Receive_Store_Adapter;
import pneumax.mobilesystembygolaung.manager.ListPartAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class ReceivePackingListActivity extends AppCompatActivity {
    StaffLogin userLogin;
    String strDataBaseName;
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;

    //parameter สำกรับส่งไป Execute
    String strServerAddress;
    String strTableName,strField,strCondition,strURL;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;
    //From Layout
    ImageView mimgBackTop;
    RadioButton mrbtnProductToWarehoue,mrbtnCancelProductToWarehoue,mrbtnProductCheckComplete;
    EditText metScanPackingList,metSelectPackingList;
    Button mbtnConfirmPackingList;
    CircularImageView mbtnSearch;
    CheckBox mchkAllIem;
    Group mgpProductChecked;

    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;

    ListView mlvListPart,mlvListDocument;
    Boolean blnListPartEmpty;
    View updateview;// above oncreate method
    String strSelectPartnid,strSelectDoctype,strSelectDocdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_receive_packinglist);

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
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
        mrbtnProductToWarehoue =(RadioButton) findViewById(R.id.rbtnProductToWarehoue);
        mrbtnCancelProductToWarehoue =(RadioButton) findViewById(R.id.rbtnCancelProductToWarehoue);
        mrbtnProductCheckComplete =(RadioButton) findViewById(R.id.rbtnProductCheckComplete);
        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);

        metScanPackingList=(EditText) findViewById(R.id.etScanPackingList);
        metSelectPackingList=(EditText) findViewById(R.id.etSelectPackingList);
        mbtnConfirmPackingList=(Button) findViewById(R.id.btnConfirmPackingList);

        mchkAllIem = (CheckBox) findViewById(R.id.chkAllIem);
        mlvListDocument=(ListView) findViewById(R.id.lvListDocument);
        mlvListPart =(ListView) findViewById(R.id.lvListPart);
        mgpProductChecked = (Group)  findViewById(R.id.gpProductChecked);

    }

    private void InitializeData() {
        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDocument.setAdapter(null);

        mlvListPart.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListPart.setAdapter(null);
        mbtnSearch.setImageResource(R.drawable.ic_search_blue);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        getResources().getColor(R.color.gray_700) //disabled
                        ,getResources().getColor(R.color.orange_700) //enabled

                }
        );
        mrbtnProductToWarehoue.setButtonTintList(colorStateList);//set the color tint list
        mrbtnProductToWarehoue.invalidate(); //could not be necessary

        mrbtnCancelProductToWarehoue.setButtonTintList(colorStateList);//set the color tint list
        mrbtnCancelProductToWarehoue.invalidate(); //could not be necessary

        mrbtnProductCheckComplete.setButtonTintList(colorStateList);//set the color tint list
        mrbtnProductCheckComplete.invalidate(); //could not be necessary

        mchkAllIem.setButtonTintList(colorStateList);//set the color tint list
        ClearData();
    }

    private void ClearData() {
        mbtnSearch.setVisibility(View.INVISIBLE);
        mlvListDocument.setVisibility(View.INVISIBLE);

        mbtnConfirmPackingList.setEnabled(false);
        metSelectPackingList.setText("");
        mgpProductChecked.setVisibility(View.GONE);
        strSelectPartnid="";
        metScanPackingList.requestFocus();
    }


    private void SetEvent() {
        mbtnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlvListDocument.setVisibility(View.VISIBLE);
                mgpProductChecked.setVisibility(View.INVISIBLE);
                createListViewDocument();
            }
        });

        metScanPackingList.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanPackingList.setText(metScanPackingList.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });


        metScanPackingList.addTextChangedListener(new TextWatcher() {
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

                if (metScanPackingList.isEnabled()) {
                    if (!metScanPackingList.getText().toString().isEmpty() && !metScanPackingList.getText().toString().equals("")) {
                        metScanPackingList.setText("");
                        blnManualInput = false;
                        blnManualInput=false;
                        if (s.toString().length() == 10) {
                            metSelectPackingList.setText(s.toString().trim());
                            CheckInputPackingList(metSelectPackingList.getText().toString().trim());
                        } else {
                            ClearData();
                            strAlertMessage = "ไม่พบ Packing ที่คุณ Scan";
                            metSelectPackingList.setText(strAlertMessage);
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    }
                }
            }
        });

        mrbtnProductToWarehoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mrbtnProductToWarehoue.isChecked()){
                    ClearData();
                }
            }
        });

        mrbtnCancelProductToWarehoue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mrbtnCancelProductToWarehoue.isChecked()){
                    ClearData();
                }
            }
        });

        mrbtnProductCheckComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mrbtnProductCheckComplete.isChecked()){
                    ClearData();
                    mbtnSearch.setVisibility(View.VISIBLE);
                }
            }
        });



        mchkAllIem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mchkAllIem.isChecked()){
                    mlvListPart.setVisibility(View.INVISIBLE);
                    mbtnConfirmPackingList.setEnabled(true);
                }
                else {
                    mlvListPart.setVisibility(View.VISIBLE);
                    strSelectPartnid="";
                    mbtnConfirmPackingList.setEnabled(false);
                }
            }
        });

        mbtnConfirmPackingList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mrbtnProductToWarehoue.isChecked()) {
                    strAlertMessage = "คุณมั่นใจว่าสิ้นค้ามาถึงโกดังเรียบร้อยแล้วใช่หรือไม่.";
                }
                else if (mrbtnCancelProductToWarehoue.isChecked()) {
                    strAlertMessage = "คุณต้องการยกเเลิกสิ้นค้ามาถึงโกดังใช่หรือไม่.";
                }
                else {
                    if (mchkAllIem.isChecked()) {
                        strAlertMessage = "คูณทำการตรวจเช็คสินค้าที่เข้ามาครบถ้วนทุกรายการแล้วใช่หรือไม่.";
                    }
                    else{
                        strAlertMessage = "คุณทำการตรวจเช็คสินค้ารายการนี้ครบถ้วนแล้วใช่หรือไม่.";
                    }

                }
                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
            }
        });


        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);

            }
        });

    }

    private void CheckInputPackingList(String s) {
        String strScan = "";
        if (metScanPackingList.isEnabled()) {
            if (!metScanPackingList.getText().toString().isEmpty() && !metScanPackingList.getText().toString().equals("")) {
                metScanPackingList.setText("");
                metScanPackingList.setHint(s.toString().trim());
                if (s.length() == 10) {
                    strScan = s.trim();
                    metSelectPackingList.setText(strScan);
                    if (mrbtnProductCheckComplete.isChecked()) {
                        mgpProductChecked.setVisibility(View.VISIBLE);
                        createListViewPart(metSelectPackingList.getText().toString());
                    } else {
                        mbtnConfirmPackingList.setEnabled(true);
                    }
                } else {
                    ClearData();
                    strAlertMessage = "คุณ Scan เอกสารไม่ถูต้องนะครับ";
                    ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                }
            }
        }
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

//            mlvListDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    if (updateview != null){updateview.setBackgroundColor(Color.TRANSPARENT);}
//                    updateview = view;
//                    view.setBackgroundColor(getResources().getColor(R.color.gray_100));
//                    metSelectPackingList.setText(arrListDocumentString[i].toString());
//                    mgpProductChecked.setVisibility(View.VISIBLE);
//                    createListViewPart(metSelectPackingList.getText().toString());
//                }
//            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView Document==> " + e.toString());
        }
    }//createListView



    private void createListViewPart(final String strDocno) {
        mlvListDocument.setVisibility(View.INVISIBLE);
        strSelectPartnid="";
        blnListPartEmpty=false;
        mchkAllIem.setChecked(false);
        //mspnLocation.setSelection(AdapterSpinnerLocation.getPosition(strLocation));
        //ListView listViewPart = findViewById(R.id.lvListPartnid);
        String tag = "6SepV2";
        strTableName=" PackingDT A inner join part B ON A.partnid=B.partnid ";
        strField=" A.PartNid,rtrim(B.partno)+' '+B.partdes  as Description"  ;
        strCondition = " A.Pkno='" + strDocno + "' and  PKdt_ChkFinishDate is null ";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+ myConstant.urlMobile_GetListPart();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition,strURL);
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
                mgpProductChecked.setVisibility(View.INVISIBLE);
                strAlertMessage="ทำการตรวจบครบทุกรายการแล้ว";
                globalUtility.ShowCustomToast(getApplicationContext(), strAlertMessage, "CENTER", 1000, getResources().getColor(R.color.blue_100), getResources().getColor(R.color.black));
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
                    strSelectPartnid=arrListPartNidStrings[i];
                    mbtnConfirmPackingList.setEnabled(true);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }//createListView


    private void Execute_Update_ProductExWH(String strDocno) {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress + myConstant.urlMobile_Update_ProductExWH();
            executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno",strDocno,
                    "strStfCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Update_ProductExWH();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage= clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                }
                else{
                    mbtnConfirmPackingList.setEnabled(false);
//                    strAlertMessage="Update Product to Warehouse Error>> " + strDocno + "Complete" ;
//                    ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage,R.drawable.alertdialog_ic_success);
                }

            }

        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
    }


    private void Execute_Cancel_ProductExWH(String strDocno) {
        String tag = "6SepV2";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress + myConstant.urlMobile_Cancel_ProductExWH();
            executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno",strDocno,
                    "strStfCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Cancel_ProductExWH();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage= clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                }
                else{
                    mbtnConfirmPackingList.setEnabled(false);
                }

            }

        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
    }



    private void Execute_Update_ProductChecked(String strDocno) {
        String tag = "6SepV2";
        String strOption="";
        //กำหนดการส่ง พารามิเตอร์ว่าตรวจสอบครบทุกรายการ หรือ ตรวจสอบบางรายการ
        if (mchkAllIem.isChecked()) {
            strSelectPartnid="";
            strOption="A";
        }
        else{
            strOption="I";
        }

        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL=strServerAddress + myConstant.urlMobile_Update_ProductChecked();
            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocno",strDocno,
                    "strStfCode",userLogin.STFcode.trim(),
                    "strOption",strOption,
                    "strPartnid",strSelectPartnid,
                    strURL);
            String resultJSON = executeFiveParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_Update_ProductChecked();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage= clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                }
                else{
                    mbtnConfirmPackingList.setEnabled(false);
                    //21-06-2021 ทำการแสดงเฉพาะรายการที่เหลือ
                    createListViewPart(strDocno);
//                    strAlertMessage="Product check  Error>> " + strDocno + "Complete" ;
//                    ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage,R.drawable.alertdialog_ic_success);
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
    }


    private void ShowAlertDialog(int intAlertType, String strAlertTitle, String strAlertMessage, int ic_AlertDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AlertDialog);

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
                    if (mrbtnProductToWarehoue.isChecked()) {
                        Execute_Update_ProductExWH(metSelectPackingList.getText().toString().trim());
                    }
                    else if (mrbtnCancelProductToWarehoue.isChecked()) {
                        Execute_Cancel_ProductExWH(metSelectPackingList.getText().toString().trim());
                    }
                    else{
                        Execute_Update_ProductChecked(metSelectPackingList.getText().toString().trim());
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