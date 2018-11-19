package adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.parkseunghyun.achievementofall.ExoplayerActivity
import com.example.parkseunghyun.achievementofall.GlobalVariables
import com.example.parkseunghyun.achievementofall.R
import model.ThumbnailModel
import org.jetbrains.anko.startActivity


/**
 * Created by A on 23-03-2018.
 */

class ThumbnailAdapter(private val context: Context, private val thumbnailModels: List<ThumbnailModel>) : RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>() {

    // 서버 ip 주소
    private var globalVariables: GlobalVariables?= GlobalVariables()
    private var ipAddress: String = globalVariables!!.ipAddress

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_pic_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var videoPath = thumbnailModels[position].videoPath!!.getString("path")
        println("fasdfasdfasdfasdf")
        println(videoPath)
//        println(pathJSON)
//        var videoPath = pathJSON.get

        var jwtToken = thumbnailModels[position].userToken
        var contentName = thumbnailModels[position].contentName

        // 비디오 썸네일 코드
        val requestOptions = RequestOptions()
        requestOptions.isMemoryCacheable
        Glide.with(context).setDefaultRequestOptions(requestOptions).load("${ipAddress}/getVideo/${jwtToken}/${contentName}/${videoPath}").into(holder.thumbnailView)
        holder.videoName = videoPath
//        holder.thumbnailView.setImageResource(thumbnailModels[position].accountpic!!)
    }

    override fun getItemCount(): Int {
        return thumbnailModels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        internal var thumbnailView: ImageView
        internal var videoName: String ?= null

        init {
            itemView.setOnClickListener(this)
            thumbnailView = itemView.findViewById(R.id.accountpic)
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            println("clickcliclcilcicliclic")

            var jwtToken = thumbnailModels[pos].userToken
            var contentName = thumbnailModels[pos].contentName
            var videoPath = thumbnailModels[position].videoPath!!.getString("path")

            Toast.makeText(v.context, "You clicked "+ videoName, Toast.LENGTH_SHORT).show()
            context.startActivity<ExoplayerActivity>(
                    "token" to jwtToken,
                    "contentName" to contentName,
                    "videoPath" to videoPath,
                    "who" to "me"
            )
        }
    }
}