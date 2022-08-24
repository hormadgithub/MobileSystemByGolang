package pneumax.mobilesystembygolaung;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import pneumax.mobilesystembygolaung.activity.CheckPartActivity;
import pneumax.mobilesystembygolaung.activity.ClearDocumentError;
import pneumax.mobilesystembygolaung.activity.ConfirmActivity;
import pneumax.mobilesystembygolaung.activity.DeliveryActivity;
import pneumax.mobilesystembygolaung.activity.PhysicalCountActivity;
import pneumax.mobilesystembygolaung.activity.PickingActivity;
import pneumax.mobilesystembygolaung.activity.PickingJobtubeActivity;
import pneumax.mobilesystembygolaung.activity.ReceivingActivity;
import pneumax.mobilesystembygolaung.activity.StoringActivity;
import pneumax.mobilesystembygolaung.connected.GlobalUtility;
import pneumax.mobilesystembygolaung.manager.GlobalVar;
import pneumax.mobilesystembygolaung.manager.MyConstant;
import pneumax.mobilesystembygolaung.object.StaffLogin;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    private  String[] userLoginString;
    private StaffLogin userLogin;
    private String strServerName;
    private String strServerAddress;
    private String strDataBaseName;
    private MyConstant myConstant;
    private GlobalVar globalVar;
    private GlobalUtility globalUtility;

    String strTableName,strField,strCondition,strURL;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Menu  nvaLeftMenuItem;

    Toolbar toolbar;
    View AlertDialogView;
    //Toolbar
    ImageView mimgToolbarMenu;

    //Home Menu
    LinearLayout  mlLayoutPicking,mlLayoutReceive,mlLayoutOther,mlLayoutToolbar,mlLayoutSummary;
    RelativeLayout mrlPicking,mrlConfirm,mrlDelivery,mrlReceive,mrlStoring,mrlPhysicalCount,mrlClearError,mrlPickingJobtube,mrlChecking,mrlSummary;
    ImageView mimgPicking,mimgConfirm,mimgDelivery,mimgReceive,mimgStoring,mimgPhysicalCount,mimgClearError,mimgPickingJobtube,mimgChecking,mimgSummary;
    TextView mlblPicking,mlblConfirm,mlblDelivery,mlblReceive,mlblStoring,mlblPhysicalCount,mlblClearError,mlblPickingJobtube,mlblChecking,mlblSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);

        setContentView(R.layout.activity_main);
        BindWidgets();
        SetEvent();
        InitializeData();
    }

    private void getValueFromIntent() {
        Intent inboundIntent = getIntent();
        userLogin = (StaffLogin) getIntent().getParcelableExtra(StaffLogin.TABLE_NAME);
        strDataBaseName = inboundIntent.getStringExtra(globalVar.getDataBaseName);
        strServerName = inboundIntent.getStringExtra(globalVar.getServerName);


        //สำหรับทดสอบโปรแกรม Golaung เนื่องจาก Server อยู่ที่เคร์่ง Local
        //strServerName=GlobalVar.getInstance().getServerLocal;
        //strDataBaseName="DATATEST";
        //สำหรับทดสอบโปรแกรม Golaung เนื่องจาก Server อยู่ที่เคร์่ง Local

        strTableName="Mobile_Server";
        strField="ServerAddress";
        strCondition="ServerName='"+strServerName+"'";
        // เอา ServerAddress  จาก Server Main และ ต้องเป็น PNUMAXDB
        strURL= myConstant.urlMobile_GetServerAddress();
        strServerAddress=globalUtility.Find_ServerAddress(getApplicationContext(), "PNEUMAXDB",strTableName,strField,strCondition,strURL);
        //strServerAddress=globalUtility.Find_ServerAddress(getApplicationContext(), "PNEUMAXDB",strTableName,strField,strCondition,strURL);

        //แสดงให้เห็นว่าเข้า Database ตัวไหน
        nvaLeftMenuItem=navigationView.getMenu();
        nvaLeftMenuItem.findItem(R.id.nav_server).setTitle(strServerName);
        nvaLeftMenuItem.findItem(R.id.nav_databasename).setTitle(strDataBaseName);

        //17-05-2021 แก้ไขกรณี Error แล้วทำให้ Database หาย ให้ทำการออกจากโปรแกรมทันที
        if (strDataBaseName.equals("")) {
            finish();
            System.exit(0);
        }
    }


    private void BindWidgets() {
        /* Start Left Menu */
       // toolbar = (Toolbar) findViewById(R.id.toolbar1);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        /* End Left Menu */

        //Toolbar
        mimgToolbarMenu=(ImageView) findViewById(R.id.imgToolbarMenu);

        //Menu Large Icon
        mlLayoutPicking=(LinearLayout)  findViewById(R.id.linearLayoutPicking);
        mlLayoutReceive=(LinearLayout)  findViewById(R.id.linearLayoutReceive);
        mlLayoutOther=(LinearLayout)  findViewById(R.id.linearLayoutOther);
        mlLayoutToolbar=(LinearLayout)  findViewById(R.id.linearLayoutToolbar);
        mlLayoutSummary=(LinearLayout)  findViewById(R.id.linearLayoutSummary);


        //Relative
        mrlPicking = (RelativeLayout) findViewById(R.id.rlPicking);
        mrlConfirm = (RelativeLayout) findViewById(R.id.rlConfirm);
        mrlDelivery = (RelativeLayout) findViewById(R.id.rlDelicery);
        mrlReceive = (RelativeLayout) findViewById(R.id.rlReceive);
        mrlStoring = (RelativeLayout) findViewById(R.id.rlStoring);
        mrlPhysicalCount = (RelativeLayout) findViewById(R.id.rlPhysicalCount);
        mrlPickingJobtube = (RelativeLayout) findViewById(R.id.rlPickingJobtube);
        mrlChecking = (RelativeLayout) findViewById(R.id.rlChecking);
        mrlClearError = (RelativeLayout) findViewById(R.id.rlClearError);
        mrlSummary = (RelativeLayout) findViewById(R.id.rlSummary);


        //Image
        mimgPicking = (ImageView) findViewById(R.id.imgPicking);
        mimgConfirm = (ImageView) findViewById(R.id.imgConfirm);
        mimgDelivery = (ImageView) findViewById(R.id.imgDelicery);
        mimgReceive = (ImageView) findViewById(R.id.imgReceive);
        mimgStoring = (ImageView) findViewById(R.id.imgStoring);
        mimgPhysicalCount = (ImageView) findViewById(R.id.imgPhysicalCount);
        mimgPickingJobtube = (ImageView) findViewById(R.id.imgPickingJobtube);
        mimgChecking = (ImageView) findViewById(R.id.imgChecking);
        mimgClearError = (ImageView) findViewById(R.id.imgClearError);
        mimgSummary = (ImageView) findViewById(R.id.imgSummary);

        //Label
        mlblPicking= (TextView) findViewById(R.id.lblPicking);
        mlblConfirm = (TextView) findViewById(R.id.lblConfirm);
        mlblDelivery = (TextView) findViewById(R.id.lblDelicery);
        mlblReceive = (TextView) findViewById(R.id.lblReceive);
        mlblStoring = (TextView) findViewById(R.id.lblStoring);
        mlblPhysicalCount = (TextView) findViewById(R.id.lblPhysicalCount);
        mlblPickingJobtube = (TextView) findViewById(R.id.lblPickingJobtube);
        mlblChecking = (TextView) findViewById(R.id.lblChecking);
        mlblClearError = (TextView) findViewById(R.id.lblClearError);
        mlblSummary = (TextView) findViewById(R.id.lblSummary);

    }

    private void InitializeData() {
        myConstant = new MyConstant();
        globalVar = new GlobalVar();
        globalUtility = new GlobalUtility();

        mlLayoutToolbar.bringToFront();
        Intent sourceIntent = getIntent();
        if(sourceIntent == null){
            Hide_Home_Menu();
            Show_Left_Menu();
            Intent intentLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLoginActivity);
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            //Toast.makeText(MainActivity.this, "sourceIntent == null" , Toast.LENGTH_SHORT).show();
        }else{
            Bundle params = sourceIntent.getExtras();
            if(params != null)
            {
                getValueFromIntent();
                Show_Left_Menu();
                Show_Home_Menu();
                //Toast.makeText(MainActivity.this, "params != null" , Toast.LENGTH_SHORT).show();
            }else{
                Hide_Home_Menu();
                Show_Left_Menu();
                Intent intentLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLoginActivity);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                //Toast.makeText(MainActivity.this, "Default" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetColor_MenuHome_Default (){

        //Relative Layout
        mrlPicking.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlConfirm.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlDelivery.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        mrlReceive.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlStoring.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlPhysicalCount.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        mrlPickingJobtube.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlChecking.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mrlClearError.setBackgroundColor(getColor(R.color.color_Cardview_Default));


        mrlSummary.setBackgroundColor(getColor(R.color.color_Cardview_Default));

       //image
        mimgPicking.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgConfirm.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgDelivery.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        mimgReceive.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgStoring.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgPhysicalCount.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        mimgPickingJobtube.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgChecking.setBackgroundColor(getColor(R.color.color_Cardview_Default));
        mimgClearError.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        mimgSummary.setBackgroundColor(getColor(R.color.color_Cardview_Default));

        //label
        mlblPicking.setTextColor(getColor(R.color.color_MenuHome_Default));
        mlblConfirm.setTextColor(getColor(R.color.color_MenuHome_Default));
        mlblDelivery.setTextColor(getColor(R.color.color_MenuHome_Default));

        mlblReceive.setTextColor(getColor(R.color.color_MenuHome_Default));
        mlblStoring.setTextColor(getColor(R.color.color_MenuHome_Default));
        mlblPhysicalCount.setTextColor(getColor(R.color.color_MenuHome_Default));

        mlblPickingJobtube.setTextColor(getColor(R.color.color_MenuHome_Default));
        mlblChecking.setTextColor(getColor(R.color.color_MenuHome_Default));
        mlblClearError.setTextColor(getColor(R.color.color_MenuHome_Default));

        mlblSummary.setTextColor(getColor(R.color.color_MenuHome_Default));

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_login:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                Hide_Home_Menu();
                break;
            case R.id.nav_exit:
                finish();
                System.exit(0);
                break;
//ปิดเมนูอื่นไว้ให้ใช้หน้า Home อย่างเดียว
//
//            case R.id.nav_picking:
//                startPicking();
//                break;
//            case R.id.nav_confirm:
//                startConfirm();
//                break;
//            case R.id.nav_delivery:
//                startDelivery();
//                break;
//
//            case R.id.nav_receive:
//                break;
//            case R.id.nav_storing:
//                break;
//            case R.id.nav_PhysicalCount:
//                startPhysicalCount();
//                break;
//            case R.id.nav_PickingJobtube:
//                break;
//            case R.id.nav_Checking:
//                startChecking();
//                break;
//            case R.id.nav_ClearError:
//                break;

        }

        return true ;
    }



    private void Hide_Home_Menu() {
        nvaLeftMenuItem=navigationView.getMenu();
        //ไม่ทำงานตามที่สั่ง ซ่อนได้แต่ สั่งให้ Show ไม่ยอม Show
//        nvaLeftMenuItem.findItem(R.id.nav_logout).setVisible(false);
//        nvaLeftMenuItem.findItem(R.id.nav_login).setVisible(true);
//        nvaLeftMenuItem.findItem (R.id.itemMenu_Main).getSubMenu ().setGroupVisible (R.id.subMenu_Main, false);

        nvaLeftMenuItem.findItem(R.id.nav_logout).setEnabled(false);

        nvaLeftMenuItem.findItem(R.id.nav_login).setEnabled(true);
       // nvaLeftMenuItem.findItem (R.id.itemMenu_Main).getSubMenu ().setGroupEnabled (R.id.subMenu_Main, false);
        mlLayoutPicking.setVisibility(View.GONE);
        mlLayoutReceive.setVisibility(View.GONE);
        mlLayoutOther.setVisibility(View.GONE);
        Show_Left_Menu();
    }

    private void Show_Home_Menu() {
        nvaLeftMenuItem = navigationView.getMenu();
        //ไม่ทำงานตามที่สั่ง ซ่อนได้แต่ สั่งให้ Show ไม่ยอม Show
//        nvaLeftMenuItem.findItem(R.id.nav_login).setVisible(true);
//        nvaLeftMenuItem.findItem(R.id.nav_logout).setVisible(true);
//        nvaLeftMenuItem.findItem (R.id.itemMenu_Main).getSubMenu ().setGroupVisible (R.id.subMenu_Main, true);

        nvaLeftMenuItem.findItem(R.id.nav_logout).setEnabled(true);
        nvaLeftMenuItem.findItem(R.id.nav_login).setEnabled(false);
//        nvaLeftMenuItem.findItem (R.id.itemMenu_Main).getSubMenu ().setGroupEnabled (R.id.subMenu_Main, true);
        mlLayoutPicking.setVisibility(View.VISIBLE);
        mlLayoutReceive.setVisibility(View.VISIBLE);
        mlLayoutOther.setVisibility(View.VISIBLE);

        //31-08-2021 เมนู Summary ให้เห็นเฉพาะ MIS
        if (userLogin.DPCode.trim().equals("MIS")){
            mlLayoutSummary.setVisibility(View.VISIBLE);
        }
        else {
            mlLayoutSummary.setVisibility(View.GONE);
        }
        //ปิดบางเมนูไม่ให้ใช้งาน
        //27-04-2021 เปิดใช้งานทุกเมนู
//        if (! userLogin.DPCode.trim().equals("MIS")){
//            Disable_AllMenu();
//            mimgPicking.setEnabled(true);
//            mimgPicking.setImageResource(R.drawable.ic_picking);
//
//            mimgConfirm.setEnabled(true);
//            mimgConfirm.setImageResource(R.drawable.ic_confirm);
//
//            mimgDelivery.setEnabled(true);
//            mimgDelivery.setImageResource(R.drawable.ic_delivery);
//
//            mimgPickingJobtube.setEnabled(true);
//            mimgPickingJobtube.setImageResource(R.drawable.ic_picking_jobtube_orange);
//
//            mimgChecking.setEnabled(true);
//            mimgChecking.setImageResource(R.drawable.ic_checking_orange_300);
//
//            mimgPhysicalCount.setEnabled(true);
//            mimgPhysicalCount.setImageResource(R.drawable.ic_physicalcount_orange_300);
//
//            mimgClearError.setEnabled(true);
//            mimgClearError.setImageResource(R.drawable.ic_clearerror_300);

//            mimgSummary.setEnabled(true);
//            mimgSummary.setImageResource(R.drawable.ic_clearerror_300);
//
//        }

        Hide_Left_Menu();
    }

    private void Disable_AllMenu(){
        mimgPicking.setEnabled(false);
        mimgPicking.setImageResource(R.drawable.ic_waitprocess);
        mimgConfirm.setEnabled(false);
        mimgConfirm.setImageResource(R.drawable.ic_waitprocess);
        mimgDelivery.setEnabled(false);
        mimgDelivery.setImageResource(R.drawable.ic_waitprocess);

        mimgReceive.setEnabled(false);
        mimgReceive.setImageResource(R.drawable.ic_waitprocess);
        mimgStoring.setEnabled(false);
        mimgStoring.setImageResource(R.drawable.ic_waitprocess);
        mimgPhysicalCount.setEnabled(false);
        mimgPhysicalCount.setImageResource(R.drawable.ic_waitprocess);

        mimgClearError.setEnabled(false);
        mimgClearError.setImageResource(R.drawable.ic_waitprocess);
        mimgPickingJobtube.setEnabled(false);
        mimgPickingJobtube.setImageResource(R.drawable.ic_waitprocess);
        mimgChecking.setEnabled(false);
        mimgChecking.setImageResource(R.drawable.ic_waitprocess);

        mimgSummary.setEnabled(false);
        mimgSummary.setImageResource(R.drawable.ic_waitprocess);


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
           drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    private void Show_Left_Menu() {
        if (! drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.openDrawer(GravityCompat.START);
            navigationView.bringToFront();
        }
    }

    private void Hide_Left_Menu() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    private void SetEvent() {
        mimgToolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        mimgPicking.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlPicking.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblPicking.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgPicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowAlertDialog(R.string.alertdialog_success,"Complete","Job Completed",R.drawable.alertdialog_ic_success);
                SetColor_MenuHome_Default();
                mrlPicking.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblPicking.setTextColor(getColor(R.color.color_Cardview_Default));
                startPicking();
            }
        });

        mimgConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlConfirm.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblConfirm.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlConfirm.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblConfirm.setTextColor(getColor(R.color.color_Cardview_Default));
                startConfirm();
            }
        });

        mimgDelivery.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlDelivery.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblDelivery.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlDelivery.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblDelivery.setTextColor(getColor(R.color.color_Cardview_Default));
                startDelivery();
            }
        });

        mimgReceive.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlReceive.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblReceive.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlReceive.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblReceive.setTextColor(getColor(R.color.color_Cardview_Default));
                startReceiving();
            }
        });

        mimgStoring.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlStoring.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblStoring.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgStoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlStoring.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblStoring.setTextColor(getColor(R.color.color_Cardview_Default));
                startStoring();
            }
        });

        mimgPhysicalCount.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlPhysicalCount.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblPhysicalCount.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgPhysicalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlPhysicalCount.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblPhysicalCount.setTextColor(getColor(R.color.color_Cardview_Default));
                startPhysicalCount();
            }
        });

        mimgPickingJobtube.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlPickingJobtube.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblPickingJobtube.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgPickingJobtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlPickingJobtube.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblPickingJobtube.setTextColor(getColor(R.color.color_Cardview_Default));
                startPickingJobtube();
            }
        });

        mimgChecking.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlChecking.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblChecking.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlChecking.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblChecking.setTextColor(getColor(R.color.color_Cardview_Default));
                startChecking();
            }
        });


        mimgClearError.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlClearError.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblClearError.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgClearError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlClearError.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblClearError.setTextColor(getColor(R.color.color_Cardview_Default));
                startClearDocumentError();
            }
        });

        mimgSummary.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                SetColor_MenuHome_Default();
                if (hasFocus) {
                    mrlSummary.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                    mlblSummary.setTextColor(getColor(R.color.color_Cardview_Default));
                }
            }
        });
        mimgSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetColor_MenuHome_Default();
                mrlSummary.setBackgroundColor(getColor(R.color.color_Cardview_Selected));
                mlblSummary.setTextColor(getColor(R.color.color_Cardview_Default));
                startSummaryMenu();
            }
        });
    }

    private void startPicking() {
        Intent intent = new Intent(MainActivity.this, PickingActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_top,R.anim.slide_in_bottom);
    }

    private void startConfirm() {
        Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void startDelivery() {
        Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    private void startReceiving() {
        Intent intent = new Intent(MainActivity.this, ReceivingActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void startStoring() {
        Intent intent = new Intent(MainActivity.this, StoringActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    private void startPhysicalCount() {
        Intent intent = new Intent(MainActivity.this, PhysicalCountActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    private void startPickingJobtube() {
        Intent intent = new Intent(MainActivity.this, PickingJobtubeActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void startChecking() {
        Intent intent = new Intent(MainActivity.this, CheckPartActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    private void startClearDocumentError() {
        Intent intent = new Intent(MainActivity.this, ClearDocumentError.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    private void startSummaryMenu() {
        Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
        intent.putExtra(GlobalVar.getServerAddress, strServerAddress);
        intent.putExtra(GlobalVar.getDataBaseName, strDataBaseName);
        intent.putExtra(StaffLogin.TABLE_NAME, userLogin);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void ShowAlertDialog(int intAlertType, String strAlertTitle, String strAlertMessage, int ic_AlertDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AlertDialog);

        if (intAlertType == R.string.alertdialog_success) {
            AlertDialogView = LayoutInflater.from(MainActivity.this).inflate(
                    R.layout.layout_success_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        if (intAlertType == R.string.alertdialog_warning) {
            AlertDialogView = LayoutInflater.from(MainActivity.this).inflate(
                    R.layout.layout_warning_dialog,
                    (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
            );
        }

        if (intAlertType == R.string.alertdialog_error) {
            AlertDialogView = LayoutInflater.from(MainActivity.this).inflate(
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