package ranglerz.com.laptophub;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import ranglerz.com.laptophub.util.IabBroadcastReceiver;
import ranglerz.com.laptophub.util.IabHelper;
import ranglerz.com.laptophub.util.IabResult;
import ranglerz.com.laptophub.util.Inventory;
import ranglerz.com.laptophub.util.Purchase;


public class LaptopsList extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener{

    ImageView iv_laptop1, iv_laptop2, iv_laptop3;
    TextView tv_laptop1, tv_laptop2, tv_laptop3;
    TextView tv_price1, tv_price2, tv_price3;
    Button bt_laptop1, bt_laptop2, bt_laptop3;
    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    TextView whenAllLaptopBought;
    HorizontalScrollView h_scrollView;
    ImageView rightArrow, leftArrow;

    int samsung_laptop = 0;
    int apple_laptop = 0;
    int lenovo_laptop = 0;
    int accer_laptop = 0;
    int dell_laptop = 0;
    int hp_laptop = 0;

    static final String TAG = "LaptopsListActivty";
    static final String BILLING_CHARGES = "+ (30% Google Biling Service Charges)";

    //inApp purchase ID for Beard
    static final String SKU_SAMSUNG_NP35E7C = "samsungnp35e7c";
    static final String SKU_SAMSUNG_TOUCBSCREEN_SLATE = "sumsungtouchscreenslate";
    static final String SKU_SAMSUNG_CHROMEBOOK3 = "samsungchromebook3";
    static final String SKU_APPLE_MACKBOOK_A1342 = "applemackbooka1342";
    static final String SKU_APPLE_MACKBOOK_PRO = "applemackbookpro";
    static final String SKU_APPLE_MACKBOOK_UNIBODY = "applemackbookunibody";
    static final String SKU_LENOVO_K500 = "lenovok500";
    static final String SKU_LENOVO_THINKPAD_X230I = "lenovothinkpadx230i";
    static final String SKU_LENOVO_THINKPAD_X240 = "lenovothinkpadx240";
    static final String SKU_ACCER_ES1531POJJ = "acceres1531pojj";
    static final String SKU_ACCER_ASPIRE5741G = "accernotebookaspire5741g";
    static final String SKU_ACCER_ONENOTEBOOKD270 = "acceraspireonenotebookd270";
    static final String SKU_DELL_INPIRON1545 = "dellinpiron1545";
    static final String SKU_DELL_INPIRON7746 = "dellinpiron7746";
    static final String SKU_DELL_LATITUDE3330 = "delllatitude3330";
    static final String SKU_HP_PAVILION_A40725A = "hppaviliona40725a";
    static final String SKU_HP_FOLIO1029WM = "hpfolio1029wm";
    static final String SKU_HP_TOUCHSCREEN_INTEL_6100U = "hptouchscreen6100u";

    String cutomer_phn, cutomer_adrs;

