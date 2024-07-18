package retrofit2;


import org.jetbrains.annotations.Nullable;

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
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import okhttp3.Request;

public final class MonitorCallAdapterFactory extends CallAdapter.Factory {
    private final @Nullable Executor callbackExecutor;
    private final @Nullable Function3<String, Exception, Integer, Unit> errorHandler;
    private final @Nullable Function1<Integer, Unit> successHandler;

    public interface ErrorHandler extends Function3<String, Exception, Integer, Unit> {
    }

    public interface SuccessHandler extends Function1<Integer, Unit> {
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor,
                              @Nullable Function3<String, Exception, Integer, Unit> errorHandler,
                              @Nullable Function1<Integer, Unit> successHandler
    ) {
        this.callbackExecutor = callbackExecutor;
        this.errorHandler = errorHandler;
        this.successHandler = successHandler;
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable Function3<String, Exception, Integer, Unit> errorHandler) {
        this(callbackExecutor, errorHandler, null);
    }

    public MonitorCallAdapterFactory(@Nullable Executor callbackExecutor, @Nullable ErrorHandler errorHandler) {
        this(callbackExecutor, errorHandler, null);
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
                    if (errorHandler != null || successHandler != null) {
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

                                                        onError(request, exception, okHttpCall);
                                                    }
                                                    if (method.getName().equals("onResponse") && args.length == 2 && args[0] instanceof OkHttpCall && args[1] instanceof Response) {
                                                        OkHttpCall okHttpCall = (OkHttpCall) args[0];
                                                        Request request = okHttpCall.request();
                                                        Response response = (Response) args[1];
                                                        if (response.code() != 200) {
                                                            onError(request, new HttpException(response), okHttpCall);
                                                        } else {
                                                            onSuccess(request, okHttpCall);
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

    private void onError(Request request, Exception exception, OkHttpCall okHttpCall) {
        if (errorHandler != null) {
            errorHandler.invoke(request.toString(), exception, request.hashCode());
        }
    }

    /**
     * 请求成功时的回调
     * 这里是为了删除网络监控中的对应的数据记录
     * <p>
     *
     * @param request
     * @param okHttpCall
     * @throws NoSuchFieldException
     */
    private void onSuccess(Request request, OkHttpCall okHttpCall) {
        if (successHandler != null) {
            successHandler.invoke(request.hashCode());
        }
    }

    private static int getRawCallHashCode(OkHttpCall okHttpCall) {
        try {
            Field rawCall = OkHttpCall.class.getDeclaredField("rawCall");
            rawCall.setAccessible(true);
            return (rawCall.get(okHttpCall)).hashCode();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
