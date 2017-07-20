package com.guster.rxandroiddemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RxAndroidUnitTest {

    private Random rnd = new Random();
    private OkHttpClient httpClient = new OkHttpClient.Builder().build();

    @Before
    public void init() {
        Util.logd("Begin testing");
    }

    @Test
    public void testSimple() {
        Util.logd("--- Simple Test ---");

        final String[] values = {"one", "two", "three", "delta", "rain", "kerry", "wilson", "ivan", "sexy", "vertical"};
        Observer observer = new Observer() {
            int count = 0;

            @Override
            public void onSubscribe(Disposable d) {
                Util.logd("emit -> onSubscribe");
            }

            @Override
            public void onNext(Object value) {
                Util.logd("emit -> onNext: " + value);
                count++;
            }

            @Override
            public void onError(Throwable e) {
                Util.logd("emit -> onError");
            }

            @Override
            public void onComplete() {
                Util.logd("emit -> onComplete");
                assertEquals("all values should be emitted", values.length, count);
            }
        };
        Observable<String> observable = Observable.fromArray(values);
        observable.delay(3, TimeUnit.SECONDS);
        observable.subscribe(observer);
    }

    @Test
    public void testAsync() {
        Util.logd("--- Async Test ---");

        final List<UrlItem> items = new ArrayList<>();
        items.add(new UrlItem("facebook", "https://www.facebook.com"));
        items.add(new UrlItem("google", "https://www.google.com"));
        items.add(new UrlItem("twitter", "https://www.twitter.com"));
        items.add(new UrlItem("baidu", "https://www.baidu.com"));
        items.add(new UrlItem("digital ocean", "https://cloud.digitalocean.com"));
        items.add(new UrlItem("stack overflow", "https://www.stackoverflow.com"));
        items.add(new UrlItem("invalid url", "https://oqwoqwqow.com"));

        Observable<UrlItem> loadUrlTask = Observable.create(new ObservableOnSubscribe<UrlItem>() {
            @Override
            public void subscribe(ObservableEmitter<UrlItem> e) throws Exception {
                for(UrlItem urlItem : items) {
                    Request request = new Request.Builder().url(urlItem.url).get().build();
                    Response response = httpClient.newCall(request).execute();
                    if(response.isSuccessful()) {
                        e.onNext(urlItem);
                    } else {
                        e.onError(new Exception("Unable to load: " + urlItem.name));
                    }
                }
                e.onComplete();
            }
        });
        Observer<UrlItem> observer = new Observer<UrlItem>() {
            int count = 0;

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UrlItem value) {
                Util.logd("URL loaded complete: " + value.name + " from " + value.url);
                count++;
            }

            @Override
            public void onError(Throwable e) {
                Util.logd(e.getMessage());
            }

            @Override
            public void onComplete() {
                assertEquals("All url loaded", items.size(), count);
            }
        };
        loadUrlTask.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        //loadUrlTask.subscribeOn(Schedulers.newThread());
        //loadUrlTask.observeOn(AndroidSchedulers.mainThread());
        loadUrlTask.subscribe(observer);
    }

    private long getRandomDelay() {
        return rnd.nextInt(3000);
    }

    @After
    public void complete() {
        Util.logd("Test completed");
    }

    public Context getContext() throws Exception {
        return InstrumentationRegistry.getTargetContext();
    }
}
