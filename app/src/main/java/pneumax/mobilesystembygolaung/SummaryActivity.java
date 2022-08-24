package pneumax.mobilesystembygolaung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pneumax.mobilesystembygolaung.Summary.SummaryHandHeldOperate;
import pneumax.mobilesystembygolaung.Summary.SummaryRFCheckInActivity;
import pneumax.mobilesystembygolaung.Summary.SummaryRFCheckOutActivity;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.Result;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class SummaryActivity extends AppCompatActivity {
    MyConstant myConstant;
    GlobalVar globalVar;
    GlobalUtility globalUtility;
    //parameter
    StaffLogin userLogin;
    String strServerAddress;
    String strServerName;
    String strDataBaseName;

    //parameter สำกรับส่งไป Execute
    String strTableName,strField,strCondition,strURL;

    String strReturnValue;
    Result clsResult;
    String strResultReturnValue;
    View AlertDialogView;
    String strAlertMessage;

    //From Layout
    LinearLayout mlLayoutSummary;
    RelativeLayout mrlSummaryRFCheckOut,mrlSummaryRFCheckIn,mrlSummaryHandHeldOperate;
    ImageView mimgBackTop,mimgSummaryRFCheckOut,mimgSummaryRFCheckIn,mimgSummaryHandHeldOperate;
    TextView mlblSummaryRFCheckOut,mlblSummaryRFCheckIn,mlblSummaryHandHeldOperate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_summary);

        getValueFromIntent();
        BindWidgets();
        SetEvent();
        InitializeData();
    }

    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strServerAddress = inboundIntent.getStringExtra(globalVar.getServerAddress);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
    }


    private void BindWidgets() {
        mimgBackTop = (ImageView) findViewById(R.id.imgBackTop);

        //Menu Large Icon
        mlLayoutSummary=(LinearLayout)  findViewById(R.id.linearLayoutSummary);

        //Relative
        mrlSummaryRFCheckOut = (RelativeLayout) findViewById(R.id.rlSummaryRFCheckOut);
        mrlSummaryRFCheckIn = (RelativeLayout) findViewById(R.id.rlSummaryRFCheckIn);
        mrlSummaryHandHeldOperate = (RelativeLayout) findViewById(R.id.rlSummaryHandHeldOperate);

        //Image
        mimgSummaryRFCheckOut = (ImageView) findViewById(R.id.imgSummaryRFCheckOut);
        mimgSummaryRFCheckIn = (ImageView) findViewById(R.id.imgSummaryRFCheckIn);
        mimgSummaryHandHeldOperate = (ImageView) findViewById(R.id.imgSummaryHandHeldOperate);

        //Label
        mlblSummaryRFCheckOut = (TextView) findViewById(R.id.lblSummaryRFCheckOut);
        mlblSummaryRFCheckIn = (TextView) findViewById(R.id.lblSummaryRFCheckIn);
        mlblSummaryHandHeldOperate = (TextView) findViewById(R.id.lblSummaryHandHeldOperate);
    }

    private void InitializeData() {
        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility=new GlobalUtility();
    }

    private void SetColor_MenuSummary_Default (){
        //Relative Layout
        mrlSummaryRFCheckOut.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlSummaryRFCheckIn.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlSummaryHandHeldOperate.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        //image
        mimgSummaryRFCheckOut.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgSummaryRFCheckIn.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgSummaryHandHeldOperate.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        //label
        mlblSummaryRFCheckOut.setTextColor(getColor(R.color.color_MenuSummary_Default));
        mlblSummaryRFCheckIn.setTextColor(getColor(R.color.color_MenuSummary_Default));
        mlblSummaryHandHeldOperate.setTextColor(getColor(R.color.color_MenuSummary_Default));
    }

    private void Disable_AllMenu(){
        mimgSummaryRFCheckOut.setEnabled(false);
        mimgSummaryRFCheckOut.setImageResource(R.drawable.ic_waitprocess);
        mimgSummaryRFCheckIn.setEnabled(false);
        mimgSummaryRFCheckIn.setImageResource(R.drawable.ic_waitprocess);
        mimgSummaryHandHeldOperate.setEnabled(false);
        mimgSummaryHandHeldOperate.setImageResource(R.drawable.ic_waitprocess);
    }

    private void SetEvent() {
        mimgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
            }
        });
        mimgSummaryRFCheckOut.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuSummary_Default();
                if (hasFocus) {
                    mrlSummaryRFCheckOut.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblSummaryRFCheckOut.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgSummaryRFCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowAlertDialog(R.string.alertdialog_success,"Complete","Job Completed",R.drawable.alertdialog_ic_success);
                SetColor_MenuSummary_Default();
                mrlSummaryRFCheckOut.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblSummaryRFCheckOut.setTextColor(getColor(R.color.color_Cardview_Default));
                startSummaryRFCheckOut();
            }
        });

        mimgSummaryRFCheckIn.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuSummary_Default();
                if (hasFocus) {
                    mrlSummaryRFCheckIn.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblSummaryRFCheckIn.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgSummaryRFCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuSummary_Default();
                mrlSummaryRFCheckIn.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblSummaryRFCheckIn.setTextColor(getColor(R.color.color_Cardview_Default));
                startSummaryRFCheckIn();
            }
        });

        mimgSummaryHandHeldOperate.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuSummary_Default();
                if (hasFocus) {
                    mrlSummaryHandHeldOperate.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblSummaryHandHeldOperate.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgSummaryHandHeldOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowAlertDialog(R.string.alertdialog_success,"Complete","Job Completed",R.drawable.alertdialog_ic_success);
                SetColor_MenuSummary_Default();
                mrlSummaryHandHeldOperate.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblSummaryHandHeldOperate.setTextColor(getColor(R.color.color_Cardview_Default));
                startSummaryHandHeldOperate();
            }
        });
    }

    private void startSummaryRFCheckOut() {
        Intent intent = new Intent(SummaryActivity.this, SummaryRFCheckOutActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_top,R.anim.slide_in_bottom);
    }

    private void startSummaryRFCheckIn() {
        Intent intent = new Intent(SummaryActivity.this, SummaryRFCheckInActivity.class);

        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_top,R.anim.slide_in_bottom);
    }

    private void startSummaryHandHeldOperate() {
        Intent intent = new Intent(SummaryActivity.this, SummaryHandHeldOperate.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_top,R.anim.slide_in_bottom);
    }


    private void ShowAlertDialog(int intAlertType, String strAlertTitle, String strAlertMessage, int ic_AlertDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this, R.style.Theme_AlertDialog);

        if (intAlertType == R.string.alertdialog_success) {
            AlertDialogView = LayoutInflater.from(SummaryActivity.this).inflate(
                    R.layout.layout_success_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        if (intAlertType == R.string.alertdialog_warning) {
            AlertDialogView = LayoutInflater.from(SummaryActivity.this).inflate(
                    R.layout.layout_warning_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        if (intAlertType == R.string.alertdialog_error) {
            AlertDialogView = LayoutInflater.from(SummaryActivity.this).inflate(
                    R.layout.layout_error_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        builder.setView(AlertDialogView);
        ((TextView) AlertDialogView.findViewById(R.id.textTitle)).setText(strAlertTitle);
        ((TextView) AlertDialogView.findViewById(R.id.textMessage)).setText(strAlertMessage);
        ((ImageView) AlertDialogView.findViewById(R.id.imageIcon)).setImageResource(ic_AlertDialog);

        if (intAlertType == R.string.alertdialog_success || intAlertType == R.string.alertdialog_error) {
            ((Button) AlertDialogView.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.alertdialog_okay));
            final AlertDialog alertDialog = builder.create();
            AlertDialogView.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        }

        if (intAlertType == R.string.alertdialog_warning ) {
            ((Button) AlertDialogView.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.yes));
            ((Button) AlertDialogView.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.no));
            final AlertDialog alertDialog = builder.create();
            AlertDialogView.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            AlertDialogView.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        }

    }
}