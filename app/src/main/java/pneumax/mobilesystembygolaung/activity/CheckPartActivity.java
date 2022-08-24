package pneumax.mobilesystembygolaung.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteGetPartTubeDetail;
import pneumax.mobilesystembygolaung.connected.ExecuteGetProductDesc;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.PartTubeDetail;
import pneumax.mobilesystembygolaung.object.Product;
import pneumax.mobilesystembygolaung.object.StaffLogin;

import static pneumax.mobilesystembygolaung.manager.GlobalVar.getDataBaseName;

public class CheckPartActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    View AlertDialogView;
    String strAlertMessage;

    //parameter
    StaffLogin userLogin;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strServerAddress;
    String strTableName,strField,strCondition,strURL;

    EditText metScanPartnid;
    TextView metShowPartnid,metShowPartno,metShowPartDesc,metShowPackqty,metShowWarehouse,
            metShowLocation,metShowLocation2,metShowLocationWH,metShowLocationWH2,
            metShowDigitno, metShowStatus,metShowFullLength,metShowDamageLength;
    ImageView mimgBackTop;

    Product clsProduct;
    PartTubeDetail clsPartTubeDetail;
    String strPartnid,strDigitno, strStatus;

    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);
        setContentView(R.layout.activity_check_part);

        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
    }


    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);

    }
    private void InitializeData() {
        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        metScanPartnid.requestFocus();
        ClearData();
    }

    private void ClearData() {
        metShowPartnid.setText("");
        metShowPartno.setText("");
        metShowPartDesc.setText("");

        metShowPackqty.setText("");
        metShowWarehouse.setText("");

        metShowLocation.setText("");
        metShowLocationWH.setText("");
        metShowLocationWH.setHint("");
        metShowLocation2.setText("");
        metShowLocationWH2.setText("");
        metShowLocationWH2.setHint("");

        metShowDigitno.setText("");
        metShowStatus.setText("");

        metShowFullLength.setText("");
        metShowDamageLength.setText("");

    }


    private void BindWidgets() {
        metScanPartnid = (EditText) findViewById(R.id.etScanPartnid);
        metShowPartnid = (EditText) findViewById(R.id.etShowPartnid);
        metShowPartno = (EditText) findViewById(R.id.etShowPartno);
        metShowPartDesc = (EditText) findViewById(R.id.etShowPartDesc);
        metShowPackqty = (EditText) findViewById(R.id.etShowPackqty);
        metShowWarehouse = (EditText) findViewById(R.id.etShowWarehouse);
        metShowLocation = (EditText) findViewById(R.id.etShowLocation);
        metShowLocationWH = (EditText) findViewById(R.id.etShowLocationWH);
        metShowLocation2 = (EditText) findViewById(R.id.etShowLocation2);
        metShowLocationWH2 = (EditText) findViewById(R.id.etShowLocationWH2);

        metShowDigitno = (EditText) findViewById(R.id.etShowDigitno);
        metShowStatus = (EditText) findViewById(R.id.etShowStatus);

        metShowFullLength = (EditText) findViewById(R.id.etShowFullLength);
        metShowDamageLength = (EditText) findViewById(R.id.etShowDamageLength);

        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
    }

    private void SetEvent() {
        metScanPartnid.setOnKeyListener(new View.OnKeyListener() {
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
                    metScanPartnid.setText(metScanPartnid.getText().toString()+" ");
                    return true;
                }
                return false;
            }
        });

        metScanPartnid.addTextChangedListener(new TextWatcher() {

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

                if (!metScanPartnid.getText().toString().isEmpty()) {
                    metScanPartnid.setText("");
                    blnManualInput = false;
                    //Barcode แบบเก่า ความยาว 17 มี digit 3 หลัก อันไหม่มี 4หลัก ความยาวเป็น 18
                    if (s.toString().length()== 17 || s.toString().length()== 18) {
                        arrGetDescFromScanBarcode = globalVar.GetDescFromScanBarcode(s.toString());
                        strPartnid = arrGetDescFromScanBarcode[0];
                        strStatus = arrGetDescFromScanBarcode[1];
                        strDigitno = arrGetDescFromScanBarcode[2];

                        GetProductDetail(strPartnid,strStatus,strDigitno);
                    }
                    else {
                        ClearData();
                        strAlertMessage="Not Found Data !!!";
                        ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage, R.drawable.alertdialog_ic_error);
                    }
                } else {
                        //Toast.makeText(getApplicationContext(), "สินค้าที่คุณ Scan ไม่ตรงกับ Part ที่คุณต้องการตรวจนับนะครับ", Toast.LENGTH_SHORT).show();
                }
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





    private void GetProductDetail(String strPartNid,String strStatus,String strDigitno) {
        String tag = "6SepV2";
        String strCondition;
        try {
            ExecuteGetProductDesc getProductDesc = new ExecuteGetProductDesc(getApplicationContext());
            strURL=strServerAddress+ myConstant.urlMobile_GetProductDetail();
            getProductDesc.execute(getDataBaseName, strPartNid,strURL);

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
                metShowPartnid.setText(clsProduct.PartNID);
                metShowPartno.setText(clsProduct.PartNo);
                metShowPartDesc.setText(clsProduct.PartDes);

                metShowPackqty.setText(clsProduct.PackQty);
                metShowWarehouse.setText(clsProduct.WHcode);
                metShowLocation.setText(clsProduct.LCcode);
                metShowLocationWH.setText(clsProduct.Warehouse);
                metShowLocation2.setText(clsProduct.LCcode2);
                metShowLocationWH2.setText(clsProduct.Warehouse2);

                metShowDigitno.setText(strDigitno);
                metShowStatus.setText(GetStringStatus(strStatus));
                //แสดงรายละเอียด Tube เพิ่มเติม
                GetPartTubeDetail(clsProduct.PartNID,strDigitno)     ;
            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }


    private void GetPartTubeDetail(String strPickTubePartnid,String strDigitno) {
        String tag = "6SepV2";
        try {
            ExecuteGetPartTubeDetail executeGetPartTubeDetail = new ExecuteGetPartTubeDetail(getApplicationContext());
            strURL=strServerAddress+  myConstant.urlMobile_GetPartTubeDetail();
            executeGetPartTubeDetail.execute(strDataBaseName, strPickTubePartnid,strDigitno,strURL);

            String resultJSON = executeGetPartTubeDetail.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                metShowFullLength.setText("-");
                metShowDamageLength.setText("-");
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsPartTubeDetail = gson.fromJson(resultJSON.toString(), PartTubeDetail.class);

                metShowFullLength.setText(clsPartTubeDetail.Onhand);
                metShowDamageLength.setText(clsPartTubeDetail.DMLength);
            }
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
    }



   private String GetStringStatus(String strStatus){
    if (strStatus.equals("0")) {
        return "Normal";
    }
     else  if (strStatus.equals("1")) {
           return "Defect";
       }
    else  if (strStatus.equals("2")) {
        return "Damage";
    }
    else {
        return "Scrap";
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