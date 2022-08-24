package pneumax.mobilesystembygolaung.manager;

/**
 * Created by Sitrach on 04/09/2017.
 */

public class MyConstant {

    //เริ่มต้นให้เข้าที่ Server Main ก่อนเพื่อตรวจสอบคน Login ว่าสามารถเลือก Server ได้หรือไม่
    String strHostGolaung = "http://192.168.2.112:8080/";
    //String strHostGolaung = "http://192.168.9.35:8080/";


    //URL urlMobile_GetServerAddress
    private String urlMobile_GetServerAddress =  strHostGolaung +"Mobile_GetServerAddress";
    public String urlMobile_GetServerAddress() {return urlMobile_GetServerAddress;}

    //--------------------------------------------------------------------
    //ด้านล่างเอา strHost ออก เพื่อให้เรียกจาก Server ที่เลือก
    //URL urlMobile_CheckLogin_Golaung
    private String urlMobile_CheckLogin_Golaung =  "Mobile_CheckLogin";
    public String urlMobile_CheckLogin_Golaung() {
        return urlMobile_CheckLogin_Golaung;
    }

    //URL urlMobile_SummaryRFCheckIn
    private String urlMobile_SummaryRFCheckIn =   "Mobile_SummaryRFCheckIn";
    public String urlMobile_SummaryRFCheckIn() {
        return urlMobile_SummaryRFCheckIn;
    }

    //URL urlMobile_SummaryRFCheckOut
    private String urlMobile_SummaryRFCheckOut =   "Mobile_SummaryRFCheckOut";
    public String urlMobile_SummaryRFCheckOut() {
        return urlMobile_SummaryRFCheckOut;
    }

    //URL urlMobile_SummaryHandHeldOperate
    private String urlMobile_SummaryHandHeldOperate =   "Mobile_SummaryHandHeldOperate";
    public String urlMobile_SummaryHandHeldOperate() {
        return urlMobile_SummaryHandHeldOperate;
    }

    //URL urlMobile_GetDataSpinner
    private String urlMobile_GetDataSpinner =   "Mobile_GetDataSpinner";
    public String urlMobile_GetDataSpinner() {
        return urlMobile_GetDataSpinner;
    }


    //URL urlspExecute
    private String urlspExecute =   "spExecute";
    public String urlspExecute() {
        return urlspExecute;
    }    
    
    //URL urlMobile_GetProductDetail
    private String urlMobile_GetProductDetail =   "Mobile_GetProductDetail";
    public String urlMobile_GetProductDetail() {
        return urlMobile_GetProductDetail;
    }

    //URL urlMobile_ListDocument
    private String urlMobile_ListDocument =  "Mobile_ListDocument";
    public String urlMobile_ListDocument() {
        return urlMobile_ListDocument;
    }

    //URL urlMobile_ListDoctypeReceive
    private String urlMobile_ListDoctypeReceive =   "Mobile_ListDoctypeReceive";
    public String urlMobile_ListDoctypeReceive() {
        return urlMobile_ListDoctypeReceive;
    }


    //URL urlMobile_ListDocument_Receive
    private String urlMobile_ListDocument_Receive =   "Mobile_ListDocument_Receive";
    public String urlMobile_ListDocument_Receive() {
        return urlMobile_ListDocument_Receive;
    }

    //URL urlMobile_ListDoctypeStore
    private String urlMobile_ListDoctypeStore =   "Mobile_ListDoctypeStore";
    public String urlMobile_ListDoctypeStore() {
        return urlMobile_ListDoctypeStore;
    }

    //URL urlMobile_ListDocument_Store
    private String urlMobile_ListDocument_Store =   "Mobile_ListDocument_Store";
    public String urlMobile_ListDocument_Store() {
        return urlMobile_ListDocument_Store;
    }

    //URL urlMobile_ListDocument_Store_Parttube
    private String urlMobile_ListDocument_Store_Parttube =   "Mobile_ListDocument_Store_Parttube";
    public String urlMobile_ListDocument_Store_Parttube() {
        return urlMobile_ListDocument_Store_Parttube;
    }


    //URL urlMobile_ClearDocNoError
    private String urlMobile_ClearDocNoError =   "Mobile_ClearDocNoError";
    public String urlMobile_ClearDocNoError() {
        return urlMobile_ClearDocNoError;
    }


    //URL urlMobile_Update_ProductExWH
    private String urlMobile_Update_ProductExWH =   "Mobile_Update_ProductExWH";
    public String urlMobile_Update_ProductExWH() {
        return urlMobile_Update_ProductExWH;
    }

    //URL urlMobile_Cancel_ProductExWH
    private String urlMobile_Cancel_ProductExWH =   "Mobile_Cancel_ProductExWH";
    public String urlMobile_Cancel_ProductExWH() {
        return urlMobile_Cancel_ProductExWH;
    }

