package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 * @author Olle Renard, Johan Johansson
 */

public class OptionsPopup extends Activity {

    Button profileButton, dealsButton, filterButton, groButton;
    public static Activity optionsPopup;
    public static ProgressDialog mProgressDlg;

    /**
     * Sets options_list_popup as layout. Initiates all buttons in options menu.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_list_popup);
        optionsPopup = this;
        dealsButton = (Button) findViewById(R.id.dealsButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        groButton = (Button) findViewById(R.id.grocodeButton);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels-200;
        int height = metrics.heightPixels-200;

        // Gets linearlayout
        LinearLayout layout = (LinearLayout)findViewById(R.id.optionsLayout);
// Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);

        postCreate();
    }

    /**
     * Set onClickListener on profileButton, dealsButton, filterButton and groButton.
     * If one of these buttons are clicked a new activity is created and the specified popup is opened.
     */
    private void postCreate(){
        dealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsPopup.this,SavedDealsPopup.class));
                finish();
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterButton.setClickable(false);
                mProgressDlg = new ProgressDialog(OptionsPopup.this);
                mProgressDlg.setMessage("Getting filter");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                MainActivity.messages.getFilters();
            }
        });
        groButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groButton.setClickable(false);
                mProgressDlg = new ProgressDialog(OptionsPopup.this);
                mProgressDlg.setMessage("Matching with deals");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                MainActivity.messages.getFromGrocode();
            }
        });
    }
}
