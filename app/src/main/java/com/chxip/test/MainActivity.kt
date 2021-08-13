package com.chxip.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.TextView
import com.chxip.view.flowlayout.FlowLayout
import com.chxip.view.flowlayout.FlowLayoutAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listStr = listOf<String>(
            "bbbb",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA",
            "AAAAA"
        )

        flowLayout.setAdatper(object : FlowLayoutAdapter<String>(listStr) {

            override fun getView(flowLayout: FlowLayout, position: Int, data: Any): View {
                val view = layoutInflater.inflate(R.layout.item, flowLayout,false)
                val tv_title: TextView = view.findViewById(R.id.tv_title)
                tv_title.setText(data.toString())
                return view
            }

        })

    }
}
