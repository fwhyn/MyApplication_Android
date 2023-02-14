package com.fwhyn.bluetooth.ble

interface BtMgr {
    fun ableToScan()
    fun unableToScan(reason: Reason)

    // --------------------------------
    enum class Reason {
        NEED_RATIONALE, NO_PERMISSION, BT_OFF, NOT_SUPPORTED
    }
}

//https://proyekruspitaa.wordpress.com/2013/06/28/cara-menghitung-z-score-2/
//67,6	69,9	72,2	74,5	76,9	79,2	81,5
//2,3     2,3     2,3      2,4      2,3      2,3
//
//79,3	82,5	85,6	88,8	92	95,2	98,3
//3,2     3,1      3,2     3,2     3,2      3,1
//
//--------------------
//Age (ageMonth: String)
//Gender (gender: String)
//Measured Value (measured: Double)
//Media (median: Double)
//Standard deviation (deviation: Double) = upperValue - lowerValue
//Z-Score (zScore: Double)
//Height data (heightData: HeightData)
//
//zScore = (measured - median)/deviation
//
//Get heightData -> get array data from gender key -> get data by age key
//Get measured -> from height meter
//Get median -> heightData -> get median
//Get deviation
//-> heightData convert to array height
//-> insert measured to array height
//-> get index of measured from array height
//-> lowerValue
//-> if index measured > 0 & -> array height[index measured - 1] else array height[index measured + 1]
//-> if index measured < array height.size - 1 -> array height[index measured + 1] else height[index measured - 1]