    //URL urlMobile_Update_ProductChecked
    private String urlMobile_Update_ProductChecked =   "Mobile_Update_ProductChecked";
    public String urlMobile_Update_ProductChecked() {
        return urlMobile_Update_ProductChecked;
    }

    //URL urlMobile_ReturnValue
    private String urlMobile_ReturnValue =  "Mobile_ReturnValue";
    public String urlMobile_ReturnValue() {
        return urlMobile_ReturnValue;
    }

    //URL urlMobile_MinValue
    private String urlMobile_MinValue =   "Mobile_MinValue";
    public String urlMobile_MinValue() {
        return urlMobile_MinValue;
    }


    //URL urlMobile_MaxValue
    private String urlMobile_MaxValue =   "Mobile_MaxValue";
    public String urlMobile_MaxValue() {
        return urlMobile_MaxValue;
    }

    //URL urlMobile_SumValue
    private String urlMobile_SumValue =   "Mobile_SumValue";
    public String urlMobile_SumValue() {
        return urlMobile_SumValue;
    }


    //URL getReturnTwoValue
    private String urlMobile_ReturnTwoValue =   "Mobile_ReturnTwoValue";
    public String urlMobile_ReturnTwoValue() {
        return urlMobile_ReturnTwoValue;
    }

    //URL urlMobile_CountRecord
    private String urlMobile_CountRecord =   "Mobile_CountRecord";
    public String urlMobile_CountRecord() {
        return urlMobile_CountRecord;
    }


    //URL urlMobile_GetDoctype
    private String urlMobile_GetDoctype =   "Mobile_GetDoctype";
    public String urlMobile_GetDoctype() {
        return urlMobile_GetDoctype;
    }

    //URL urlMobile_Picking_Hold_Reset
    private String urlMobile_Picking_Hold_Reset =   "Mobile_Picking_Hold_Reset";
    public String urlMobile_Picking_Hold_Reset() {
        return urlMobile_Picking_Hold_Reset;
    }

    //URL urlMobile_Picking_Jobtube_Hold_Reset
    private String urlMobile_Picking_Jobtube_Hold_Reset =   "Mobile_Picking_Jobtube_Hold_Reset";
    public String urlMobile_Picking_Jobtube_Hold_Reset() {
        return urlMobile_Picking_Jobtube_Hold_Reset;
    }

    //URL urlMobile_Clear_Tmp_RFCheckOut
    private String urlMobile_Clear_Tmp_RFCheckOut =   "Mobile_Clear_Tmp_RFCheckOut";
    public String urlMobile_Clear_Tmp_RFCheckOut() {
        return urlMobile_Clear_Tmp_RFCheckOut;
    }


    //URL urlMobile_Clear_Tmp_RFCheckOut_Jobtube
    private String urlMobile_Clear_Tmp_RFCheckOut_Jobtube =   "Mobile_Clear_Tmp_RFCheckOut_Jobtube";
    public String urlMobile_Clear_Tmp_RFCheckOut_Jobtube() {
        return urlMobile_Clear_Tmp_RFCheckOut_Jobtube;
    }

    //URL urlMobile_GetPickPartDetail
    private String urlMobile_GetPickPartDetail =   "Mobile_GetPickPartDetail";
    public String urlMobile_GetPickPartDetail() {
        return urlMobile_GetPickPartDetail;
    }

    //URL urlMobile_GetPickPart_JobtubeDetail
    private String urlMobile_GetPickPart_JobtubeDetail =   "Mobile_GetPickPart_JobtubeDetail";
    public String urlMobile_GetPickPart_JobtubeDetail() {
        return urlMobile_GetPickPart_JobtubeDetail;
    }

    //URL urlMobile_GetPartTubeDetail
    private String urlMobile_GetPartTubeDetail =   "Mobile_GetPartTubeDetail";
    public String urlMobile_GetPartTubeDetail() {
        return urlMobile_GetPartTubeDetail;
    }



    //URL urlMobile_ConfirmDocument
    private String urlMobile_ConfirmDocument =  "Mobile_ConfirmDocument";
    public String urlMobile_ConfirmDocument() {
        return urlMobile_ConfirmDocument;
    }

    //URL urlMobile_Update_RFCheckOut_PartSerialNo
    private String urlMobile_Update_RFCheckOut_PartSerialNo =   "Mobile_Update_RFCheckOut_PartSerialNo";
    public String urlMobile_Update_RFCheckOut_PartSerialNo() {
        return urlMobile_Update_RFCheckOut_PartSerialNo;
    }

    //URL urlMobile_Insert_Tmp_RFPartTube
    private String urlMobile_Insert_Tmp_RFPartTube =   "Mobile_Insert_Tmp_RFPartTube";
    public String urlMobile_Insert_Tmp_RFPartTube() {
        return urlMobile_Insert_Tmp_RFPartTube;
    }



