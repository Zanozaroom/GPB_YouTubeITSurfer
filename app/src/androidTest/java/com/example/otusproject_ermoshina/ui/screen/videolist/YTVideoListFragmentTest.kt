package com.example.otusproject_ermoshina.ui.screen.videolist

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.domain.helpers.*
import com.example.otusproject_ermoshina.espresso.HelperRecyclerView
import com.example.otusproject_ermoshina.creatordata.CreatorVideoListData
import com.example.otusproject_ermoshina.launchFragment
import com.example.otusproject_ermoshina.rules.BaseTest
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListFragment
import com.example.otusproject_ermoshina.ui.screen.videolist.AdapterVideoLists.VideoListViewHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton


@UninstallModules(NavigatorModule::class, Helpers::class)
@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class YTVideoListFragmentTest : BaseTest() {
    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helper: VideoListLoad

    private lateinit var scenario: AutoCloseable
    private val resultData = CreatorVideoListData.createYTVideoListPaging()

    @After
    fun close() {
        scenario.close()
    }


    @Test
    fun launchWhenHasErrorNetworkResult() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.ErrorLoadingViewModel

        scenario = launchFragment {
            YTPlayListFragment.newInstance("idVideo3")
        }

        Espresso.onView(ViewMatchers.withId(R.id.buttonErrorLoad)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.messageErrorLoad)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun launchWhenHasLoadingResult() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.LoadingViewModel

        scenario = launchFragment {
            YTVideoListFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun launchWhenHasSuccessResult() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment {
            YTVideoListFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkedOnRecyclerRightThenClickOnButtonOpenChannel() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)
        coEvery {
            navigator.startYTPlayListFragmentMainStack(any())
        } just runs

        scenario = launchFragment {
            YTVideoListFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.titleChannel)
                )
            )


        verify {
            navigator.startYTPlayListFragmentMainStack("channelId")
        }
    }

    @Test
    fun checkedOnRecyclerRightThenClickOnOpenVideo() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        coEvery {
            navigator.startPageOfVideoFragmentMainStack(any())
        } just runs

        scenario = launchFragment {
            YTVideoListFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.iconGoWatch)
                )
            )

        verify {
            navigator.startPageOfVideoFragmentMainStack("idVideo3")
        }
    }
    @Test
    fun checkedOnRecyclerRightThenClickOnImageToOpenVideo() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        coEvery {
            navigator.startPageOfVideoFragmentMainStack(any())
        } just runs

        scenario = launchFragment {
            YTVideoListFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.imageVideo)
                )
            )

        verify {
            navigator.startPageOfVideoFragmentMainStack("idVideo3")
        }
    }

    @Test
    fun checkedOnRecyclerRightThenClickAddVideoToFavorite() {
        coEvery {
            helper.getVideoList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment {
            YTVideoListFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.actionPositiveButton)
                )
            )
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class Faker {
        @Provides
        @Singleton
        fun provideNavigator(): ContractNavigator {
            return mockk(relaxed = true)
        }
    }
}



