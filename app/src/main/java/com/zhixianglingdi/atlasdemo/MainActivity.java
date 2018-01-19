package com.zhixianglingdi.atlasdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.taobao.atlas.dex.util.FileUtils;
import com.taobao.atlas.update.AtlasUpdater;
import com.taobao.atlas.update.model.UpdateInfo;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void remote(View view) {
        Intent intent = new Intent();
        intent.setClassName(view.getContext(),"com.zhixianglingdi.remotebundle.RemoteActivity");
        startActivity(intent);
    }

    public void local(View view) {
        Intent intent = new Intent();
        intent.setClassName(view.getContext(), "com.zhixianglingdi.localbundle.LocalActivity");
        startActivity(intent);

    }

    public void update(View view) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                update();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(MainActivity.this, "更新完成，请重启", Toast.LENGTH_LONG).show();
            }
        }.execute();


    }
    private void update(){
        File updateInfo = new File(getExternalCacheDir(), "update.json");
        if (!updateInfo.exists()) {
            Toast.makeText(MainActivity.this, "更新信息不存在，请先 执行 buildTpatch.sh", Toast.LENGTH_LONG).show();

            return;
        }

        String jsonStr = new String(FileUtils.readFile(updateInfo));
        UpdateInfo info = JSON.parseObject(jsonStr, UpdateInfo.class);

        File patchFile = new File(getExternalCacheDir(), "patch-" + info.updateVersion + "@" + info.baseVersion + ".tpatch");
        try {
            AtlasUpdater.update(info, patchFile);
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "更新失败, " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }


}
