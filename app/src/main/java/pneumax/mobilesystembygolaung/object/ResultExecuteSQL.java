package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultExecuteSQL implements Parcelable {

    public String ResultID;
    public String ResultMessage;

    public final static String TABLE_NAME = "ResultExecuteSQL";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ResultID);
        dest.writeString(this.ResultMessage);
    }

    public ResultExecuteSQL() {
    }

    protected ResultExecuteSQL(Parcel in) {
        this.ResultID = in.readString();
        this.ResultMessage = in.readString();
    }

    public static final Creator<ResultExecuteSQL> CREATOR = new Creator<ResultExecuteSQL>() {
        @Override
        public ResultExecuteSQL createFromParcel(Parcel source) {
            return new ResultExecuteSQL(source);
        }

        @Override
        public ResultExecuteSQL[] newArray(int size) {
            return new ResultExecuteSQL[size];
        }
    };
}
