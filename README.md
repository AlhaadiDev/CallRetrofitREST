# CallRetrofitREST
show how to call REST JSON using RETROFIT



Retrofit

**1. Declare Constant variables**

    public static String SERVER_DOMAIN = "you link here";
    public static String SERVER = SERVER_DOMAIN + "";

**2. Create preferences**

    public class LocalData {
    private static LocalData localData;
    private static SharedPreferences preferences;

    public static final String SP_KEY_ACCESS_TOKEN = "access_token";

    private LocalData(){
        preferences = MyApplication.getInstance().getApplicationContext().getSharedPreferences("",Context.MODE_PRIVATE);
    }

    public static LocalData getInstance(){
        if (localData == null){
            localData = new LocalData();
        }
        return localData;
    }

    public void saveString(String key, String value){
        preferences.edit().putString(key, value).apply();
    }

    public String loadString(String key){
        return preferences.getString(key,"");
    }
}

**3. Create one class to extend Application**

    public class MyApplication extends Application {
    private static MyApplication myApplication;

    public static MyApplication getInstance(){
        return myApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}

**4. Retrofit libraries**

    implementation 'com.squareup.picasso:picasso:2.5.2'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    implementation 'com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.9.0'

**5. Create Retrofit class**

    public class RetrofitRequest {
    private static Retrofit retrofit;
    private static Retrofit retrofit2;

    public static Retrofit getRetrofitRequest() {
        if (retrofit2 == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.interceptors().add(interceptor);
            OkHttpClient client = builder.build();
            retrofit2= new Retrofit.Builder()
                    .baseUrl(Utils.SERVER)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }

    public static Retrofit getRetrofitToken() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.SERVER_DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

**6. Create ApiService Interface**

    public interface ApiServiceInterface {

    @POST("/oauth/token")
    Call<JsonObject> postAccessToken(@Body HashMap<String, String> inputParams);

    @POST("/auth/login")
    Call<JsonObject> UserLogin(@Body HashMap<String, String> inputParams);

    @GET("users/{id}")
    Call<JsonObject> userDetails(@Header("Authorization") String header, @Path("id") String id);
}

**7. Call the API and get information**

     private void getUser() {
        MyProperties.ShowLogD("onclick","getUser");
        ApiServiceInterface apiSevice = RetrofitRequest.getRetrofitRequest().create(ApiServiceInterface.class);
        Call<JsonObject> call = apiSevice.userDetails("Bearer " +                  LocalData.getInstance().loadString(LocalData.SP_KEY_ACCESS_TOKEN),"139");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    MyProperties.ShowLogD("token",response.body().toString());
                    JSONObject jsonData = new JSONObject(response.body().toString());
                    String name = jsonData.getString("name");
                    txName.setText(name);
                } catch (Throwable r) {
                    r.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

*****************
