package pneumax.mobilesystembygolaung.activity;

import android.content.Intent;
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
import pneumax.mobilesystembygolaung.connected.ExecuteGetListDocument;
import pneumax.mobilesystembygolaung.connected.ExecuteThreeParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.ListDocumentAdapter;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class ConfirmActivity extends AppCompatActivity {

    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL;

    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;

    //From Layout
    Button mbtnConfirm;
    CircularImageView mbtnSearch;
    ImageView mimgBackTop;
    EditText metScanDocument,metSelectDocno;
    TextView mtvShowDocSelected,metShowStaffConfirm;
    Group mgplvListDocument,mgpStaffConfirm;

    View updateview;// above oncreate method
    ListView mlvListDocument;


    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_confirm);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility=new GlobalUtility();

        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
    }

    private void InitializeData() {
        mlvListDocument.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mlvListDocument.setAdapter(null);
        mbtnSearch.setImageResource(R.drawable.ic_search_blue);


        mbtnSearch.setVisibility(View.VISIBLE);
        metScanDocument.setEnabled(true);
        mgplvListDocument.setVisibility(View.GONE);
        mgpStaffConfirm.setVisibility(View.GONE);
        ClearData();
        metScanDocument.requestFocus();
    }


    private void ClearData() {
        mgpStaffConfirm.setVisibility(View.GONE);
        metScanDocument.setEnabled(true);
        metSelectDocno.setText("");
        mtvShowDocSelected.setText("");
        metShowStaffConfirm.setText("");
        //ใส่แล้ว Error มันวน Loop
        //metScanDocument.setText("");
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
        mtvShowDocSelected=(TextView) findViewById(R.id.tvShowDocSelected);
        metShowStaffConfirm=(EditText) findViewById(R.id.etShowStaffConfirm);

        mlvListDocument =(ListView) findViewById(R.id.lvListDocument);
        mgplvListDocument=(Group) findViewById(R.id.gplvListDocument);
        mgpStaffConfirm=(Group) findViewById(R.id.gpStaffConfirm);

        mbtnSearch=(CircularImageView) findViewById(R.id.btnSearch);
        mbtnConfirm=(Button) findViewById(R.id.btnConfirm);
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
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

              if (metScanDocument.isEnabled()) {
                  if (!metScanDocument.getText().toString().isEmpty() && !metScanDocument.getText().toString().equals("")) {
                      metScanDocument.setText("");
                      blnManualInput = false;
                      if (s.toString().length() == 10) {
                          strScan = s.toString().trim();
                          metSelectDocno.setText(strScan);
                          GetCustName(strScan);
                      } else {
                          ClearData();
                          strAlertMessage = "ไม่พบเอกสารที่คุณ Scan";
                          mtvShowDocSelected.setText(strAlertMessage);
                          //22-01-2021 ยังทำไม่สำเร็จในการเรียกจากส่วนกลาง
                          //globalVar.ShowAlertDialog(getApplicationContext(),R.string.alertdialog_error,"คำเตือน:",strAlertMessage,R.drawable.alertdialog_ic_error);
                          ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                      }
                  }
                  //mbtnSearch.setText(getResources().getString(R.string.search));
                  mbtnSearch.setImageResource(R.drawable.ic_search_blue);
                  mgplvListDocument.setVisibility(View.GONE);
              }
              else {
                  if(! s.toString().equals("")){
                        strAlertMessage = "กรุณากดปุ่ม Confirm นะครับ.";
                        ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        metScanDocument.setText("");
                  }
              }
          }
      });

        mbtnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListViewDocument();
            }
        });


        mbtnConfirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 01/03/2021 สโตร์ไม่ต้องการ CLick หลายครั้ง
