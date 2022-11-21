package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Hole
import org.junit.Assert.*

import org.junit.Test

class StoredStatisticsViewModelTest {

    @Test
    fun getRatingForRoundCourseObject() {
        val course = Course(
            0,
            "TestBane",
            listOf<Hole>(),
            null,
            "Testfold og Telemark",
            "TestBy",
            null,
            59.385593F,
            10.402774F,
            2,
            39,
            790.48,
            39.76,
            1000.0,
            28.12,
            20172,
            12,
            842
        )

        val result = 39
        val parRating = StoredStatisticsViewModel().getRatingForRound(result, course)
        val expectedParRating = 804
        assertEquals(expectedParRating, parRating)
    }

    @Test
    fun getRatingForRoundParameters() {
        val parRating = StoredStatisticsViewModel().getRatingForRound(
            39,
            790.48,
            1000.0,
            39.76,
            28.12,
        )
        val expectedParRating = 804
        assertEquals(expectedParRating, parRating)
    }
}