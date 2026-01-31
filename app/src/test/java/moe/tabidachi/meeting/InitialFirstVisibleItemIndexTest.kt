package moe.tabidachi.meeting

import org.junit.Assert.assertEquals
import org.junit.Test

class InitialFirstVisibleItemIndexTest {
    @Test
    fun test() {
        val initialValue = 10
        val numeralSystem = 60
        val initialFirstVisibleItemIndex =
            Int.MAX_VALUE / 2 + ((initialValue - (Int.MAX_VALUE / 2) % numeralSystem + numeralSystem) % numeralSystem)
        println(initialFirstVisibleItemIndex)
        println(initialFirstVisibleItemIndex % numeralSystem)
        assertEquals(initialFirstVisibleItemIndex % numeralSystem, initialValue)
    }
}