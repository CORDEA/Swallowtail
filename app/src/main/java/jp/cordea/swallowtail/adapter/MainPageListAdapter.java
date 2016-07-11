package jp.cordea.swallowtail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.agera.Receiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.cordea.swallowtail.R;
import jp.cordea.swallowtail.viewmodel.MainPageListItemViewModel;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */

public class MainPageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Receiver<List<MainPageListItemViewModel>> {

    private Context context;
    private List<MainPageListItemViewModel> viewModels = new ArrayList<>();

    public MainPageListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void accept(@NonNull List<MainPageListItemViewModel> value) {
        viewModels.clear();
        viewModels.addAll(value);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_main_page, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainPageListItemViewModel viewModel = viewModels.get(position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(viewModel.getTitle());
            viewHolder.subTitle.setText(viewModel.getSubTitle());
            viewHolder.duration.setText(viewModel.getDuration());
            viewHolder.updatedDate.setText(viewModel.getUpdatedDate());
            viewHolder.publishedDate.setText(viewModel.getPublishedDate());
            viewHolder.itemView.setOnClickListener(view -> viewModel.onClick());
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            if ((position + 1) == viewModels.size()) {
                layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.list_item_main_card_margin);
            } else {
                layoutParams.bottomMargin = 0;
            }
            viewHolder.itemView.setLayoutParams(layoutParams);
        }
   }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.sub_title)
        TextView subTitle;

        @BindView(R.id.duration)
        TextView duration;

        @BindView(R.id.updated_date)
        TextView updatedDate;

        @BindView(R.id.published_date)
        TextView publishedDate;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
