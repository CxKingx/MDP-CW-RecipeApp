package cn.edu.nottingham.s20125628.recipecw;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

//https://developer.android.com/reference/android/text/InputFilter
//https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
public class DecimalDigitsInputFilter implements InputFilter {
    private final int decimalDigits;
    float input;

    public DecimalDigitsInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {
        try{
             input = Float.parseFloat(dest.toString() + source.toString());
             Log.d("2","input float int  is "+input );
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            input=0;
            Log.d("2","error so input is "+input );

        }

        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {
            Log.d("2"," Check intside" );
            // if many dots
            if (source.equals(".") || source.equals(","))
            {
                //Log.d("2"," not ok" );
                return "";
            }
            // text before dots
            if (dend <= dotPos) {
                //Log.d("2"," ok inside" );
                return null;
            }
            if (len - dotPos > decimalDigits) {
                //Log.d("2"," not ok" );
                return "";
            }


        }
        //Log.d("2"," ok outside" );
        if (input>=0.00&& input <=5.00)
            return null;
        else
            return "";
    }

}
