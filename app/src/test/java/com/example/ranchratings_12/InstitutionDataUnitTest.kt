package com.example.ranchratings_12

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ranchratings_12.dtos.Institution
import com.example.ranchratings_12.service.InstitutionService
import com.example.ranchratings_12.ui.main.MainViewModel
import io.mockk.every
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
class InstitutionDataUnitTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm:MainViewModel

    var institutionService = mockk<InstitutionService>()

    @Test
    fun searchForTexasRoadhouse_returnsTexasRoadhouse(){
        givenAFeedOfMockedInstitutionDataAreAvailable()
        whenSearchForTexasRoadhouse()
        thenResultContainsTexasRoadhouse()
    }
    private fun whenSearchForTexasRoadhouse() {
        mvm.fetchInstitutions("Texas Roadhouse")
    }
    private fun thenResultContainsTexasRoadhouse() {
        var texasRoadhouseFound = false;
        mvm.institutions.observeForever{
            //here is where we do the observing
            assertNotNull(it)
            assertTrue(it.size>0)
            it.forEach{
                if(it.streetAddress == "375 Rivers Edge Dr, Milford, OH 45150" && it.phoneNumber == "5138319700" && it.name == "Texas Roadhouse"){
                    texasRoadhouseFound = true
                }
            }
        }
        assertTrue(texasRoadhouseFound)
    }
    @Test
    fun givenAFeedOfMockedInstitutionDataAreAvailable(){
        mvm = MainViewModel()
        createMockData()
    }

    private fun createMockData() {
        var allInstitutionsLiveData = MutableLiveData<ArrayList<Institution>>()
        var allInstitutions = ArrayList<Institution>()
        //create and add institutions to our collection
        var texasRoadhouse = Institution( 1,"Texas Roadhouse","375 Rivers Edge Dr, Milford, OH 45150","5138319700" )
        allInstitutions.add(texasRoadhouse)
        var moes = Institution( 2, "Moes","7426 Beechmont Ave, Cincinnati, OH 45255","5132326400" )
        allInstitutions.add(moes)
        var laMexicana = Institution( 3, "La Mexicana","642 Monmouth St, Newport, KY 41071","8592616112" )
        allInstitutions.add(laMexicana)
        allInstitutionsLiveData.postValue(allInstitutions)
        every{institutionService.fetchInstitutions(or("Texas Roadhouse", "5132326400"))} returns allInstitutionsLiveData
        every{institutionService.fetchInstitutions(not(or("Texas Roadhouse", "5132326400")))} returns MutableLiveData<ArrayList<Institution>>()
        mvm.institutionService = institutionService
    }

    @Test
    fun searchForGarbage_returnsNothing(){
        givenAFeedOfMockedInstitutionDataAreAvailable()
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