    //in app billing
    IabHelper mHelper;
    String devPayLoad = "";
    private static final int IAPCODE = 10001;
    static final int RC_REQUEST = 10001;
    boolean mIsPremium = false;
    boolean mSubscribedToInfiniteGas = false;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptops_list);

        init();
        keyInitialization();
        startUpLab();
        loadData();
        getTingIntent();
        getVisibiliteyForLaptops();


    }
    public void init(){
        iv_laptop1 = (ImageView) findViewById(R.id.iv_laptop1);
        iv_laptop2 = (ImageView) findViewById(R.id.iv_laptop2);
        iv_laptop3 = (ImageView) findViewById(R.id.iv_laptop3);
        tv_laptop1 = (TextView) findViewById(R.id.tv_laptop1);
        tv_laptop2 = (TextView) findViewById(R.id.tv_laptop2);
        tv_laptop3 = (TextView) findViewById(R.id.tv_laptop3);
        tv_price1 = (TextView) findViewById(R.id.tv_price1);
        tv_price2 = (TextView) findViewById(R.id.tv_price2);
        tv_price3 = (TextView) findViewById(R.id.tv_price3);
        bt_laptop1 = (Button) findViewById(R.id.bt_addtocart1);
        bt_laptop2 = (Button) findViewById(R.id.bt_addtocart2);
        bt_laptop3 = (Button) findViewById(R.id.bt_addtocart3);

        h_scrollView = (HorizontalScrollView) findViewById(R.id.h_scrol_view);
        leftArrow = (ImageView) findViewById(R.id.arrow_left);
        rightArrow = (ImageView) findViewById(R.id.arrow_right);
        rightArrow.setVisibility(View.GONE);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relative_layout_1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative_layout_2);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.relative_layout_3);
        whenAllLaptopBought = (TextView) findViewById(R.id.allbought);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(LaptopsList.this ,R.color.colorBlack)));
    }

    public void getTingIntent(){
        Intent intent = getIntent();
        String samsung = intent.getStringExtra("Samsung");
        String apple = intent.getStringExtra("Apple");
        String lenovo = intent.getStringExtra("Lenovo");
        String accer = intent.getStringExtra("Accer");
        String dell = intent.getStringExtra("Dell");
        String hp = intent.getStringExtra("HP");

        sharedPreferences = getSharedPreferences("FORSELLLAPTOP", 0);

        if (samsung!=null){
        if (samsung.equals("Samsung")) {

            int samsung1 = sharedPreferences.getInt("SAMSUNG1", 0);
            int samsung2 = sharedPreferences.getInt("SAMSUNG2", 0);
            int samsung3 = sharedPreferences.getInt("SAMSUNG3", 0);


            if (samsung1==1){
                relativeLayout1.setVisibility(View.GONE);
            }else {

                relativeLayout1.setVisibility(View.VISIBLE);

                iv_laptop1.setImageResource(R.drawable.samsung_np350e7c_300);
                tv_laptop1.setText("Samsung NP35E7C Notebook A4-4300M AMD Dual Core 17.3 inch 4GB Ram 500GB Hard Disk ");
                tv_price1.setText("$300");


                bt_laptop1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        samsung_laptop = 1;
                        showSingleImageDialog();
                    }
                });
            }


            if (samsung2==2){
                relativeLayout2.setVisibility(View.GONE);
            }else {
                relativeLayout2.setVisibility(View.VISIBLE);
                iv_laptop2.setImageResource(R.drawable.samsung_toucscreen_pc_table_150);
                tv_laptop2.setText("Samsung Touch Screen Slate 11 inch Tablec PC Core i5 1.6GHz 4GB RAM 128GB Hard ");
                tv_price2.setText("$140");

                bt_laptop2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        samsung_laptop = 2;
                        showSingleImageDialog();
                    }
                });
            }


            if (samsung3==3){
                relativeLayout3.setVisibility(View.GONE);
            }else {
                relativeLayout3.setVisibility(View.VISIBLE);
            iv_laptop3.setImageResource(R.drawable.samung_chrombook3_180);
            tv_price3.setText("$180");
            tv_laptop3.setText("Samsung Chromebook 3 11.6 inch 1.65GHz Webcam ");

            bt_laptop3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    samsung_laptop = 3;
                    showSingleImageDialog();
                }
            });
            }

            if (relativeLayout1.getVisibility()==View.GONE && relativeLayout2.getVisibility()==View.GONE && relativeLayout3.getVisibility()==View.GONE){
                whenAllLaptopBought.setVisibility(View.VISIBLE);
                whenAllLaptopBought.setText("You Bought All Samsung Laptops");
            }

            getSupportActionBar().setTitle("Samsung Laptops");
        }

        }//end of samsung



        if (apple!=null){
        if (apple.equals("Apple")){

            int apple1 = sharedPreferences.getInt("APPLE1", 0);
            int apple2 = sharedPreferences.getInt("APPLE2", 0);
            int apple3 = sharedPreferences.getInt("APPLE3", 0);

            if (apple1 == 1){
                relativeLayout1.setVisibility(View.GONE);
            }else {
                relativeLayout1.setVisibility(View.VISIBLE);

                iv_laptop1.setImageResource(R.drawable.apple_mackbook_a1342_245);
                tv_laptop1.setText("Apple Mackbook A1342 C2D~2.4Ghz 4GB RAM, 250GB Hard Disck E1 Caption ");
                tv_price1.setText("$245");

                bt_laptop1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        apple_laptop = 1;
                        showSingleImageDialog();
                    }
                });
            }

            if (apple2 == 2){
                relativeLayout2.setVisibility(View.GONE);
            }else {
                relativeLayout2.setVisibility(View.VISIBLE);

                iv_laptop2.setImageResource(R.drawable.apple_mackbook_pro_295);
            tv_laptop2.setText("Apple Mackbook Pro 13 inch 2.66Ghz 4GB RAM, 320 Hard Disck ");
            tv_price2.setText("$295");

            bt_laptop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    apple_laptop = 2;
                    showSingleImageDialog();
                }
            });
            }


            if (apple3 == 3){
                relativeLayout3.setVisibility(View.GONE);
            }else {
                relativeLayout3.setVisibility(View.VISIBLE);

                iv_laptop3.setImageResource(R.drawable.apple_mackbook_unibody_190);
            tv_laptop3.setText("Apple Mackbook Unibody 13 inche 2.26GHz C2D 2GB RAM, 250 Hard Disck ");
            tv_price3.setText("$190");

            bt_laptop3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    apple_laptop = 3;
                    showSingleImageDialog();
                }
            });
            }

            if (relativeLayout1.getVisibility()==View.GONE && relativeLayout2.getVisibility()==View.GONE && relativeLayout3.getVisibility()==View.GONE){
                whenAllLaptopBought.setVisibility(View.VISIBLE);
                whenAllLaptopBought.setText("You Bought All Apple Laptops");
            }


            getSupportActionBar().setTitle("Apple Laptops");
        }
    }//end of apple



        if (lenovo!=null){
            if (lenovo.equals("Lenovo")){

                int lenovo1 = sharedPreferences.getInt("LENOVO1", 0);
                int lenovo2 = sharedPreferences.getInt("LENOVO2", 0);
                int lenovo3 = sharedPreferences.getInt("LENOVO3", 0);

                if (lenovo1==1){
                    relativeLayout1.setVisibility(View.GONE);
                }else {
                    relativeLayout1.setVisibility(View.VISIBLE);

                    iv_laptop1.setImageResource(R.drawable.lenovo_p860d_125);
                    tv_laptop1.setText("Lenovo K500 core 2 Duo P8600 2.4GHz, 2GB RAM, 160GB Hard Disck ");
                    tv_price1.setText("$125");

                    bt_laptop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            lenovo_laptop = 1;
                            showSingleImageDialog();

                        }
                    });
                }


                if (lenovo2==2){
                    relativeLayout2.setVisibility(View.GONE);
                }else {
                    relativeLayout2.setVisibility(View.VISIBLE);

                    iv_laptop2.setImageResource(R.drawable.lenovo_thinkpad_x230i_260);
                tv_laptop2.setText("Lenovo Thinkpad X230i Core i3 3110 2.4GHz, 4GB RAM, 320GB Hard Disck 12 inch ");
                tv_price2.setText("$260");

                bt_laptop2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        lenovo_laptop = 2;
                        showSingleImageDialog();
                    }
                });
                }

                if (lenovo3==3){
                    relativeLayout3.setVisibility(View.GONE);
                }else {
                    relativeLayout3.setVisibility(View.VISIBLE);

                    iv_laptop3.setImageResource(R.drawable.lenovo_thinkpad_x240_corei4_375);
                    tv_laptop3.setText("Lenovo Thinkpad X240 Core i3 4030u 1.9GHz, 4GB RAM, 500GB Hard Disck ");
                    tv_price3.setText("$299");

                    bt_laptop3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            lenovo_laptop = 3;
                            showSingleImageDialog();
                        }
                    });
                }

                if (relativeLayout1.getVisibility()==View.GONE && relativeLayout2.getVisibility()==View.GONE && relativeLayout3.getVisibility()==View.GONE){
                    whenAllLaptopBought.setVisibility(View.VISIBLE);
                    whenAllLaptopBought.setText("You Bought All Lenovo Laptops");
                }



                getSupportActionBar().setTitle("Lenovo Laptops");
            }
        }//end of lenovo

        if (accer!=null){
            if (accer.equals("Accer")){

                int accer1  = sharedPreferences.getInt("ACCER1", 0);
                int accer2  = sharedPreferences.getInt("ACCER2", 0);
                int accer3  = sharedPreferences.getInt("ACCER3", 0);

                if (accer1==1){
                    relativeLayout1.setVisibility(View.GONE);
                }else {
                    relativeLayout1.setVisibility(View.VISIBLE);

                    iv_laptop1.setImageResource(R.drawable.accer_e52_531_pojj_249);
                    tv_laptop1.setText("Accer ES1-531-POJJ 15.6 inch Intel Pentium Quad Core 4GB RAM, 500GB Hard Disck ");
                    tv_price1.setText("$249");

                    bt_laptop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            accer_laptop = 1;
                            showSingleImageDialog();
                        }
                    });
                }

                if (accer2==2){
                    relativeLayout2.setVisibility(View.GONE);
                }else {
                    relativeLayout2.setVisibility(View.VISIBLE);

                    iv_laptop2.setImageResource(R.drawable.accer_notebook_aspire_5741g_177);
                    tv_laptop2.setText("Accer Notebook Aspire 5741G  15.6 inch 4GB RAM, 500GB Hard Disck ");
                    tv_price2.setText("$177");

                    bt_laptop2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            accer_laptop = 2;
                            showSingleImageDialog();
                        }
                    });
                }

                if (accer3==3){
                    relativeLayout3.setVisibility(View.GONE);
                }else {
                    relativeLayout3.setVisibility(View.VISIBLE);

                    iv_laptop3.setImageResource(R.drawable.accer_one_notebook_d270_55);
                    tv_laptop3.setText("Accer Aspire One Notebook D270-1806 Intel Atom 1.6GHz, 1GB RAM, 320GB Hard Disck");
                    tv_price3.setText("$55");

                    bt_laptop3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            accer_laptop = 3;
                            showSingleImageDialog();
                        }
                    });
                }


                if (relativeLayout1.getVisibility()==View.GONE && relativeLayout2.getVisibility()==View.GONE && relativeLayout3.getVisibility()==View.GONE){
                    whenAllLaptopBought.setVisibility(View.VISIBLE);
                    whenAllLaptopBought.setText("You Bought All Accer Laptops");
                }


                getSupportActionBar().setTitle("Accer Laptops");
            }
        }//end of accer


        if (dell!=null){
            if (dell.equals("Dell")){

                int dell1 = sharedPreferences.getInt("DELL1", 0);
                int dell2 = sharedPreferences.getInt("DELL2", 0);
                int dell3 = sharedPreferences.getInt("DELL3", 0);

                if (dell1==1){
                    relativeLayout1.setVisibility(View.GONE);
                }else {
                    relativeLayout1.setVisibility(View.VISIBLE);

                    iv_laptop1.setImageResource(R.drawable.dell_inpiron_1545_178);
                    tv_laptop1.setText("Dell Inpiron 1545 15.6 inche Intel Due @ 1.80GHz 3GB RAM, 250GB Hard Disck ");
                    tv_price1.setText("$178");

                    bt_laptop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dell_laptop = 1;
                            showSingleImageDialog();
                        }
                    });
                }


                if (dell2==2){
                    relativeLayout2.setVisibility(View.GONE);
                }else {
                    relativeLayout2.setVisibility(View.VISIBLE);
                    iv_laptop2.setImageResource(R.drawable.dell_inpiron_7746_320);
                    tv_laptop2.setText("Dell Inpiron 17 inch 7746 700 series intel i7 5th genration 2.4GHz, 8GB RAM, 500GB Hard Disck ");
                    tv_price2.setText("$300");

                    bt_laptop2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dell_laptop = 2;
                            showSingleImageDialog();
                        }
                    });
                }

                if (dell3==3){
                    relativeLayout3.setVisibility(View.GONE);
                }else {
                    relativeLayout3.setVisibility(View.VISIBLE);
                    iv_laptop3.setImageResource(R.drawable.dell_latitude_3330_229);
                    tv_laptop3.setText("Dell Latitude 3330 Dual Core (10074) 1.5GHz, 4GB RAM, 320GB Hard Disck ");
                    tv_price3.setText("$290");

                    bt_laptop3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dell_laptop = 3;
                            showSingleImageDialog();
                        }
                    });
                }


                if (relativeLayout1.getVisibility()==View.GONE && relativeLayout2.getVisibility()==View.GONE && relativeLayout3.getVisibility()==View.GONE){
                    whenAllLaptopBought.setVisibility(View.VISIBLE);
                    whenAllLaptopBought.setText("You Bought All Dell Laptops");
                }


                getSupportActionBar().setTitle("Dell Laptops");
            }
        }//end of dell

        if (hp!=null){
            if (hp.equals("HP")){

                int hp1 = sharedPreferences.getInt("HP1", 0);
                int hp2 = sharedPreferences.getInt("HP2", 0);
                int hp3 = sharedPreferences.getInt("HP3", 0);

                if (hp1==1){
                    relativeLayout1.setVisibility(View.GONE);
                }else {
                    relativeLayout1.setVisibility(View.VISIBLE);

                iv_laptop1.setImageResource(R.drawable.hp_61004_190);
                tv_laptop1.setText("HP Pavilion a40725a 15 inch core i3 61004 8GB RAM, 1TB Hard Disck ");
                tv_price1.setText("$190");

                bt_laptop1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        hp_laptop = 1;
                        showSingleImageDialog();
                    }
                });
                }

                if (hp2==2){
                    relativeLayout2.setVisibility(View.GONE);
                }else {
                    relativeLayout2.setVisibility(View.VISIBLE);

                    iv_laptop2.setImageResource(R.drawable.hp_folio_1029wm_139);
                    tv_laptop2.setText("HP Folio 1029WM 13.3 inch Ultrabook 1.4GHz, 4GB RAM, 128GB Hard Disck ");
                    tv_price2.setText("$139");

                    bt_laptop2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            hp_laptop = 2;
                            showSingleImageDialog();
                        }
                    });
                }


                if (hp3==3){
                    relativeLayout3.setVisibility(View.GONE);
                }else {
                    relativeLayout3.setVisibility(View.VISIBLE);

                    iv_laptop3.setImageResource(R.drawable.hp_i3_6100u_330);
                    tv_laptop3.setText("HP Touchscreen Intel core i3 6100u 2.30GHz 8GB RAM, 1TB Hard Disck Webcam ");
                    tv_price3.setText("$305");

                    bt_laptop3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            hp_laptop = 3;
                            showSingleImageDialog();
                        }
                    });
                }


                if (relativeLayout1.getVisibility()==View.GONE && relativeLayout2.getVisibility()==View.GONE && relativeLayout3.getVisibility()==View.GONE){
                    whenAllLaptopBought.setVisibility(View.VISIBLE);
                    whenAllLaptopBought.setText("You Bought All HP Laptops");
                }


                getSupportActionBar().setTitle("HP Laptops");

            }
        }//end of apple
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IAPCODE )
        {
            android.util.Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
            if (mHelper == null) return;

            // Pass on the activity result to the helper for handling
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {




                super.onActivityResult(requestCode, resultCode, data);
            }
            else {
                android.util.Log.d(TAG, "onActivityResult handled by IABUtil.");
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        inAppBilling_onDestroy();

    }

//starting in app billing code
    private static final char[] payloadSymbols = new char[36];

    static {
        for (int idx = 0; idx < 10; ++idx)
            payloadSymbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            payloadSymbols[idx] = (char) ('a' + idx - 10);
    }


    public void inAppBilling_onDestroy()
    {

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        if (mHelper != null) try {
            mHelper.dispose();
            mHelper.disposeWhenFinished();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }





    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            android.util.Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            android.util.Log.d(TAG, "Purchase successful.");

            //*********************************** Method for mobils *******************
            //***************Samsung Laptop 1*****************
            if (samsung_laptop == 1) {
                if (purchase.getSku().equals(SKU_SAMSUNG_NP35E7C)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of Samsung Laptop 1

            //***************Samsung Laptop 2*****************
            if (samsung_laptop == 2) {
                if (purchase.getSku().equals(SKU_SAMSUNG_TOUCBSCREEN_SLATE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of Samsung Laptop 2

            //***************Samsung Laptop 3*****************
            if (samsung_laptop == 3) {
                if (purchase.getSku().equals(SKU_SAMSUNG_CHROMEBOOK3)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of Samsung Laptop 3

            //***************apple laptop 1*****************
            if (apple_laptop == 1) {
                if (purchase.getSku().equals(SKU_APPLE_MACKBOOK_A1342)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of apple laptop 1

            //***************apple laptop 2*****************
            if (apple_laptop == 2) {
                if (purchase.getSku().equals(SKU_APPLE_MACKBOOK_PRO)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of apple laptop 2

            //***************apple laptop 2*****************
            if (apple_laptop == 3) {
                if (purchase.getSku().equals(SKU_APPLE_MACKBOOK_UNIBODY)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of apple laptop 3

            //***************lenovo laptop 1*****************
            if (lenovo_laptop == 1) {
                if (purchase.getSku().equals(SKU_LENOVO_K500)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of lenovo laptop 1

            //***************lenovo laptop 2*****************
            if (lenovo_laptop == 2) {
                if (purchase.getSku().equals(SKU_LENOVO_THINKPAD_X230I)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of lenovo laptop 2

            //***************lenovo laptop 3*****************
            if (lenovo_laptop == 3) {
                if (purchase.getSku().equals(SKU_LENOVO_THINKPAD_X240)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of lenovo laptop 3

            //***************accer laptop 1*****************
            if (accer_laptop == 1) {
                if (purchase.getSku().equals(SKU_ACCER_ES1531POJJ)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of accer laptop 1

            //***************accer laptop 2*****************
            if (accer_laptop == 2) {

                if (purchase.getSku().equals(SKU_ACCER_ASPIRE5741G)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of accer laptop 2

            //***************accer laptop 3*****************
            if (accer_laptop == 3) {

                if (purchase.getSku().equals(SKU_ACCER_ONENOTEBOOKD270)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of accer laptop 3

            //***************dell laptop 1*****************
            if (dell_laptop == 1) {

                if (purchase.getSku().equals(SKU_DELL_INPIRON1545)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of dell laptop 1

            //***************dell laptop 2*****************
            if (dell_laptop == 2) {

                if (purchase.getSku().equals(SKU_DELL_INPIRON7746)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of dell laptop 2

            //***************dell laptop 3*****************
            if (dell_laptop == 3) {

                if (purchase.getSku().equals(SKU_DELL_LATITUDE3330)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of dell laptop 3

            //***************hp laptop 1*****************
            if (hp_laptop == 1) {

                if (purchase.getSku().equals(SKU_HP_PAVILION_A40725A)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of hp laptop 1

//***************hp laptop 2*****************
            if (hp_laptop == 2) {

                if (purchase.getSku().equals(SKU_HP_FOLIO1029WM)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of hp laptop 2

            //***************hp laptop 3*****************
            if (hp_laptop == 3) {

                if (purchase.getSku().equals(SKU_HP_TOUCHSCREEN_INTEL_6100U)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of hp laptop 3

            //********************************************* Ranglerz Team **********************************

        }
    };



    @Override
    public void receivedBroadcast() {
// Received a broadcast notification that the inventory of items has changed
        android.util.Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            android.util.Log.e(TAG, "Error querying inventory. Another async operation in progress.");
        }
    }




    public class RandomString {

       /*
        * static { for (int idx = 0; idx < 10; ++idx) symbols[idx] = (char)
        * ('0' + idx); for (int idx = 10; idx < 36; ++idx) symbols[idx] =
        * (char) ('a' + idx - 10); }
        */


        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = payloadSymbols[random.nextInt(payloadSymbols.length)];
            return new String(buf);
        }

    }

    public final class SessionIdentifierGenerator {

        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }

    }


    public void keyInitialization(){
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApMKnZbdpTbYXsbrhTr+xUionkKdEZ9fApszm87JJLiEUY472PtD1Az+gxVnzrC28/wKtjWHyHf0cQT01HbBl0GM2btuI6oVQvJW34ubuZE/oLQLod6I1zKg+tAi14VAu0g4J12SU/UW1dSI1gDDyvS3jHJyKLRzvHd3DdSfo0UDiF72rz7P1DxF6+aw5sVlZ0j98mJnxBH/UiowNGEbSBqBvgHQV6iIczeqRDqwXcC+1rMtxyg38IjtDdX7gvFFNi9UGDIDn4NFSFkXCBE/1SjGHZkr870jC82D/XpnwMy/hxWOif6M0aNQPeBzZ49fK1UB0lWJzj/vdRenp0+Kt3QIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        android.util.Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);



    }//end of keyInitialization

    public void startUpLab(){
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        android.util.Log.d(TAG, "Starting setup.");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                android.util.Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                mBroadcastReceiver = new IabBroadcastReceiver(LaptopsList.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                android.util.Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });


    }//end of startup lab

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            android.util.Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            android.util.Log.d(TAG, "Query inventory was successful.");

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately

            //************************************** for Laptops Perchase ********************************

            //************for samsung laptop 1 perchase*********************
            if (samsung_laptop==1){
                Purchase gasPurchase = inventory.getPurchase(SKU_SAMSUNG_NP35E7C);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_SAMSUNG_NP35E7C), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for samsung laptop 1 perchase

            //************for samsung laptop 2 perchase*********************
            if (samsung_laptop==2){
                Purchase gasPurchase = inventory.getPurchase(SKU_SAMSUNG_TOUCBSCREEN_SLATE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_SAMSUNG_TOUCBSCREEN_SLATE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for samsung laptop 2 perchase

            //************for samsung laptop 3 perchase*********************
            if (samsung_laptop==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_SAMSUNG_CHROMEBOOK3);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_SAMSUNG_CHROMEBOOK3), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for samsung laptop 3 perchase

//************for apple laptop 1 perchase*********************
            if (apple_laptop==1){
                Purchase gasPurchase = inventory.getPurchase(SKU_APPLE_MACKBOOK_A1342);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_APPLE_MACKBOOK_A1342), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for apple laptop 1 perchase

            //************for apple laptop 2 perchase*********************
            if (apple_laptop==2){
                Purchase gasPurchase = inventory.getPurchase(SKU_APPLE_MACKBOOK_PRO);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_APPLE_MACKBOOK_PRO), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for apple laptop 2 perchase

            //************for apple laptop 3 perchase*********************
            if (apple_laptop==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_APPLE_MACKBOOK_UNIBODY);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_APPLE_MACKBOOK_UNIBODY), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for apple laptop 3 perchase

            //************for lenovo laptop 1 perchase*********************
            if (lenovo_laptop==1){
                Purchase gasPurchase = inventory.getPurchase(SKU_LENOVO_K500);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_LENOVO_K500), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for lenovo laptop 1 perchase

            //************for lenovo laptop 2 perchase*********************
            if (lenovo_laptop==2){
                Purchase gasPurchase = inventory.getPurchase(SKU_LENOVO_THINKPAD_X230I);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_LENOVO_THINKPAD_X230I), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for lenovo laptop 2 perchase

            //************for lenovo laptop 3 perchase*********************
            if (lenovo_laptop==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_LENOVO_THINKPAD_X240);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_LENOVO_THINKPAD_X240), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for lenovo laptop 3 perchase

            //************for accer laptop 1 perchase*********************
            if (accer_laptop==1){
                Purchase gasPurchase = inventory.getPurchase(SKU_ACCER_ES1531POJJ);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_ACCER_ES1531POJJ), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for accer laptop 1 perchase

//************for accer laptop 2 perchase*********************
            if (accer_laptop==2){
                Purchase gasPurchase = inventory.getPurchase(SKU_ACCER_ASPIRE5741G);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_ACCER_ASPIRE5741G), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for accer laptop 2 perchase

            //************for accer laptop 3 perchase*********************
            if (accer_laptop==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_ACCER_ONENOTEBOOKD270);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_ACCER_ONENOTEBOOKD270), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for accer laptop 3 perchase

            //************for dell laptop 1 perchase*********************
            if (dell_laptop==1){
                Purchase gasPurchase = inventory.getPurchase(SKU_DELL_INPIRON1545);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_DELL_INPIRON1545), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for dell laptop 1 perchase


            //************for dell laptop 2 perchase*********************
            if (dell_laptop==2){
                Purchase gasPurchase = inventory.getPurchase(SKU_DELL_INPIRON7746);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_DELL_INPIRON7746), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for dell laptop 2 perchase


            //************for dell laptop 3 perchase*********************
            if (dell_laptop==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_DELL_LATITUDE3330);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_DELL_LATITUDE3330), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for dell laptop 3 perchase


            //************for hp laptop 1 perchase*********************
            if (hp_laptop==1){
                Purchase gasPurchase = inventory.getPurchase(SKU_HP_PAVILION_A40725A);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_HP_PAVILION_A40725A), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for hp laptop 1 perchase


            //************for hp laptop 2 perchase*********************
            if (hp_laptop==2){
                Purchase gasPurchase = inventory.getPurchase(SKU_HP_FOLIO1029WM);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_HP_FOLIO1029WM), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for hp laptop 2 perchase


            //************for hp laptop 3 perchase*********************
            if (hp_laptop==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_HP_TOUCHSCREEN_INTEL_6100U);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_HP_TOUCHSCREEN_INTEL_6100U), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for hp laptop 3 perchase





            //************************************** end for laptop Perchase ********************************

            setWaitScreen(false);
            android.util.Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            android.util.Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                android.util.Log.d(TAG, "Consumption successful. Provisioning.");
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                alert("You have successfully bought Laptop");

               onSuccessLaptopBought();
                DialogForCustomerInfo();





                //alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
            //updateUi();

            setWaitScreen(false);
            android.util.Log.d(TAG, "End consumption flow.");
        }
    };


    void complain(String message) {
        android.util.Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }


    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.util.Log.d(TAG, "Showing alert dialog: " + message);
        bld.setCancelable(false);
        bld.create().show();
    }
    void setWaitScreen(boolean set) {

       // Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();

		/*findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
		findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);*/
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        return true;
    }

    public void showSingleImageDialog(){

        final Dialog dialog = new Dialog(LaptopsList.this);
        dialog.setContentView(R.layout.single_image_view_dialog);


        ImageView singleImageView = (ImageView) dialog.findViewById(R.id.single_image_view);
        //TextView mobileColor = (TextView)dialog.findViewById(R.id.mobile_color);
        TextView mobilePrice = (TextView)dialog.findViewById(R.id.mobile_price);
        Button buyNow = (Button) dialog.findViewById(R.id.buy_this_mobile);
        Button buyLater = (Button) dialog.findViewById(R.id.buy_later);


        //start if for samsung laptop1
        if (samsung_laptop==1){

            dialog.setTitle("Samsung NP35E7C");
            //mobileColor.setText("Black");
            mobilePrice.setText("Mobile Price: $300 " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.samsung_np350e7c_300);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buySamsungLaptop1();

                }
            });
        }//end of if for samsung laptop2


        //start if for mobile2
        if (samsung_laptop==2){
            dialog.setTitle("Samung Touch Screen Slate");
            //mobileColor.setText("Mobile Color: Black");
            mobilePrice.setText("Mobile Price: 140$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.samsung_toucscreen_pc_table_150);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buySamsungLaptop2();

                }
            });
        }//end of if for samsung laptop2

//start if for samsung laptop3
        if (samsung_laptop==3){
            dialog.setTitle("Samung Chromebook 3");
           // mobileColor.setText("Mobile Color: Black");
            mobilePrice.setText("Mobile Price: 180$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.samung_chrombook3_180);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buySamsungLaptop3();

                }
            });
        }//end of if for samsung laptop3

//start if for apple laptop1
        if (apple_laptop==1){
            dialog.setTitle("Apple Mackbook A1342 D2D");
           // mobileColor.setText("Mobile Color: White");
            mobilePrice.setText("Mobile Price: 245$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.apple_mackbook_a1342_245);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyAppleLaptop1();

                }
            });
        }//end of if for apple laptop1

//start if for apple laptop2
        if (apple_laptop==2){
            dialog.setTitle("Apple Mackbook Pro");
           // mobileColor.setText("Mobile Color: Black/White");
            mobilePrice.setText("Mobile Price: 295$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.apple_mackbook_pro_295);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyAppleLaptop2();

                }
            });
        }//end of if for apple laptop2

//start if for apple laptop3
        if (apple_laptop==3){
            dialog.setTitle("Apple Mackbook Unibody");
            //mobileColor.setText("Mobile Color: Silver");
            mobilePrice.setText("Mobile Price: 190$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.apple_mackbook_unibody_190);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyAppleLaptop3();

                }
            });
        }//end of if for apple laptop3

//start if for lenovo laptop1
        if (lenovo_laptop==1){
            dialog.setTitle("Lenovo K500");
            //mobileColor.setText("Mobile Color: Black");
            mobilePrice.setText("Mobile Price: 125$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.lenovo_p860d_125);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyLenovoLaptop1();

                }
            });
        }//end of if for lenovo laptop1

//start if for lenovo laptop2
        if (lenovo_laptop==2){
            dialog.setTitle("Lenovo Thinkpad x230i");
            //mobileColor.setText("Mobile Color: Black");
            mobilePrice.setText("Mobile Price: 260$"  + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.lenovo_thinkpad_x230i_260);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyLenovoLaptop2();

                }
            });
        }//end of if for lenovo laptop2

//start if for lenovo laptop3
        if (lenovo_laptop==3){
            dialog.setTitle("Lenovo Thinkpad x240");
            //mobileColor.setText("Mobile Color: Black");
            mobilePrice.setText("Mobile Price: 299$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.lenovo_thinkpad_x240_corei4_375);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyLenovoLaptop3();

                }
            });
        }//end of if for lenovo laptop3

//start if for accer laptop1
        if (accer_laptop==1){
            dialog.setTitle("Accer ES1PoJJ");
            //mobileColor.setText("Mobile Color: Gray");
            mobilePrice.setText("Mobile Price: 249$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.accer_e52_531_pojj_249);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyAccerLaptop1();

                }
            });
        }//end of if for accer laptop1

//start if for accer laptop2
        if (accer_laptop==2){
            dialog.setTitle("Accer Notebook Aspire 5741G");
            //mobileColor.setText("Mobile Color: Silver Gray");
            mobilePrice.setText("Mobile Price: 177$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.accer_notebook_aspire_5741g_177);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyAccerLaptop2();

                }
            });
        }//end of if for accer laptop2

//start if for accer laptop3
        if (accer_laptop==3){
            dialog.setTitle("Accer Aspire One Notebook D270-1806");
            //mobileColor.setText("Mobile Color: Silver White");
            mobilePrice.setText("Mobile Price: 55$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.accer_one_notebook_d270_55);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyAccerLaptop3();

                }
            });
        }//end of if for accer laptop3

        //start if for dell laptop1
        if (dell_laptop==1){
            dialog.setTitle("Dell Inpiron 1545");
            //mobileColor.setText("Mobile Color: Silver Black");
            mobilePrice.setText("Mobile Price: 178$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.dell_inpiron_1545_178);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyDellLaptop1();

                }
            });
        }//end of if for dell laptop1

        //start if for dell laptop2
        if (dell_laptop==2){
            dialog.setTitle("Dell 320 Inpiron 7746");
            //mobileColor.setText("Mobile Color: Silver ");
            mobilePrice.setText("Mobile Price: 300$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.dell_inpiron_7746_320);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyDellLaptop2();

                }
            });
        }//end of if for dell laptop2


        //start if for dell laptop3
        if (dell_laptop==3){
            dialog.setTitle("Dell Latitude 3330");
           // mobileColor.setText("Mobile Color: Shining Black");
            mobilePrice.setText("Mobile Price: 290$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.dell_latitude_3330_229);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyDellLaptop3();

                }
            });
        }//end of if for dell laptop3

        //start if for hp laptop1
        if (hp_laptop==1){
            dialog.setTitle("HP Pavilion a40725a");
           // mobileColor.setText("Mobile Color: Shining Silver");
            mobilePrice.setText("Mobile Price: 190$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.hp_61004_190);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyHpaptop1();

                }
            });
        }//end of if for hp laptop1

        //start if for hp laptop2
        if (hp_laptop==2){
            dialog.setTitle("HP Folio 1029WM");
            //mobileColor.setText("Mobile Color: Shining Silver");
           mobilePrice.setText("Mobile Price: 139$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.hp_folio_1029wm_139);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyHpaptop2();

                }
            });
        }//end of if for hp laptop2


        //start if for hp laptop3
        if (hp_laptop==3){
            dialog.setTitle("HP Touchscreen 6100u");
            //mobileColor.setText("Mobile Color: Silver White");
            mobilePrice.setText("Mobile Price: 305" +
                    ".99$ " + BILLING_CHARGES);
            singleImageView.setImageResource(R.drawable.hp_i3_6100u_330);

            buyLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buyHpaptop3();

                }
            });
        }//end of if for hp laptop3




        dialog.setCancelable(true);

        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(LaptopsList.this, R.color.colorBlack));

        dialog.show();

    }

    //************************************ Starts methods for buy laptops x ************************************

    //buy Samsung laptop 1
    public void buySamsungLaptop1(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_SAMSUNG_NP35E7C, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buySamsungLaptop1

    //buy Samsung laptop 2
    public void buySamsungLaptop2(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_SAMSUNG_TOUCBSCREEN_SLATE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buySamsungLaptop2

    //buy Samsung laptop 3
    public void buySamsungLaptop3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_SAMSUNG_CHROMEBOOK3, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buySamsungLaptop3


    //buy Apple laptop 1
    public void buyAppleLaptop1(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_APPLE_MACKBOOK_A1342, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyAppleLaptop1

    //buy Apple laptop 2
    public void buyAppleLaptop2(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_APPLE_MACKBOOK_PRO, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyAppleLaptop2


    //buy Apple laptop 3
    public void buyAppleLaptop3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_APPLE_MACKBOOK_UNIBODY, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyAppleLaptop3


    //buy Lenovo laptop 1
    public void buyLenovoLaptop1(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_LENOVO_K500, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyLenovoLaptop1

    //buy Lenovo laptop 2
    public void buyLenovoLaptop2(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_LENOVO_THINKPAD_X230I, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyLenovoLaptop1

    //buy Lenovo laptop 3
    public void buyLenovoLaptop3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_LENOVO_THINKPAD_X240, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyLenovoLaptop3

    //buy Accer laptop 1
    public void buyAccerLaptop1(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_ACCER_ES1531POJJ, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyAccerLaptop1

    //buy Accer laptop 2
    public void buyAccerLaptop2(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_ACCER_ASPIRE5741G, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyAccerLaptop2

    //buy Accer laptop 3
    public void buyAccerLaptop3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_ACCER_ONENOTEBOOKD270, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyAccerLaptop3

    //buy Dell laptop 1
    public void buyDellLaptop1(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_DELL_INPIRON1545, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyDellLaptop1

    //buy Dell laptop 2
    public void buyDellLaptop2(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_DELL_INPIRON7746, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyDellLaptop2

    //buy Dell laptop 3
    public void buyDellLaptop3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_DELL_LATITUDE3330, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyDellLaptop3


    //buy Hp laptop 1
    public void buyHpaptop1(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_HP_PAVILION_A40725A, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyHpLaptop1

    //buy Hp laptop 2
    public void buyHpaptop2(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_HP_FOLIO1029WM, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyHpLaptop2

    //buy Hp laptop 3
    public void buyHpaptop3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_HP_TOUCHSCREEN_INTEL_6100U, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyHpLaptop3


    //hinding views when bought invertory successfull
    public void onSuccessLaptopBought(){
        sharedPreferences = getSharedPreferences("FORSELLLAPTOP", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //start
        if (samsung_laptop==1){
            //additing values to sharepreferences
            editor.putInt("SAMSUNG1", 1);//int value 1 is for stargoggle

            relativeLayout1.setVisibility(View.GONE);
        }//end
//start
        if (samsung_laptop==2){
            //additing values to sharepreferences
            editor.putInt("SAMSUNG2", 2);//int value 1 is for stargoggle

            relativeLayout2.setVisibility(View.GONE);
        }//end
//start
        if (samsung_laptop==3){
            //additing values to sharepreferences
            editor.putInt("SAMSUNG3", 3);//int value 1 is for stargoggle
            relativeLayout3.setVisibility(View.GONE);
        }//end
//start
        if (apple_laptop==1){
            //additing values to sharepreferences
            editor.putInt("APPLE1", 1);//int value 1 is for stargoggle
            relativeLayout1.setVisibility(View.GONE);
        }//end
//start
        if (apple_laptop==2){
            //additing values to sharepreferences
            editor.putInt("APPLE2", 2);//int value 1 is for stargoggle
            relativeLayout2.setVisibility(View.GONE);
        }//end
//start
        if (apple_laptop==3){
            //additing values to sharepreferences
            editor.putInt("APPLE3", 3);//int value 1 is for stargoggle
            relativeLayout3.setVisibility(View.GONE);
        }//end
//start
        if (lenovo_laptop==1){
            //additing values to sharepreferences
            editor.putInt("LENOVO1", 1);//int value 1 is for stargoggle
            relativeLayout1.setVisibility(View.GONE);
        }//end
//start
        if (lenovo_laptop==2){
            //additing values to sharepreferences
            editor.putInt("LENOVO2", 2);//int value 1 is for stargoggle
            relativeLayout2.setVisibility(View.GONE);
        }//end
//start
        if (lenovo_laptop==3){
            //additing values to sharepreferences
            editor.putInt("LENOVO3", 3);//int value 1 is for stargoggle
            relativeLayout3.setVisibility(View.GONE);
        }//end
//start
        if (accer_laptop==1){
            //additing values to sharepreferences
            editor.putInt("ACCER1", 1);//int value 1 is for stargoggle
            relativeLayout1.setVisibility(View.GONE);
        }//end

        //start
        if (accer_laptop==2){
            //additing values to sharepreferences
            editor.putInt("ACCER2", 2);//int value 1 is for stargoggle
            relativeLayout2.setVisibility(View.GONE);
        }//end

        //start
        if (accer_laptop==3){
            //additing values to sharepreferences
            editor.putInt("ACCER3", 3);//int value 1 is for stargoggle
            relativeLayout3.setVisibility(View.GONE);
        }//end

        //start
        if (dell_laptop==1){
            //additing values to sharepreferences
            editor.putInt("DELL1", 1);//int value 1 is for stargoggle
            relativeLayout1.setVisibility(View.GONE);
        }//end


        //start
        if (dell_laptop==2){
            //additing values to sharepreferences
            editor.putInt("DELL2", 2);//int value 1 is for stargoggle
            relativeLayout2.setVisibility(View.GONE);
        }//end


        //start
        if (dell_laptop==3){
            //additing values to sharepreferences
            editor.putInt("DELL3", 3);//int value 1 is for stargoggle
            relativeLayout3.setVisibility(View.GONE);
        }//end


        //start
        if (hp_laptop==1){
            //additing values to sharepreferences
            editor.putInt("HP1", 1);//int value 1 is for stargoggle
            relativeLayout1.setVisibility(View.GONE);
        }//end


        //start
        if (hp_laptop==2){
            //additing values to sharepreferences
            editor.putInt("HP2", 2);//int value 1 is for stargoggle
            relativeLayout2.setVisibility(View.GONE);
        }//end

        //start
        if (hp_laptop==3){
            //additing values to sharepreferences
            editor.putInt("HP3", 3);//int value 1 is for stargoggle
            relativeLayout3.setVisibility(View.GONE);
        }//end

        editor.commit();

       // ifAllMobileBought();


    }//end of on Successfully mobile bought


    public void loadData(){
        sharedPreferences = getSharedPreferences("FORSELLLAPTOP", 0);

        if(sharedPreferences!=null) {
            int boughtSamsung1 = sharedPreferences.getInt("SAMSUNG1", 0);//defualt values is zero for all
            int boughtSamsung2 = sharedPreferences.getInt("SAMSUNG2", 0);
            int boughtSamsung3 = sharedPreferences.getInt("SAMSUNG3", 0);
            int boughtApple1 = sharedPreferences.getInt("APPLE1", 0);
            int boughtApple2 = sharedPreferences.getInt("APPLE2", 0);
            int boughtApple3 = sharedPreferences.getInt("APPLE3", 0);
            int boughtLenovo1 = sharedPreferences.getInt("LENOVO1", 0);
            int boughtLenovo2 = sharedPreferences.getInt("LENOVO2", 0);
            int boughtLenovo3 = sharedPreferences.getInt("LENOVO3", 0);
            int boughtAccer1 = sharedPreferences.getInt("ACCER1", 0);
            int boughtAccer2 = sharedPreferences.getInt("ACCER2", 0);
            int boughtAccer3 = sharedPreferences.getInt("ACCER3", 0);
            int boughtDell1 = sharedPreferences.getInt("DELL1", 0);
            int boughtDell2 = sharedPreferences.getInt("DELL2", 0);
            int boughtDell3 = sharedPreferences.getInt("DELL3", 0);
            int boughtHP1 = sharedPreferences.getInt("HP1", 0);
            int boughtHP2 = sharedPreferences.getInt("HP2", 0);
            int boughtHP3 = sharedPreferences.getInt("HP3", 0);



            if (boughtSamsung1==1){
                relativeLayout1.setVisibility(View.GONE);
            }
            if (boughtSamsung2==2){
                relativeLayout2.setVisibility(View.GONE);
            }
            if (boughtSamsung3==3){
                relativeLayout3.setVisibility(View.GONE);
            }
            if (boughtApple1==1){
                relativeLayout1.setVisibility(View.GONE);
            }
            if (boughtApple2==2){
                relativeLayout2.setVisibility(View.GONE);
            }
            if (boughtApple3==3){
                relativeLayout3.setVisibility(View.GONE);
            }
            if (boughtLenovo1==1){
                relativeLayout1.setVisibility(View.GONE);
            }
            if (boughtLenovo2==2){
                relativeLayout2.setVisibility(View.GONE);
            }
            if (boughtLenovo3==3){
                relativeLayout3.setVisibility(View.GONE);
            }
            if (boughtAccer1==1){
                relativeLayout1.setVisibility(View.GONE);
            }

            if (boughtAccer2==2){
                relativeLayout2.setVisibility(View.GONE);
            }

            if (boughtAccer3==3){
                relativeLayout3.setVisibility(View.GONE);
            }
///

            if (boughtDell1==1){
                relativeLayout1.setVisibility(View.GONE);
        }

            if (boughtDell2==2){
                relativeLayout2.setVisibility(View.GONE);
            }

            if (boughtDell3==3){
                relativeLayout3.setVisibility(View.GONE);
            }

            if (boughtHP1==1){
                relativeLayout1.setVisibility(View.GONE);
            }

            if (boughtHP2==2){
                relativeLayout2.setVisibility(View.GONE);
            }

            if (boughtHP3==3){
                relativeLayout3.setVisibility(View.GONE);
            }

        }

        //ifAllMobileBought();
    }



    public void DialogForCustomerInfo(){
        final Dialog dialog = new Dialog(LaptopsList.this);
        dialog.setContentView(R.layout.customer_info_layout);
        dialog.setTitle("Delivery Information");
        final EditText customer_mobile_number = (EditText)dialog.findViewById(R.id.ed_customer_phon);
        final EditText customer_address = (EditText)dialog.findViewById(R.id.cutomer_address);
        Button bt_ok = (Button)dialog.findViewById(R.id.dialog_ok);



        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customer_mobile_number.getText().length()==0){
                    Toast.makeText(LaptopsList.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else if(customer_address.getText().length()<9){
                    Toast.makeText(LaptopsList.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else if (customer_address.getText().length()==0){
                    Toast.makeText(LaptopsList.this, "Pleas Enter Delivery Address", Toast.LENGTH_SHORT).show();
                }else {

                    cutomer_phn = customer_mobile_number.getText().toString().trim();
                    cutomer_adrs = customer_address.getText().toString();
                    dialog.dismiss();
                    sendMailOnPurchaseSucess();
                    finish();
                }

            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(LaptopsList.this, R.color.colorBlack));
        dialog.show();
    }

    public void sendMailOnPurchaseSucess(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"hwgt1979@gmail.com"});
        if (samsung_laptop==1) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Samsung NP35E7C Notebook A4-4300M"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (samsung_laptop==2) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Samsung Touchscreen Tablet PC 11 inch"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (samsung_laptop==3) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Samsung Chromebook 3 "
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (apple_laptop==1) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Apple Mackbook A1342 C2d"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (apple_laptop==2) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Apple Mackbook Pro 13 inch"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (apple_laptop==3) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Apple Mackbook Unibody");
        }

        if (lenovo_laptop==1) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Lenovo K500 Core 2 Due"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (lenovo_laptop==2) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Lenovo Thinkpad X230i Core i3"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (lenovo_laptop==3) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Lenovo Thinkpad x240 core i3"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (accer_laptop==1) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Accer ES1-531-POJJ"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (accer_laptop==2) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Accer Notebook Aspire 5741G"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (accer_laptop==3) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Accer Aspire One Notebook D270-1806"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        if (dell_laptop==1) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Dell Inpiron 1545 15.6 inch"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }
        if (dell_laptop==2) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Dell Inpiron 7746 17 inch"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }
        if (dell_laptop==3) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "Dell Latitude 3300 dual core"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }
        if (hp_laptop==1) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "HP Pavilion A40725A 15 inch"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }
        if (hp_laptop==2) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "HP Folio 1029WM Ultrabook"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }
        if (hp_laptop==3) {
            i.putExtra(Intent.EXTRA_SUBJECT, "Order From Buyer");
            i.putExtra(Intent.EXTRA_TEXT, "HP Touchscreen 6100u core i3"
                    + "\n" + "Customer Mobile Number = "
                    + cutomer_phn + "\n" + "Customer Address = " + cutomer_adrs);
        }

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(LaptopsList.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public void getVisibiliteyForLaptops(){
        h_scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = v.getScrollX();
                int y = v.getScrollY();

                h_scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (h_scrollView != null) {
                            View view = h_scrollView.getChildAt(h_scrollView.getChildCount()-1);
                            int startDiff = ((view.getLeft()+h_scrollView.getPaddingLeft())-(h_scrollView.getWidth()+h_scrollView.getScrollX()));
                            int diff = ((view.getRight()+h_scrollView.getPaddingRight())-(h_scrollView.getWidth()+h_scrollView.getScrollX()));

                            // if diff is zero, then the bottom has been reached
                            Log.v("TAG", "SSSSSSSS: "  + diff);
                            if (diff == 0) {
                                rightArrow.setVisibility(View.GONE);
                            }
                            if (diff!=0){
                                rightArrow.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });


                if (x==0){
                    leftArrow.setVisibility(View.GONE);
                }
                if (x!=0){
                    leftArrow.setVisibility(View.VISIBLE);
                }




                Log.v("TAG", "THE VALUES x IS: "  + x + " AND  for y : " + y);
                return false;
            }
        });

    }

}//***************** Shoaib Anwar # 03233008757 ********************
