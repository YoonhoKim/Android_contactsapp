package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;
        findViewById(R.id.detailUpdateBtn).setOnClickListener(
                (View v)->{
                    this.startActivity(new Intent(ctx,MemberUpdate.class));
                }
        );
        findViewById(R.id.detailListBtn).setOnClickListener(
                (View v)->{
                    this.startActivity(new Intent(ctx,MemberList.class));
                }
        );
    }
}
