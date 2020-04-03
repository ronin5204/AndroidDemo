package com.agriculture.farmer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agriculture.farmer.ui.home.HomeActivity;
import com.agriculture.my_library.CircleImageView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Activity_login extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "tcnr19=>";
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private CircleImageView img;
    private EditText edt_username;
    private String imgurl;
    private ArrayList<String> mem_data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlelogin);
        setupviewcomponent();
        signIn();
    }

    private void setupviewcomponent() {
        mStatusTextView = findViewById(R.id.status);
        img = findViewById(R.id.google_icon);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        //連線
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        登錄按鈕
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signOut() {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //--START_EXCLUDE--
                        updateUI(null);
                        // [END_EXCLUDE]
                        img.setImageResource(R.drawable.google); //還原圖示
                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            imgurl = account.getPhotoUrl().toString();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            String g_DisplayName=account.getDisplayName(); //暱稱
            String g_Email=account.getEmail();  //信箱
            imgurl = account.getPhotoUrl().toString();
            mem_data = new ArrayList<String>();
            mem_data.add(g_DisplayName);
            mem_data.add(g_Email);
            mem_data.add(imgurl);
//            String g_GivenName=account.getGivenName(); //Firstname
//            String g_FamilyName=account.getFamilyName(); //Last name
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName())+"\n Email:"+
//                    account.getEmail()+"\n Firstname:"+
//                    account.getGivenName()+"\n Last name:"+
//                    account.getFamilyName()
//            );
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            u_loaddata();
        } else {
            setContentView(R.layout.googlelogin);
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // --START onActivityResult--
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    //--END onActivityResult--

    // --TART handleSignInResult--
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    private void u_savadata(String username) {
        // 儲存SharedPreferences資料
        SharedPreferences gameResultData =
                getSharedPreferences("user", MODE_PRIVATE);
        //呼叫getSharedPreferences()方法，產生一個檔名為GAME_RESULT.xml的設定儲存檔，
        // 並只供本專案(app)可讀取，物件名稱為gameResultData,0就是MODE_PRIVATE ；
        gameResultData
                .edit() //呼叫edit()方法取得編輯器物件，
                // 此時使用匿名方式呼叫Editor的putInt方法，將內容寫入設定檔，資料標籤為”win lose draw等”。
                .putString("username", username)
                .commit();//最後必須呼叫commit()方法，此時資料才真正寫入到設定檔中。

        Toast.makeText(this, "註冊成功！", Toast.LENGTH_LONG).show();
        Intent it = new Intent(this, HomeActivity.class);
        startActivity(it);
    }
    private void u_loaddata() {
        // 載入SharedPreferences資料
        SharedPreferences gameResultData =
                getSharedPreferences("user", 0);

        String username = gameResultData.getString("username", "0"); //沒有的話補0
        if(username.equals("0")){
            setContentView(R.layout.signin001);
            edt_username = (EditText) findViewById(R.id.edt_username);
            Button btn_register = (Button)findViewById(R.id.btn_register);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = edt_username.getText().toString();
                    u_savadata(username);
                }
            });
        }else{
            Intent it = new Intent(this, HomeActivity.class);
            Bundle bd = new Bundle();
//            Log.d("aa", mem_data.get(2)+"");
            bd.putStringArrayList("memdata",mem_data);
            it.putExtra("BUNDLE",bd);
            startActivity(it);
        }
    }
}
