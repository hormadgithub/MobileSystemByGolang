package pneumax.mobilesystembygolaung.connected;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class ExecuteTwoParameter extends AsyncTask<String, Void, String> {


    private Context context;
    public ExecuteTwoParameter(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            okhttp3.RequestBody data = new FormBody.Builder()
                    .add( strings[0], strings[1])
                    .add( strings[2], strings[3])
                    .build();

            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            builder.url(strings[4]).post(data).build();
            okhttp3.Request request = builder.build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor())
                    .build();
            okhttp3.Response response = client.newCall(request).execute();
            String result = response.body().string();
            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}