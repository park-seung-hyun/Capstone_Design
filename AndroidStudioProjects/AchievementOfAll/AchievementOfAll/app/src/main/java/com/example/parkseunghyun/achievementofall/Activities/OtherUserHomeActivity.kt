package com.example.parkseunghyun.achievementofall

import adapter.JoinedContentsAdapter
import adapter.ThumbnailAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.parkseunghyun.achievementofall.Configurations.GlobalVariables
import com.example.parkseunghyun.achievementofall.Configurations.VolleyHttpService
import com.example.parkseunghyun.achievementofall.Interfaces.RecyclerViewClickListener
import de.hdodenhof.circleimageview.CircleImageView
import model.JoinedContentsModel
import model.ThumbnailModel
import org.json.JSONObject
import java.util.*

class OtherUserHomeActivity : AppCompatActivity() , RecyclerViewClickListener {

    // 서버 ip 주소
    private var globalVariables: GlobalVariables?= GlobalVariables()
    private var ipAddress: String = globalVariables!!.ipAddress

    private var otherUserNameView:TextView? = null
    private var otherUserName:String? = null
    private var otherUserEmail: String? = null

    private var otherUserProfile: CircleImageView ?= null

    private var joinedContentsView: RecyclerView? = null
    private var joinedContentsModelArrayList: ArrayList<JoinedContentsModel>? = null
    private var joinedContents = mutableListOf<String>()
    private var joinedContentsAdapter: JoinedContentsAdapter? = null

    // 사용자의 비디오 목록
    private var videoList = mutableListOf<JSONObject>()
    private var videoContentList = mutableListOf<String>()
    private var thumbnailView: RecyclerView? = null

    private var thumbnailAdapter: ThumbnailAdapter? = null
    private var thumbnailModelList: ArrayList<ThumbnailModel>? = null

    override fun recyclerViewListClicked(v: View, position: Int) {
        Toast.makeText(this, "position is $position", Toast.LENGTH_LONG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.other_user_home_layout)

        otherUserNameView = findViewById(R.id.other_user_name)
        otherUserProfile = findViewById(R.id.other_user_profile_image)

        if(intent.getStringExtra("email")!=null){
            otherUserEmail = intent.getStringExtra("email")
            otherUserName = intent.getStringExtra("userName")

            otherUserNameView!!.setText(otherUserName)
//            otherUserParticipatedInfo()
            println("TEST_____" + otherUserName)
        }

        setOtherUserInfo(otherUserEmail!!)
    }

    private fun setOtherUserInfo(email: String) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)

        VolleyHttpService.getOtherUserInfo(this, jsonObject) { success ->
            println(success)

            // 사용자 프로필 사진 갱신
            Glide.with(this).load("${ipAddress}/getOtherUserImage/"+otherUserEmail).into(otherUserProfile)

            // 사용자 참여 컨텐츠 정보 갱신
            var contentList: JSONObject

            for(i in 0.. (success.getJSONArray("contentList").length()-1)){
                contentList = success.getJSONArray("contentList")[i] as JSONObject
                var contentName =  contentList.getString("contentName")
                joinedContents?.add(contentName.toString())

                for(i in 0..(contentList.getJSONArray("videoPath").length()-1)){
                    videoList?.add(contentList.getJSONArray("videoPath").getJSONObject(i))
                    videoContentList?.add(contentName)
                }
            }
            generateJoinedContentsView()
            generateVideoCollection()
        }
    }

    private fun generateJoinedContentsView() {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        joinedContentsView = findViewById(R.id.id_other_user_contents)
        joinedContentsView!!.layoutManager = layoutManager
        joinedContentsView!!.itemAnimator = DefaultItemAnimator()
        joinedContentsModelArrayList = ArrayList()

        for (i in joinedContents.indices) {
            val joinedContentsModel = JoinedContentsModel()
            joinedContentsModel.name = joinedContents?.get(i)
            joinedContentsModelArrayList!!.add(joinedContentsModel)
        }

        joinedContentsAdapter = JoinedContentsAdapter(this, joinedContentsModelArrayList!!, this)
        joinedContentsView!!.adapter = joinedContentsAdapter

    }
    private fun generateVideoCollection() {

        val layoutManager: RecyclerView.LayoutManager
        thumbnailView = findViewById(R.id.id_other_thumbnail)
        layoutManager = GridLayoutManager(this, 3)
        thumbnailView!!.layoutManager = layoutManager
        thumbnailView!!.itemAnimator = DefaultItemAnimator()

        thumbnailModelList = ArrayList()

        for (i in videoList.indices) {
            val thumbnailModel = ThumbnailModel()

            thumbnailModel.userEmail = otherUserEmail
            thumbnailModel.who = "other"
            thumbnailModel.videoPath = videoList?.get(i)
            thumbnailModel.contentName = videoContentList?.get(i)

            thumbnailModelList!!.add(thumbnailModel)
        }

        thumbnailAdapter = ThumbnailAdapter(this, thumbnailModelList!!)
        thumbnailView!!.adapter = thumbnailAdapter

    }

}