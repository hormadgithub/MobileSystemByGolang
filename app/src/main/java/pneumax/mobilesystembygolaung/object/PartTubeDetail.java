package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santi on 23-06-2020.
 */


public class PartTubeDetail implements Parcelable {

    public String Partnid;
    public String Digitno;
    public String Onhand;
    public String DMLength;
    public String RealQty;


    public final static String TABLE_NAME = "PartTube";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Partnid);
        dest.writeString(this.Digitno);
        dest.writeString(this.Onhand);
        dest.writeString(this.DMLength);
        dest.writeString(this.RealQty);
    }

    public PartTubeDetail() {
    }

    protected PartTubeDetail(Parcel in) {
        this.Partnid = in.readString();
        this.Digitno = in.readString();
        this.Onhand = in.readString();
        this.DMLength = in.readString();
        this.RealQty = in.readString();

    }

    public static final Creator<PartTubeDetail> CREATOR = new Creator<PartTubeDetail>() {
        @Override
        public PartTubeDetail createFromParcel(Parcel source) {
            return new PartTubeDetail(source);
        }

        @Override
        public PartTubeDetail[] newArray(int size) {
            return new PartTubeDetail[size];
        }
    };
}
