package com.at.ac.tuwien.sepm.ss15.edulium.gui.util;

import javafx.scene.control.TextField;

/**
 * Created by phili on 6/17/15.
 */
public class NumericTextField extends TextField
{

    private double minValue = Double.MIN_VALUE;
    private double maxValue = Double.MAX_VALUE;

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            String newText = replaceBetween(start, end, text);
            if(newText.isEmpty()) {
                setText("0");
            } else if (Double.valueOf(newText) >= minValue && Double.valueOf(newText) <= maxValue) {
                super.replaceText(start, end, text);
            }
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            String newText = replaceCurrentSelection(text);
            if(newText.isEmpty()) {
                setText("0");
            } else if (Double.valueOf(newText) >= minValue && Double.valueOf(newText) <= maxValue) {
                super.replaceSelection(text);
            }
        }
    }

    @Override
    public void clear() {
        setText("0");
    }

    private String replaceBetween(int start, int end, String text) {
        String originalText = getText();
        String preString = originalText.substring(0, start);
        String postString = originalText.substring(end, originalText.length());

        return preString + text + postString;
    }

    private String replaceCurrentSelection(String text) {
        int start = getSelection().getStart();
        int end = getSelection().getEnd();

        String originalText = getText();
        String preString = originalText.substring(0, start);
        String postString = originalText.substring(end, originalText.length());

        return preString + text + postString;
    }

    private boolean validate(String text)
    {
        return text.isEmpty() || text.matches("[0-9]");
    }

    public double getValue() {
        if(isEmpty()) {
            return 0.0;
        }
        return Double.valueOf(getText());
    }

    public void setMinMax(double min, double max) {
        minValue = min;
        maxValue = max;
    }

    public boolean isEmpty() {
        return getText().isEmpty();
    }
}