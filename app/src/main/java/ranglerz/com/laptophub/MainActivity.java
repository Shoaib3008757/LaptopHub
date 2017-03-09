package ranglerz.com.laptophub;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rl_samsung, rl_apple, rl_lenovo, rl_accer, rl_dell, rl_hp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        tvSamsunClickHandler();
        tvAppleClickHandler();
        tvLenovoClickHandler();
        tvAccerClickHandler();
        tvDellClickHandler();
        tvHPClickHandler();
        onTouchandRlease();
    }
    public void init(){


        rl_samsung = (RelativeLayout) findViewById(R.id.rl_samsung);
        rl_apple = (RelativeLayout) findViewById(R.id.rl_apple);
        rl_lenovo = (RelativeLayout) findViewById(R.id.rl_lenovo);
        rl_accer = (RelativeLayout) findViewById(R.id.rl_accer);
        rl_dell = (RelativeLayout) findViewById(R.id.rl_dell);
        rl_hp= (RelativeLayout) findViewById(R.id.rl_hp);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MainActivity.this ,R.color.colorBlack)));
    }

    public void onTouchandRlease(){
        rl_samsung.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rl_samsung.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ontouch));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    rl_samsung.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.corner));
                }
                return false;
            }
        });

        rl_apple.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rl_apple.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ontouch));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    rl_apple.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.corner));
                }
                return false;
            }
        });

        rl_lenovo.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rl_lenovo.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ontouch));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    rl_lenovo.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.corner));
                }
                return false;
            }
        });

        rl_accer.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rl_accer.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ontouch));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    rl_accer.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.corner));
                }
                return false;
            }
        });

        rl_dell.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rl_dell.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ontouch));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    rl_dell.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.corner));
                }
                return false;
            }
        });

        rl_hp.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rl_hp.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ontouch));
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    rl_hp.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.corner));
                }
                return false;
            }
        });
    }//end of onTocu

    public void tvSamsunClickHandler(){

        rl_samsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent samsungIntent = new Intent(MainActivity.this, LaptopsList.class);
                samsungIntent.putExtra("Samsung", "Samsung");
                startActivity(samsungIntent);

            }
        });
    }//end of tv_samsung halder

    public void tvAppleClickHandler(){
        rl_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent samsungIntent = new Intent(MainActivity.this, LaptopsList.class);
                samsungIntent.putExtra("Apple", "Apple");
                startActivity(samsungIntent);
            }
        });
    }//end of tvAppleClickHandler

    public void tvLenovoClickHandler(){
        rl_lenovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent samsungIntent = new Intent(MainActivity.this, LaptopsList.class);
                samsungIntent.putExtra("Lenovo", "Lenovo");
                startActivity(samsungIntent);
            }
        });
    }//end of tvLenovoClickHandler

    public void tvAccerClickHandler(){
        rl_accer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent samsungIntent = new Intent(MainActivity.this, LaptopsList.class);
                samsungIntent.putExtra("Accer", "Accer");
                startActivity(samsungIntent);
            }
        });
    }//end of tvAccerClickHandler

    public void tvDellClickHandler(){
        rl_dell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent samsungIntent = new Intent(MainActivity.this, LaptopsList.class);
                samsungIntent.putExtra("Dell", "Dell");
                startActivity(samsungIntent);
            }
        });
    }//end of tvDellClickHandler

    public void tvHPClickHandler(){
        rl_hp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent samsungIntent = new Intent(MainActivity.this, LaptopsList.class);
                samsungIntent.putExtra("HP", "HP");
                startActivity(samsungIntent);
            }
        });
    }//end of tvHPClickHandler
}//***************** Shoaib Anwar # 03233008757 ********************
