package com.example.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ExtensionsKtTest {

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(2022, 9, 24)
    }
    @Test
    fun `Incorrect input pattern empty output string date`() {
        assertEquals("", calendar.time.parseDate("qweewq"))
    }

    @Test
    fun `Correct input pattern correct output string date`() {
        assertEquals("2022-10-24", calendar.time.parseDate("yyyy-MM-dd"))
    }

    @Test
    fun `Incorrect input pattern null date output`() {
        assertEquals(
            null,
            "2022-10-24".toDate("yyyy.MM.dd")
        )
    }

    @Test
    fun `Incorrect input string date null date output`() {
        assertEquals(
            null,
            "2022.10.24".toDate("yyyy-MM-dd")
        )
    }

    @Test
    fun `Correct input string date correct date output`() {
        val actualCalendar = Calendar.getInstance()
        actualCalendar.time = "2022-10-24".toDate("yyyy-MM-dd")!!
        assertEquals(
            calendar.get(Calendar.YEAR),
            actualCalendar.get(Calendar.YEAR)
        )
        assertEquals(
            calendar.get(Calendar.MONTH),
            actualCalendar.get(Calendar.MONTH)
        )
        assertEquals(
            calendar.get(Calendar.DAY_OF_MONTH),
            actualCalendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}