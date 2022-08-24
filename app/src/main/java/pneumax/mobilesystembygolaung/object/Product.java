package pneumax.mobilesystembygolaung.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sitrach on 02/09/2017.
 */

public class Product implements Parcelable {

    public String PartNID;
    public String PartNo;
    public String PartDes;
    public String PackQty;
    public String WHcode;
    public String LCcode;
    public String Warehouse;
    public String LCcode2;
    public String Warehouse2;
    public String PartOnHand;
    public String PartOnDefect;
    public String PartOnCRO;
    public String PartOnBOR;
    public String PartOnDamage;
    public String PartOnRSV;
    public String PartOnOrder;
    public String PrintLabel;
    public String PartHaveSerialNo;


    public final static String TABLE_NAME = "Product";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PartNID);
        dest.writeString(this.PartNo);
        dest.writeString(this.PartDes);
        dest.writeString(this.PackQty);
        dest.writeString(this.WHcode);
        dest.writeString(this.LCcode);
        dest.writeString(this.Warehouse);
        dest.writeString(this.LCcode2);
        dest.writeString(this.Warehouse2);
        dest.writeString(this.PartOnHand);
        dest.writeString(this.PartOnDefect);
        dest.writeString(this.PartOnCRO);
        dest.writeString(this.PartOnBOR);
        dest.writeString(this.PartOnDamage);
        dest.writeString(this.PartOnRSV);
        dest.writeString(this.PartOnOrder);
        dest.writeString(this.PrintLabel);
        dest.writeString(this.PartHaveSerialNo);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.PartNID = in.readString();
        this.PartNo = in.readString();
        this.PartDes = in.readString();
        this.PackQty = in.readString();
        this.WHcode = in.readString();
        this.LCcode = in.readString();
        this.Warehouse = in.readString();
        this.LCcode2 = in.readString();
        this.Warehouse2 = in.readString();
        this.PartOnHand = in.readString();
        this.PartOnDefect = in.readString();
        this.PartOnCRO = in.readString();
        this.PartOnBOR = in.readString();
        this.PartOnDamage = in.readString();
        this.PartOnRSV = in.readString();
        this.PartOnOrder = in.readString();
        this.PrintLabel = in.readString();
        this.PartHaveSerialNo = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
