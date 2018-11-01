package me.pood1e.converter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private String src;
    private String dst;
    private TextView resView;
    private boolean permission;
    private Button convertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chooseFile = findViewById(R.id.file_btn);
        convertBtn = findViewById(R.id.convert_btn);
        convertBtn.setEnabled(false);
        resView = findViewById(R.id.res_view);
        chooseFile.setOnClickListener(v -> {
            if (requestPermission()) {
                showFileChooser();
            } else {
                Toast.makeText(this, "Denied Permission", Toast.LENGTH_SHORT).show();
            }
        });
        convertBtn.setOnClickListener(v -> {
            convertBtn.setEnabled(false);
            dst = FileUtil.getDstFile();
            TransferUtil.convert(src, dst, s -> {
                runOnUiThread(() -> {
                    resView.append("转换成功\n");
                    resView.append("存储在:" + s + "\n");
                });
            });
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a Video to convert"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            return true;
        }
        return permission;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileUtil.getPath(this, uri);
                    src = path;
                    resView.append("\n已选择:" + src + "\n");
                    convertBtn.setEnabled(true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                permission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }


}
