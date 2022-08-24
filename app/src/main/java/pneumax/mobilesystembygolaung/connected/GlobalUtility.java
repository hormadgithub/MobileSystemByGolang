package pneumax.mobilesystembygolaung.connected;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.ResultExecuteSQL;
import pneumax.mobilesystembygolaung.object.ReturnValue;

public class GlobalUtility {
    MyConstant myConstant=new MyConstant();
    String strReturnValue,strResultExcuteSQL;



    //ส่งคืน Field เดียว ในชชื่อ ResultReturn
    public String ReturnValue_ExecuteTwoParameter(Context context,String strParam1,String strParam1Value,String strParam2,String strParam2Value,String strURL) {
        String tag = "6SepV2";
        strReturnValue="";

        try {
            ExecuteTwoParameter executeTwoParameter = new ExecuteTwoParameter(context);
            //ขึ้นอยู่กับว่าจะเอา Class ไหนมารับ เพราะจำนวน Field ที่คืนมาไม่เท่ากัน
            //Class clsReturnValue รับคืนมาแค่ Field ที่ชื่อ  ResultReturn Field เดียว
            ReturnValue clsReturnValue;

            executeTwoParameter.execute(strParam1,strParam1Value,strParam2,strParam2Value,strURL);

            String resultJSON = executeTwoParameter.get();
            resultJSON = GlobalVar.getInstance().JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="";
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                //ขึ้นอยู่กับว่าจะเอา Class ไหนมารับ เพราะจำนวน Field ที่คืนมาไม่เท่ากัน
                //Class clsReturnValue รับคืนมาแค่ Field ที่ชื่อ  ResultReturn Field เดียว
                clsReturnValue = gson.fromJson(resultJSON.toString(), ReturnValue.class);
                strReturnValue=clsReturnValue.ResultReturn;
            }
        } catch (Exception e) {
            Log.d(tag, "e Get Confirm Description ==> " + e.toString());
        }
        return  strReturnValue;
    }



    //ส่งคืน Field เดียว ในชชื่อ ResultReturn
    public String ReturnValue_ExecuteThreeParameter(Context context,String strParam1,String strParam1Value,String strParam2,String strParam2Value,
                                                   String strParam3,String strParam3Value,String strURL) {
        String tag = "6SepV2";
        strReturnValue="";

        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(context);
            //ขึ้นอยู่กับว่าจะเอา Class ไหนมารับ เพราะจำนวน Field ที่คืนมาไม่เท่ากัน
            //Class clsReturnValue รับคืนมาแค่ Field ที่ชื่อ  ResultReturn Field เดียว
            ReturnValue clsReturnValue;

            executeThreeParameter.execute(strParam1,strParam1Value,strParam2,strParam2Value,
                    strParam3,strParam3Value,strURL);

            String resultJSON = executeThreeParameter.get();
            resultJSON = GlobalVar.getInstance().JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="";
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                //ขึ้นอยู่กับว่าจะเอา Class ไหนมารับ เพราะจำนวน Field ที่คืนมาไม่เท่ากัน
                //Class clsReturnValue รับคืนมาแค่ Field ที่ชื่อ  ResultReturn Field เดียว
                clsReturnValue = gson.fromJson(resultJSON.toString(), ReturnValue.class);
                strReturnValue=clsReturnValue.ResultReturn;
            }
        } catch (Exception e) {
            Log.d(tag, "e Get Confirm Description ==> " + e.toString());
        }
        return  strReturnValue;
    }


    //ส่งคืน Field เดียว ในชชื่อ ResultReturn
    public String ReturnValue_ExecuteFourParameter(Context context,String strParam1,String strParam1Value,String strParam2,String strParam2Value,
                                                   String strParam3,String strParam3Value,String strParam4,String strParam4Value,String strURL) {
        String tag = "6SepV2";
        strReturnValue="";

        try {
            ExecuteFourParameter executeFourParameter = new ExecuteFourParameter(context);
            //ขึ้นอยู่กับว่าจะเอา Class ไหนมารับ เพราะจำนวน Field ที่คืนมาไม่เท่ากัน
            //Class clsReturnValue รับคืนมาแค่ Field ที่ชื่อ  ResultReturn Field เดียว
            ReturnValue clsReturnValue;

            executeFourParameter.execute(strParam1,strParam1Value,strParam2,strParam2Value,
                                        strParam3,strParam3Value,strParam4,strParam4Value,strURL);

            String resultJSON = executeFourParameter.get();


            resultJSON = GlobalVar.getInstance().JsonXmlToJsonString_Golaung(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strReturnValue="";
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                //ขึ้นอยู่กับว่าจะเอา Class ไหนมารับ เพราะจำนวน Field ที่คืนมาไม่เท่ากัน
                //Class clsReturnValue รับคืนมาแค่ Field ที่ชื่อ  ResultReturn Field เดียว
                clsReturnValue = gson.fromJson(resultJSON.toString(), ReturnValue.class);
                strReturnValue=clsReturnValue.ResultReturn;
            }
        } catch (Exception e) {
            Log.d(tag, "e Get Confirm Description ==> " + e.toString());
        }
        return  strReturnValue;
    }


    //ใช้ Function ด้านบนช่วย
    public String GetStaffName(Context context,String strDataBaseName,String strStfCode,String strURL){
        String strTableName="STaff";
        String strField="StfFullname";
        String strCondition="StfCode='" + strStfCode.trim() +"'";
        //String strURL=myConstant.urlMobile_ReturnValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,"strTableName",
                                                        strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }


    //ใช้ Function ด้านบนช่วย
    public String GetCustName(Context context,String strDataBaseName,String strCSCode,String strURL){
        String strTableName="Customer";
        String strField="CSThiname";
        String strCondition="CScode='" + strCSCode.trim() +"'";
        //String strURL=myConstant.urlMobile_ReturnValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,"strTableName",
                strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }


    //ใช้ Function ด้านบนช่วย
    public String Find_ReturnValue(Context context,String strDataBaseName,String strTableName,String strField,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_ReturnValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }


    //ใช้ Function ด้านบนช่วย
    public String Find_ServerAddress(Context context,String strDataBaseName,String strTableName,String strField,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_ReturnValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }

    public String Find_Record(Context context,String strDataBaseName,String strTableName,String strField,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_ReturnValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }

    //ใช้ Function ด้านบนช่วย ใช้ได้กับการส่งคืค่าเดียวเี่ยวนั้น
    public String MaxValue(Context context,String strDataBaseName,String strTableName,String strField,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_MaxValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }

    //ใช้ Function ด้านบนช่วย ใช้ได้กับการส่งคืค่าเดียวเี่ยวนั้น
    public String MinValue(Context context,String strDataBaseName,String strTableName,String strField,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_MinValue();
        strReturnValue=ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }

    //ใช้ Function ด้านบนช่วย ใช้ได้กับการส่งคืค่าเดียวเี่ยวนั้น
    public String SumValue(Context context,String strDataBaseName,String strTableName,String strField,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_SumValue();
        strReturnValue = ReturnValue_ExecuteFourParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strField",strField,"strCondition",strCondition,strURL);
        if (strReturnValue.equals("null")) {
            strReturnValue = "0";
        }
        return  strReturnValue;
    }

    //ใช้ Function ด้านบนช่วย ใช้ได้กับการส่งคืค่าเดียวเี่ยวนั้น
    public String CountRecord(Context context,String strDataBaseName,String strTableName,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_CountRecord();
        strReturnValue=ReturnValue_ExecuteThreeParameter(context,"strDataBaseName",strDataBaseName,
                "strTableName",strTableName,"strCondition",strCondition,strURL);
        return  strReturnValue;
    }



    //สำหรับส่งคืนค่า 2 Field คือ Success Or UNSeccess ส่งคืนในชชื่อ ResultID,ResultMessage
    public String ReturnResult_ExecuteTwoParameter(Context context,String strParam1,String strParam1Value,String strParam2,String strParam2Value,String strURL) {
        String tag = "6SepV2";
        strResultExcuteSQL="";
        try {
            ExecuteTwoParameter executeTwoParameter = new ExecuteTwoParameter(context);
            ResultExecuteSQL clsresultExecuteSQL;

            executeTwoParameter.execute(strParam1,strParam1Value,strParam2,strParam2Value,strURL);

            String resultJSON = executeTwoParameter.get();
            resultJSON = GlobalVar.getInstance().JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultExcuteSQL="UNSUCCESS";
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsresultExecuteSQL = gson.fromJson(resultJSON.toString(), ResultExecuteSQL.class);
                //Return 2 Field คือ  ResultID,ResultMessage
                strResultExcuteSQL=clsresultExecuteSQL.ResultID;//Success Or UnSuccess
            }
        } catch (Exception e) {
            Log.d(tag, "e Get Confirm Description ==> " + e.toString());
        }
        return  strResultExcuteSQL;
    }


    //สำหรับส่งคืนค่า 2 Field คือ Success Or UNSeccess ส่งคืนในชชื่อ ResultID,ResultMessage
    public String ReturnResult_ExecuteThreeParameter(Context context,String strParam1,String strParam1Value,String strParam2,String strParam2Value,String strParam3,String strParam3Value,String strURL) {
        String tag = "6SepV2";
        strResultExcuteSQL="";
        try {
            ExecuteThreeParameter executeThreeParameter = new ExecuteThreeParameter(context);
            ResultExecuteSQL clsresultExecuteSQL;

            executeThreeParameter.execute(strParam1,strParam1Value,strParam2,strParam2Value,strParam3,strParam3Value,strURL);

            String resultJSON = executeThreeParameter.get();
            resultJSON = GlobalVar.getInstance().JsonXmlToJsonString(resultJSON);
            //for Not User Pacel
            if (resultJSON.equals("[]")) {
                strResultExcuteSQL="UNSUCCESS";
            } else {
                resultJSON = GlobalVar.getInstance().JsonXmlToJsonStringNotSquareBracket(resultJSON);
                Gson gson = new Gson();
                clsresultExecuteSQL = gson.fromJson(resultJSON.toString(), ResultExecuteSQL.class);
                //Return 2 Field คือ  ResultID,ResultMessage
                strResultExcuteSQL=clsresultExecuteSQL.ResultID;//Success Or UnSuccess
            }
        } catch (Exception e) {
            Log.d(tag, "e Get Confirm Description ==> " + e.toString());
        }
        return  strResultExcuteSQL;
    }
    
    
    
    //ใช้สำหรับ Execute ตามต้องการ ส่งคืน Sussess Or Unsuccess
    public String Execute(Context context,String strDataBaseName,String strForExecute,String strURL){
        //String strURL=myConstant.urlspExecute();
        strResultExcuteSQL=ReturnResult_ExecuteTwoParameter(context,"strDataBaseName",strDataBaseName,"strForExecute",
                strForExecute,strURL);
        return  strResultExcuteSQL;
    }

    //ใช้สำหรับ Execute ตามต้องการ
    public String Delete_Record(Context context,String strDataBaseName,String strTableName,String strCondition,String strURL){
        //String strURL=myConstant.urlMobile_DeleteRecord();
        strResultExcuteSQL=ReturnResult_ExecuteThreeParameter(context,"strDataBaseName",strDataBaseName,"strTableName",strTableName,"strCondition",strCondition,strURL);
        return  strResultExcuteSQL;
    }


    public void ShowCustomToast(Context context, String text,String strGravity, int duration,
                            @Nullable Integer backgroundColor,
                            @Nullable Integer textColor){
        Toast t = Toast.makeText(context,text,duration);

        if (strGravity=="TOP")
            t.setGravity(Gravity.TOP,0,0);
        if (strGravity=="CENTER")
            t.setGravity(Gravity.CENTER,0,0);
        if (strGravity=="BOTTOM")
            t.setGravity(Gravity.BOTTOM,0,0);


        if (backgroundColor != null)
            t.getView().setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        if (textColor != null)
            ((TextView)t.getView().findViewById(android.R.id.message))
                    .setTextColor(textColor);
        t.show();
    }






