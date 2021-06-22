package com.example.pdfreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PdfAdapter pdfAdapter;
    RecyclerView recyclerView;
    List<File> pdfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTimePermission();
    }

    private void runTimePermission(){
        Dexter.withContext(this)
                .withPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                displayPdf();
                Toast.makeText(MainActivity.this, "permission Granted", Toast.LENGTH_SHORT).show();
                File file=  Environment.getExternalStorageDirectory();
                findPdf(file);

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "permission required", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }



    public ArrayList<File> findPdf(File file){

        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        assert files != null;
        for (File singleFile: files ){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findPdf(singleFile));

            }else {
                if (singleFile.getName().endsWith(".pdf")){
                    arrayList.add(singleFile);
                }

            }
        }
        return arrayList;
    }

    private void displayPdf() {

        recyclerView=findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
       pdfList=new ArrayList<>();
       pdfList.addAll(findPdf(Environment.getExternalStorageDirectory()));
       pdfAdapter =new PdfAdapter(MainActivity.this,pdfList);
       recyclerView.setAdapter(pdfAdapter);
    }

}