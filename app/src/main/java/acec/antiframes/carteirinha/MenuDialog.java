package acec.antiframes.carteirinha;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.Intent.ACTION_VIEW;

public class MenuDialog extends DialogFragment {
    RecyclerView recyclerView;
    public MenuDialog(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View v = inflater.inflate(R.layout.fragment_menu, null);
        builder.setView(v);
        recyclerView = (RecyclerView) v.findViewById(R.id.menu_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new MenuAdapter(
                ((MainActivity) getActivity()).getMenuItems()
        ));
        return builder.create();
    }


    private class MenuAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<MenuItem> items;

        MenuAdapter(List<MenuItem> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu,parent,false);

            return new ViewHolder((TextView) view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final MenuItem item = items.get(position);
            holder.menuItemText.setText(item.getTitle());
            holder.menuItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ACTION_VIEW);
                    intent.setData(Uri.parse(item.getUrl()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (items==null) return 0;
            return items.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView menuItemText;
        ViewHolder(TextView itemView) {
            super(itemView);
            menuItemText = itemView;
        }
    }
}
