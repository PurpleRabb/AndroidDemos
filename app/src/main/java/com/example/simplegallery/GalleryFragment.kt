package com.example.simplegallery

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.simplegallery.R.layout
import kotlinx.android.synthetic.main.fragment_gallery.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var galleryViewModel: GalleryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val galleryAdapter = GalleryAdapter()
        //recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = galleryAdapter
        //ViewModelProvider(getActivity(),new SavedStateViewModelFactory(getActivity().getApplication(),this)).get(MyViewModel.class);
        galleryViewModel = ViewModelProvider(
            requireActivity(),
            SavedStateViewModelFactory(requireActivity().application, this)
        ).get(GalleryViewModel::class.java)
        galleryViewModel!!.photoList.observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
            swipeLayout.isRefreshing = false //停止下拉转圈
            if (galleryViewModel!!.scrollToTop) {
                recyclerView.scrollToPosition(0)
                galleryViewModel!!.scrollToTop = false
            }
        })

        galleryViewModel!!.DataStatus.observe(viewLifecycleOwner, Observer {
            galleryAdapter.footerViewStatus = it
            requireView().post { // Notify adapter with appropriate notify methods
                //avoid "Cannot call this method in a scroll callback"
                galleryAdapter.notifyItemChanged(galleryAdapter.itemCount - 1)
            }
            Log.i("GalleryViewModel", it.toString())
            if (it == NETWORK_ERROR) swipeLayout.isRefreshing = false //网络错误停止刷新
        })

        //数据初始化
        galleryViewModel!!.photoList.value
            ?: galleryViewModel!!.fetchData(galleryViewModel!!.getCurrentKeyWord())
        swipeLayout.setOnRefreshListener {
            //下拉重新刷新
            galleryViewModel!!.fetchData(galleryViewModel!!.getCurrentKeyWord())
        }

        recyclerView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            //int scrollX, int scrollY, int oldScrollX, int oldScrollY
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                if (p4 > 0) {
                    return
                } else {
                    //向下滑
                    val pos: IntArray = intArrayOf(0, 0)
                    (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                        pos
                    )
                    if (pos[0] == galleryAdapter.itemCount - 1) {
                        //Log.i("onScrollChange", "reach bottom!!!")
                        galleryViewModel!!.fetchMore()
                        if (galleryViewModel!!.isLoadingFinish()) {
                            Log.i("onScrollChange", "loading finish!!!")
//                            (recyclerView.layoutManager as StaggeredGridLayoutManager).findViewByPosition(pos[0])
//                                ?.findViewById<TextView>(R.id.textView)?.text = "加载完成"
//                            (recyclerView.layoutManager as StaggeredGridLayoutManager).findViewByPosition(pos[0])
//                                ?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.INVISIBLE
                        } else {
//                            (recyclerView.layoutManager as StaggeredGridLayoutManager).findViewByPosition(pos[0])
//                                ?.findViewById<TextView>(R.id.textView)?.text = "加载中"
//                            (recyclerView.layoutManager as StaggeredGridLayoutManager).findViewByPosition(pos[0])
//                                ?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.apply {
            maxWidth = 1000
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    p0?.let { galleryViewModel!!.fetchData(it) }
                    swipeLayout.isRefreshing = true
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> Log.i("GalleryFragment", "app_bar_search")
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}