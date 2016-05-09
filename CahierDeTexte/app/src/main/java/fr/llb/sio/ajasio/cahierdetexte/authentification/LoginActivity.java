package fr.llb.sio.ajasio.cahierdetexte.authentification;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.frau1108gmail.antoine.cahierdetexte.R;



import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {
    public Utilisateurs user;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       final EditText login_entry = (EditText) findViewById((R.id.login_entry));
       final EditText password_entry = (EditText) findViewById((R.id.password_entry));
       password_entry.setBackgroundResource(R.drawable.lost_focus_edit_txt);
       login_entry.setBackgroundResource(R.drawable.lost_focus_edit_txt);
       Button bt_connexion = (Button) findViewById(R.id.bt_connect);

       login_entry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if (hasFocus) {
                   v.setBackgroundResource(R.drawable.focus_edit_txt);

               } else {
                   {
                       hideKeyboard(v);
                       v.setBackgroundResource(R.drawable.lost_focus_edit_txt);
                   }
               }
           }
       });

        bt_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_connexion(login_entry,password_entry);
            }
        });

        password_entry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.focus_edit_txt);
                } else {
                    hideKeyboard(v);
                    v.setBackgroundResource(R.drawable.lost_focus_edit_txt);
                }

            }
        });
        progressDialog = initializeProgressDialog();
    }
    private ProgressDialog initializeProgressDialog(){
        ProgressDialog p = new ProgressDialog(this);
        p.setTitle("Processing...");
        p.setMessage("Please wait.");
        p.setCancelable(false);
        p.setIndeterminate(true);
        return p;
    }

    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public void click_connexion( final EditText login, EditText mdp){

        final String login_str = login.getText().toString();
        final String mdp_str = mdp.getText().toString();
        //AsyncTask est une classe Android permettant de lancer des instructions en tache de fond
        //Tout est gere par cette classe
        AsyncTask<Void, Boolean, Boolean> task = new AsyncTask<Void, Boolean, Boolean>() {

            //Ici on met les instructions avant le background
            //Ces intructions sont donc executees sur l'UI Thread
            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            //La ce sont les instrcutions executees en arriere plan
            @Override
            protected Boolean doInBackground(Void... arg0) {
                boolean res = false;
                //On recupere les controles de l'interface


                user = new Utilisateurs(login_str, mdp_str);

                try {
                    LDAPConnection connexion = ConnexionLDAP.loginLDAP(user);

                    System.out.println(connexion.getConnectionName());

                    Utilisateurs userType = ConnexionLDAP.searchType(connexion, user);



                    if(!userType.type.isEmpty()){
                        user.type=userType.type;
                        String nom = ConnexionLDAP.searchNom(connexion, user);
                        if(!nom.isEmpty()){

                            user.setNom(nom);
                            res = true;
                            connexion.close();
                        }else{
                            res = false;
                        }
                    }
                    else{
                        res = false;
                    }
                }
                catch (LDAPException e) {
                    e.printStackTrace();

                }
                return res;

            }

            //Les instructions sont a nouveau executee sur l'UI Thread a la fin du traitement en arrier plan
            @Override
            protected void onPostExecute(Boolean result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (result){
                    //On demarre une nouvelle activite
                    //Toast.makeText(getApplicationContext(), user.getNom(), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(LoginActivity.this, cahierdetexteleve.class);
                    intent.putExtra("nom", user.getNom());
                    startActivity(intent);*/
                    Toast.makeText(getApplicationContext(), "Réussite !"+user.getNom(), Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Erreur connexion", Toast.LENGTH_SHORT).show();
                }
            }

        };

        //On lance la classe AsyncTask
        task.execute((Void[]) null);

    }

}
