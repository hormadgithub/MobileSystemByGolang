package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

public class ReturnTwoValue implements Parcelable {
    public String ResultReturnValue1;
    public String ResultReturnValue2;
    public final static String TABLE_NAME = "ReturnTwoValue";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ResultReturnValue1);
        dest.writeString(this.ResultReturnValue2);
    }

    public ReturnTwoValue() {
    }

    protected ReturnTwoValue(Parcel in) {
        this.ResultReturnValue1 = in.readString();
        this.ResultReturnValue2 = in.readString();
    }

    public static final Creator<ReturnTwoValue> CREATOR = new Creator<ReturnTwoValue>() {
        @Override
        public ReturnTwoValue createFromParcel(Parcel source) {
            return new ReturnTwoValue(source);
        }

        @Override
        public ReturnTwoValue[] newArray(int size) {
            return new ReturnTwoValue[size];
        }
    };
}

