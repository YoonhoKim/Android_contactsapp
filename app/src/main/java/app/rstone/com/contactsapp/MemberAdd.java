package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static app.rstone.com.contactsapp.Main.*;

public class MemberAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);
        final Context ctx = MemberAdd.this;
        ItemAdd query = new ItemAdd(ctx);
        findViewById(R.id.addSubmitBtn).setOnClickListener(
                (View v)->{
                    Member m = new Member();
                    EditText addPw = findViewById(R.id.addPw);
                    EditText addName = findViewById(R.id.addName);
                    EditText addEmail = findViewById(R.id.addEmail);
                    EditText addPhone = findViewById(R.id.addPhone);
                    EditText addAddr = findViewById(R.id.addAddr);

                    query.m.pw = addPw.getText().toString();
                    query.m.name = addName.getText().toString();
                    query.m.email = addEmail.getText().toString();
                    query.m.phone = addPhone.getText().toString();
                    query.m.addr = addAddr.getText().toString();
                    Log.d("aaa:::","///"+m.seq+"///"+m.pw+"///"+m.name+"///"+m.email+"///"+m.phone+"///"+m.addr);
                    query.execute();
                    //this.startActivity(new Intent(ctx,MemberList.class));
                }
        );

    }
    private class AddQuery extends QueryFactory{
        SQLiteHelper helper;
        public AddQuery(Context ctx) {
            super(ctx);
            helper = new SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemAdd extends  AddQuery{
        Member m;
        public ItemAdd(Context ctx) {
            super(ctx);
            m = new Member();
        }
        public void execute(){
            Log.d("bbb:::","///"+m.seq+"///"+m.pw+"///"+m.name+"///"+m.email+"///"+m.phone+"///"+m.addr);
            this.getDatabase()
                    .execSQL(String.format(" INSERT INTO %s ( '%s', '%s', '%s', '%s', '%s'  ) VALUES ( '%s', '%s', '%s', '%s', '%s' ) ",MEMTAB,MEMPW,MEMNAME,MEMEMAIL,MEMPHONE,MEMADDR,     m.pw, m.name, m.email, m.phone, m.addr));
        }
    }
}
