package jp.cordea.swallowtail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.cordea.swallowtail.adapter.MainPageListAdapter;
import jp.cordea.swallowtail.api.JenkinsRepository;
import jp.cordea.swallowtail.api.JenkinsRssClient;
import jp.cordea.swallowtail.model.Entry;
import jp.cordea.swallowtail.observable.OnRefreshObservable;
import jp.cordea.swallowtail.viewmodel.MainPageListItemViewModel;

public class MainPageFragment extends Fragment {

    private static final String TYPE_KEY = "TypeKey";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Repository<Result<List<MainPageListItemViewModel>>> repository;

    private JenkinsRepository jenkinsRepository;

    private Updatable updatable;

    public MainPageFragment() {
        // Required empty public constructor
    }

    public static MainPageFragment newInstance(BuildType type) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_KEY, type.name());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MainPageListAdapter adapter = new MainPageListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        OnRefreshObservable observable = new OnRefreshObservable();
        refreshLayout.setOnRefreshListener(observable);

        BuildType type = BuildType.valueOf(getArguments().getString(TYPE_KEY, BuildType.LATEST.name()));
        if (type == BuildType.LATEST) {
            jenkinsRepository =
                    new JenkinsRepository(observable,
                            JenkinsRssClient.getJenkinsService(getContext()).getLatestBuild()
                    );
        } else {
            jenkinsRepository =
                    new JenkinsRepository(observable,
                            JenkinsRssClient.getJenkinsService(getContext()).getFailedBuild()
                    );
        }

        repository =
                Repositories
                        .repositoryWithInitialValue(Result.<List<MainPageListItemViewModel>>absent())
                        .observe(jenkinsRepository)
                        .onUpdatesPerLoop()
                        .getFrom(jenkinsRepository)
                        .check(Result::isPresent)
                        .orSkip()
                        .transform(Result::get)
                        .thenTransform(input -> {
                            List<MainPageListItemViewModel> vms = new ArrayList<>();
                            for (Entry e : input) {
                                vms.add(new MainPageListItemViewModel(getContext(), e));
                            }
                            return Result.success(vms);
                        })
                        .compile();

        updatable = () ->
                repository.get()
                        .ifSucceededSendTo(value -> {
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                            }
                            refreshLayout.setRefreshing(false);
                        })
                        .ifSucceededSendTo(adapter)
                        .ifFailedSendTo(value -> refreshLayout.setRefreshing(false));

    }

    @Override
    public void onResume() {
        super.onResume();
        jenkinsRepository.addUpdatable();
        repository.addUpdatable(updatable);
    }

    @Override
    public void onPause() {
        jenkinsRepository.removeUpdatable();
        repository.removeUpdatable(updatable);
        super.onPause();
    }

}
