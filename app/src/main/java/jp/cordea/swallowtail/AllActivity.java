package jp.cordea.swallowtail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.cordea.swallowtail.adapter.AllListAdapter;
import jp.cordea.swallowtail.api.JenkinsRepository;
import jp.cordea.swallowtail.api.JenkinsRssClient;
import jp.cordea.swallowtail.model.Entry;
import jp.cordea.swallowtail.observable.OnItemClickObservable;
import jp.cordea.swallowtail.observable.OnRefreshObservable;
import jp.cordea.swallowtail.viewmodel.AllListItemViewModel;

public class AllActivity extends AppCompatActivity {

    private static final int ITEM_CLICK_WAIT_MILLIS = 1000;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.list_view)
    ListView listView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Repository<Result<List<AllListItemViewModel>>> fetchRepository;

    private Repository<Result<Intent>> itemClickRepository;

    private JenkinsRepository jenkinsRepository;

    private Updatable fetchUpdatable;

    private Updatable itemClickUpdatable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final AllListAdapter adapter = new AllListAdapter(this);
        listView.setAdapter(adapter);
        OnItemClickObservable itemClickObservable = new OnItemClickObservable();
        listView.setOnItemClickListener(itemClickObservable);

        OnRefreshObservable observable = new OnRefreshObservable();
        refreshLayout.setOnRefreshListener(observable);

        jenkinsRepository = new JenkinsRepository(
                observable,
                JenkinsRssClient.getJenkinsService(this).getAllBuild()
        );

        final Context context = this;
        fetchRepository =
                Repositories
                        .repositoryWithInitialValue(Result.<List<AllListItemViewModel>>absent())
                        .observe(jenkinsRepository)
                        .onUpdatesPerLoop()
                        .getFrom(jenkinsRepository)
                        .check(Result::isPresent)
                        .orSkip()
                        .transform(Result::get)
                        .thenTransform(input -> {
                            List<AllListItemViewModel> vms = new ArrayList<>();
                            for (Entry e : input) {
                                vms.add(new AllListItemViewModel(context, e));
                            }
                            return Result.success(vms);
                        })
                        .compile();

        itemClickRepository =
                Repositories
                        .repositoryWithInitialValue(Result.<Intent>absent())
                        .observe(itemClickObservable)
                        .onUpdatesPer(ITEM_CLICK_WAIT_MILLIS)
                        .attemptGetFrom(itemClickObservable)
                        .orSkip()
                        .transform(adapter::getItem)
                        .thenTransform(input -> Result.success(new Intent(Intent.ACTION_VIEW, input.getUri())))
                        .compile();

        fetchUpdatable = () -> fetchRepository.get()
                .ifSucceededSendTo(value -> {
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                        refreshLayout.setVisibility(View.VISIBLE);
                    }
                    refreshLayout.setRefreshing(false);
                })
                .ifSucceededSendTo(adapter)
                .ifFailedSendTo(value -> refreshLayout.setRefreshing(false));

        itemClickUpdatable = () ->
                itemClickRepository.get()
                        .ifSucceededSendTo(value -> itemClickObservable.reset())
                        .ifSucceededSendTo(this::startActivity);

    }

    @Override
    public void onResume() {
        super.onResume();
        jenkinsRepository.addUpdatable();
        itemClickRepository.addUpdatable(itemClickUpdatable);
        fetchRepository.addUpdatable(fetchUpdatable);
    }

    @Override
    public void onPause() {
        itemClickRepository.removeUpdatable(itemClickUpdatable);
        fetchRepository.removeUpdatable(fetchUpdatable);
        jenkinsRepository.removeUpdatable();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
