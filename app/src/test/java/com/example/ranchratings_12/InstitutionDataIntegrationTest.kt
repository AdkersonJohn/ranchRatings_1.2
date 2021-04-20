package com.example.ranchratings_12

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ranchratings_12.service.InstitutionService
import com.example.ranchratings_12.ui.main.MainViewModel
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class InstitutionDataIntegrationTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm:MainViewModel

    var institutionService = mockk<InstitutionService>()

    @Test
    fun searchForTexasRoadhouse_returnsTexasRoadhouse(){
        givenAFeedOfInstitutionDataAreAvailable()
        whenSearchForTexasRoadhouse()
        thenResultContainsTexasRoadhouse()
    }
    private fun whenSearchForTexasRoadhouse() {
        mvm.fetchInstitutions("Texas Roadhouse")
    }
    private fun thenResultContainsTexasRoadhouse() {
        var texasRoadhouseFound = false
        mvm.institutions.observeForever{
            //here is where we do the observing
            assertNotNull(it)
            assertTrue(it.size>0)
            it.forEach{
                if(it.streetAddress == "375 Rivers Edge Dr, Milford, OH 45150" && it.phoneNumber == "5138319700" && it.name == "Texas Roadhouse"){
                    texasRoadhouseFound = true
                }
            }
            assertTrue(texasRoadhouseFound)
        }

    }
    @Test
    fun givenAFeedOfInstitutionDataAreAvailable(){
        mvm = MainViewModel()
    }


    @Test
    fun searchForGarbage_returnsNothing(){
        givenAFeedOfInstitutionDataAreAvailable()
        whenISearchForGarbage()
        thenIGetZeroResults()
    }

    private fun whenISearchForGarbage() {
        mvm.fetchInstitutions("bnxuasefasinvgv")

    }

    private fun thenIGetZeroResults() {
        mvm.institutions.observeForever{
            assertEquals(0, it.size)
        }
    }
}