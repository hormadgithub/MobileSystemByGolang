
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

        import com.github.mikephil.charting.charts.BarChart;
        import com.github.mikephil.charting.components.XAxis;
        import com.github.mikephil.charting.data.BarData;
        import com.github.mikephil.charting.data.BarDataSet;
        import com.github.mikephil.charting.data.BarEntry;
        import com.github.mikephil.charting.data.PieEntry;
        import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

        import org.json.JSONArray;
        import org.json.JSONObject;


        import java.util.ArrayList;
        import java.util.Calendar;

        import pneumax.mobilesystembygolaung.R;
        import pneumax.mobilesystembygolaung.connected.ExecuteFourParameter;
        import pneumax.mobilesystembygolaung.connected.ExecuteSixParameter;
        import pneumax.mobilesystembygolaung.connected.GlobalUtility;
        import pneumax.mobilesystembygolaung.manager.GlobalVar;
        import pneumax.mobilesystembygolaung.manager.MyConstant;
        import pneumax.mobilesystembygolaung.object.Result;
        import pneumax.mobilesystembygolaung.object.StaffLogin;

public class SummaryHandHeldOperate extends AppCompatActivity{
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
    BarChart mbcSummaryHandHeldOperate;
    ImageView mimgBackTop;
    Spinner mspnDeviceName;
    ArrayAdapter<String> AdapterSpinnerDeviceName;
    Button mbtnConfirm_SummaryHandHeldOperate;
    //CustomCalendar mccFromDate,mccToDate;
    TextView mtvFromDate,mtvToDate;
    RadioButton mradBtnDay,mradBtnMonth,mradBtnYear;

    // String strFromDate,strToDate;

    private String[]  arrDeviceNameStrings,arrSummaryLabel;
    private String  strDeviceNameStrings,strDeviceNameSelect;
    private Integer intNumChart;


