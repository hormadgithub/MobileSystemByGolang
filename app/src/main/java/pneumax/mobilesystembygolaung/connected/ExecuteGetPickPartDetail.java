package pneumax.mobilesystembygolaung.connected;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class ExecuteGetPickPartDetail extends AsyncTask<String, Void, String> {

    private Context context;
    public ExecuteGetPickPartDetail(Context context) {
        this.context = context;
    }

        @Override
        protected String doInBackground(String... strings) {
        try {
            okhttp3.RequestBody data = new FormBody.Builder()
                    .add("strDataBaseName", strings[0])
                    .add("strDocno", strings[1])
                    .add("strPartnid", strings[2])
                    .build();
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            builder.url(strings[3]).post(data).build();
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