package pneumax.mobilesystembygolaung.Summary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pneumax.mobilesystembygolaung.R;
import pneumax.mobilesystembygolaung.connected.ExecuteFiveParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
import pneumax.mobilesystembygolaung.connected.ExecuteSixParameter;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class SummaryRFCheckInActivity extends AppCompatActivity{
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
    String strAlertMessage,strSaveOption="";

    //From Layout
    PieChart  mpcSummaryRFCheckIn;
    ImageView mimgBackTop;
    Spinner  mspnStoreEmplyee,mspnWorktype;
    ArrayAdapter<String> AdapterSpinnerLocation;
    Button   mbtnConfirm_SummaryRFCheckIn;
    TextView mtvFromDate,mtvToDate;
    RadioButton mradBtnDay,mradBtnMonth,mradBtnYear;


    private String[]  arrStoreEmployeeStrings,arrWorktypeStrings;
    private String  strStoreEmployeeStrings,strStoreEmployeeSelect;
    private Integer intNumChart;
    private String strWorkTypeString="" ;

    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_summary_rfcheckin);

        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility=new GlobalUtility();

        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
        ClearData();
    }

    private void InitializeData() {
        ColorStateList colorStateRadioButton = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        getResources().getColor(R.color.gray_700) //disabled
                        ,getResources().getColor(R.color.orange_700) //enabled

                }
        );

        mradBtnDay.setButtonTintList(colorStateRadioButton);//set the color tint list
        mradBtnMonth.setButtonTintList(colorStateRadioButton);//set the color tint list
        mradBtnYear.setButtonTintList(colorStateRadioButton);//set the color tint list
        createSpinnerWorktype();
        createSpinnerStoreEmployee();
    }


    private void ClearData() {
        mtvFromDate.setText("");
        mtvFromDate.setHint("วันที่เริ่มต้น");
        mtvToDate.setText("");
        mtvToDate.setHint("วันที่สิ้นสุด");
    }

    public String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);

    }

    private void BindWidgets() {
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
        mspnWorktype=(Spinner) findViewById(R.id.spnWorktype);
        mspnStoreEmplyee=(Spinner) findViewById(R.id.spnStoreEmplyee);
        mpcSummaryRFCheckIn=(PieChart) findViewById(R.id.pcSummaryRFCheckIn);

        mtvFromDate=(TextView) findViewById(R.id.tvFromDate);
        mtvToDate=(TextView) findViewById(R.id.tvToDate);
        mbtnConfirm_SummaryRFCheckIn=(Button) findViewById(R.id.btnConfirm_SummaryRFCheckIn);

        mradBtnDay=(RadioButton) findViewById(R.id.radBtnDay);
        mradBtnMonth=(RadioButton) findViewById(R.id.radBtnMonth);
        mradBtnYear=(RadioButton) findViewById(R.id.radBtnYear);

    }




    private void SetEvent() {
        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_top);
            }
        });

        mtvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateDialog("From Date",mtvFromDate);
            }
        });

        mtvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateDialog("From Date",mtvToDate);
            }
        });

        mbtnConfirm_SummaryRFCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOption="" ,strWorkType="",strLabel="";
                strWorkType=strWorkTypeString.substring(0,1);
                   strLabel=strWorkTypeString;
