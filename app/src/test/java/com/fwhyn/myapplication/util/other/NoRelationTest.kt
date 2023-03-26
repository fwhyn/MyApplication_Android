package com.fwhyn.myapplication.util.other

import org.junit.Test

class NoRelationTest {

    @Test(expected = StorageException::class)
    fun retrieveSectionShouldThrowOnInvalidFileName() {
        NoRelation().retrieveSection("invalid - file")
    }
}