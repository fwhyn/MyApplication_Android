package com.fwhyn.myapplication.util.rack

class Util {
    companion object {
        val printers = arrayOf(
            PrinterBase("Test1", 40, 60, 30, 7, true),
            PrinterBase("Test2", 35, 40, 25, 7, false),
            PrinterBase("Test3", 30, 35, 22, 7, true),
            PrinterBase("Test4", 33, 37, 24, 7, false),
            PrinterBase("Test5", 37, 45, 30, 7, true),
            PrinterBase("Test6", 40, 40, 25, 7, true),
            PrinterBase("Test7", 30, 40, 30, 7, false),
            PrinterBase("Test8", 45, 62, 35, 7, true),
        )

        val racks = arrayOf(
            Rack(1, 40, 200, 60, 4),
            Rack(2, 40, 200, 60, 5),
            Rack(3, 40, 200, 60, 4),
            Rack(4, 40, 200, 60, 3),
            Rack(5, 40, 200, 60, 4),
            Rack(6, 40, 200, 60, 4)
        )
    }
}