    //ทำให้สามารป้อนด้วยมือแทนการ Scan ได้
    Boolean blnManualInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_summary_hand_held_operate);

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
        createSpinnerDeviceName();
    }


    private void ClearData() {
        mtvFromDate.setText("");
        mtvFromDate.setHint("วันที่เริ่มต้น");
        mtvToDate.setText("");
        mtvToDate.setHint("วันที่สิ้นสุด");

    }


    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);

    }

    private void BindWidgets() {
        mimgBackTop=(ImageView) findViewById(R.id.imgBackTop);
        mspnDeviceName=(Spinner) findViewById(R.id.spnDeviceName);
        mbcSummaryHandHeldOperate=(BarChart) findViewById(R.id.bcSummaryHandHeldOperate);
        mtvFromDate=(TextView) findViewById(R.id.tvFromDate);
        mtvToDate=(TextView) findViewById(R.id.tvToDate);


        mbtnConfirm_SummaryHandHeldOperate=(Button) findViewById(R.id.btnConfirm_SummaryHandHeldOperate);

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


        mbtnConfirm_SummaryHandHeldOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOption="" ;

                if (mradBtnDay.isChecked())
                { strOption="D";}
                else if(mradBtnMonth.isChecked())
                {strOption="M";}
                else if(mradBtnYear.isChecked())
                {strOption="Y";}

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
                    ShowBarChartSummaryHandHeldOperate(strOption, strDeviceNameSelect);
                }
                else {
                    //ต้องเรียก 2 ครั้งเพื่อยกเลิกการ Zoom ก่อนหน้า
                    ShowBarChartSummaryHandHeldOperate(strOption, strDeviceNameSelect);
                    ShowBarChartSummaryHandHeldOperate(strOption, strDeviceNameSelect);
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




    private void createSpinnerDeviceName() {
        strTableName="HandHeldOperate";
        strField= "DeviceName as ResultReturn";
        strCondition="DeviceName <> ''";
        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(getApplicationContext());
            strURL = strServerAddress+ myConstant.urlMobile_GetDataSpinner();
            executeFourParameter.execute("strDataBaseName", strDataBaseName, "strTableName",strTableName, "strField",strField, "strCondition", strCondition, strURL);
            String resultJSON = executeFourParameter.get();
            resultJSON = globalVar.JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            JSONArray jsonArray = new JSONArray(resultJSON);
            //จองหน่วยความจำ
            arrDeviceNameStrings = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrDeviceNameStrings[i] = jsonObject.getString("ResultReturn");
            }//for

            AdapterSpinnerDeviceName = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrDeviceNameStrings);
            mspnDeviceName.setAdapter(AdapterSpinnerDeviceName);
            mspnDeviceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strDeviceNameSelect=arrDeviceNameStrings[i].toString().substring(0,6);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    strDeviceNameStrings = arrDeviceNameStrings[0];
                }

            });
        } catch (Exception e) {
            Toast.makeText(this, "e Create Spinner Device Name ==> " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }//createSpinnerWeekno


    private  void  ShowBarChartSummaryHandHeldOperate(String  strOption,String strDeviceName){

        if(mbcSummaryHandHeldOperate.getData() != null){
            mbcSummaryHandHeldOperate.getData().clearValues();
            mbcSummaryHandHeldOperate.clear();
            mbcSummaryHandHeldOperate.fitScreen();
            mbcSummaryHandHeldOperate.notifyDataSetChanged();
            mbcSummaryHandHeldOperate.clear();
            mbcSummaryHandHeldOperate.invalidate() ;
        }

        //Bar Count Screen
        BarDataSet barCountScanDataSet = new BarDataSet(bcdataHandHeldOperateValue(strOption,"S",strDeviceName),"จำนวน Scan");
        barCountScanDataSet.setColor(Color.RED);

        //Bar Active Time
        BarDataSet barActiveTimeDataSet = new BarDataSet(bcdataHandHeldOperateValue(strOption,"A",strDeviceName),"เวลาใช้งาน(min)");
        barActiveTimeDataSet.setColor(Color.GREEN);


        //Bar Travel Distince
        BarDataSet barTravelDataSet = new BarDataSet(bcdataHandHeldOperateValue(strOption,"T",strDeviceName),"ระยะทาง(m)");
        barTravelDataSet.setColor(Color.BLUE);


        //ใส่จำนวนแท่ง ให้ Barchart
        BarData barData = new BarData(barCountScanDataSet,barActiveTimeDataSet,barTravelDataSet);
        mbcSummaryHandHeldOperate.setData(barData);


        XAxis xAxis = mbcSummaryHandHeldOperate.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(arrSummaryLabel));

        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        mbcSummaryHandHeldOperate.setHorizontalScrollBarEnabled(true);
        mbcSummaryHandHeldOperate.setDragEnabled(true);
        //mbcSummaryHandHeldOperate.setVisibleXRangeMaximum(intNumChart);
        intNumChart=3;
        mbcSummaryHandHeldOperate.setVisibleXRangeMaximum(intNumChart);

        //groupspace =  1-(barwidth+barspace)*จำนวนแท่ง bar
        barData.setBarWidth(0.2f);
        float barSpace = 0.05f;
        float groupSpace = 0.25f;

        mbcSummaryHandHeldOperate.getXAxis().setAxisMinimum(0);
        mbcSummaryHandHeldOperate.getXAxis().setAxisMaximum(0+mbcSummaryHandHeldOperate.getBarData().getGroupWidth(groupSpace,barSpace)*intNumChart);//จำนวน 3 แท่ง
        mbcSummaryHandHeldOperate.getAxisLeft().setAxisMinimum(0);

        mbcSummaryHandHeldOperate.groupBars(0,groupSpace,barSpace);
        mbcSummaryHandHeldOperate.invalidate();

        mbcSummaryHandHeldOperate.setFitBars(true);
        mbcSummaryHandHeldOperate.getDescription().setText("");
        mbcSummaryHandHeldOperate.animateXY(500,2000);
    }



    private ArrayList<BarEntry> bcdataHandHeldOperateValue(String strOption, String strWorkType, String strDeviceName){
        Integer intSelOption,intCountDoc;
        String strFromDate,strToDate;
        strFromDate=mtvFromDate.getText().toString().trim();
        strToDate=mtvToDate.getText().toString().trim();

        Double dblSelOption,dblCountDoc;
        ArrayList<BarEntry> bcdataVals = new ArrayList<BarEntry>();
        String tag = "6SepV2";
        try {
            ExecuteSixParameter executeSixParameter = new ExecuteSixParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_SummaryHandHeldOperate();
            executeSixParameter.execute("strDataBaseName", strDataBaseName,
                    "strOption", strOption,
                    "strWorkType", strWorkType,
                    "strDeviceName", strDeviceName,
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
                intSelOption = Integer.parseInt(arrListYearStrings[i]);
                intCountDoc = Integer.parseInt(arrAmount[i]);
//                dblSelOption = Double.parseDouble(arrListYearStrings[i]);
//                dblCountDoc = Double.parseDouble(arrAmount[i]);
                bcdataVals.add(new BarEntry(intSelOption,intCountDoc));

            }//for
            intNumChart=jsonArray.length();//จำนวนแท่งของกราฟ
            arrSummaryLabel=arrListYearStrings;
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
        return bcdataVals;
    }


    //ใส่ข้อมูลให้กับ Bar แต่ละแท่ง
    private  ArrayList<BarEntry> bcdataRFCheckinValue(String strOption,String strWorkType,String strDeviceName){
        Integer intSelOption,intCountDoc;
        String strFromDate,strToDate;

        strFromDate=mtvFromDate.getText().toString().trim();
        strToDate=mtvToDate.getText().toString().trim();

        ArrayList<BarEntry> bcdataVals = new ArrayList<BarEntry>();
        String tag = "6SepV2";
        try {
            ExecuteSixParameter executeSixParameter = new ExecuteSixParameter(getApplicationContext());
            strURL=strServerAddress+myConstant.urlMobile_SummaryRFCheckIn();
            executeSixParameter.execute("strDataBaseName", strDataBaseName,
                    "strOption", strOption,
                    "strWorkType", strWorkType,
                    "strDeviceName", strDeviceName,
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
                intSelOption = Integer.parseInt(arrListYearStrings[i]);
                // dblCountDoc = Double.parseDouble(arrAmount[i]);
                intCountDoc = Integer.parseInt(arrAmount[i]);
                bcdataVals.add(new BarEntry(intSelOption,intCountDoc));

            }//for
            intNumChart=jsonArray.length();//จำนวนแท่งของกราฟ
            arrSummaryLabel=arrListYearStrings;
        } catch (Exception e) {
            Log.d(tag, "e Create ListView ==> " + e.toString());
        }
        return bcdataVals;
    }


    private  ArrayList<PieEntry> pcdataValue(String strDeviceName){
        Integer intYear,intAmount;
        Double dblAmount;

        ArrayList<PieEntry> pcdataVals = new ArrayList<PieEntry>();
        String tag = "6SepV2";
        strTableName=" VW_Mobile_SummaryHandHeldOperate ";
        strField="year as FieldName1,(CountDoc) as FieldName2"  ;
        strCondition = "year>year(getdate())-3 and Stfcode='" + strDeviceName.trim() + "'";
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