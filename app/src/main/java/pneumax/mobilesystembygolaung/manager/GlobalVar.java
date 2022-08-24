package pneumax.mobilesystembygolaung.manager;

import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by sitrach on 21/08/2017.
 */

public class GlobalVar {

    //ทำให้สามารถเลือกเข้า Server ได้ โดยเเข้าไปที่ server หลักก่อน
    public static String getServerName = "ServerName";
    public static String getServerLocal = "LOCALGO";
    public static String getServerMain = "MAINGO";

    public static String getServerAddress = "Server";
    //ดึงค่าจาก Database แทน
//    public static String getServerAddress_Main = "http://192.168.2.112/webservice/service.asmx/";
//    public static String getServerAddress_Local = "http://192.168.11.19/webservice/service.asmx/";


    public static String getDataBaseName = "DataBaseName";
    public static String getDataBasePneumaxDB = "PNEUMAXDB";
    public static String getDataBaseAnalysisDB = "AnalysisDB";
    public static String getDataBaseDataTest = "DataTest";

    //Get Partnid From Scanbarcode
    public String GetPartnidFromScanBarcode(String  strScanBarcode) {
        String resultString = null;
        resultString = strScanBarcode.substring(0,4) + "-" + strScanBarcode.substring(4,8) + "-" + strScanBarcode.substring(8, 10) + "-" + strScanBarcode.substring(10, 13);
        return resultString;
    }

    //Get Partnid From Scanbarcode
    public String[] GetDescFromScanBarcode(String  strScanBarcode) {
        Integer intStrlen;
        String strpartid;
        String strstatus;
        String strdigitno;
        //เนื่องจาก barcode มี Digit 3 หลักกับ 4 หลัก
        intStrlen=strScanBarcode.trim().length();
        strpartid=strScanBarcode.substring(0,4) + "-" + strScanBarcode.substring(4,8) + "-" + strScanBarcode.substring(8, 10) + "-" + strScanBarcode.substring(10, 13);
        strstatus=strScanBarcode.substring(13,14);
        strdigitno=strScanBarcode.substring(14,intStrlen);

        String[] resultString={strpartid,strstatus,strdigitno};
        return resultString;
    }

    public String GetTempTime(String strStraffCode) {

        String strTmpTime;

        Random r = new Random();
        int randomNumber = r.nextInt(100);
        String strRandom;
        strRandom=String.valueOf(randomNumber).trim();
        if (strRandom.length()==1){
            strRandom="00"+strRandom;
        }
        else if  (strRandom.length()==2) {
            strRandom="0"+strRandom;
        }

        strTmpTime = "Tmp"+strStraffCode.trim()+ strRandom;

        return strTmpTime;
    }


    //Convert Data Webservice To JSONArray Not [ ]
    public String JsonXmlToJsonStringNotSquareBracket(String string) {
        int position = string.indexOf("["); // position = 10
        string = string.substring(position, string.length());
        if(string.indexOf("localhost/Webservice/") > 0 )
        {
            string = string.replace("<string xmlns=\"localhost/Webservice/\">", "");
        }
        else
        {
            string = string.replace("<string xmlns=\"http://58.181.171.24/Webservice/\">", "");
        }

        string = string.replace("</string>", "");
        string = string.replace("[", "").replace("]", "");
        return string;

    }



    //Convert Data Webservice To JSONArray Not [ ]
    public String JsonXmlToJsonStringNotSquareBracket_Golaung(String string) {
        int position = string.indexOf("["); // position = 10
        string = string.substring(position, string.length());
        if(string.indexOf("localhost/Webservice/") > 0 )
        {
            string = string.replace("<string xmlns=\"localhost/Webservice/\">", "");
        }
        else
        {
            string = string.replace("<string xmlns=\"http://58.181.171.24/Webservice/\">", "");
        }

        string = string.replace("</string>", "");
        string = string.replace("[", "").replace("]", "");
        return string;

    }

    //Convert Data Webservice To JSONArray
    public String JsonXmlToJsonString(String string) {
        int position = string.indexOf("[");

        //ถ้าไม่พบต้องใส่ [] เข้าไปตามเงือนไขของ Json
        if(position<0 ) {
            string = string.replace("{", "[{").replace("}", "}]");
            position = string.indexOf("[");
        }

        string = string.substring(position, string.length());
        if(string.indexOf("localhost/Webservice/") > 0 )
        {
            string = string.replace("<string xmlns=\"localhost/Webservice/\">", "");
        }
        else
        {
            string = string.replace("<string xmlns=\"http://58.181.171.24/Webservice/\">", "");
        }
        string = string.replace("</string>", "");

        return string;
    }



    //Convert Data Webservice To JSONArray
    public String JsonXmlToJsonString_Golaung(String string) {
        String LastString;
        int position = string.indexOf("webservice");
        if (position<0) {
            LastString = string.substring(string.length() - 1, string.length());
            //ถ้าขวาสุดเป็น , เนื่องจากเป็นการ join กันหลาย Record มันจะเกินมาให้เอาออก
            if (LastString.equals(","))
                string = string.substring(0, string.length() - 1);
            if (!LastString.equals("]"))
                string = "[" + string + "]";
        }
        else {
            string = JsonXmlToJsonString(string);
        }
        return string;
    }



