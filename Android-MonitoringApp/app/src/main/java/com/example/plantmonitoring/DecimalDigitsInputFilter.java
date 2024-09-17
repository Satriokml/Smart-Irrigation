package com.example.plantmonitoring;
import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {
    private final int decimalDigits;

    public DecimalDigitsInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder newText = new StringBuilder(dest).replace(dstart, dend, source.subSequence(start, end).toString());
        if (newText.toString().contains(".")) {
            if (newText.substring(newText.indexOf(".") + 1).length() > decimalDigits) {
                return "";
            }
        } else {
            if (newText.length() > decimalDigits) {
                return "";
            }
        }
        return null;
    }
}

