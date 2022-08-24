package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sitrach on 02/09/2017.
 */

public class PickPartDetail implements Parcelable {

    public String TmpTime;
    public String DocType;
    public String DocNo;    
    public String PartNID;
    public String LCCode;
    public String Qty;
    public String QtyOut;
    public String QtyDf;
    public String QtyDfOut;
    public String QtyScrap;
    public String QtyScrapOut;
    public String QtyDM;
    public String QtyDMOut;
    public String DstbQty;
    public String DstbQtyOut;
    public String FixDigitNo;
    public String DigitNo;




    public final static String TABLE_NAME = "PickPartDetail";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TmpTime);
        dest.writeString(this.DocType);
        dest.writeString(this.DocNo);
        dest.writeString(this.PartNID);
        dest.writeString(this.LCCode);
        dest.writeString(this.Qty);
        dest.writeString(this.QtyOut);
        dest.writeString(this.QtyDf);
        dest.writeString(this.QtyDfOut);
        dest.writeString(this.QtyScrap);
        dest.writeString(this.QtyScrapOut);
        dest.writeString(this.QtyDM);
        dest.writeString(this.QtyDMOut);
        dest.writeString(this.DstbQty);
        dest.writeString(this.DstbQtyOut);
        dest.writeString(this.FixDigitNo);
        dest.writeString(this.DigitNo);


    }

    public PickPartDetail() {
    }

    protected PickPartDetail(Parcel in) {
        this.TmpTime = in.readString();
        this.DocType = in.readString();
        this.DocNo = in.readString();
        this.PartNID = in.readString();
        this.LCCode = in.readString();
        this.Qty = in.readString();
        this.QtyOut = in.readString();
        this.QtyDf = in.readString();
        this.QtyDfOut = in.readString();
        this.QtyScrap = in.readString();
        this.QtyScrapOut = in.readString();
        this.QtyDM = in.readString();
        this.QtyDMOut = in.readString();
        this.DstbQty = in.readString();
        this.DstbQtyOut = in.readString();
        this.FixDigitNo = in.readString();
        this.DigitNo = in.readString();

    }

    public static final Creator<PickPartDetail> CREATOR = new Creator<PickPartDetail>() {
        @Override
        public PickPartDetail createFromParcel(Parcel source) {
            return new PickPartDetail(source);
        }

        @Override
        public PickPartDetail[] newArray(int size) {
            return new PickPartDetail[size];
        }
    };
}