    //URL urlMobile_Clear_RFCheckOut_PartSerialNo
    private String urlMobile_Clear_RFCheckOut_PartSerialNo =   "Mobile_Clear_RFCheckOut_PartSerialNo";
    public String urlMobile_Clear_RFCheckOut_PartSerialNo() {
        return urlMobile_Clear_RFCheckOut_PartSerialNo;
    }


    //URL urlMobile_DeliveryDocument
    private String urlMobile_DeliveryDocument =   "Mobile_DeliveryDocument";
    public String urlMobile_DeliveryDocument() {
        return urlMobile_DeliveryDocument;
    }


    //URL urlMobile_GetPhysicalCount
    private String urlMobile_GetPhysicalCount =   "Mobile_GetPhysicalCount";
    public String urlMobile_GetPhysicalCount() {
        return urlMobile_GetPhysicalCount;
    }


    //URL urlMobile_GetSerialNo
    private String urlMobile_GetSerialNo =   "Mobile_GetSerialNo";
    public String urlMobile_GetSerialNo() {
        return urlMobile_GetSerialNo;
    }

    //URL urlMobile_Update_PhysicalCount
    private String urlMobile_Update_PhysicalCount =   "Mobile_Update_PhysicalCount";
    public String urlMobile_Update_PhysicalCount() {
        return urlMobile_Update_PhysicalCount;
    }

    //URL urlMobile_Update_Tmp_RFPhysicalCount
    private String urlMobile_Update_Tmp_RFPhysicalCount =   "Mobile_Update_Tmp_RFPhysicalCount";
    public String urlMobile_Update_Tmp_RFPhysicalCount() {
        return urlMobile_Update_Tmp_RFPhysicalCount;
    }

    //URL urlMobile_DeleteRecord
    private String urlMobile_DeleteRecord =   "Mobile_DeleteRecord";
    public String urlMobile_DeleteRecord() {
        return urlMobile_DeleteRecord;
    }


    //URL urlMobile_Insert_Tmp_RFPartDigitNo
    private String urlMobile_Insert_Tmp_RFPartDigitNo =   "Mobile_Insert_Tmp_RFPartDigitNo";
    public String urlMobile_Insert_Tmp_RFPartDigitNo() {return urlMobile_Insert_Tmp_RFPartDigitNo;
    }

    //URL urlMobile_Insert_Tmp_RFPhysicalCount
    private String urlMobile_Insert_Tmp_RFPhysicalCount =   "Mobile_Insert_Tmp_RFPhysicalCount";
    public String urlMobile_Insert_Tmp_RFPhysicalCount() {return urlMobile_Insert_Tmp_RFPhysicalCount;
    }

    //URL urlMobile_Get_Tmp_RFPhysicalCount
    private String urlMobile_Get_Tmp_RFPhysicalCount =   "Mobile_Get_Tmp_RFPhysicalCount";
    public String urlMobile_Get_Tmp_RFPhysicalCount() {return urlMobile_Get_Tmp_RFPhysicalCount;
    }


    //URL urlMobile_Crt_Tmp_RFCheckOut_Picking
    private String urlMobile_Crt_Tmp_RFCheckOut_Picking =   "Mobile_Crt_Tmp_RFCheckOut_Picking";
    public String urlMobile_Crt_Tmp_RFCheckOut_Picking() {return urlMobile_Crt_Tmp_RFCheckOut_Picking;
    }

    //URL urlMobile_UPD_Tmp_RFCheckOut
    private String urlMobile_UPD_Tmp_RFCheckOut =   "Mobile_UPD_Tmp_RFCheckOut";
    public String urlMobile_UPD_Tmp_RFCheckOut() {return urlMobile_UPD_Tmp_RFCheckOut;
    }

    //URL urlMobile_UPD_Tmp_RFCheckOut_Jobtube
    private String urlMobile_UPD_Tmp_RFCheckOut_Jobtube =   "Mobile_UPD_Tmp_RFCheckOut_Jobtube";
    public String urlMobile_UPD_Tmp_RFCheckOut_Jobtube() {return urlMobile_UPD_Tmp_RFCheckOut_Jobtube;
    }

    //URL urlMobile_Insert_RFCheckOut_PartSerialNo
    private String urlMobile_Insert_RFCheckOut_PartSerialNo =   "Mobile_Insert_RFCheckOut_PartSerialNo";
    public String urlMobile_Insert_RFCheckOut_PartSerialNo() {return urlMobile_Insert_RFCheckOut_PartSerialNo;
    }

    //URL urlMobile_Confirm_PickPart
    private String urlMobile_Confirm_PickPart =   "Mobile_Confirm_PickPart";
    public String urlMobile_Confirm_PickPart() {return urlMobile_Confirm_PickPart;
    }


    //URL urlMobile_Confirm_PickPart_Jobtube
    private String urlMobile_Confirm_PickPart_Jobtube =   "Mobile_Confirm_PickPart_Jobtube";
    public String urlMobile_Confirm_PickPart_Jobtube() {return urlMobile_Confirm_PickPart_Jobtube;
    }

