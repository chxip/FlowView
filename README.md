## 简介
FlowLayout流式布局，用来实现搜索记录，热门搜索等效果<br/>
## 效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/143a7157065f49df89606732f5606337.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM3MTA3NTI=,size_16,color_FFFFFF,t_70)
## 使用方式
```kotlin
val listStr = listOf<String>("你好，Android", "Android", "FlowView", "ViewModel", "DataBinding",
    "协程", "Room", "ListView", "自定义View", "自定义ViewGroup", "疾风亦有归途",
    "是时候表演真正的技术了", "我用双手成就你的梦想", "我还以为你从来都不会选我",
    "真可怜，让我抱抱你")
//点击事件，需要在setAdapter之前调用
flowLayout.setOnItemClickListener (object : FlowLayout.OnItemClickListener{
    override fun onItemClick(view: View, position: Int, data: Any) {
        Toast.makeText(this@MainActivity,data.toString(),Toast.LENGTH_LONG).show()
    }

})
//设置数据
flowLayout.setAdatper(object : FlowLayoutAdapter<String>(listStr) {
    override fun getView(flowLayout: FlowLayout, position: Int, data: Any): View {
        val view = layoutInflater.inflate(R.layout.item, flowLayout, false)
        val tv_title: TextView = view.findViewById(R.id.tv_title)
        tv_title.setText(data.toString())
        return view
    }
})	
```
## 属性
```kotlin
//设置列边距 
flowLayout.setHorizontalSpacing(10f)
//设置行边距
flowLayout.setVerticalSpacing(10f)
```
getView中的view设置Margin,也可以设置边距
