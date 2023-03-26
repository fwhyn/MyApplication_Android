package com.fwhyn.myapplication.util.other

import java.io.FileInputStream

class NoRelation {

//    fun retrieveSection(sectionName: String?): Boolean {
//    // dummy return until we have a real implementation
//        return true
//    }

    fun retrieveSection(sectionName: String): Boolean {
        try {
            val stream = FileInputStream(sectionName)
        } catch (e: Exception) {
            throw StorageException("retrieval error", e)
        }
        return true
    }
}