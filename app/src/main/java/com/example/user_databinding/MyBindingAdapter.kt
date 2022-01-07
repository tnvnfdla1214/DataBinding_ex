package com.example.user_databinding


import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object MyBindingAdapter{

    /*
    - @JvmStatic : java의 static을 사용하기 위한 Annotation 입니다.

    - @BindingAdapter : BindingAdapter 함수를 만들기 위한 Annotation 입니다.

    - @BindingAdapter("visible")의 "visible"이 속성 이름이 됩니다.
     */


    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items : ArrayList<User>){

        if(recyclerView.adapter == null) {
            val adapter = MyAdapter()
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        }

        val myAdapter = recyclerView.adapter as MyAdapter
        myAdapter.userList = items
        myAdapter.notifyDataSetChanged()
    }
    @JvmStatic
    @BindingAdapter("visible")
    fun setVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
    @JvmStatic
    @BindingAdapter( value = ["profileUrl", "profilePlaceHolder"], requireAll = false )
    fun setProfileUrl(imageView: ImageView, url: String?, placeHolder: Drawable?) {
        val ph = placeHolder ?: ContextCompat.getDrawable(imageView.context, R.drawable.ic_launcher_background)

        Glide.with(imageView.context) //View, Fragment 혹은 Activity로부터 Context를 가져온다.
            .load(url) //이미지를 로드한다. 다양한 방법으로 이미지를 불러올 수 있다. (Bitmap, Drawable, String, Uri, File, ResourId(Int), ByteArray
            .override(300, 300) //크기 조절
            .placeholder(ph) //Glide 로 이미지 로딩을 시작하기 전에 보여줄 이미지를 설정한다.
            .error(ph) //리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정한다.
            .apply(RequestOptions.circleCropTransform())
            .into(imageView) //이미지를 보여줄 View를 지정한다.

    }
    @BindingAdapter("bind_text")
    @JvmStatic
    fun bindText(textView: TextView, string:String){
        textView.setText(string)
    }



}