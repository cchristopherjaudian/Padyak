package com.padyak.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.padyak.R;

public class frmFindLocation extends AppCompatActivity {

    TextView txNearestCategory, txNearestName, txFindNearestTitle, txFindTitle;
    Button btnFind;
    String findCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_find_location);
        btnFind = findViewById(R.id.btnFind);
        txNearestCategory = findViewById(R.id.txNearestCategory);
        txNearestName = findViewById(R.id.txNearestName);
        txFindNearestTitle = findViewById(R.id.txFindNearestTitle);
        txFindTitle = findViewById(R.id.txFindTitle);

        Bundle bundle = getIntent().getExtras();
        findCategory = bundle.getString("find","");

        txNearestCategory.setText(findCategory);
        txNearestName.setText(findCategory);
        txFindNearestTitle.setText("Nearest " + findCategory);
        txFindTitle.setText("Find " + findCategory);
        btnFind.setText("Find " + findCategory);
    }
}