package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import static app.rstone.com.contactsapp.Main.*;
public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context ctx = MemberUpdate.this;

        Intent intent = this.getIntent();
        String[] spec = getIntent()
                .getStringExtra("spec")
                .split(",");
        Log.d("vvv :::",""+spec[0]+"///"+spec[1]+"///"+spec[2]+"///"+spec[3]+"///"+spec[4]+"///"+spec[5]+"///");
        int prof = getResources().getIdentifier(this.getPackageName()+":drawable/"+spec[5],null,null);
        ImageView profile = findViewById(R.id.profileImg);
        profile.setImageDrawable(
                getResources()
                        .getDrawable(prof,ctx.getTheme())
        );
        TextView name = findViewById(R.id.textName);
        name.setText(spec[1]);
        EditText email = findViewById(R.id.changeEmail);
        email.setText(spec[2]);
        EditText phone = findViewById(R.id.changePhone);
        phone.setText(spec[3]);
        EditText addr = findViewById(R.id.changeAddress);
        addr.setText(spec[4]);
        /*m.seq+","+m.name+","+m.email+","+m.phone+","+m.addr+","+m.photo*/
        findViewById(R.id.confirmBtn).setOnClickListener(
                (View v)->{
                    ItemUpdate query = new ItemUpdate(ctx);
                    query.m.name =
                            (name.getText().toString().equals(""))?
                            spec[1] : name.getText().toString() ;
                    query.m.addr =
                            (addr.getText().toString().equals(""))?
                            spec[4] : addr.getText().toString();
                    query.m.email =
                            (email.getText().toString().equals(""))?
                            spec[2] : email.getText().toString();
                    query.m.phone =
                            (phone.getText().toString().equals(""))?
                            spec[3]: phone.getText().toString();
                    query.m.seq = Integer.parseInt(spec[0]);
                    new StatusService(){

                        @Override
                        public void perform() {
                            query.execute();
                        }
                    }.perform();
                    Intent moveToDetail = new Intent(ctx,MemberDetail.class);
                    moveToDetail.putExtra("seq",spec[0]);
                    startActivity(moveToDetail);

                }
        );
        findViewById(R.id.cancelBtn).setOnClickListener(
                (View v)->{
                    Intent moveDetail = new Intent(ctx,MemberDetail.class);
                    moveDetail.putExtra("seq",spec[0]);
                    /*startActivity();*/
                }
        );
    }
    private class UpdateQuery extends Main.QueryFactory{
        Main.SQLiteHelper helper;

        public UpdateQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends UpdateQuery{
        Member m ;
        public ItemUpdate(Context ctx) {
            super(ctx);
            m = new Member();
        }
        public void execute(){
           this.getDatabase()
                    .execSQL(String.format(
                            " UPDATE %s SET " +
                                    " %s = '%s' , " +
                                    " %s = '%s' , " +
                                    " %s = '%s' , " +
                                    " %s = '%s' " +
                                    " WHERE %s LIKE '%s' ",
                            MEMTAB,
                            MEMNAME,m.name,
                            MEMEMAIL,m.email,
                            MEMPHONE,m.phone,
                            MEMADDR,m.addr,
                            MEMSEQ,m.seq));

        }
    }
}
