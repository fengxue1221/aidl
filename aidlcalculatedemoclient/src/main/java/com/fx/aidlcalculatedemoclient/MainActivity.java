package com.fx.aidlcalculatedemoclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fx.aidl.calculate.CalculateInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText num1, num2;
    private Button button;
    private TextView answer;
    private CalculateInterface mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            logE("connect service");
            mService = CalculateInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            logE("disconnect service");
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        button = findViewById(R.id.button);
        answer = findViewById(R.id.answer);


        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setAction("com.fx.aidl.calculate.CalculateService");
        intent.setPackage("com.fx.aidl");
        intent.putExtras(bundle);
//        bindService(new Intent(createExplicitFromImplicitIntent(this,intent)), mServiceConnection, Context.BIND_AUTO_CREATE);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double n1 = Double.parseDouble(num1.getText().toString().trim());
                double n2 = Double.parseDouble(num2.getText().toString().trim());
                try {
                    String an = "计算结果：" + mService.doCalculate(n1, n2);
                    answer.setTextColor(Color.BLUE);
                    answer.setText(an);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logE(String str) {
        Log.e(TAG, "--------" + str + "--------");
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

}
