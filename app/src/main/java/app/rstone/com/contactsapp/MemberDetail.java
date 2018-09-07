package app.rstone.com.contactsapp;

import app.rstone.com.contactsapp.Main.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import app.rstone.com.contactsapp.Main;

import java.util.ArrayList;

import static app.rstone.com.contactsapp.Main.*;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        ItemDetail query = new ItemDetail(ctx);
        query.id=seq;
        Member m = (Member) new RetrieveService() {
            @Override
            public Object perform() {
                return query.execute();
            }
        }.perform();
        Log.d("검색된 이름 : ","");
        int prof = getResources().getIdentifier(this.getPackageName()+":drawable/"+m.photo,null,null);
        ImageView profile = findViewById(R.id.profileimg);
        profile.setImageDrawable(
                getResources()
                .getDrawable(prof,ctx.getTheme())
        );
        TextView name = findViewById(R.id.name);
        name.setText(m.name);
        TextView phone = findViewById(R.id.phone);
        phone.setText(m.phone);
        TextView email = findViewById(R.id.email);
        email.setText(m.email);
        TextView addr = findViewById(R.id.addr);
        addr.setText(m.addr);
        findViewById(R.id.callBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.updateBtn).setOnClickListener(
                (View v)->{
                      Intent moveUpdate = new Intent(ctx,MemberUpdate.class);
                      moveUpdate.putExtra("spec",
                              m.seq+","+m.name+","+m.email+","+m.phone+","+m.addr+","+m.photo);
                      startActivity(moveUpdate);
                }
        );


    }
    private class DetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;

        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemDetail extends DetailQuery{
        String id;
        public ItemDetail(Context ctx) {
            super(ctx);
        }
        public Member execute(){
           Member mem = new Member();
           Cursor cursor = this
                    .getDatabase()
                    .rawQuery(String.format(" SELECT * FROM %s WHERE %s LIKE '%s' ", MEMTAB,MEMSEQ,id),null);
           cursor.moveToFirst();
           mem.seq = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEMSEQ)));
           mem.name = cursor.getString(cursor.getColumnIndex(MEMNAME));
           mem.pw = cursor.getString(cursor.getColumnIndex(MEMPW));
           mem.email = cursor.getString(cursor.getColumnIndex(MEMEMAIL));
           mem.phone = cursor.getString(cursor.getColumnIndex(MEMPHONE));
           mem.addr = cursor.getString(cursor.getColumnIndex(MEMADDR));
           mem.photo = cursor.getString(cursor.getColumnIndex(MEMPHOTO));
           return mem;
        }

    }
    private class MemberAdapter extends BaseAdapter{
        Member mem;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, Member mem) {
            this.mem = mem;
            this.inflater = LayoutInflater.from(ctx);
        }


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
