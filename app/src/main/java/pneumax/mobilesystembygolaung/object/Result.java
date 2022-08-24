package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Result   implements Parcelable {
    public String ResultID;
    public String ResultMessage;

    public final static String TABLE_NAME = "Result";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ResultID);
        dest.writeString(this.ResultMessage);

    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.ResultID = in.readString();
        this.ResultMessage = in.readString();

    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
