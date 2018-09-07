package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static app.rstone.com.contactsapp.Main.MEMPW;
import static app.rstone.com.contactsapp.Main.MEMSEQ;
import static app.rstone.com.contactsapp.Main.MEMTAB;

public class MemberLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_login);
        final Context ctx = MemberLogin.this;
        findViewById(R.id.loginSubmit).setOnClickListener(
                (View v)->{
                    ItemExist query = new ItemExist(ctx);
                    EditText x = findViewById(R.id.idTxt);
                    EditText y = findViewById(R.id.pwTxt);
                    query.id = x.getText().toString();
                    query.pw = y.getText().toString();
                    this.startActivity(new Intent(ctx,MemberList.class));
                    new Main.StatusService() {
                        @Override
                        public void perform() {
                            if(query.execute()){
                                Toast.makeText(ctx,"로그인 성공",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx, MemberList.class));
                            }else{
                                Toast.makeText(ctx,"실패",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx, MemberLogin.class));
                            }
                        }
                    }.perform();

                }
        );
    }
    private class LoginQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();//READ ONLY
        }
    }
    private class ItemExist extends LoginQuery{
        String id,pw;
        public ItemExist(Context ctx) {
            super(ctx);
        }
        public boolean execute(){
            return super
                    .getDatabase()
                    .rawQuery(String.format(" SELECT * FROM %s WHERE %s LIKE '%s' AND %s LIKE '%s' ", MEMTAB,MEMSEQ,id,MEMPW,pw),null) //
                    .moveToNext();
        }
    }
}
