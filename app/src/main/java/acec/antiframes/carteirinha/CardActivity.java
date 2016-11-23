package acec.antiframes.carteirinha;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.content.ContentValues.TAG;

public class CardActivity extends Activity {
    private TextView userName;
    private TextView userCPF;
    private TextView userCNPJ;
    private TextView userType;
    private TextView userDueDate;
    private TextView userCompany;
    private ImageView userPicture;
    private TextView watermark;
    private TextView watermark2;
    private LinearLayout noCardView;

    private RelativeLayout mainLayout;
    private SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mainLayout = (RelativeLayout) findViewById(R.id.card_main_layout);
        //campos de texto
        userName = (TextView) findViewById(R.id.user_name);
        userCPF = (TextView) findViewById(R.id.user_cpf);
        userCNPJ = (TextView) findViewById(R.id.user_cnpj);
        userType = (TextView) findViewById(R.id.user_type);
        userDueDate = (TextView) findViewById(R.id.user_dueDate);
        userCompany = (TextView) findViewById(R.id.user_company);
        userPicture = (ImageView) findViewById(R.id.user_picture);
        noCardView = (LinearLayout) findViewById(R.id.no_card_view);
        watermark = (TextView) findViewById(R.id.watermark);
        watermark2 = (TextView) findViewById(R.id.watermark2);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");

        watermark.setTypeface(myTypeface);
        watermark2.setTypeface(myTypeface);

        Animation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -1f);
        mAnimation.setDuration(10000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.INFINITE);
        mAnimation.setInterpolator(new LinearInterpolator());

        Animation mAnimationHor = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -1f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f);
        mAnimationHor.setDuration(10000);
        mAnimationHor.setRepeatCount(-1);
        mAnimationHor.setRepeatMode(Animation.INFINITE);
        mAnimationHor.setInterpolator(new LinearInterpolator());

        watermark.setAnimation(mAnimation);
        watermark2.setAnimation(mAnimationHor);

        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        User user = getUser();
        if (user==null)
            getCredentials();
        else {
            noCardView.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            fillData(user);
        }
    }

    private User getUser(){
        User user = new User();

        String userName = prefs.getString("userName",null);
        String userCpf = prefs.getString("userCPF",null);
        String userOccupation = prefs.getString("userOccupation",null);
        String userCnpj = prefs.getString("userCNPJ",null);
        String userCompany = prefs.getString("userCompany",null);
        String userDueDate = prefs.getString("userDueDate",null);
        String userPicUrl = prefs.getString("userPicUrl",null);

        try {
            String foo = Utils.encryptString(userName);
            String bar = Utils.decodeString(foo);

            Log.d(TAG, "getUser: " + foo + " " + bar);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (userName==null
                || userCpf==null
                || userPicUrl==null
                || userOccupation==null
                || userCnpj==null
                || userCompany==null
                || userDueDate==null
                )
            return null;

        user.setName(userName);
        user.setCpf(userCpf);
        user.setOccupation(userOccupation);
        user.setCnpj(userCnpj);
        user.setCompany(userCompany);
        user.setDueDate(userDueDate);
        user.setPicUrl(userPicUrl);
        
        

        return user;
    }

    public void update(View v){
        showDialog();
    }

    private void getCredentials(){
        String cpf=prefs.getString("cpf","");
        String password=prefs.getString("pass","");

        if ((cpf.length()>0) && (password.length()>0)) {

            try {
                String dCpf = Utils.decodeString(cpf);
                String dPass = Utils.decodeString(password);
                sendRequest(dCpf, dPass);
            }catch (Exception e){
                showDialog();
            }
        }
        else
            showDialog();
    }

    public void showDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        LoginDialog newFragment = new LoginDialog();
        newFragment.show(ft, "dialog");
    }
    public void sendRequest(String cpf,String pass){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            new GetUserTask().execute(cpf,pass);
        else{
            Toast.makeText(this,"Sem conexão",Toast.LENGTH_SHORT).show();
        }
    }

    private class GetUserTask extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return RSSHelper.getUserFromWebservice(strings[0],strings[1],CardActivity.this);
        }
    }

    public void receiveUser(User user){
        if ((user==null)||(user.getCpf()==null)){
            Toast.makeText(this,"Problemas de conta! Consulte o administrador da sua entidade.",Toast.LENGTH_SHORT).show();
            showDialog();
        }
        else {
            noCardView.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);

            fillData(user);
            saveUser(user);
        }
    }

    private void fillData(User user){
        userName.setText(user.getName());
        String cpfMsg = "CPF: "+user.getCpf();
        userCPF.setText(cpfMsg);
        String cnpjMsg = "CNPJ: "+user.getCnpj();
        userCNPJ.setText(cnpjMsg);
        userType.setText(user.getOccupation());

        String dateMsg = "Validade: "+user.getDueDate();
        String dateMsg2 = "VALIDADE: \n"+user.getDueDate();
        userDueDate.setText(dateMsg);
        userCompany.setText(user.getCompany());

        String[] numDueDate = user.getDueDate().split("/");
        // TODO: 23/11/16 pegar data 

        watermark.setText(dateMsg2);
        watermark2.setText(dateMsg2);


        //Picasso.with(getApplicationContext()).load(user.getPicUrl()).into(userPicture);
        Picasso.with(getApplicationContext()).load(user.getPicUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                userPicture.setImageBitmap(bitmap);
                Utils.saveAvatar(CardActivity.this,bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                userPicture.setImageBitmap(Utils.getAvatar(CardActivity.this));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    private void saveUser(User user){
        prefs.edit().putString("userName",user.getName()).apply();
        prefs.edit().putString("userCPF",user.getCpf()).apply();
        prefs.edit().putString("userCNPJ",user.getCnpj()).apply();
        prefs.edit().putString("userCompany",user.getCompany()).apply();
        prefs.edit().putString("userOccupation",user.getOccupation()).apply();
        prefs.edit().putString("userDueDate",user.getDueDate()).apply();
        prefs.edit().putString("userPicUrl",user.getPicUrl()).apply();
    }

    public void goBack(View v){
        finish();
    }


}
