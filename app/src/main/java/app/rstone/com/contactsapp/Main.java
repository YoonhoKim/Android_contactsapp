package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context ctx = Main.this;
        findViewById(R.id.mainLoginBtn).setOnClickListener(
                (View v)->{
                    SQLiteHelper helper = new SQLiteHelper(ctx);
                    this.startActivity(new Intent(ctx,MemberLogin.class));
                }
        );
    }
    static class Member{int seq; String name, pw, email, phone, addr, photo;}
    static interface StatusService{public void perform();}
    static interface ListService{public List<?> perform();}
    static interface RetrieveService{public Object perform();}
    static String DBNAME = "yhkim.db";
    static String MEMTAB = "MEMBER";
    static String MEMSEQ = "SEQ";
    static String MEMNAME = "NAME";
    static String MEMPW = "PW";
    static String MEMEMAIL = "EMAIL";
    static String MEMPHONE = "PHONE";
    static String MEMADDR = "ADDR";
    static String MEMPHOTO = "PHOTO";
    static abstract class QueryFactory{
        Context ctx;
        public QueryFactory(Context ctx) {
            this.ctx = ctx;
        }
        public abstract SQLiteDatabase getDatabase();
    }
    static class SQLiteHelper extends SQLiteOpenHelper{ //sql을 여는것을 도와줌?

        public SQLiteHelper(Context context) {//생성자
            super(context, DBNAME, null, 1);//context, 내디비이름, 디폴트팩토리,1버전
            this.getWritableDatabase();//쓸수있고
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format( " CREATE TABLE IF NOT EXISTS %s " + " (%s INTEGER PRIMARY KEY AUTOINCREMENT, " + " %s TEXT, " + " %s TEXT, " + " %s TEXT, " + " %s TEXT, " + " %s TEXT, " + " %s TEXT " + " ) ", MEMTAB,MEMSEQ,MEMNAME,MEMPW,MEMEMAIL,MEMPHONE,MEMADDR,MEMPHOTO);//SQLite 쿼리문임
            Log.d("실행할 쿼리 :: ",sql);
            db.execSQL(sql);
            Log.d("========================","create쿼리실행완료");
            String[] names = {"","김수현","박보검","수지","아이린","아이유"};
            String[] emails = {"","soohyun@test.com","bogum@test.com","suzy@test.com","irin@test.com","iu@test.com"};
            for(int i = 1; i < 6;i++){
                db.execSQL(String.format(  " INSERT INTO %s " + " (%s , " + " %s , " + " %s , " + " %s , " + " %s , " + " %s  " + " ) VALUES " + " ('%s' , " + " '%s' , " + " '%s' , " + " '%s' , " + " '%s' , " + " '%s' )  ", MEMTAB,MEMNAME,MEMPW,MEMEMAIL,MEMPHONE,MEMADDR,MEMPHOTO,     names[i] , "1" , emails[i] , "010-1234-567"+i , "신촌"+i+"길" , "profile_"+i));
            }
            Log.d("========================","insert쿼리실행완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+MEMTAB);
            onCreate(db);
        }

        @Override
        public SQLiteDatabase getReadableDatabase() {
            return super.getReadableDatabase();
        }

/*        @Override
        public void onOpen(SQLiteDatabase db) {
            db.execSQL(" SELECT * FROM MEMBER ");
            super.onOpen(db);
        }*/
    }
}
