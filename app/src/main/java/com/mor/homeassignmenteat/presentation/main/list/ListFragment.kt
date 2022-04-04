package com.mor.homeassignmenteat.presentation.main.list

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mor.homeassignmenteat.R
import com.mor.homeassignmenteat.buisness.domain.model.Story
import com.mor.homeassignmenteat.buisness.domain.util.MessageType
import com.mor.homeassignmenteat.buisness.domain.util.Response
import com.mor.homeassignmenteat.buisness.domain.util.StateMessage
import com.mor.homeassignmenteat.buisness.domain.util.UIComponentType
import com.mor.homeassignmenteat.presentation.BaseFragment
import com.mor.homeassignmenteat.presentation.util.TopSpacingItemDecoration
import com.mor.homeassignmenteat.presentation.util.processQueue

class ListFragment : BaseFragment(),
    StoryListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener
{

    private lateinit var searchView: SearchView
    private var recyclerAdapter: StoryListAdapter? = null // can leak memory so need to null
    private val viewModel: StoryViewModel by viewModels()
    private lateinit var menu: Menu
    private lateinit var focusView :View
    private lateinit var swipe :SwipeRefreshLayout



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        swipe = view.findViewById(R.id.swipe_refresh)
        swipe.setOnRefreshListener(this)
        focusView = view.findViewById(R.id.focusable_view)

        initRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner) { state ->
            Log.e(TAG, "subscribeObservers: $state" )

            uiCommunicationListener.displayProgressBar(state.isLoading)

            processQueue(
                context = context,
                queue = state.queue
            )

            recyclerAdapter?.apply {
                submitList(list = state.storyList)

            }
        }
    }

    private fun initSearchView(){
        activity?.apply {
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }

        // set initial value of query text after rotation/navigation
        viewModel.state.value?.let { state ->
            if(state.query.isNotBlank()){
                searchView.isIconified = false
                requireView().findViewById<View>(R.id.focusable_view).requestFocus()
            }
        }
    }

    private fun executeNewQuery(query: String){
        viewModel.onTriggerEvent(StoryEvents.UpdateQuery(query))
        viewModel.onTriggerEvent(StoryEvents.NewSearch)
        resetUI()
    }

    private  fun resetUI(){
        uiCommunicationListener.hideSoftKeyboard()
        focusView.requestFocus()
    }

    private fun initRecyclerView(){
        requireView().findViewById<RecyclerView>(R.id.stories_recyclerview).apply {
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = StoryListAdapter(this@ListFragment)
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    Log.d(TAG, "onScrollStateChanged: exhausted? ${viewModel.state.value?.isQueryExhausted}")
                    if (
                        lastPosition == recyclerAdapter?.itemCount?.minus(1)
                        && viewModel.state.value?.isLoading == false
                        && viewModel.state.value?.isQueryExhausted == false
                    ) {
                        Log.d(TAG, "listFragment: attempting to load next page...")
                        viewModel.onTriggerEvent(StoryEvents.NextPage)
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
//        this.menu = menu
//        inflater.inflate(R.menu.search_menu, this.menu)
//        initSearchView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_filter_settings -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(position: Int, item: Story) {
        try{
            viewModel.state.value?.let { state ->
                val bundle = bundleOf("storyPk" to item.title)
                Toast.makeText(requireContext(), "what we wants to do with the item ${bundle["storyPk"]}", Toast.LENGTH_SHORT).show()
            }?: throw Exception("Null story")
        }catch (e: Exception){
            e.printStackTrace()
            viewModel.onTriggerEvent(
                StoryEvents.Error(
                    stateMessage = StateMessage(
                        response = Response(
                            message = e.message,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    )
                ))
        }
    }

    override fun onRefresh() {
        viewModel.onTriggerEvent(StoryEvents.NewSearch)
        swipe.isRefreshing = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }
}













