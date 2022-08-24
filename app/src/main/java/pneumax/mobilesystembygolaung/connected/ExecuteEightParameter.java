package pneumax.mobilesystembygolaung.connected;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class ExecuteEightParameter extends AsyncTask<String, Void, String> {


    private Context context;
    public ExecuteEightParameter(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            okhttp3.RequestBody data = new FormBody.Builder()
                    .add( strings[0], strings[1])
                    .add( strings[2], strings[3])
                    .add( strings[4], strings[5])
                    .add( strings[6], strings[7])
                    .add( strings[8], strings[9])
                    .add( strings[10], strings[11])
                    .add( strings[12], strings[13])
                    .add( strings[14], strings[15])
                    .build();
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            builder.url(strings[16]).post(data).build();
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