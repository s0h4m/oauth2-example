package cc.soham.enpsectest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.soham.enpsectest.network.EnParadigm;

/**
 * Handles a redirect from a code (auth) request
 */
public class CodeRedirectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        // check if the redirect is from a code request and then store it
        String code = EnParadigm.handleRedirect(getIntent());
        if (code != null) {
            Values.storeCode(this, code);
            finish();
        }
    }
}