//                if(strWorkType.equals("P"))
//                    strLabel="จัด";
//                else if(strWorkType.equals("C"))
//                    strLabel="เช็ค";
//                else if(strWorkType.equals("D"))
//                    strLabel="จ่าย";
//                else if(strWorkType.equals("R"))
//                    strLabel="รับ";
//                else if(strWorkType.equals("S"))
//                    strLabel="เก็บ";

                if (mradBtnDay.isChecked())
                    { strOption="D";}
                else if(mradBtnMonth.isChecked())
                    {strOption="M";}
                else if(mradBtnYear.isChecked())
                    {strOption="Y";}

                if (strWorkType.equals("") ) {
                    strAlertMessage="กรุณาเลือกงานที่ต้องการดูก่อนนะครับ.";
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                    return;
                }

                if (strOption.equals("") ) {
                    strAlertMessage="กรุณาเลือกว่าต้องการดูแบบไหนก่อนนะครับ.";
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                    return;
                }

                if ( mtvFromDate.getText().toString().isEmpty() || mtvFromDate.getText().toString().equals("")) {
                    strAlertMessage="กรุณาป้อนวันที่เริ่มต้นก่อนนะครับ.";
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                    return;
                }

                if (mtvToDate.getText().toString().isEmpty() || mtvToDate.getText().toString().equals("")) {
                    strAlertMessage="กรุณาป้อนวันที่่สิ้นสุดด้วยนะครับ.";
                    ShowAlertDialog(R.string.alertdialog_error,"Error!",strAlertMessage,R.drawable.alertdialog_ic_error);
                    return;
                }

                //ผ่านทุกเงื่อนไขแล้ว
                if (strSaveOption.equals(strOption)) {
                    ShowPieChartSummaryRFCheckIn(strOption,strWorkType, strStoreEmployeeSelect,strLabel);
                }
                else {
                    //ต้องเรียก 2 ครั้งเพื่อยกเลิกการ Zoom ก่อนหน้า
                    ShowPieChartSummaryRFCheckIn(strOption,strWorkType, strStoreEmployeeSelect,strLabel);
                    ShowPieChartSummaryRFCheckIn(strOption,strWorkType, strStoreEmployeeSelect,strLabel);
                }
                strSaveOption=strOption;
            }
        });
    }
    public void getDateDialog(String title, final TextView textView) {
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        final int month =Calendar.getInstance().get(Calendar.MONTH);
        final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(textView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        dialog.setTitle(title);
        dialog.show();
    }



    private void createSpinnerWorktype() {
            arrWorktypeStrings = new String[5];
            arrWorktypeStrings[0] = "P-จัดสินค้า";
            arrWorktypeStrings[1] = "C-ตรวจสอบสินค้า";
            arrWorktypeStrings[2] = "D-จ่ายสินค้า";
            arrWorktypeStrings[3] = "R-รับสินค้า";
            arrWorktypeStrings[4] = "S-จัดเก็บสิน";


            AdapterSpinnerLocation = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrWorktypeStrings);
            mspnWorktype.setAdapter(AdapterSpinnerLocation);
             mspnWorktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strWorkTypeString=arrWorktypeStrings[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strWorkTypeString = "";
                }

            });

    }//createSpinnerWeekno



    private void createSpinnerStoreEmployee() {
        strTableName="Staff";
        strField= "rtrim(StfCode)+'-'+rtrim(stfFullName) as ResultReturn";
        if (userLogin.DPCode.equals("MIS"))
            strCondition="DPCode='STO' and StfActive='Y'";
        else
            strCondition="DPCode='STO' and StfCode='" + userLogin.STFcode.trim()+"'";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+ myConstant.urlMobile_GetDataSpinner();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName",strTableName, "strField",strField, "strCondition", strCondition, strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrStoreEmployeeStrings = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrStoreEmployeeStrings[i] = jsonObject.getString("ResultReturn");
            }//for

            AdapterSpinnerLocation = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrStoreEmployeeStrings);
            mspnStoreEmplyee.setAdapter(AdapterSpinnerLocation);
            mspnStoreEmplyee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strStoreEmployeeSelect=arrStoreEmployeeStrings[i].substring(0,4);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strStoreEmployeeStrings = arrStoreEmployeeStrings[0];
                }

            });
        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Loation ==> " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }//createSpinnerWeekno


    private  void  ShowPieChartSummaryRFCheckIn(String  strOption,String  strWorkType,String strPickCode,String strLabel){

        if(mpcSummaryRFCheckIn.getData() != null){
            mpcSummaryRFCheckIn.getData().clearValues();
            mpcSummaryRFCheckIn.clear();
            mpcSummaryRFCheckIn.notifyDataSetChanged();
            mpcSummaryRFCheckIn.clear();
            mpcSummaryRFCheckIn.invalidate() ;
        }

        if (strWorkType.equals("R") || strWorkType.equals("S")) {
            PieDataSet piePackingDataSet = new PieDataSet(pcdataRFCheckinValue( strOption,strWorkType, strPickCode), strLabel);

            piePackingDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            piePackingDataSet.setValueTextColor(Color.BLACK);
            piePackingDataSet.setValueTextSize(16f);

            PieData PieData = new PieData(piePackingDataSet);
            mpcSummaryRFCheckIn.setData(PieData);

            mpcSummaryRFCheckIn.getDescription().setText("");
            mpcSummaryRFCheckIn.animate();
        }
        else {
            PieDataSet piePackingDataSet = new PieDataSet(pcdataRFCheckOutValue(strOption,strWorkType,strPickCode), strLabel);

            piePackingDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            piePackingDataSet.setValueTextColor(Color.BLACK);
            piePackingDataSet.setValueTextSize(16f);

            PieData PieData = new PieData(piePackingDataSet);
            mpcSummaryRFCheckIn.setData(PieData);

            mpcSummaryRFCheckIn.getDescription().setText("");
            mpcSummaryRFCheckIn.animate();
        }

    }



    private  ArrayList<PieEntry> pcdataRFCheckOutValue(String strOption,String strWorkType,String strStfcode){
        Integer intCountDoc;
        String strFromDate,strToDate,strSelOption;

        strFromDate=mtvFromDate.getText().toString().trim();
        strToDate=mtvToDate.getText().toString().trim();

        ArrayList<PieEntry> pcdataVals = new ArrayList<PieEntry>();
        String tag = "6SepV2";
        try {
            ExecuteSixParameter executeSixParameter = new ExecuteSixParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_SummaryRFCheckOut();
            executeSixParameter.execute("strDataBaseName", strDataBaseName,
                    "strOption", strOption,
                    "strWorkType", strWorkType,
                    "strStfcode", strStfcode,
                    "strFromDate", strFromDate,
                    "strToDate", strToDate,strURL);
            String resultJSON = executeSixParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListYearStrings = new String[jsonArray.length()];
            final String[] arrAmount = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป
            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListYearStrings[i] = jsonObject.getString("SelOption");
                arrAmount[i] = jsonObject.getString("CountDoc");
                strSelOption = arrListYearStrings[i];
                intCountDoc = Integer.parseInt(arrAmount[i]);
                pcdataVals.add(new PieEntry(intCountDoc,strSelOption));
            }//for
            intNumChart=jsonArray.length();//จำนวนแท่งของกราฟ
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
        return pcdataVals;
    }


    private  ArrayList<PieEntry> pcdataRFCheckinValue(String strOption,String strWorkType,String strStfcode){
        Integer intCountDoc;
        String strFromDate,strToDate,strSelOption;

        strFromDate=mtvFromDate.getText().toString().trim();
        strToDate=mtvToDate.getText().toString().trim();

        ArrayList<PieEntry> pcdataVals = new ArrayList<PieEntry>();
        String tag = "6SepV2";
        try {
            ExecuteSixParameter executeSixParameter = new ExecuteSixParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_SummaryRFCheckIn();
            executeSixParameter.execute("strDataBaseName", strDataBaseName,
                    "strOption", strOption,
                    "strWorkType", strWorkType,
                    "strStfcode", strStfcode,
                    "strFromDate", strFromDate,
                    "strToDate", strToDate,strURL);
            String resultJSON = executeSixParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListYearStrings = new String[jsonArray.length()];
            final String[] arrAmount = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป
            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListYearStrings[i] = jsonObject.getString("SelOption");
                arrAmount[i] = jsonObject.getString("CountDoc");
                strSelOption = arrListYearStrings[i];
                intCountDoc = Integer.parseInt(arrAmount[i]);
                pcdataVals.add(new PieEntry(intCountDoc,strSelOption));
            }//for
            intNumChart=jsonArray.length();//จำนวนแท่งของกราฟ
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
        return pcdataVals;
    }


    private  ArrayList<PieEntry> pcdataValue(String strPickCode){
        Integer intYear,intAmount;
        Double dblAmount;

        ArrayList<PieEntry> pcdataVals = new ArrayList<PieEntry>();
        String tag = "6SepV2";
        strTableName=" VW_Mobile_SummaryRFCheckIn ";
        strField="year as FieldName1,(CountDoc) as FieldName2"  ;
        strCondition = "year>year(getdate())-3 and Stfcode='" + strPickCode.trim() + "'";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_GetListData2Field();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName", strTableName, "strField", strField, "strCondition", strCondition,strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);

            JSONArray jsonArray = new JSONArray(resultJSON);

            //Array Data Listdocument To ListView
            final String[] arrListYearStrings = new String[jsonArray.length()];
            final String[] arrAmount = new String[jsonArray.length()];
            //ทำการตรวจสอบว่ามีข้อมูลหรือไม่เพื่อให้กลับไปที่เลือก Location ต่อไป

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrListYearStrings[i] = jsonObject.getString("FieldName1");
                arrAmount[i] = jsonObject.getString("FieldName2");
                intYear=Integer.parseInt(arrListYearStrings[i]);
                dblAmount=Double.parseDouble(arrAmount[i]);
                intAmount = dblAmount.intValue();
                pcdataVals.add(new PieEntry(intAmount,intYear.toString()));
            }//for

        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
        return pcdataVals;
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
//                    ExecuteDeliveryDocument(metSelectDocno.getText().toString());
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