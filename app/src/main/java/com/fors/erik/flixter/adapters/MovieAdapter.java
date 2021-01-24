package com.fors.erik.flixter.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fors.erik.flixter.R;
import com.fors.erik.flixter.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;
    private final int NOTPOPULAR = 0, POPULAR = 1;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Inflate the layout and return it
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case POPULAR:
                View v1 = inflater.inflate(R.layout.alternate_view,parent,false);
                viewHolder = new ViewHolder2(v1);
                break;
            case NOTPOPULAR:
                View v2 = inflater.inflate(R.layout.item_movie, parent,false);
                viewHolder = new ViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //get movie at position
        Movie movie = movies.get(position);

        switch (holder.getItemViewType()){
            case POPULAR:
                ViewHolder2 vh2 = (ViewHolder2) holder;
                vh2.bind(movie);
                break;
            case NOTPOPULAR:
                ViewHolder vh1 =  (ViewHolder) holder;
                vh1.bind(movie);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
                vh.bind();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        final double ratingToCompare = 5;

        //compares the movie rating with rating of popular movie
        //return 1 if the movie is popular
        //return 0 if not popular
        //return -1 if there was a problem
        if (movies.get(position).getRating() > ratingToCompare) {
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                return NOTPOPULAR;
            }
            return POPULAR;
        } else if(movies.get(position).getRating() > 0)
            return NOTPOPULAR;

        return -1;
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                imageURL = movie.getBackdropPath();
            else
                imageURL = movie.getPostPath();

            Glide.with(context).load(imageURL).placeholder(R.drawable.loading).into(ivPoster);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        ImageView ivBackdrop;

        public ViewHolder2(@NonNull View itemView){
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
        }

        public void bind(Movie movie){
            final String imageURL = movie.getBackdropPath();
            Glide.with(context).load(imageURL).placeholder(R.drawable.loading).into(ivBackdrop);
        }
    }

    public class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder{

        TextView ivErrorText;

        public RecyclerViewSimpleTextViewHolder(@NonNull View itemView){
            super(itemView);
            ivErrorText = itemView.findViewById(android.R.id.text1);
        }

        public void bind(){
            ivErrorText.setText("An error has occurred!");
        }
    }
}
