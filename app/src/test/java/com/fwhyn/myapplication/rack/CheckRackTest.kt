package com.fwhyn.myapplication.rack

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class CheckRackTest {
    private lateinit var checkRack: CheckRack
    private lateinit var rack: Rack

    @Before
    fun setUp() {
        checkRack = CheckRack()
        rack = Rack(1, 40, 200, 65, 4)
    }

    @Test
    fun sortPrinterArea_inputListPrinterEqualsOutputListPrinter() {
        val printerList: List<PrinterBase> = mock()
        val printerListOut = checkRack.sortPrintersBottomArea(printerList)

        val inSize = printerList.size
        val outSize = printerListOut.size

        assertEquals(inSize, outSize)
    }

    @Test
    fun sortPrinterArea_inUnorderedOutOrderedDsc() {
        val printerListIn = listOf(
            PrinterBase("Test1", 40, 60, 30, 7, true), // area: 16
            PrinterBase("Test3", 30, 30, 30, 7, true), // area: 9
            PrinterBase("Test2", 30, 40, 30, 7, true), // area: 12
        )

        val printerListOut = listOf(
            PrinterBase("Test1", 40, 60, 30, 7, true), // area: 16
            PrinterBase("Test2", 30, 40, 30, 7, true), // area: 12
            PrinterBase("Test3", 30, 30, 30, 7, true), // area: 9
        )

        assertEquals(printerListOut, checkRack.sortPrintersBottomArea(printerListIn))
    }

    @Test
    fun compareParams_mustReturnTheSmallestDifference() {
        val a = 12 // |10 - 12| = 2
        val b = 14 // |10 - 14| = 4
        val d = 9 // |10 - 9| = 1

        val c = 10

        assertEquals(a, checkRack.compareParams(a, b, c))
        assertEquals(d, checkRack.compareParams(a, d, c))
    }

    @Test
    fun rackAlignment_fillToModel() {
        val printerBaseIn = PrinterBase("Test1", 35, 39, 30, 7, true) // rackAlign: 40
        val printerBaseOut = printerBaseIn.copy().apply {
            rackAlign = this.l
            rackLengthFill = this.w
        }
        val printerResult = checkRack.rackAlignment(printerBaseIn, rack)

        assertEquals(printerBaseOut, printerResult)
    }

    @Test
    fun rackAlignments_fillToEachModel() {
        // TODO(masih salah testnya)
        val printerListIn = listOf(
            PrinterBase("Test1", 25, 39, 30, 7, true), // rackAlign: 40
            PrinterBase("Test3", 40, 43, 30, 7, true), // area: 39
            PrinterBase("Test2", 38, 39, 30, 7, true), // area: 39
        )

        val printerListOut = ArrayList<PrinterBase>(printerListIn.map { it.copy() })
        with(printerListOut) {
            this[0].apply {
                rackAlign = l
                rackLengthFill = w
            }

            this[1].apply {
                rackAlign = w
                rackLengthFill = l
            }

            this[2].apply {
                rackAlign = l
                rackLengthFill = w
            }
        }

        val printerResult = checkRack.rackAlignments(printerListIn, rack)

        assertEquals(printerListOut, printerResult)
    }

    @Test
    fun fillBottomArea_findTheOptimum() {
        // 5 + 3 + 2
    }
}