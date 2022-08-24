package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sitrach on 02/09/2017.
 */

public class Tmp_RFPhysicalCount implements Parcelable {

    public String TmpTime;
    public String GenNo;
    public String WeekNo;
    public String PartNID;
    public String LCCode;
    public String Qty;
    public String Defect;
    public String Damage;
    public String Scrap;


    public final static String TABLE_NAME = "Tmp_RFPhysicalCount";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TmpTime);
        dest.writeString(this.GenNo);
        dest.writeString(this.WeekNo);
        dest.writeString(this.PartNID);
        dest.writeString(this.LCCode);
        dest.writeString(this.Qty);
        dest.writeString(this.Defect);
        dest.writeString(this.Damage);
        dest.writeString(this.Scrap);


    }

    public Tmp_RFPhysicalCount() {
    }

    protected Tmp_RFPhysicalCount(Parcel in) {
        this.TmpTime = in.readString();
        this.GenNo = in.readString();
        this.WeekNo = in.readString();
        this.PartNID = in.readString();
        this.LCCode = in.readString();
        this.Qty = in.readString();
        this.Defect = in.readString();
        this.Damage = in.readString();
        this.Scrap = in.readString();

    }

    public static final Creator<Tmp_RFPhysicalCount> CREATOR = new Creator<Tmp_RFPhysicalCount>() {
        @Override
        public Tmp_RFPhysicalCount createFromParcel(Parcel source) {
            return new Tmp_RFPhysicalCount(source);
        }

        @Override
        public Tmp_RFPhysicalCount[] newArray(int size) {
            return new Tmp_RFPhysicalCount[size];
        }
    };
}