//                strAlertMessage="คุณมั่นใจว่า สินค้าที่จัดมาถูกต้องแล้วใช่หรือไม่" ;
//                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage,R.drawable.alertdialog_ic_warning);
                ExecuteConfirmDocument(metSelectDocno.getText().toString());
                InitializeData();
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
                    InitializeData();
                    }
            }
        });
    }


    private void ShowConfirm() {
        mbtnSearch.setVisibility(View.GONE);
        metScanDocument.setEnabled(false);

        strURL=strServerAddress+myConstant.urlMobile_ReturnValue();
        strReturnValue = globalUtility.GetStaffName(getApplicationContext(),strDataBaseName,userLogin.STFcode.trim(),strURL);

        mgplvListDocument.setVisibility(View.GONE);
        mgpStaffConfirm.setVisibility(View.VISIBLE);
        metShowStaffConfirm.setText(strReturnValue);

        mbtnConfirm.setEnabled(true);
    }


    private void GetCustName(String strDocno) {
        mbtnConfirm.setEnabled(false);
        if (CheckConfirmDocument(metSelectDocno.getText().toString())){
            strTableName = "VW_Mobile_ListDocNo_Checking";
            strField = "CSName";
            strCondition = "Docno='" + strDocno.trim() + "'";
            strURL = strServerAddress + myConstant.urlMobile_ReturnValue();
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(), "strDataBaseName", strDataBaseName,
                    "strTableName", strTableName, "strField", strField,
                    "strCondition", strCondition, strURL);
            mtvShowDocSelected.setText(strReturnValue);
            //เช็คว่ามีอยู่ในเอกสารรอตรวจสอบหรือไม่
            strField = "Docno";
            strReturnValue = globalUtility.ReturnValue_ExecuteFourParameter(getApplicationContext(), "strDataBaseName", strDataBaseName,
                    "strTableName", strTableName, "strField", strField,
                    "strCondition", strCondition, strURL);
            if (strReturnValue.equals("")) {
                strAlertMessage = "เอกสารที่คุณ Scan\n ไม่อยู่ในรายการที่รอตรวจเช็คนะครับ ";
                ShowAlertDialog(R.string.alertdialog_error, "Error! ", strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                if (mtvShowDocSelected.getText().toString().equals(""))
                    mtvShowDocSelected.setText(strReturnValue);
                ShowConfirm();
            }
        }
    }


    private boolean CheckConfirmDocument(String strDocNo)  {
        String tag = "6SepV2";
        Boolean resultBoolean = Boolean.TRUE;
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
            strURL=strServerAddress + myConstant.urlMobile_ConfirmDocument();
            executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strStfCode","",
                    strURL);
            String resultJSON = executeThreeParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_ConfirmDocument();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                resultBoolean = Boolean.FALSE;
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage=clsResult.ResultMessage.toString();;
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                    resultBoolean = Boolean.FALSE;
                }
            }

        } catch (Exception e) {
            Log.d(tag, "e Confirm Document ==> " + e.toString());
        }
        return resultBoolean;
    }




    private void ExecuteConfirmDocument(String strDocNo) {
            String tag = "6SepV2";
            try {
                ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(getApplicationContext());
                strURL=strServerAddress + myConstant.urlMobile_ConfirmDocument();
                executeThreeParameter.execute("strDataBaseName", strDataBaseName,
                        "strDocNo",strDocNo,
                        "strStfCode",userLogin.STFcode.trim(),
                        strURL);
                String resultJSON = executeThreeParameter.get();
                resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
                //for Not User Pacel
                if (resultJSON.equals("[]")) {
                    strResultReturnValue = "Error Execute";
                    strAlertMessage="Error Execute " + myConstant.urlMobile_ConfirmDocument();
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



    private void createListViewDocument() {
        //mbtnSearch.setText(getResources().getString(R.string.refresh));
        mbtnSearch.setImageResource(R.drawable.ic_refresh_blue);
        mgplvListDocument.setVisibility(View.VISIBLE);
        ClearData();

        ListView listView = findViewById(R.id.lvListDocument);
        String tag = "6SepV2";
        try {
            ExecuteGetListDocument executeGetListDocument = new ExecuteGetListDocument(getApplicationContext());
            //C คือ VW_Mobile_ListDocNo_Checking  แสดงเอกสารที่รอ Confirm
            strURL=strServerAddress + myConstant.urlMobile_ListDocument();
            executeGetListDocument.execute(strDataBaseName, "C",userLogin.STFcode.trim(), strURL);
            String resultJSON = executeGetListDocument.get();
            //resultJSON = globalVar.JsonXmlToJsonString(resultJSON);
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
                    ShowConfirm();
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
//                    ExecuteConfirmDocument(metSelectDocno.getText().toString());
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