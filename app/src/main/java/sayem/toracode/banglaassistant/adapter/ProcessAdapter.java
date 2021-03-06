package sayem.toracode.banglaassistant.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import sayem.toracode.banglaassistant.R;
import sayem.toracode.banglaassistant.model.SingleProcessItem;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.MyViewHolder> {
    LayoutInflater inflater;
    List<SingleProcessItem> data = Collections.emptyList();
    Context context;

    public ProcessAdapter(Context context, List<SingleProcessItem> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.single_process_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        SingleProcessItem currentData = data.get(position);
        myViewHolder.iconImageView.setImageDrawable(currentData.getAppIcon());
        myViewHolder.applicationTitleTextView.setText(currentData.getApplicationTitle());
        myViewHolder.cpuInfoTextView.setText("CPU: " + df.format(currentData.getCpuUsage()) + "%");
        myViewHolder.memoryInfoTextView.setText("Memory Available: " + currentData.getMemoryUsage() + " MB");
        myViewHolder.checkBox.setChecked(currentData.isChecked());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView applicationTitleTextView;
        ImageView iconImageView;
        TextView cpuInfoTextView;
        TextView memoryInfoTextView;
        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            applicationTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            cpuInfoTextView = (TextView) itemView.findViewById(R.id.cpuUsageTextView);
            memoryInfoTextView = (TextView) itemView.findViewById(R.id.memoryUsageTextView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);


        }
    }
}