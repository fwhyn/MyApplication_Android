package com.fwhyn.myapplication.util.other

import com.fwhyn.myapplication.util.other.queue.Doctor
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
     * Info:
     *    - Cannot select preferred time, only queue
     *    - 1 doctor 3min consultation
     *    - 5 patients in queue, not started yet
     *    - total waiting times = 3 * 5 = 15min
     *    - 6th patient queues (John)
     *
     * Question 1:
     *    - When count down time should start?
     *      a. The moment John queues
     *      b. The moment doctor starts seeing 1th patient?
     *
     * Answer:
     *    b. The moment doctor starts seeing 1th patient
     *    Explanation:
     *    - Doctor has consultation 3 minutes means that when he starts seeing patient until
     *    consultation finished (range 0 - 3min)
     *    - Before doctor starts seeing patient, the time will be undefined (can be infinite
     *    number of time)
     *
     * Question 2:
     *    - Doctor currently seeing one of 5 patients (Peter)
     *    - What time estimation when Peter has gone to the consultation Room for
     *      a. 2 minutes
     *      b. 5 minutes
     *
     * Answer:
     *    a. John has to wait for 13 minutes
     *    Explanation:
     *    queue_time = total_time - started_time
     *    15 - 2 = 13minutes
     *
     *    b. John has to wait 10 minutes
     *    Explanation:
     *    15 - 5 = 10minutes
     */

    /**
     * Case Study 2
     * Info:
     *    - 2 doctors
     *      A 3 min consultation
     *      b 4 min consultation
     *    - 10 patients in queue, not started yet
     *    - 11th patient queues (John)
     * Question1:
     *    - patients have no specific preferences for the doctors they want to consult, what will
     *    John’s estimated waiting time?
     * Answer:
     *    20 min
     *    Explanation:
     *    - let's declare doctor's available time
     *      - each doctor has initial time (consultation in progress or not ready yet)
     *      - in this case there is no initial time, so we can set it as 0
     *      so the available time each doctor is:
     *      available_time = consultation_time*patients - initial_time
     *
     *    - let's declare time step
     *      time step is the total fragment of available time got from sum of doctor's consultation
     *      time multiplication subtract it's initial time (in this case is 0):
     *      time_step = count of (doctor_time_step*patient - initial_time)
     *      example:
     *      doctor A: 0, 3, 6, 9, ...
     *      doctor B: 0, 4, 8, 12, ...
     *      time step: 0, 0, 3, 4, 6, 9, ...
     *
     *    - let's break consultation time
     *      - we need to check current smallest available time to increment doctor time step
     *      - and biggest available time to hold increment of doctor time step
     *
     *    - operation
     *      init time_step as 0
     *      get min available time
     *          - doctor A = 0
     *          - doctor B = 0
     *      min_available_time = 0
     *      get max available time
     *          - doctor A = 0
     *          - doctor A = 0
     *      max_available_time = 0
     *
     *      see patient 1
     */


    fun getConsultationTime(doctors: List<Doctor>, patient: Int): Int? {
        // make sure that do not edit original source of doctor
        val copyOfDoctors = getCopyOfDoctors(doctors)

        var availableDoctor: Doctor? = null
        for (i in 1..patient) {
            availableDoctor = getDoctorWithMinTimeStepIncrement(copyOfDoctors)
            val minTimeStep: Int = availableDoctor.timeStep

            val duplicatedDoctorTimeStepList =
                getDuplicatedDoctorTimeStep(minTimeStep, copyOfDoctors)
            if (duplicatedDoctorTimeStepList.size > 1) {
                availableDoctor = getDoctorWithMinConsultationTime(duplicatedDoctorTimeStepList)
            }

            incrementDoctorTimeStep(availableDoctor)
        }

        val curentTimeStep: Int? = availableDoctor?.timeStep

        return curentTimeStep
    }

    private fun getCopyOfDoctors(doctorsToCopy: List<Doctor>): List<Doctor> {
        val copiedArray = ArrayList<Doctor>()
        doctorsToCopy.map {
            copiedArray.add(it.copy())
        }

        return copiedArray
    }

    private fun getDuplicatedDoctorTimeStep(timeStep: Int, doctors: List<Doctor>): List<Doctor> {
        val duplicatedList = ArrayList<Doctor>()

        doctors.map {
            if (timeStep == it.timeStep) {
                duplicatedList.add(it)
            }
        }

        return duplicatedList
    }

    private fun getDoctorWithMinConsultationTime(doctors: List<Doctor>): Doctor {
        return doctors.minBy { it.consultationTimeMinute }
    }

    private fun getDoctorWithMinTimeStepIncrement(doctors: List<Doctor>): Doctor {
        return doctors.minBy { it.timeStep + it.consultationTimeMinute }
    }

    private fun incrementDoctorTimeStep(doctor: Doctor) {
        doctor.timeStep += doctor.consultationTimeMinute
    }
}