    //URL urlMobile_GetListData2Field
    private String urlMobile_GetListData2Field =   "Mobile_GetListData2Field";
    public String urlMobile_GetListData2Field() {
        return urlMobile_GetListData2Field;
    }

    //URL urlMobile_GetListPart
    private String urlMobile_GetListPart =   "Mobile_GetListPart";
    public String urlMobile_GetListPart() {
        return urlMobile_GetListPart;
    }

    //URL urlMobile_GetListPartTube
    private String urlMobile_GetListPartTube =   "Mobile_GetListPartTube";
    public String urlMobile_GetListPartTube() {
        return urlMobile_GetListPartTube;
    }


    //URL urlMobile_Clear_Tmp_RFCheckIn
    private String urlMobile_Clear_Tmp_RFCheckIn =   "Mobile_Clear_Tmp_RFCheckIn";
    public String urlMobile_Clear_Tmp_RFCheckIn() {return urlMobile_Clear_Tmp_RFCheckIn;
    }


    //URL urlMobile_INS_Tmp_RFCheckIn
    private String urlMobile_INS_Tmp_RFCheckIn =   "Mobile_INS_Tmp_RFCheckIn";
    public String urlMobile_INS_Tmp_RFCheckIn() {return urlMobile_INS_Tmp_RFCheckIn;
    }


    //URL urlMobile_INS_Tmp_RFCheckIn_Parttube
    private String urlMobile_INS_Tmp_RFCheckIn_Parttube =   "Mobile_INS_Tmp_RFCheckIn_Parttube";
    public String urlMobile_INS_Tmp_RFCheckIn_Parttube() {return urlMobile_INS_Tmp_RFCheckIn_Parttube;
    }

    //URL urlMobile_INS_Tmp_RFCheckIn_ChkDigit
    private String urlMobile_INS_Tmp_RFCheckIn_ChkDigit =   "Mobile_INS_Tmp_RFCheckIn_ChkDigit";
    public String urlMobile_INS_Tmp_RFCheckIn_ChkDigit() {return urlMobile_INS_Tmp_RFCheckIn_ChkDigit;
    }


    //URL urlMobile_GetReceivePartDetail
    private String urlMobile_GetReceivePartDetail =   "Mobile_GetReceivePartDetail";
    public String urlMobile_GetReceivePartDetail() {
        return urlMobile_GetReceivePartDetail;
    }


    //URL urlMobile_Receive_Hold_Reset
    private String urlMobile_Receive_Hold_Reset =   "Mobile_Receive_Hold_Reset";
    public String urlMobile_Receive_Hold_Reset() {
        return urlMobile_Receive_Hold_Reset;
    }

    //URL urlMobile_Confirm_ReceivePart
    private String urlMobile_Confirm_ReceivePart =   "Mobile_Confirm_ReceivePart";
    public String urlMobile_Confirm_ReceivePart() {
        return urlMobile_Confirm_ReceivePart;
    }

    //URL urlMobile_GetStorePartDetail
    private String urlMobile_GetStorePartDetail =   "Mobile_GetStorePartDetail";
    public String urlMobile_GetStorePartDetail() {
        return urlMobile_GetStorePartDetail;
    }

    //URL urlMobile_GetStorePartTubeDetail
    private String urlMobile_GetStorePartTubeDetail =   "Mobile_GetStorePartTubeDetail";
    public String urlMobile_GetStorePartTubeDetail() {
        return urlMobile_GetStorePartTubeDetail;
    }

    //URL urlMobile_Store_Hold_Reset
    private String urlMobile_Store_Hold_Reset =   "Mobile_Store_Hold_Reset";
    public String urlMobile_Store_Hold_Reset() {
        return urlMobile_Store_Hold_Reset;
    }


    //URL urlMobile_Store_Parttube_Hold_Reset
    private String urlMobile_Store_Parttube_Hold_Reset =   "Mobile_Store_Parttube_Hold_Reset";
    public String urlMobile_Store_Parttube_Hold_Reset() {
        return urlMobile_Store_Parttube_Hold_Reset;
    }


    //URL urlMobile_Confirm_StorePart
    private String urlMobile_Confirm_StorePart =   "Mobile_Confirm_StorePart";
    public String urlMobile_Confirm_StorePart() {
        return urlMobile_Confirm_StorePart;
    }


    //URL urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube
    private String urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube =   "Mobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube";
    public String urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube() {return urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube; }

    //URL urlMobile_UPD_Tmp_RFCheckIn
    private String urlMobile_UPD_Tmp_RFCheckIn =   "Mobile_UPD_Tmp_RFCheckIn";
    public String urlMobile_UPD_Tmp_RFCheckIn() {return urlMobile_UPD_Tmp_RFCheckIn;
    }

