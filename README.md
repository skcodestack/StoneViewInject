# StoneViewInject
基于编译时注解框架


###使用方式

     @ViewInjector(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjectUtil.injectView(this);

        tv.setText("fddfdfdfdfdfdfdfdfdfdf");

    }
