package com.fwhyn.myapplication.util.other.dice_cup;

public class Cup {

    private boolean diceExist = false;

    private int cupId;

    public Cup(int cupId) {
        this.cupId = cupId;
    }

    public Cup(int cupId, boolean diceExist) {
        this.cupId = cupId;
        this.diceExist = diceExist;
    }

    public boolean isDiceExist() {
        return diceExist;
    }

    public void setDiceExist(boolean diceExist) {
        this.diceExist = diceExist;
    }

    public int getCupId() {
        return cupId;
    }
}
