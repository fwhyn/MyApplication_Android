package com.fwhyn.myapplication.rack

import kotlin.math.abs

class CheckRack {
    // check available rack
    // check weight category
    // select available floor
    //      compare volume
    //      fill first floor
    //          order from the big area first (must order dsc the area) --------
    //          align the smallest w or l difference between w of rack --------
    //              get smallest difference
    //              fill to each model
    //          find maximum bottom length if total length > rack length
    //          sum stackable printer first check until end of array

    fun sortPrintersBottomArea(printers: List<PrinterBase>): List<PrinterBase> {
        return printers.sortedByDescending { it.bottomArea }
    }

    /**
     * return smallest difference
     */
    fun compareParams(a: Int, b: Int, c: Int): Int {
        return if (abs(c - a) < abs(c - b)) {
            a
        } else {
            b
        }
    }

    fun rackAlignment(printerBase: PrinterBase, rack: Rack): PrinterBase {
        with(printerBase) {
            if (compareParams(w, l, rack.w) == w) {
                rackAlign = w
                rackLengthFill = l
            } else {
                rackAlign = l
                rackLengthFill = w
            }
        }

        return printerBase
    }

    fun rackAlignments(printers: List<PrinterBase>, rack: Rack): List<PrinterBase> {
        for (printerBase in printers) {
            rackAlignment(printerBase, rack)
        }

        return printers
    }
}