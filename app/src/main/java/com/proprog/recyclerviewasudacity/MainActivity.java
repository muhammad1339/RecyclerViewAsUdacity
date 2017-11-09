package com.proprog.recyclerviewasudacity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.proprog.model.MovieProvider;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements View.OnLongClickListener
        , SearchView.OnQueryTextListener {

    FloatingActionButton fab_plus, fab_fb, fab_tw;
    Animation anim_open, anim_close, anim_rotate_clock, anim_rotate_anti;

    boolean in_action_mode = false;
    boolean is_open = false;

    TextView toolbar_text;
    EditText search_bar;
    RecyclerView recyclerView;
    Toolbar toolbar;

    MovieAdapter movieAdapter;
    String[] titles, directors;
    int[] poster_resources;
    ArrayList<MovieProvider> movieProviders;
    ArrayList<MovieProvider> selected_items;


    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_fb = (FloatingActionButton) findViewById(R.id.fab_fb);
        fab_tw = (FloatingActionButton) findViewById(R.id.fab_tw);

        anim_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        anim_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        anim_rotate_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clockwise);
        anim_rotate_anti = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlockwise);

        toolbar = (Toolbar) findViewById(R.id.movie_toolbar);
        setSupportActionBar(toolbar);

        search_bar = (EditText) findViewById(R.id.search_key);
        toolbar_text = (TextView) findViewById(R.id.selected_count);
        toolbar_text.setVisibility(View.GONE);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        titles = getResources().getStringArray(R.array.titles);
        directors = getResources().getStringArray(R.array.directors);
        poster_resources = new int[]{R.drawable.avatar, R.drawable.deadpool,
                R.drawable.lightsout, R.drawable.me_myself_and_irene,
                R.drawable.moonlight, R.drawable.rocky, R.drawable.startrek,
                R.drawable.starwarsempireneedsyou, R.drawable.superbean,
                R.drawable.supermanreturns};

        movieProviders = new ArrayList<>();
        selected_items = new ArrayList<>();

        MovieProvider movieProvider;
        for (int i = 0; i < titles.length; i++) {
            movieProvider = new MovieProvider(titles[i], directors[i], poster_resources[i]);
            movieProviders.add(movieProvider);
        }

        movieAdapter = new MovieAdapter(movieProviders, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);

        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_open) {
                    fab_fb.startAnimation(anim_close);
                    fab_tw.startAnimation(anim_close);
                    fab_plus.startAnimation(anim_rotate_anti);
                    fab_fb.setClickable(false);
                    fab_tw.setClickable(false);
                    is_open = false;
                } else {
                    fab_fb.startAnimation(anim_open);
                    fab_tw.startAnimation(anim_open);
                    fab_plus.startAnimation(anim_rotate_clock);
                    fab_fb.setClickable(true);
                    fab_tw.setClickable(true);
                    is_open = true;
                }
            }
        });
        fab_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Intent chooser = Intent.createChooser(intent, "Follow us !");
                try {
                    if (isPackageExisted(MainActivity.this, "com.facebook.katana")) {
                        getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        intent.setData(Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/"));
                    } else {
                        intent.setData(Uri.parse("https://www.facebook.com/"));
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                startActivity(chooser);
            }
        });
        fab_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.twitter.com"));
                startActivity(intent);
            }
        });

    }

    private boolean isPackageExisted(MainActivity c, String s) {
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("com.facebook.katana", PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MenuItem item = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_delete) {
            MovieAdapter adapter = (MovieAdapter) this.movieAdapter;
            adapter.updateList(selected_items);
            clearActionMode();
        } else if (item.getItemId() == android.R.id.home) {
            clearActionMode();
            movieAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.action_mode_menu);
        toolbar_text.setVisibility(View.VISIBLE);
        in_action_mode = true;
        movieAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (in_action_mode) {
            clearActionMode();
            movieAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
            movieAdapter.notifyDataSetChanged();
        }
    }

    public void prepare_selection_list(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            selected_items.add(movieProviders.get(position));
            counter++;
            if (counter > 1) {
                toolbar_text.setText(counter + " selected items");
            } else {
                toolbar_text.setText(counter + " selected item");
            }

        } else {
            selected_items.remove(movieProviders.get(position));
            counter--;
            if (counter > 1) {
                toolbar_text.setText(counter + " selected items");
            } else {
                toolbar_text.setText(counter + " selected item");
            }
        }
    }

    public void clearActionMode() {
        in_action_mode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.main_activity_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar_text.setVisibility(View.GONE);
        toolbar_text.setText("0 selected items");
        counter = 0;
        selected_items.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ArrayList<MovieProvider> filteredMovie = new ArrayList<>();
        newText = newText.toLowerCase();
        for(MovieProvider  movieProvider : movieProviders){
            String match = movieProvider.getTitle().toLowerCase();
            if(match.contains(newText)){
                filteredMovie.add(movieProvider);
            }
        }
        movieAdapter.setFilter(filteredMovie);
        return true;
    }
}
