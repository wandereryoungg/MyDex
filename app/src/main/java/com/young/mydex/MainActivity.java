package com.young.mydex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDex();
            }
        });
    }

    private void loadDex() {
        File cacheDir = FileUtil.getCacheDir(this);
        String desPath = cacheDir.getAbsolutePath() + File.separator + "mylibrary_dex.jar";
        File desFile = new File(desPath);
        FileUtil.copyFile(this, "mylibrary_dex.jar", desFile);
        DexClassLoader classLoader = new DexClassLoader(desPath, cacheDir.getAbsolutePath(), null, getClassLoader());
        try {
            Dynamic dynamic = (Dynamic) classLoader.loadClass("com.young.mylibrary.MyLibrary").newInstance();
            Toast.makeText(this, dynamic.sayHello(), Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拒绝权限无法使用该应用", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
