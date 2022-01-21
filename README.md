## 소개
먼저 이번 프로젝트는 데이터 바인딩 예시 프로젝트를 기술 하였습니다.

## 목차(해당 링크들은 더욱 이해를 하기 쉽게 따로 포스팅을 한 것 입니다.)
1. DataBinding 기본(Activity + ViewModel)
2. [LiveData + DataBinding](https://github.com/tnvnfdla1214/DataBinding_2)
3. [RecyclerView + DataBinding](https://github.com/tnvnfdla1214/DataBinding_3)
4. DI(Koin) + DataBinding
5. DataBindingAdapter
6. Tow -way Binding(추가 예정)
7. [Context 에 대한 의존이 필요한 경우에 어떻게 처리해야 하나?](https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150) : 이 지침은 [공식 지침](https://developer.android.com/jetpack/guide/ui-layer/events) 을 위해 더 이상 사용되지 않습니다

![image](https://user-images.githubusercontent.com/48902047/148549776-2a7e3b73-5d17-4756-b27f-2ca500eec6d1.png)

## 1. DataBinding 기본
![image](https://user-images.githubusercontent.com/48902047/148550860-d7af7f87-9026-44e3-a4ed-be764ffb83e9.png)

먼저 activity_main에 \<data> 에는 실제 액티비티를 구동 시키는 ViewModel을 추가하였습니다.

그중 아래 사진과 같은 onClick()은 이렇게 처리해줍니다.

![image](https://user-images.githubusercontent.com/48902047/148551548-f827aaab-fa1a-4bf3-8e76-dfce461998f3.png)

MainActivity에서는 Binding과 ViewModel을 연결시켜줍니다.

![image](https://user-images.githubusercontent.com/48902047/148551702-234cbee3-dd71-4a3d-873d-60fcb4a9c576.png)

ViewModel은 buttononClick()함수를 만들어 onClick을 실행 시켜줍니다.

![image](https://user-images.githubusercontent.com/48902047/148552929-d5b00e88-58ab-4809-9be0-b20a073e41cf.png)

또한 format은 아래와 같이 할 수 있습니다.

![image](https://user-images.githubusercontent.com/48902047/148565178-92b808fd-92e3-48f6-bd0e-2843c17bfccc.png)

![image](https://user-images.githubusercontent.com/48902047/148565295-d4daa973-c1c1-45a3-88d3-51ffb3bf3e5f.png)

## 2. LiveData + DataBinding
activity_main.xml을 보면 Recyclerview안에 @{viewModel.userList}로 연결합니다.

![image](https://user-images.githubusercontent.com/48902047/148553382-d972191a-a75c-4f3e-af27-e45e692486ff.png)

MainViewModel에서 \_userList 는 MutableLiveData 로 userList는 LiveData로 지정후 \_userList이 변경될때마다 변화를 감지시킵니다.

그리고 \_userList에 이벤트 발생시 추가시킵니다.

![image](https://user-images.githubusercontent.com/48902047/148553685-f60fd1e2-b439-49e9-85db-4bbb3cd5f493.png)

## 3. RecyclerView + DataBinding
위와 같은 연결후 MyAdapter라는 이름의 RecyclerView.Adapter를 만들어 줍니다.

그 후 RecyclerView에 필요한 함수를 지정하며 binding을 끌어다 사용합니다.
```Java
class MyAdapter()
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var userList = arrayListOf<User>()

    // 생성된 뷰 홀더에 값 지정
    class MyViewHolder(private val binding: MainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentUser : User) {
            binding.user = currentUser
        }
    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
```
나중에 설명하겠지만 bindingAdapter에 item을 set하는 setItem함수를 설정합니다.

아래의 함수는 recyclerView를 받아 null인지 아닌지 확인후 myAdapter에 데이터가 들어오는것을 감지하면 추가후 재 등록합니다.

```Java
object MyBindingAdapter{

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
    .
    .

}
```
activity_main에 app:items="@{viewModel.userList}" 으로 지정합니다.

![image](https://user-images.githubusercontent.com/48902047/148558952-a0219ca6-8449-4dd1-a3c1-ceee85468cf0.png)

## 4. DI(Koin) + DataBinding
```Java
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule)
        }
    }

}
```

```Java
val appModule = module {
    viewModel { MainViewModel() }
}
```
ViewModel과 Activity연결시 직접적으로 연결하지 않고 getViewModel()으로 연결 됩니다.
```Java
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    //private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        // 뷰모델 연결
        //mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //binding.viewModel = mainViewModel
        binding.viewModel = getViewModel() //koin(DI)으로 뷰모델 연결

        // 뷰모델을 LifeCycle 에 종속시킴, LifeCycle 동안 옵저버 역할을 함
        binding.lifecycleOwner = this
    }
}
```

## 5. DataBindingAdapter
setVisible의 연결은 아래와 같습니다.

![image](https://user-images.githubusercontent.com/48902047/148560345-2f094580-6ff3-40ac-9392-409f2fd64e8c.png)

liveVisible 라는 함수의 boolean 결정은 ViewModel에서 합니다.

![image](https://user-images.githubusercontent.com/48902047/148560529-dd4b3ac3-7c85-43b7-8057-176ab95dfc91.png)

activity_main에서 app:visible="@{viewModel.liveVisible}" 으로 연결합니다.

![image](https://user-images.githubusercontent.com/48902047/148560744-37498a9d-f5e0-4c5f-9f25-cd13fde927ef.png)

setProfileUrl의 연결은 아래와 같습니다.

![image](https://user-images.githubusercontent.com/48902047/148561170-8d35bbcd-400e-42fe-bf22-ab9eadee623e.png)

위와 같이 두가지를 지정하고 싶을때는 아래와 같이 설명한다.

![image](https://user-images.githubusercontent.com/48902047/148561650-ceb29aa1-5299-410c-b121-38b08e5e4b9d.png)

![image](https://user-images.githubusercontent.com/48902047/148561664-40bbd533-3e74-41a0-950b-2cd8e516bdc7.png)

@BindingAdapter 어노테이션 옆에 원하는 수의 이름을 지정해주고

매개변수의 순서를 맞춰서 넣어주고

xml에서는 두가지를 이름을 따로해서 선언해주면된다.





