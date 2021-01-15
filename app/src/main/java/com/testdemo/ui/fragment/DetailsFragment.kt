package com.testdemo.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import com.testdemo.R
import com.testdemo.databinding.FragmentDetailsBinding
import com.testdemo.themeColor

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var vb: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host
            duration = 300
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb = FragmentDetailsBinding.bind(view).apply {
            NavigationUI.setupWithNavController(toolbar, findNavController())

            Glide.with(view).load(args.user.avatarUrl).centerCrop().into(imageAvatar)
            labelTitle.text = args.user.login
        }
    }
}