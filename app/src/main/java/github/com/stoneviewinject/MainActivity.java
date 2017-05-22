package github.com.stoneviewinject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ViewInjectUtil;
import com.example.annotation.ViewInjector;

public class MainActivity extends AppCompatActivity {


    @ViewInjector(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjectUtil.injectView(this);

        tv.setText("fddfdfdfdfdfdfdfdfdfdf");

    }
}
