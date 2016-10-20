package acec.antiframes.carteirinha;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CardActivity extends Activity {
    private TextView userName;
    private TextView userCPF;
    private TextView userCNPJ;
    private TextView userType;
    private TextView userDueDate;
    private TextView userCompany;
    private ImageView userPicture;
    private TextView watermark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //campos de texto
        userName = (TextView) findViewById(R.id.user_name);
        userCPF = (TextView) findViewById(R.id.user_cpf);
        userCNPJ = (TextView) findViewById(R.id.user_cnpj);
        userType = (TextView) findViewById(R.id.user_type);
        userDueDate = (TextView) findViewById(R.id.user_dueDate);
        userCompany = (TextView) findViewById(R.id.user_company);
        userPicture = (ImageView) findViewById(R.id.user_picture);
        watermark = (TextView) findViewById(R.id.watermark);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        watermark.setTypeface(myTypeface);
        Animation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -1f);
        mAnimation.setDuration(10000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.INFINITE);
        mAnimation.setInterpolator(new LinearInterpolator());
        watermark.setAnimation(mAnimation);
        sendRequest();
        //getUser();
    }

    private void sendRequest(){
        String cpf = "90317769049";
        String pass = "000000";
        new GetUserTask().execute(cpf,pass);
    }

    private class GetUserTask extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return RSSHelper.getUserFromWebservice(strings[0],strings[1],CardActivity.this);
        }
    }

    public void receiveUser(User user){
        fillData(user);
    }

    private void getUser(){
        User user = new User("Antônio Augusto Liberato",
                "000.000.000-00",
                "000.000.000/0000-00",
                "Sócio",
                "01/01/2020",
                "Satélite Online",
                "http://www.tribunapr.com.br/wp-content/uploads/sites/1/2013/06/12-06-13_gugu.jpg");
        fillData(user);
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
        watermark.setText(dateMsg2);

        Picasso.with(getApplicationContext()).load(user.getPicUrl()).into(userPicture);

    }

    public void goBack(View v){
        finish();
    }


}
