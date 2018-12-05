package com.example.parkseunghyun.achievementofall.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.parkseunghyun.achievementofall.Configurations.VolleyHttpService
import com.example.parkseunghyun.achievementofall.R

class HomeAppInfoPager : Fragment() {

    private var view_: View? = null
    private var homeInfoPagerContext: Context? = null

    private var appInfo: TextView ?= null
    private var noticeInfo: TextView ?= null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        homeInfoPagerContext = activity
        view_ = inflater!!.inflate(R.layout.fragment_home_info, container, false)
        appInfo = view_!!.findViewById<TextView>(R.id.appInfo)
        noticeInfo = view_!!.findViewById<TextView>(R.id.noticeInfo)
        var scrollLayout = view_!!.findViewById<NestedScrollView>(R.id.info_layout)
        scrollLayout.isVerticalScrollBarEnabled = true

        setAppInfo()

        return view_
    }
    private fun setAppInfo() {
        VolleyHttpService.getAppInfo(homeInfoPagerContext!!){ success ->

            println(success)
            appInfo?.setText(success.getString("appInfo"))
            noticeInfo?.setText(success.getString("noticeInfo"))
        }

    }
}
