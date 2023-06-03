package com.fwhyn.myapplication.util.other.queue

class GetWaitingTime {

    fun getWaitingTimeOrNull(doctors: List<Doctor>, patient: Int): Int? {
        // make sure that do not edit original source of doctor
        val copyOfDoctors = getCopyOfDoctors(doctors)

        var availableDoctor: Doctor? = null
        if (copyOfDoctors.isNotEmpty()) {
            for (i in 1..patient) {
                availableDoctor = getAvailableDoctor(copyOfDoctors)

                incrementDoctorTimeStep(availableDoctor)
            }
        }

        return availableDoctor?.timeStep
    }

    private fun getCopyOfDoctors(doctorsToCopy: List<Doctor>): List<Doctor> {
        val copiedArray = ArrayList<Doctor>()
        doctorsToCopy.map {
            copiedArray.add(it.copy())
        }

        return copiedArray
    }

    private fun getAvailableDoctor(doctors: List<Doctor>): Doctor {
        val timeStep = getMinTimeStep(doctors)
        val doctorsWithMinTimeStep = getDoctorsWithMinTimeStep(timeStep, doctors)

        return getDoctorWithMinConsultationTime(doctorsWithMinTimeStep)
    }

    private fun getMinTimeStep(doctors: List<Doctor>): Int {
        val doctor = getDoctorWithMinTimeStepIncrement(doctors)

        return doctor.timeStep
    }

    private fun getDoctorWithMinTimeStepIncrement(doctors: List<Doctor>): Doctor {
        return doctors.minBy { it.timeStep + it.consultationTimeMinute }
    }

    private fun getDoctorsWithMinTimeStep(timeStep: Int, doctors: List<Doctor>): List<Doctor> {
        val doctorList = ArrayList<Doctor>()
        doctors.map {
            if (timeStep == it.timeStep) {
                doctorList.add(it)
            }
        }

        return doctorList
    }

    private fun getDoctorWithMinConsultationTime(doctors: List<Doctor>): Doctor {
        return doctors.minBy { it.consultationTimeMinute }
    }

    private fun incrementDoctorTimeStep(doctor: Doctor) {
        doctor.timeStep += doctor.consultationTimeMinute
    }
}