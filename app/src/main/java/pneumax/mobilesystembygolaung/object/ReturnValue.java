package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

public class ReturnValue  implements Parcelable {
    public String ResultReturn;
    public final static String TABLE_NAME = "ReturnValue";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ResultReturn);
    }

    public ReturnValue() {
    }

    protected ReturnValue(Parcel in) {
        this.ResultReturn = in.readString();
    }

    public static final Creator<ReturnValue> CREATOR = new Creator<ReturnValue>() {
        @Override
        public ReturnValue createFromParcel(Parcel source) {
            return new ReturnValue(source);
        }

        @Override
        public ReturnValue[] newArray(int size) {
            return new ReturnValue[size];
        }
    };
}