    //URL urlMobile_Upd_Tmp_RFCheckIn_PartTube
    private String urlMobile_Upd_Tmp_RFCheckIn_PartTube =   "Mobile_Upd_Tmp_RFCheckIn_PartTube";
    public String urlMobile_Upd_Tmp_RFCheckIn_PartTube() {return urlMobile_Upd_Tmp_RFCheckIn_PartTube; }

    //End Barcode System



    //About URL getCheckVersion
    private String urlGetCheckVersion =   "getCheckVersion";
    public String getUrlGetCheckVersion() {
        return urlGetCheckVersion;
    }



//
//    //Start Barcode System
//
//    //URL urlspExecute
//    private String urlspExecute = strHost + "spExecute";
//    public String urlspExecute() {
//        return urlspExecute;
//    }
//
//    //URL urlMobile_CheckLogin
//    private String urlMobile_CheckLogin = strHost + "Mobile_CheckLogin";
//    public String urlMobile_CheckLogin() {
//        return urlMobile_CheckLogin;
//    }
//
//    //URL urlMobile_GetProductDetail
//    private String urlMobile_GetProductDetail = strHost + "Mobile_GetProductDetail";
//    public String urlMobile_GetProductDetail() {
//        return urlMobile_GetProductDetail;
//    }
//
//    //URL urlMobile_ListDocument
//    private String urlMobile_ListDocument = strHost + "Mobile_ListDocument";
//    public String urlMobile_ListDocument() {
//        return urlMobile_ListDocument;
//    }
//
//    //URL urlMobile_ListDoctypeReceive
//    private String urlMobile_ListDoctypeReceive = strHost + "Mobile_ListDoctypeReceive";
//    public String urlMobile_ListDoctypeReceive() {
//        return urlMobile_ListDoctypeReceive;
//    }
//
//
//    //URL urlMobile_ListDocument_Receive
//    private String urlMobile_ListDocument_Receive = strHost + "Mobile_ListDocument_Receive";
//    public String urlMobile_ListDocument_Receive() {
//        return urlMobile_ListDocument_Receive;
//    }
//
//    //URL urlMobile_ListDoctypeStore
//    private String urlMobile_ListDoctypeStore = strHost + "Mobile_ListDoctypeStore";
//    public String urlMobile_ListDoctypeStore() {
//        return urlMobile_ListDoctypeStore;
//    }
//
//    //URL urlMobile_ListDocument_Store
//    private String urlMobile_ListDocument_Store = strHost + "Mobile_ListDocument_Store";
//    public String urlMobile_ListDocument_Store() {
//        return urlMobile_ListDocument_Store;
//    }
//
//    //URL urlMobile_ListDocument_Store_Parttube
//    private String urlMobile_ListDocument_Store_Parttube = strHost + "Mobile_ListDocument_Store_Parttube";
//    public String urlMobile_ListDocument_Store_Parttube() {
//        return urlMobile_ListDocument_Store_Parttube;
//    }
//
//
//    //URL urlMobile_ClearDocNoError
//    private String urlMobile_ClearDocNoError = strHost + "Mobile_ClearDocNoError";
//    public String urlMobile_ClearDocNoError() {
//        return urlMobile_ClearDocNoError;
//    }
//
//
//    //URL urlMobile_Update_ProductExWH
//    private String urlMobile_Update_ProductExWH = strHost + "Mobile_Update_ProductExWH";
//    public String urlMobile_Update_ProductExWH() {
//        return urlMobile_Update_ProductExWH;
//    }
//
//    //URL urlMobile_Update_ProductChecked
//    private String urlMobile_Update_ProductChecked = strHost + "Mobile_Update_ProductChecked";
//    public String urlMobile_Update_ProductChecked() {
//        return urlMobile_Update_ProductChecked;
//    }
//
//    //URL urlMobile_ReturnValue
//    private String urlMobile_ReturnValue = strHost + "Mobile_ReturnValue";
//    public String urlMobile_ReturnValue() {
//        return urlMobile_ReturnValue;
//    }
//
//    //URL urlMobile_MinValue
//    private String urlMobile_MinValue = strHost + "Mobile_MinValue";
//    public String urlMobile_MinValue() {
//        return urlMobile_MinValue;
//    }
//
//
//    //URL urlMobile_MaxValue
//    private String urlMobile_MaxValue = strHost + "Mobile_MaxValue";
//    public String urlMobile_MaxValue() {
//        return urlMobile_MaxValue;
//    }
//
//    //URL urlMobile_SumValue
//    private String urlMobile_SumValue = strHost + "Mobile_SumValue";
//    public String urlMobile_SumValue() {
//        return urlMobile_SumValue;
//    }
//
//
//    //URL getReturnTwoValue
//    private String urlMobile_ReturnTwoValue = strHost + "Mobile_ReturnTwoValue";
//    public String urlMobile_ReturnTwoValue() {
//        return urlMobile_ReturnTwoValue;
//    }
//
//    //URL urlMobile_CountRecord
//    private String urlMobile_CountRecord = strHost + "Mobile_CountRecord";
//    public String urlMobile_CountRecord() {
//        return urlMobile_CountRecord;
//    }
//
//
//    //URL urlMobile_GetDoctype
//    private String urlMobile_GetDoctype = strHost + "Mobile_GetDoctype";
//    public String urlMobile_GetDoctype() {
//        return urlMobile_GetDoctype;
//    }
//
//    //URL urlMobile_Picking_Hold_Reset
//    private String urlMobile_Picking_Hold_Reset = strHost + "Mobile_Picking_Hold_Reset";
//    public String urlMobile_Picking_Hold_Reset() {
//        return urlMobile_Picking_Hold_Reset;
//    }
//
//    //URL urlMobile_Picking_Jobtube_Hold_Reset
//    private String urlMobile_Picking_Jobtube_Hold_Reset = strHost + "Mobile_Picking_Jobtube_Hold_Reset";
//    public String urlMobile_Picking_Jobtube_Hold_Reset() {
//        return urlMobile_Picking_Jobtube_Hold_Reset;
//    }
//
//    //URL urlMobile_Clear_Tmp_RFCheckOut
//    private String urlMobile_Clear_Tmp_RFCheckOut = strHost + "Mobile_Clear_Tmp_RFCheckOut";
//    public String urlMobile_Clear_Tmp_RFCheckOut() {
//        return urlMobile_Clear_Tmp_RFCheckOut;
//    }
//
//
//    //URL urlMobile_Clear_Tmp_RFCheckOut_Jobtube
//    private String urlMobile_Clear_Tmp_RFCheckOut_Jobtube = strHost + "Mobile_Clear_Tmp_RFCheckOut_Jobtube";
//    public String urlMobile_Clear_Tmp_RFCheckOut_Jobtube() {
//        return urlMobile_Clear_Tmp_RFCheckOut_Jobtube;
//    }
//
//    //URL urlMobile_GetPickPartDetail
//    private String urlMobile_GetPickPartDetail = strHost + "Mobile_GetPickPartDetail";
//    public String urlMobile_GetPickPartDetail() {
//        return urlMobile_GetPickPartDetail;
//    }
//
//    //URL urlMobile_GetPickPart_JobtubeDetail
//    private String urlMobile_GetPickPart_JobtubeDetail = strHost + "Mobile_GetPickPart_JobtubeDetail";
//    public String urlMobile_GetPickPart_JobtubeDetail() {
//        return urlMobile_GetPickPart_JobtubeDetail;
//    }
//
//    //URL urlMobile_GetPartTubeDetail
//    private String urlMobile_GetPartTubeDetail = strHost + "Mobile_GetPartTubeDetail";
//    public String urlMobile_GetPartTubeDetail() {
//        return urlMobile_GetPartTubeDetail;
//    }
//
//
//
//    //URL urlMobile_ConfirmDocument
//    private String urlMobile_ConfirmDocument = strHost + "Mobile_ConfirmDocument";
//    public String urlMobile_ConfirmDocument() {
//        return urlMobile_ConfirmDocument;
//    }
//
//    //URL urlMobile_Update_RFCheckOut_PartSerialNo
//    private String urlMobile_Update_RFCheckOut_PartSerialNo = strHost + "Mobile_Update_RFCheckOut_PartSerialNo";
//    public String urlMobile_Update_RFCheckOut_PartSerialNo() {
//        return urlMobile_Update_RFCheckOut_PartSerialNo;
//    }
//
//    //URL urlMobile_Insert_Tmp_RFPartTube
//    private String urlMobile_Insert_Tmp_RFPartTube = strHost + "Mobile_Insert_Tmp_RFPartTube";
//    public String urlMobile_Insert_Tmp_RFPartTube() {
//        return urlMobile_Insert_Tmp_RFPartTube;
//    }
//
//
//
//    //URL urlMobile_Clear_RFCheckOut_PartSerialNo
//    private String urlMobile_Clear_RFCheckOut_PartSerialNo = strHost + "Mobile_Clear_RFCheckOut_PartSerialNo";
//    public String urlMobile_Clear_RFCheckOut_PartSerialNo() {
//        return urlMobile_Clear_RFCheckOut_PartSerialNo;
//    }
//
//
//    //URL urlMobile_DeliveryDocument
//    private String urlMobile_DeliveryDocument = strHost + "Mobile_DeliveryDocument";
//    public String urlMobile_DeliveryDocument() {
//        return urlMobile_DeliveryDocument;
//    }
//
//
//    //URL urlMobile_GetPhysicalCount
//    private String urlMobile_GetPhysicalCount = strHost + "Mobile_GetPhysicalCount";
//    public String urlMobile_GetPhysicalCount() {
//        return urlMobile_GetPhysicalCount;
//    }
//
//
//    //URL urlMobile_Update_PhysicalCount
//    private String urlMobile_Update_PhysicalCount = strHost + "Mobile_Update_PhysicalCount";
//    public String urlMobile_Update_PhysicalCount() {
//        return urlMobile_Update_PhysicalCount;
//    }
//
//    //URL urlMobile_Update_Tmp_RFPhysicalCount
//    private String urlMobile_Update_Tmp_RFPhysicalCount = strHost + "Mobile_Update_Tmp_RFPhysicalCount";
//    public String urlMobile_Update_Tmp_RFPhysicalCount() {
//        return urlMobile_Update_Tmp_RFPhysicalCount;
//    }
//
//    //URL urlMobile_DeleteRecord
//    private String urlMobile_DeleteRecord = strHost + "Mobile_DeleteRecord";
//    public String urlMobile_DeleteRecord() {
//        return urlMobile_DeleteRecord;
//    }
//
//
//    //URL urlMobile_Insert_Tmp_RFPartDigitNo
//    private String urlMobile_Insert_Tmp_RFPartDigitNo = strHost + "Mobile_Insert_Tmp_RFPartDigitNo";
//    public String urlMobile_Insert_Tmp_RFPartDigitNo() {return urlMobile_Insert_Tmp_RFPartDigitNo;
//    }
//
//    //URL urlMobile_Insert_Tmp_RFPhysicalCount
//    private String urlMobile_Insert_Tmp_RFPhysicalCount = strHost + "Mobile_Insert_Tmp_RFPhysicalCount";
//    public String urlMobile_Insert_Tmp_RFPhysicalCount() {return urlMobile_Insert_Tmp_RFPhysicalCount;
//    }
//
//    //URL urlMobile_Get_Tmp_RFPhysicalCount
//    private String urlMobile_Get_Tmp_RFPhysicalCount = strHost + "Mobile_Get_Tmp_RFPhysicalCount";
//    public String urlMobile_Get_Tmp_RFPhysicalCount() {return urlMobile_Get_Tmp_RFPhysicalCount;
//    }
//
//
//    //URL urlMobile_Crt_Tmp_RFCheckOut_Picking
//    private String urlMobile_Crt_Tmp_RFCheckOut_Picking = strHost + "Mobile_Crt_Tmp_RFCheckOut_Picking";
//    public String urlMobile_Crt_Tmp_RFCheckOut_Picking() {return urlMobile_Crt_Tmp_RFCheckOut_Picking;
//    }
//
//    //URL urlMobile_UPD_Tmp_RFCheckOut
//    private String urlMobile_UPD_Tmp_RFCheckOut = strHost + "Mobile_UPD_Tmp_RFCheckOut";
//    public String urlMobile_UPD_Tmp_RFCheckOut() {return urlMobile_UPD_Tmp_RFCheckOut;
//    }
//
//    //URL urlMobile_UPD_Tmp_RFCheckOut_Jobtube
//    private String urlMobile_UPD_Tmp_RFCheckOut_Jobtube = strHost + "Mobile_UPD_Tmp_RFCheckOut_Jobtube";
//    public String urlMobile_UPD_Tmp_RFCheckOut_Jobtube() {return urlMobile_UPD_Tmp_RFCheckOut_Jobtube;
//    }
//
//    //URL urlMobile_Insert_RFCheckOut_PartSerialNo
//    private String urlMobile_Insert_RFCheckOut_PartSerialNo = strHost + "Mobile_Insert_RFCheckOut_PartSerialNo";
//    public String urlMobile_Insert_RFCheckOut_PartSerialNo() {return urlMobile_Insert_RFCheckOut_PartSerialNo;
//    }
//
//    //URL urlMobile_Confirm_PickPart
//    private String urlMobile_Confirm_PickPart = strHost + "Mobile_Confirm_PickPart";
//    public String urlMobile_Confirm_PickPart() {return urlMobile_Confirm_PickPart;
//    }
//
//
//    //URL urlMobile_Confirm_PickPart_Jobtube
//    private String urlMobile_Confirm_PickPart_Jobtube = strHost + "Mobile_Confirm_PickPart_Jobtube";
//    public String urlMobile_Confirm_PickPart_Jobtube() {return urlMobile_Confirm_PickPart_Jobtube;
//    }
//
//
//    //URL urlMobile_GetListPart
//    private String urlMobile_GetListPart = strHost + "Mobile_GetListPart";
//    public String urlMobile_GetListPart() {
//        return urlMobile_GetListPart;
//    }
//
//
//
//
//    //URL urlMobile_Clear_Tmp_RFCheckIn
//    private String urlMobile_Clear_Tmp_RFCheckIn = strHost + "Mobile_Clear_Tmp_RFCheckIn";
//    public String urlMobile_Clear_Tmp_RFCheckIn() {return urlMobile_Clear_Tmp_RFCheckIn;
//    }
//
//
//    //URL urlMobile_INS_Tmp_RFCheckIn
//    private String urlMobile_INS_Tmp_RFCheckIn = strHost + "Mobile_INS_Tmp_RFCheckIn";
//    public String urlMobile_INS_Tmp_RFCheckIn() {return urlMobile_INS_Tmp_RFCheckIn;
//    }
//
//
//    //URL urlMobile_INS_Tmp_RFCheckIn_Parttube
//    private String urlMobile_INS_Tmp_RFCheckIn_Parttube = strHost + "Mobile_INS_Tmp_RFCheckIn_Parttube";
//    public String urlMobile_INS_Tmp_RFCheckIn_Parttube() {return urlMobile_INS_Tmp_RFCheckIn_Parttube;
//    }
//
//    //URL urlMobile_INS_Tmp_RFCheckIn_ChkDigit
//    private String urlMobile_INS_Tmp_RFCheckIn_ChkDigit = strHost + "Mobile_INS_Tmp_RFCheckIn_ChkDigit";
//    public String urlMobile_INS_Tmp_RFCheckIn_ChkDigit() {return urlMobile_INS_Tmp_RFCheckIn_ChkDigit;
//    }
//
//
//    //URL urlMobile_GetReceivePartDetail
//    private String urlMobile_GetReceivePartDetail = strHost + "Mobile_GetReceivePartDetail";
//    public String urlMobile_GetReceivePartDetail() {
//        return urlMobile_GetReceivePartDetail;
//    }
//
//
//    //URL urlMobile_Receive_Hold_Reset
//    private String urlMobile_Receive_Hold_Reset = strHost + "Mobile_Receive_Hold_Reset";
//    public String urlMobile_Receive_Hold_Reset() {
//        return urlMobile_Receive_Hold_Reset;
//    }
//
//    //URL urlMobile_Confirm_ReceivePart
//    private String urlMobile_Confirm_ReceivePart = strHost + "Mobile_Confirm_ReceivePart";
//    public String urlMobile_Confirm_ReceivePart() {
//        return urlMobile_Confirm_ReceivePart;
//    }
//
//    //URL urlMobile_GetStorePartDetail
//    private String urlMobile_GetStorePartDetail = strHost + "Mobile_GetStorePartDetail";
//    public String urlMobile_GetStorePartDetail() {
//        return urlMobile_GetStorePartDetail;
//    }
//
//    //URL urlMobile_GetStorePartTubeDetail
//    private String urlMobile_GetStorePartTubeDetail = strHost + "Mobile_GetStorePartTubeDetail";
//    public String urlMobile_GetStorePartTubeDetail() {
//        return urlMobile_GetStorePartTubeDetail;
//    }
//
//    //URL urlMobile_Store_Hold_Reset
//    private String urlMobile_Store_Hold_Reset = strHost + "Mobile_Store_Hold_Reset";
//    public String urlMobile_Store_Hold_Reset() {
//        return urlMobile_Store_Hold_Reset;
//    }
//
//
//    //URL urlMobile_Store_Parttube_Hold_Reset
//    private String urlMobile_Store_Parttube_Hold_Reset = strHost + "Mobile_Store_Parttube_Hold_Reset";
//    public String urlMobile_Store_Parttube_Hold_Reset() {
//        return urlMobile_Store_Parttube_Hold_Reset;
//    }
//
//
//    //URL urlMobile_Confirm_StorePart
//    private String urlMobile_Confirm_StorePart = strHost + "Mobile_Confirm_StorePart";
//    public String urlMobile_Confirm_StorePart() {
//        return urlMobile_Confirm_StorePart;
//    }
//
//
//    //URL urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube
//    private String urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube = strHost + "Mobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube";
//    public String urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube() {return urlMobile_INS_Tmp_RFCheckIn_ChkDigit_PartTube; }
//
//    //URL urlMobile_UPD_Tmp_RFCheckIn
//    private String urlMobile_UPD_Tmp_RFCheckIn = strHost + "Mobile_UPD_Tmp_RFCheckIn";
//    public String urlMobile_UPD_Tmp_RFCheckIn() {return urlMobile_UPD_Tmp_RFCheckIn;
//    }
//
//    //URL urlMobile_Upd_Tmp_RFCheckIn_PartTube
//    private String urlMobile_Upd_Tmp_RFCheckIn_PartTube = strHost + "Mobile_Upd_Tmp_RFCheckIn_PartTube";
//    public String urlMobile_Upd_Tmp_RFCheckIn_PartTube() {return urlMobile_Upd_Tmp_RFCheckIn_PartTube; }
//
//    //End Barcode System
//
//
//
//    //About URL getCheckVersion
//    private String urlGetCheckVersion = strHost + "getCheckVersion";
//    public String getUrlGetCheckVersion() {
//        return urlGetCheckVersion;
//    }

}// Main Class
