package cu.phibrain.plugins.cardinal.io.network.api.interceptors;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cu.phibrain.plugins.cardinal.io.network.api.APIError;
import cu.phibrain.plugins.cardinal.io.utils.GsonHelper;
import okhttp3.Interceptor;
import okhttp3.Request;

public class ErrorInterceptor implements Interceptor {
    @NotNull
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);

        // todo deal with the issues the way you need to
        if (response.code() >= 400 && response.code() <= 500) {
            throw new IOException(GsonHelper.createJSONStringFromPojo(new APIError(response.code(), response.message())));
        }

        return response;
    }
}
