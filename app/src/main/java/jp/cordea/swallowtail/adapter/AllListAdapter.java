package jp.cordea.swallowtail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.agera.Receiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.cordea.swallowtail.R;
import jp.cordea.swallowtail.viewmodel.AllListItemViewModel;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */

public class AllListAdapter extends BaseAdapter implements Receiver<List<AllListItemViewModel>> {

    private List<AllListItemViewModel> viewModels = new ArrayList<>();

    private Context context;

    public AllListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return viewModels.size();
    }

    @Override
    public AllListItemViewModel getItem(int i) {
        return viewModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        AllListItemViewModel viewModel = getItem(i);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_all, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(viewModel.getTitle());
        holder.description.setText(viewModel.getDescription());

        return convertView;
    }

    @Override
    public void accept(@NonNull List<AllListItemViewModel> value) {
        viewModels.clear();
        viewModels.addAll(value);
        notifyDataSetChanged();
    }

    static class ViewHolder {

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.title)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
