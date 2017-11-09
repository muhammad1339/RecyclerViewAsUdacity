package com.proprog.recyclerviewasudacity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Movie;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.proprog.model.MovieProvider;

import java.util.ArrayList;
import java.util.HashMap;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    ArrayList<MovieProvider> movieProviders;
    Context context;
    MainActivity mainActivity;
    HashMap<Integer, Boolean> status;
    int counter = 0;
    int counter1 = 0;

    public MovieAdapter(ArrayList<MovieProvider> movieProviders, Context context) {
        status = new HashMap<>();
        this.movieProviders = movieProviders;
        this.context = context;
        this.mainActivity = (MainActivity) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        MyViewHolder myViewHolder =
                new MyViewHolder(rootView, this.context, this.movieProviders, this.mainActivity);
        Log.d("Create THEM FOR ", myViewHolder.getAdapterPosition() + ", " + (counter1++));

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.movie_title.setText(this.movieProviders.get(position).getTitle());
        holder.movie_director.setText(this.movieProviders.get(position).getDirector());

        holder.movie_poster.setImageResource(this.movieProviders.get(position).getPoster_resource());
        Log.d("BIND THEM FOR ", holder.getAdapterPosition() + ", " + (counter++));

        if (!mainActivity.in_action_mode) {
            status.clear();
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            if (status.get(holder.getAdapterPosition()) != null) {
                holder.checkBox.setChecked(status.get(holder.getAdapterPosition()));
            } else {
                holder.checkBox.setChecked(false);
            }
        }
        if (holder.getAdapterPosition() % 9 == 0) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.one));
        } else if (holder.getAdapterPosition() % 9 == 1) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.two));
        } else if (holder.getAdapterPosition() % 9 == 2) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.three));
        } else if (holder.getAdapterPosition() % 9 == 3) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.four));
        } else if (holder.getAdapterPosition() % 9 == 4) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.five));
        } else if (holder.getAdapterPosition() % 9 == 5) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.six));
        } else if (holder.getAdapterPosition() % 9 == 6) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.seven));
        } else if (holder.getAdapterPosition() % 9 == 7) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.eight));
        } else if (holder.getAdapterPosition() % 9 == 8) {
            holder.relativeLayout.setBackgroundColor(mainActivity.getResources().getColor(R.color.nine));
        }
    }

    @Override
    public int getItemCount() {
        return movieProviders.size();
    }

    public void updateList(ArrayList<MovieProvider> selected_items) {
        for (MovieProvider movieProvider : selected_items) {
            this.movieProviders.remove(movieProvider);
            status.clear();
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movie_title, movie_director;
        ImageView movie_poster;
        CheckBox checkBox;
        Context context;
        ArrayList<MovieProvider> movieProviders;
        Toast toast;
        MainActivity mainActivity;
        CardView cardView;
        RelativeLayout relativeLayout;
        public MyViewHolder(View view, Context context, ArrayList<MovieProvider> movieProviders, MainActivity mainActivity) {
            super(view);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.card_layout);
            cardView = (CardView) view.findViewById(R.id.card_view);
            this.mainActivity = mainActivity;
            this.context = context;
            this.movieProviders = movieProviders;

            movie_title = (TextView) view.findViewById(R.id.movie_title);
            movie_director = (TextView) view.findViewById(R.id.movie_director);
            movie_poster = (ImageView) view.findViewById(R.id.movie_poster);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            cardView.setOnLongClickListener(mainActivity);
            checkBox.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mainActivity.prepare_selection_list(v, position);
            status.put(getAdapterPosition(), checkBox.isChecked());
            Log.d("HASH THEM FOR ", getAdapterPosition() + ", " + (status.get(getAdapterPosition())) + ", " + status.size());
        }
    }
    public void setFilter(ArrayList<MovieProvider> movies){
        movieProviders = new ArrayList<>();
        movieProviders.addAll(movies);
        notifyDataSetChanged();
    }
}
