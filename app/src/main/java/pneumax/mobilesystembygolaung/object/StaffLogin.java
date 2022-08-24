package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sitrach on 02/09/2017.
 */

public class StaffLogin implements Parcelable {

    public String STFcode;
    public String STFfname;
    public String STFLname;
    public String STFfullname;
    public String DPCode;
    public String MobilePickInputManual;
    public String MobileStoreInputManual;

    public final static String TABLE_NAME = "StaffLogin";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.STFcode);
        dest.writeString(this.STFfname);
        dest.writeString(this.STFLname);
        dest.writeString(this.STFfullname);
        dest.writeString(this.DPCode);
        dest.writeString(this.MobilePickInputManual);
        dest.writeString(this.MobileStoreInputManual);
    }

    public StaffLogin() {
    }

    protected StaffLogin(Parcel in) {
        this.STFcode = in.readString();
        this.STFfname = in.readString();
        this.STFLname = in.readString();
        this.STFfullname = in.readString();
        this.DPCode = in.readString();
        this.MobilePickInputManual = in.readString();
        this.MobileStoreInputManual = in.readString();
    }

    public static final Creator<StaffLogin> CREATOR = new Creator<StaffLogin>() {
        @Override
        public StaffLogin createFromParcel(Parcel source) {
            return new StaffLogin(source);
        }

        @Override
        public StaffLogin[] newArray(int size) {
            return new StaffLogin[size];
        }
    };
}
