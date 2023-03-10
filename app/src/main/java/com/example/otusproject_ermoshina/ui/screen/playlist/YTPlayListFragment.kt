package com.example.otusproject_ermoshina.ui.screen.playlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.databinding.FragmentPlaylistBinding
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.ui.base.BasePLFragment
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.utill.DecoratorParentGrid
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface ActionYTPlayList {
    fun openPlayList(idPlayList: String)
    fun addToFavoritePL(list: YTPlayList)
}

@AndroidEntryPoint
class YTPlayListFragment : BasePLFragment(), ActionYTPlayList {
    @Inject
    lateinit var navigator: ContractNavigator

    private val viewModel: YTPlayListsVM by viewModels()
    override lateinit var binding: FragmentPlaylistBinding
    private lateinit var ytChannelAndPlayList: YTPlayListPaging
    private var isLoading = true
    private val adapterVideoIdList: AdapterPlayList by lazy {
        AdapterPlayList(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator.setActionBarNavigateBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        viewModel.toastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()}

        viewModel.screenState.observe(viewLifecycleOwner) {
            stateScreen(it)}
        initAdapterSetting()
        return binding.root
    }

    override fun openPlayList(idPlayList: String) {
        navigator.startYTVideoListFragmentMainStack(idPlayList)
    }

    override fun addToFavoritePL(list: YTPlayList) {
        viewModel.addToFavoritePL(list)
    }

    private fun initAdapterSetting() {
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = adapterLayoutManager
            addItemDecoration(DecoratorParentGrid(adapterVideoIdList.currentList.size, context))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                        && !isLoading
                    ) {
                        viewModel.loadMore(ytChannelAndPlayList)
                        isLoading = true
                    }
                }
            })
        }
    }

    private fun stateScreen(state: ViewModelResult<YTPlayListPaging>) {
        when (state) {
            is EmptyResultViewModel -> showEmpty()
            is ErrorLoadingViewModel -> showError()
            is LoadingViewModel -> showLoading()
            is NotMoreLoadingViewModel -> isLoading = false
            is SuccessViewModel -> {
                ytChannelAndPlayList = state.dataViewModelResult
                adapterVideoIdList.submitList(ytChannelAndPlayList.listPlayList)
                navigator.setTitle(ytChannelAndPlayList.titleChannel)
                showResult()
                isLoading = false
            }
        }
    }

    companion object {
        const val ARGS_ID_CHANNEL = "idChannel"
        fun newInstance(idChannel: String): YTPlayListFragment {
            val myFragment = YTPlayListFragment()

            val args = Bundle()
            args.putString(ARGS_ID_CHANNEL, idChannel)
            myFragment.arguments = args
            return myFragment
        }
    }
}