//
////ยังทำไม่สำเร็จ
//    public void ShowAlertDialog(Context context, int intAlertType, String strAlertTitle, String strAlertMessage, int ic_AlertDialog) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_AlertDialog);
//        View AlertDialogView;
//        AlertDialogView = LayoutInflater.from(context ).inflate( R.layout.layout_success_dialog,null);
//
//        if (intAlertType == R.string.alertdialog_success) {
//            AlertDialogView = LayoutInflater.from(context ).inflate( R.layout.layout_success_dialog,null);
//        }
//
//        if (intAlertType == R.string.alertdialog_warning) {
//            AlertDialogView = LayoutInflater.from(context).inflate(R.layout.layout_warning_dialog, null);
//        }
//        if (intAlertType == R.string.alertdialog_error) {
//            AlertDialogView = LayoutInflater.from(context).inflate(R.layout.layout_error_dialog, null);
//        }
//
//        builder.setView(AlertDialogView.findViewById(R.id.layoutDialogContainer));
//        ((TextView) AlertDialogView.findViewById(R.id.textTitle)).setText(strAlertTitle);
//        ((TextView) AlertDialogView.findViewById(R.id.textMessage)).setText(strAlertMessage);
//        ((ImageView) AlertDialogView.findViewById(R.id.imageIcon)).setImageResource(ic_AlertDialog);
//
//        if (intAlertType == R.string.alertdialog_success || intAlertType == R.string.alertdialog_error) {
//            ((Button) AlertDialogView.findViewById(R.id.buttonAction)).setText(R.string.alertdialog_okay);
//            final AlertDialog alertDialog = builder.create();
//            AlertDialogView.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
//            if (alertDialog.getWindow() != null) {
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            }
//            alertDialog.show();
//        }
//
//        if (intAlertType == R.string.alertdialog_warning) {
//            ((Button) AlertDialogView.findViewById(R.id.buttonYes)).setText(R.string.yes);
//            ((Button) AlertDialogView.findViewById(R.id.buttonNo)).setText(R.string.no);
//            final AlertDialog alertDialog = builder.create();
//
//            AlertDialogView.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
//
//            AlertDialogView.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
//            if (alertDialog.getWindow() != null) {
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            }
//            alertDialog.show();
//        }
//    }

    public boolean isEmptyString(String s) {
        return s.toString().trim().length() == 0;
    }

    public GlobalUtility() {
    }


    private static GlobalUtility mInstance = null;

    public int someValueIWantToKeep;

//    protected GlobalVar(){}

    public static synchronized GlobalUtility getInstance() {
        if (null == mInstance) {
            mInstance = new GlobalUtility();
        }
        return mInstance;
    }


}
