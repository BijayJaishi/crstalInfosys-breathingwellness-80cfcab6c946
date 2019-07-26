package com.infosys.crystal.breathingwellness.progress_bar;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

import com.infosys.crystal.breathingwellness.R;

/**
 * Created by Bijay on 7/1/2019.
 */

public class Play_progressbar {

    Context context;
    ProgressDialog pDialog;
    String pleaseWait = "loading ....";

    public Play_progressbar(Context context) {
        this.context = context;
    }


    ////// show progress dialog /////
    public void showProgress(){

        pDialog = ProgressDialog.show(new ContextThemeWrapper(context, R.style.NewDialog),"", pleaseWait,true);

        pDialog.show();

    }

    public void hideProgress(){
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}
