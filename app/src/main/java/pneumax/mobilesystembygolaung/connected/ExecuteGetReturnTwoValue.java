package pneumax.mobilesystembygolaung.connected;


import android.content.Context;
import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class ExecuteGetReturnTwoValue extends AsyncTask<String, Void, String> {

    private Context context;
    public ExecuteGetReturnTwoValue(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            okhttp3.RequestBody data = new FormBody.Builder()
                    .add("strDataBaseName", strings[0])
                    .add("strTable", strings[1])
                    .add("strField1", strings[2])
                    .add("strField2", strings[3])
                    .add("strCondition", strings[4])
                    .build();
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            builder.url(strings[5]).post(data).build();
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