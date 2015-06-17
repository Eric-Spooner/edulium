package com.at.ac.tuwien.sepm.ss15.edulium.gui;

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
            String buffer = getText();
            super.replaceText(start, end, text);
            if(getValue() < minValue || getValue() > maxValue) {
                setText(buffer);
            }
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
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