package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santi on 23-06-2020.
 */


public class PhysicalCount implements Parcelable {

    public String GenNo;
    public int WeekNo;
    public int IsstimePeriod;
    public String LCcode;
    public String PartNid;


    public final static String TABLE_NAME = "PhysicalCount";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.GenNo);
        dest.writeInt(this.WeekNo);
        dest.writeInt(this.IsstimePeriod);
        dest.writeString(this.LCcode);
        dest.writeString(this.PartNid);
    }

    public PhysicalCount() {
    }

    protected PhysicalCount(Parcel in) {
        this.GenNo = in.readString();
        this.WeekNo = in.readInt();
        this.IsstimePeriod = in.readInt();
        this.LCcode = in.readString();
        this.PartNid = in.readString();
        this.LCcode = in.readString();
    }

    public static final Creator<PhysicalCount> CREATOR = new Creator<PhysicalCount>() {
        @Override
        public PhysicalCount createFromParcel(Parcel source) {
            return new PhysicalCount(source);
        }

        @Override
        public PhysicalCount[] newArray(int size) {
            return new PhysicalCount[size];
        }
    };
}
