package pneumax.mobilesystembygolaung.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class ClearDocumentError extends AppCompatActivity {
    StaffLogin userLogin;
    String strDataBaseName;
    //parameter สำกรับส่งไป Execute
    String strServerAddress;
    String strTableName,strField,strCondition,strURL;

    MyConstant myConstant;
    GlobalVar globalVar;
    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;

    //From Layout
    ImageView mimgBackTop;
    RadioButton mrbtnPicking,mrbtnReceive,mrbtnStore;
    EditText metScanDocumentError,metSelectDocnoError;
    Button   mbtnConfirmClearDocno;
    CheckBox mchkPartnotfinish;

    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_clear_document_error);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();

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
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
        mrbtnPicking =(RadioButton) findViewById(R.id.rbtnPicking);
        mrbtnReceive =(RadioButton) findViewById(R.id.rbtnReceive);
        mrbtnStore =(RadioButton) findViewById(R.id.rbtnStore);

        mchkPartnotfinish=(CheckBox) findViewById(R.id.chkPartnotfinish);

        metScanDocumentError=(EditText) findViewById(R.id.etScanDocumentError);
        metSelectDocnoError=(EditText) findViewById(R.id.etSelectDocnoError);
        mbtnConfirmClearDocno=(Button) findViewById(R.id.btnConfirmClearDocno);

    }

    private void InitializeData() {
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
        mrbtnPicking.setButtonTintList(colorStateList);//set the color tint list
        mrbtnPicking.invalidate(); //could not be necessary
        mrbtnReceive.setButtonTintList(colorStateList);//set the color tint list
        mrbtnReceive.invalidate(); //could not be necessary
        mrbtnStore.setButtonTintList(colorStateList);//set the color tint list
        mrbtnStore.invalidate(); //could not be necessary

        mchkPartnotfinish.setButtonTintList(colorStateList);//set the color tint list
        mchkPartnotfinish.invalidate(); //could not be necessary
        ClearData();
    }

    private void ClearData() {
        mbtnConfirmClearDocno.setEnabled(false);
        metSelectDocnoError.setText("");
        metScanDocumentError.requestFocus();
    }


    private void SetEvent() {

        metScanDocumentError.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanDocumentError.setText(metScanDocumentError.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });

        metScanDocumentError.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String strScan;

                s = s.toString().toUpperCase().trim();
                //ถ้ามีการป้อนเข้ามาตัวเดียวแสดงว่าเป็นการป้อน manual
                if(userLogin.DPCode.equals("MIS") &&  (s.length()==1 || blnManualInput)) {
                    blnManualInput = true;
                    return;
                }
                if (metScanDocumentError.isEnabled()) {
                    if (!metScanDocumentError.getText().toString().isEmpty() && !metScanDocumentError.getText().toString().equals("")) {
                        metScanDocumentError.setText("");
                        blnManualInput = false;
                        metScanDocumentError.setHint(s.toString().trim());
                        if (s.toString().length() == 10) {
                            strScan = s.toString().trim();
                            metSelectDocnoError.setText(strScan);
                            mbtnConfirmClearDocno.setEnabled(true);
                        } else {
                            ClearData();
                            strAlertMessage = "คุณ Scan เอกสารไม่ถูต้องนะครับ";
                            ShowAlertDialog(R.string.alertdialog_error, "คำเตือน:", strAlertMessage, R.drawable.alertdialog_ic_error);
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "สินค้าที่คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการตรวจนับนะครับ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        mbtnConfirmClearDocno.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mchkPartnotfinish.isChecked()) {
                    strAlertMessage = "คุณต้องการ Clear เฉพาะ Part ที่ยังไม่เสร็จใช่หรือไม่.";
                }
                else {
                    strAlertMessage = "คุณต้องการ Clear ทุก Part ในเอกสารนี้ใช่หรือไม่.";
                }
                ShowAlertDialog(R.string.alertdialog_warning,getResources().getString(R.string.confirmcorrect),strAlertMessage, R.drawable.alertdialog_ic_warning);
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



    private void ExecuteClearDocumentError(String strDocNo,String strOptionType,String strOptionClear) {
        String tag = "6SepV2";
        try {
            ExecuteFiveParameter executeFiveParameter = new ExecuteFiveParameter(getApplicationContext());
            strURL = strServerAddress+myConstant.urlMobile_ClearDocNoError();
            executeFiveParameter.execute("strDataBaseName", strDataBaseName,
                    "strDocNo",strDocNo,
                    "strOptionType",strOptionType,
                    "strOptionClear",strOptionClear,                    
                    "strStfCode",userLogin.STFcode.trim(),
                    strURL);
            String resultJSON = executeFiveParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultReturnValue = "Error Execute";
                strAlertMessage="Error Execute " + myConstant.urlMobile_ClearDocNoError();
                ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsResult    = gson.fromJson(resultJSON.toString(), Result.class);
                strResultReturnValue = clsResult.ResultID.toString();
                if (!strResultReturnValue.toString().toUpperCase().equals("SUCCESS")) {
                    strAlertMessage= clsResult.ResultMessage.toString();
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                }
                else{
                    strAlertMessage="Clear Document Error >> " + strDocNo + "Complete" ;
                    ShowAlertDialog(R.string.alertdialog_success,"Complete.",strAlertMessage, R.drawable.alertdialog_ic_success);
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
                    String strOptionType,strOptionClear;
                    if (mrbtnPicking.isChecked()) {
                        strOptionType="P";
                    }
                    else if (mrbtnReceive.isChecked()) {
                        strOptionType="R";
                    }else{
                        strOptionType="S";
                    }

                    if (mchkPartnotfinish.isChecked()) {
                        strOptionClear="ITEM"; //เคลียเฉพาะ Part ที่่ยังทำไม่จบ
                    }
                    else{
                        strOptionClear="ALL"; //เคลียทุก Part
                    }                    
                    ExecuteClearDocumentError(metSelectDocnoError.getText().toString().trim(),strOptionType,strOptionClear);
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