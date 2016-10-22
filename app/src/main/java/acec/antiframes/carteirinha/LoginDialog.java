package acec.antiframes.carteirinha;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;


public class LoginDialog extends DialogFragment {
    private FloatingActionButton fab;

    public LoginDialog(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v= inflater.inflate(R.layout.dialog_login,null);

        fab= (FloatingActionButton) v.findViewById(R.id.button_ok);
        final EditText cpfField = (EditText) v.findViewById(R.id.field_cpf);
        final EditText passwordField = (EditText) v.findViewById(R.id.field_password);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cpf = cpfField.getText().toString(); //"90317769049";
                String pass = passwordField.getText().toString(); //"000000";
                ((CardActivity) getActivity()).sendRequest(cpf,pass);
                LoginDialog.this.dismiss();

                SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
                prefs.edit().putString("cpf",cpf).apply();
                prefs.edit().putString("pass",pass).apply();

                // TODO: 22/10/16 CRIPTOGRAFAR DADOS
            }
        });



        builder.setView(v);
        return builder.create();
    }
}
