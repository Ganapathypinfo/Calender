package com.gaiaventure.mycalender.model;

public class CalenderDateModel {
    private String isDateSelected;

    /**
     * @return - returns the previous value
     */
    public String getPrevValue() {
        return prevValue;
    }

    /**
     * @param prevValue - set the previous value
     */
    public void setPrevValue(String prevValue) {
        this.prevValue = prevValue;
    }

    /**
     * @return - returns the current value
     */
    public String getCurrentValue() {
        return currentValue;
    }

    /**
     * @param currentValue - set the current value
     */
    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    private String prevValue;

    private String currentValue;

    /**
     * @return - returns the value if date is selected
     */
    public String getIsDateSelected() {
        return isDateSelected;
    }

    /**
     * @param isDateSelected - sets if the date is selected
     */
    public void setIsDateSelected(String isDateSelected) {
        this.isDateSelected = isDateSelected;
    }
}
