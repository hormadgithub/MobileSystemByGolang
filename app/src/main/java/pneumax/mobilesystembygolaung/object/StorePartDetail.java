package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sitrach on 02/09/2017.
 */

public class StorePartDetail implements Parcelable {

    public String TmpTime;
    public String DocType;
    public String DocNo;
    public String PartNID;
    public String LCCode;
    public String Qty;
    public String QtyIn;
    public String QtyDf;
    public String QtyDfIn;
    public String QtyScrap;
    public String QtyScrapIn;
    public String QtyDM;
    public String QtyDMIn;
    public String PartTubeStock;
    public String NumTubeQty;


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
        dest.writeString(this.QtyIn);
        dest.writeString(this.QtyDf);
        dest.writeString(this.QtyDfIn);
        dest.writeString(this.QtyScrap);
        dest.writeString(this.QtyScrapIn);
        dest.writeString(this.QtyDM);
        dest.writeString(this.QtyDMIn);
        dest.writeString(this.PartTubeStock);
        dest.writeString(this.NumTubeQty);


    }

    public StorePartDetail() {
    }

    protected StorePartDetail(Parcel in) {
        this.TmpTime = in.readString();
        this.DocType = in.readString();
        this.DocNo = in.readString();
        this.PartNID = in.readString();
        this.LCCode = in.readString();
        this.Qty = in.readString();
        this.QtyIn = in.readString();
        this.QtyDf = in.readString();
        this.QtyDfIn = in.readString();
        this.QtyScrap = in.readString();
        this.QtyScrapIn = in.readString();
        this.QtyDM = in.readString();
        this.QtyDMIn = in.readString();
        this.PartTubeStock = in.readString();
        this.NumTubeQty = in.readString();

    }

    public static final Creator<StorePartDetail> CREATOR = new Creator<StorePartDetail>() {
        @Override
        public StorePartDetail createFromParcel(Parcel source) {
            return new StorePartDetail(source);
        }

        @Override
        public StorePartDetail[] newArray(int size) {
            return new StorePartDetail[size];
        }
    };
}
