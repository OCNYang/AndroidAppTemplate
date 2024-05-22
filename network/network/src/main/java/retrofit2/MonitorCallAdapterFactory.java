package retrofit2;

import androidx.annotation.Nullable;

import com.app.network.error.ExceptionHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.Executor;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import okhttp3.Request;

public final class MonitorCallAdapterFactory extends CallAdapter.Factory {
    private final @Nullable Executor callbackExecutor;
    private final @Nullable Function3<String, Exception, Integer, Unit> errorHandler;
    private final @Nullable Function2<String, Exception, Unit> simpleErrorHandler;

    public interface ErrorHandler extends Function3<String, Exception, Integer, Unit> {
    }

    public interface SimpleErrorHandler extends Function2<String, Exception, Unit> {
    }

    private MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable Function3<String, Exception, Integer, Unit> errorHandler, @Nullable Function2<String, Exception, Unit> simpleErrorHandler) {
        this.callbackExecutor = callbackExecutor;
        this.errorHandler = errorHandler;
        this.simpleErrorHandler = simpleErrorHandler;
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable Function3<String, Exception, Integer, Unit> errorHandler) {
        this(callbackExecutor, errorHandler, null);
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable ErrorHandler errorHandler) {
        this(callbackExecutor, errorHandler, null);
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable SimpleErrorHandler simpleErrorHandler) {
        this(callbackExecutor, null, simpleErrorHandler);
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable Function2<String, Exception, Unit> simpleErrorHandler) {
        this(callbackExecutor, null, simpleErrorHandler);
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor) {
        this(callbackExecutor, null, null);
    }

    @Override
    public @Nullable CallAdapter<?, ?> get(
            Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Call.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        final Type responseType = Utils.getParameterUpperBound(0, (ParameterizedType) returnType);

        final Executor executor =
                Utils.isAnnotationPresent(annotations, SkipCallbackExecutor.class)
                        ? null
                        : callbackExecutor;

        return new CallAdapter<Object, Call>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public Call adapt(Call<Object> call) {
                if (executor == null) {
                    if (errorHandler != null || simpleErrorHandler != null) {
                        return (Call) Proxy.newProxyInstance(
                                Call.class.getClassLoader(),
                                new Class[]{Call.class},
                                (proxy, method, args) -> {
                                    if (method.getName().equals("enqueue") && args.length > 0) {
                                        Object arg0 = args[0];
                                        if (arg0 instanceof Callback) {
                                            Callback callback = (Callback) arg0;
                                            Callback proxyCallback = (Callback) Proxy.newProxyInstance(Callback.class.getClassLoader(), new Class[]{Callback.class}, new InvocationHandler() {
                                                @Override
                                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                                    if (method.getName().equals("onFailure") && args.length == 2 && args[0] instanceof OkHttpCall && args[1] instanceof Exception) {
                                                        OkHttpCall okHttpCall = (OkHttpCall) args[0];
                                                        Request request = okHttpCall.request();
                                                        Exception exception = (Exception) args[1];

                                                        errorHandler(request, exception, okHttpCall);
                                                    }
                                                    if (method.getName().equals("onResponse") && args.length == 2 && args[0] instanceof OkHttpCall && args[1] instanceof retrofit2.Response) {
                                                        OkHttpCall okHttpCall = (OkHttpCall) args[0];
                                                        Request request = okHttpCall.request();
                                                        retrofit2.Response response = (retrofit2.Response) args[1];
                                                        if (response.code() != 200) {
                                                            errorHandler(request, new HttpException(response), okHttpCall);
                                                        }
                                                    }
                                                    return method.invoke(callback, args);
                                                }
                                            });

                                            return method.invoke(call, new Object[]{proxyCallback});
                                        }
                                    }
                                    return Objects.requireNonNull(method.invoke(call, args));
                                }
                        );
                    }
                    return call;
                } else {
                    return new DefaultCallAdapterFactory.ExecutorCallbackCall<>(executor, call);
                }
            }
        };
    }

    private void errorHandler(Request request, Exception exception, OkHttpCall okHttpCall) throws NoSuchFieldException {

        if (simpleErrorHandler != null) {
            simpleErrorHandler.invoke(request.toString(), exception);
        }
        if (errorHandler != null) {
            Field rawCall = OkHttpCall.class.getDeclaredField("rawCall");
            rawCall.setAccessible(true);
            int rawCallHashCode = 0;
            try {
                rawCallHashCode = ((okhttp3.Call) rawCall.get(okHttpCall)).hashCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            errorHandler.invoke(request.toString(), exception, rawCallHashCode);
        }
    }
}
