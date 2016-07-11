package jp.cordea.swallowtail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.cordea.swallowtail.model.Url;
import jp.cordea.swallowtail.observable.OnClickObservable;
import jp.cordea.swallowtail.observable.OnTextChangedObservable;

public class LoginActivity extends AppCompatActivity {

    private static final int BUTTON_CLICK_WAIT_MILLIS = 1000;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.login_button)
    Button button;

    @BindView(R.id.url_edit_text)
    EditText editText;

    private Repository<Result<Intent>> buttonRepository;

    private Updatable buttonUpdatable;

    private Repository<Result<Boolean>> editTextRepository;

    private Updatable editTextUpdatable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final Context context = this;

        final OnClickObservable onClickObservable = new OnClickObservable();
        final OnTextChangedObservable onTextChangedObservable = new OnTextChangedObservable();

        if (!Url.getUrl(this).isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        buttonRepository =
                Repositories
                        .repositoryWithInitialValue(Result.<Intent>absent())
                        .observe(onClickObservable)
                        .onUpdatesPer(BUTTON_CLICK_WAIT_MILLIS)
                        .attemptGetFrom(() -> {
                            String query = editText.getText().toString();
                            return query.length() == 0 ? Result.absent() : Result.success(query);
                        })
                        .orSkip()
                        .thenTransform(input -> {
                            if (Url.setUrl(input, context)) {
                                return Result.success(new Intent(context, MainActivity.class));
                            }
                            return Result.failure();
                        })
                        .notifyIf((oldVal, newVal) -> !newVal.isPresent() || !oldVal.equals(newVal))
                        .compile();

        editTextRepository =
                Repositories
                        .repositoryWithInitialValue(Result.<Boolean>absent())
                        .observe(onTextChangedObservable)
                        .onUpdatesPerLoop()
                        .thenGetFrom(() -> Result.success(editText.getText().length() > 0))
                        .compile();

        editTextUpdatable = () ->
                editTextRepository.get()
                        .ifSucceededSendTo(value -> button.setClickable(value));

        buttonUpdatable = () ->
                buttonRepository.get()
                        .ifFailedSendTo(value -> showDialog())
                        .ifSucceededSendTo(value -> {
                            button.setClickable(false);
                            startActivity(value);
                            finish();
                        });

        button.setOnClickListener(onClickObservable);
        editText.addTextChangedListener(onTextChangedObservable);

    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.login_failed_dialog_title)
                .setMessage(R.string.login_failed_dialog_message)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextRepository.addUpdatable(editTextUpdatable);
        buttonRepository.addUpdatable(buttonUpdatable);
    }

    @Override
    protected void onPause() {
        editTextRepository.removeUpdatable(editTextUpdatable);
        buttonRepository.removeUpdatable(buttonUpdatable);
        super.onPause();
    }
}
