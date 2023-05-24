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

    /**
     * res/raw/case_study1.pdf
     */

    /**
     * Case Study 1
     *
     * Question 1
     * Info:
     *      - cannot select preferred time
     *      - only queue
     *      - 1 doctor 3min/consultation
     *      - 5 patients queue, not started yet
     *      - total waiting times = 3 * 5 = 15min
     *      - 6th patient queues (John)
     *
     * Question:
     *      - When count down time should start?
     *        a. The moment John queues
     *        b. The moment doctor starts seeing 1th patient?
     *
     * Answer:
     *      b. The moment doctor starts seeing 1th patient?
     * Explanation:
     *      - doctor has consultation 3 minutes means that when he starts seeing patient until
     *      consultation finished (range 0 - 3min)
     *      - before doctor starts seeing patient, the time will be undefined (can be infinite
     *      number of time)
     *
     * Question 1
     *
     */

    /**
     *
     *
     */


    fun case1() {

    }
}