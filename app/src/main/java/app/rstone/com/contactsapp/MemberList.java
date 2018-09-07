package app.rstone.com.contactsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static app.rstone.com.contactsapp.Main.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context ctx = MemberList.this;
        ItemList query = new ItemList(ctx);
        ItemDelete delquery = new ItemDelete(ctx);
        ListView memberList = findViewById(R.id.memberList);
        memberList.setAdapter(new MemberAdapter(ctx,(ArrayList<Member>) new ListService(){
            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform()));
        memberList.setOnItemClickListener(
                (AdapterView<?> p, View v, int i, long l)->{
                    Intent intent = new Intent(ctx,MemberDetail.class);
                    Member m = (Member) memberList.getItemAtPosition(i);
                    intent.putExtra("seq",""+m.seq);
                    startActivity(intent);
                }
        );
        memberList.setOnItemLongClickListener(
                (AdapterView<?> p, View v, int i, long l)->{
                    Main.Member m = (Main.Member) memberList.getItemAtPosition(i);
                    new AlertDialog.Builder(ctx)
                            .setTitle("DELETE")
                            .setMessage("정말로 삭제할까요?")
                            .setPositiveButton(
                                    android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            delquery.id = String.valueOf(m.seq);
                                            Toast.makeText(ctx,"삭제완료!",Toast.LENGTH_LONG).show();
                                            delquery.execute();
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    android.R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(ctx,"삭제취소!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                            ).show();
                    Toast.makeText(ctx,"길게눌렀다!",Toast.LENGTH_LONG).show();
                    return true;
                }
        );

    }
    private class ListQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;

        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }

    }
    private class ItemList extends ListQuery{

        public ItemList(Context ctx) {
            super(ctx);
        }
        public ArrayList<Member> execute(){
            ArrayList<Member> lst = new ArrayList<>();
            Cursor cursor = this.getDatabase()
                    .rawQuery(" SELECT * FROM MEMBER ",null);
            Member member = null;
            if(cursor!=null){
                    while (cursor.moveToNext()) {//name, pw, email, phone, addr, photo
                        member = new Member();
                        member.seq = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEMSEQ)));
                        member.name = cursor.getString(cursor.getColumnIndex(MEMNAME));
                        member.pw = cursor.getString(cursor.getColumnIndex(MEMPW));
                        member.email = cursor.getString(cursor.getColumnIndex(MEMEMAIL));
                        member.phone = cursor.getString(cursor.getColumnIndex(MEMPHONE));
                        member.addr = cursor.getString(cursor.getColumnIndex(MEMADDR));
                        member.photo = cursor.getString(cursor.getColumnIndex(MEMPHOTO));
                        lst.add(member);
                    }
                Log.d("등록된 회원수가", lst.size()+"");
                }else{
                    Log.d("등록된 회원이","없습니다.");
                }
            return lst;
        }
    }
    private class DeleteQuery extends QueryFactory{
        SQLiteHelper helper;
        public DeleteQuery(Context ctx) {
            super(ctx);
            helper = new SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemDelete extends DeleteQuery{
        Member m ;
        String id;
        public ItemDelete(Context ctx) {
            super(ctx);
            m = new Member();
        }
        public void execute(){
            this.getDatabase()
                    .execSQL(String.format(" DELETE FROM %s WHERE %s = '%s' " , MEMTAB, MEMSEQ, id));
        }
    }



    private class MemberAdapter extends BaseAdapter{


        ArrayList<Member> list;
        LayoutInflater inflater;//자전거 타이어 바람넣는거

        public MemberAdapter(Context ctx, ArrayList<Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(ctx);
        }
        private int[] photos = {
                R.drawable.profile_1,
                R.drawable.profile_2,
                R.drawable.profile_3,
                R.drawable.profile_4,
                R.drawable.profile_5
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.member_item, null);
                holder = new ViewHolder();
                holder.profile = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.profile.setImageResource(photos[i]);
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);

            return v;
        }
    }
    static class ViewHolder{
        ImageView profile;
        TextView name,phone;
    }
}


