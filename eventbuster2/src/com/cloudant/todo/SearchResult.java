package com.cloudant.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SearchResult extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();

        String fName = getIntent().getExtras().getString("searchedWord");
        System.out.println("*****************fName**********"+fName);
        java.lang.CharSequence seq = fName;
                //intent.getStringExtra("searchedWord");
        //getIntent().getExtras().getString("bucketno");

        EditText tv = (EditText)findViewById(R.id.editText);
        tv.setText(seq);



    }
}
