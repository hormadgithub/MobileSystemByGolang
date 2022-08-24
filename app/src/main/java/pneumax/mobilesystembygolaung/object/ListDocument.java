package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

public class ListDocument  implements Parcelable {
    public String Docno;
    public String CSName;

    public final static String TABLE_NAME = "ListDocument";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Docno);
        dest.writeString(this.CSName);
    }

    public ListDocument() {
    }

    protected ListDocument(Parcel in) {
        this.Docno = in.readString();
        this.CSName = in.readString();
    }

    public static final Creator<ListDocument> CREATOR = new Creator<ListDocument>() {
        @Override
        public ListDocument createFromParcel(Parcel source) {
            return new ListDocument(source);
        }

        @Override
        public ListDocument[] newArray(int size) {
            return new ListDocument[size];
        }
    };

}
