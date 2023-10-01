package com.reviwh.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.reviwh.githubuser.R
import com.reviwh.githubuser.adapter.FollowListAdapter
import com.reviwh.githubuser.data.remote.response.ItemsItem
import com.reviwh.githubuser.databinding.FragmentFollowBinding
import com.reviwh.githubuser.unit.FollowViewModel

class FollowFragment : Fragment() {
    private var position: Int? = null
    private var username: String? = null
    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION.toString())
            username = it.getString(ARG_USERNAME).toString()
        }
        val followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        followViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showToast(errorMessage)
            }
        }

        when (position) {
            1 -> {
                followViewModel.getFollowers(username.toString())
                followViewModel.hasFollower.observe(viewLifecycleOwner) {
                    showUserHaveNoFollow(
                        it, "followes"
                    )
                }
            }

            else -> {
                followViewModel.getFollowing(username.toString())
                followViewModel.hasFollowing.observe(viewLifecycleOwner) {
                    showUserHaveNoFollow(it, "following")
                }
            }
        }

        followViewModel.followResponseItem.observe(viewLifecycleOwner) { followResponseItem ->
            setUserData(followResponseItem)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showSelectedUser(username: String) {
        val intent = Intent(requireActivity(), UserDetailsActivity::class.java)
        intent.putExtra(UserDetailsActivity.USERNAME, username)
        requireActivity().startActivity(intent)
    }

    private fun setUserData(users: List<ItemsItem?>?) {
        val adapter = FollowListAdapter()
        adapter.submitList(users)
        binding.rvFollow.adapter = adapter
        adapter.setOnItemClickCallback(object : FollowListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data.login.toString())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserHaveNoFollow(hasFollow: Boolean, followType: String) {
        if (hasFollow) {
            binding.tvUserHaveNoFollow.visibility = View.GONE
            binding.tvUserHaveNoFollow.text = ""
        } else {
            binding.tvUserHaveNoFollow.visibility = View.VISIBLE
            binding.tvUserHaveNoFollow.text = getString(R.string.have_no, username, followType)
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

    companion object {
        val ARG_POSITION: Int? = null
        val ARG_USERNAME: String? = null
    }
}