    //Check Date1 More than Date2 = True
    public Boolean CheckDate1MorethanDate2(Calendar calendarDate1, Calendar calendarDate2) {
        Boolean isblnCheckDate1MorethanDate2 = false;
        if (calendarDate1.get(Calendar.YEAR) > calendarDate2.get(Calendar.YEAR)) {
            isblnCheckDate1MorethanDate2 = true;
        } else if (calendarDate1.get(Calendar.YEAR) == calendarDate2.get(Calendar.YEAR)) {
            if (calendarDate1.get(Calendar.DAY_OF_YEAR) > calendarDate2.get(Calendar.DAY_OF_YEAR)) {
                isblnCheckDate1MorethanDate2 = true;
            }
        }

        Log.d("Date1Morethan2Year", "Year=" + calendarDate1.get(Calendar.YEAR) + " " + calendarDate2.get(Calendar.YEAR));
        Log.d("Date1Morethan2Day", "Day=" + calendarDate1.get(Calendar.DATE) + " " + calendarDate2.get(Calendar.DATE));
        return  isblnCheckDate1MorethanDate2;
    }

    //Check Date1 More than Date2 = True Add Holiday เผื่อวันหยุดให้ 1 วัน เพราะไม่อยากเชื่อมต่อ Server เพื่อดึง Holiday
    public Boolean CheckDate1MorethanDate2_AddHoliday(Calendar calendarDate1, Calendar calendarDate2) {
        Boolean isblnCheckDate1MorethanDate2 = false;
        //ถ้าเป็นปีเดียวกัน ตรวจสอบวันที่นัดหมายย้อนหลังให้ 3 วัน เช่นเลือกวันที่นัด 30/12/2019
        //ถ้าบันทึกผลแล้วให้สามารถแก้ไขข้อความได้อยา่งเดียว หรือยังไม่บันทึกผล ให้บันทึกได้ทั้งผลและข้อความ
        if (calendarDate1.get(Calendar.YEAR) > calendarDate2.get(Calendar.YEAR)) {
            if ((calendarDate2.get(Calendar.DAY_OF_YEAR)-3) > 363) {
                isblnCheckDate1MorethanDate2 = true;
            }
        } else if (calendarDate1.get(Calendar.YEAR) == calendarDate2.get(Calendar.YEAR)) {
            if (calendarDate1.get(Calendar.DAY_OF_YEAR) > (calendarDate2.get(Calendar.DAY_OF_YEAR)+1)) {
                isblnCheckDate1MorethanDate2 = true;
            }
        }

        Log.d("Date1Morethan2Year", "Year=" + calendarDate1.get(Calendar.YEAR) + " " + calendarDate2.get(Calendar.YEAR));
        Log.d("Date1Morethan2Day", "Day=" + calendarDate1.get(Calendar.DATE) + " " + calendarDate2.get(Calendar.DATE));
        return  isblnCheckDate1MorethanDate2;
    }

    //Format Date integer To yyyy-MM-dd
    public String FormatDateyyyy_MM_dd_fromDateInteger(int intYear, int intMonth, int intDay) {
        String tag = "7SepV1";
        String resultString = null;

        String strYear = Integer.toString(intYear);
        String strMonth = Integer.toString(intMonth + 1);;
        if (strMonth.length() == 1) {
            strMonth = "0" + strMonth;
        }

        String strDay = Integer.toString(intDay);
        if (strDay.length() == 1) {
            strDay = "0" + strDay;
        }
        resultString = strYear + "-" + strMonth + "-" + strDay;
        Log.d(tag, "resultString ==> " + resultString);

        return resultString;
    }

    //Format Date of String dd/MM/yyyy from yyyy-MM-dd
    public String FormatDateddMMyyyy_fromStringYYYY_MM_DD(String s) {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        try {
            Date test = simpleDateFormat.parse(s);
        } catch (ParseException pe) {
            //Date is invalid, try next format
            return null;
        }
        String strYear = s.substring(0, 4);
        String strMonth = s.substring(5, 7);
        String strDay = s.substring(8, 10);
        s = strDay + "/" + strMonth + "/" + strYear;
        return s;
    }


    //Format Date of String yyyy-MM-dd from dd/MM/yyyy
    public String FormatStringDate_ddMMyyyy_To_yyyyMMdd(String strAppDate) {
        String resultString = null;
        String[] strings = strAppDate.split("/");
        resultString = strings[2] + "-" + strings[1] + "-" + strings[0];

        return resultString;
    }//myFormatAppDate

    //get Year int from yyyy-MM-dd
    public int getYear_fromStringYYYYMMDD(String strAppDate) {
        int resultInt = 0;
        String[] strings = strAppDate.split("-");
        resultInt =  Integer.parseInt(strings[0]);

        return resultInt;
    }//getYear_fromStringYYYYMMDD

    //get Month int from yyyy-MM-dd
    public int getMonth_fromStringYYYYMMDD(String strAppDate) {
        int resultInt = 0;
        String[] strings = strAppDate.split("-");
        resultInt =  Integer.parseInt(strings[1]);

        return resultInt;
    }//getMonth_fromStringYYYYMMDD

    //get Day int from yyyy-MM-dd
    public int getDay_fromStringYYYYMMDD(String strAppDate) {
        int resultInt = 0;
        String[] strings = strAppDate.split("-");
        String s = strings[2];
        resultInt =  Integer.parseInt(strings[2]);

        return resultInt;
    }//getDay_fromStringYYYYMMDD

    public boolean isEmptyString(String s) {
        return s.toString().trim().length() == 0;
    }

    public GlobalVar() {
    }


    private static GlobalVar mInstance = null;

    public int someValueIWantToKeep;

//    protected GlobalVar(){}

    public static synchronized GlobalVar getInstance() {
        if (null == mInstance) {
            mInstance = new GlobalVar();
        }
        return mInstance;
    }


}

