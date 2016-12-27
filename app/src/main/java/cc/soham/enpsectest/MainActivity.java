package cc.soham.enpsectest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.soham.enpsectest.network.EnParadigm;
import cc.soham.enpsectest.network.model.GetResourceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        ButterKnife.bind(this);
    }

    private void initialize() {
        Timber.plant(new Timber.DebugTree());
    }

    @OnClick(R.id.button_auth)
    public void authOnClick(View view) {
        EnParadigm.launchAuthUrlIfNeeded(this);
    }

    @OnClick(R.id.button_get_resource)
    public void getResource(View view) {
        Timber.tag(TAG).d("getResource");
        Call<GetResourceResponse> resourceCall = EnParadigm.getResource(this);
        resourceCall.enqueue(new Callback<GetResourceResponse>() {
            @Override
            public void onResponse(Call<GetResourceResponse> call, Response<GetResourceResponse> response) {
                // message received successfully
                GetResourceResponse resource = response.body();
                if (resource != null) {
                    Toast.makeText(MainActivity.this, "Success " + resource.getMessage() + "," + resource.getSuccess(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetResourceResponse> call, Throwable t) {

            }
        });
    }
}
