package moe.tabidachi.meeting

import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import moe.tabidachi.meeting.ui.preview.userInfoList
import org.junit.Test

class FuzzySearchTest {
    @Test
    fun testFuzzySearch() {
        val results = FuzzySearch.extractSorted("Mi", userInfoList.map { it.username })
        results.forEach {
            println(it)
        }
    }
}