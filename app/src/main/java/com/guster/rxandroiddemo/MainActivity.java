package com.guster.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    public enum Event {
        SYSTEM_TYPING, USER_TYPING, SYSTEM_STOPPED_TYPING, USER_STOPPED_TYPING
    }

    private RecyclerView listData;
    private EditText editText;
    private Button btnSend;
    private TextView txtTooltip;
    private boolean isSystemTyping;

    private Observer<Event> eventObserver = new Observer<Event>() {
        @Override
        public void onSubscribe(Disposable d) {}
        @Override
        public void onNext(Event event) {
            switch(event) {
                case SYSTEM_TYPING:
                    txtTooltip.setText("System is typing...");
                    break;
                case USER_TYPING:
                    txtTooltip.setText("You are typing...");
                    break;
                case USER_STOPPED_TYPING:
                    if(isSystemTyping)
                        onNext(Event.SYSTEM_TYPING);
                    else
                        txtTooltip.setText("");
                    break;
                case SYSTEM_STOPPED_TYPING:
                    txtTooltip.setText("");
                    break;
                default:
                    txtTooltip.setText("");
                    break;
            }
        }
        @Override
        public void onError(Throwable e) {}
        @Override
        public void onComplete() {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listData = (RecyclerView) findViewById(R.id.list_data);
        editText = (EditText) findViewById(R.id.edit_text);
        btnSend = (Button) findViewById(R.id.btn_send);
        txtTooltip = (TextView) findViewById(R.id.txt_tooltip);

        init();
    }

    private void init() {
        initInfiniteTimer();
        initRecyclerView();
        initView();
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        llm.setStackFromEnd(true);
        listData.setLayoutManager(llm);

        MyAdapter adapter = new MyAdapter(getApplicationContext());
        listData.setAdapter(adapter);

        RxRecyclerView.scrollEvents(listData).subscribe(new Consumer<RecyclerViewScrollEvent>() {
            @Override
            public void accept(RecyclerViewScrollEvent e) throws Exception {

            }
        });
    }

    private void initView() {
        // listening to chatbox text change event
        RxTextView.textChanges(editText)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if(charSequence.toString().equals("")) return;
                        eventObserver.onNext(Event.USER_TYPING);
                    }
                });
        RxTextView.afterTextChangeEvents(editText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void accept(TextViewAfterTextChangeEvent e) throws Exception {
                        eventObserver.onNext(Event.USER_STOPPED_TYPING);
                    }
                });

        // listen to button event
        RxView.clicks(btnSend).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                // insert new message into list view
                String msg = editText.getText().toString();
                if(msg.equals("")) return;

                addMessage(ListItem.TYPE_USER, msg);
                editText.setText("");
                eventObserver.onNext(Event.USER_STOPPED_TYPING);
            }
        });
    }

    /**
     * Set a repeating timer to simulate bot messaging
     */
    private void initInfiniteTimer() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long l) throws Exception {
                        if(l == 0) return;
                        if(l % 7 == 0) {
                            isSystemTyping = true;
                            eventObserver.onNext(Event.SYSTEM_TYPING);
                        } else if(l % 10 == 0) {
                            // insert new system message into list view
                            isSystemTyping = false;
                            addMessage(ListItem.TYPE_SYSTEM, "[System] Oh yea the timer is now at " + l + ". Exciting neh");
                            eventObserver.onNext(Event.SYSTEM_STOPPED_TYPING);
                        }
                    }
                });
    }

    /**
     * Append a text message to the recycler view
     * @param type
     * @param message
     */
    private void addMessage(int type, String message) {
        final MyAdapter adapter = (MyAdapter) listData.getAdapter();
        int position = adapter.addItem(new ListItem(type, message));
        adapter.notifyItemInserted(position);

        listData.post(new Runnable() {
            @Override
            public void run() {
                // check if the list is at the bottom most position
                LinearLayoutManager lm = (LinearLayoutManager) listData.getLayoutManager();
                int lastPos = lm.findLastCompletelyVisibleItemPosition() + 1;
                int lastIndex = adapter.getItemCount() - 1;
                if(lastPos == lastIndex) {
                    int y = listData.getHeight();
                    listData.smoothScrollBy(0, y, new AccelerateDecelerateInterpolator());
                } else {
                    listData.smoothScrollToPosition(adapter.getItemCount());
                }
            }
        });
    }
}
