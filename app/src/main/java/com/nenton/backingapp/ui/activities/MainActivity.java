package com.nenton.backingapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.network.RestService;
import com.nenton.backingapp.data.network.ServiceGenerator;
import com.nenton.backingapp.data.network.res.RecipeResponse;
import com.nenton.backingapp.data.storage.dto.RecipeDto;
import com.nenton.backingapp.data.storage.dto.StepDto;
import com.nenton.backingapp.ui.fragments.DetailsFragment;
import com.nenton.backingapp.ui.fragments.DetailsFragment.OnDetailOrStepClickListener;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment.OnRecipeClickListener;
import com.nenton.backingapp.ui.fragments.StepFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener, OnDetailOrStepClickListener {
    private static final String TAG = MainActivity.class.getName();

    private RestService service;
    private List<RecipeDto> mRecipes;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MasterRecipesFragment recipesFragment;
    private StepFragment mStepFragment;
    private DetailsFragment mDetailsFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();
        service = ServiceGenerator.createService(RestService.class);
        recipesFragment = new MasterRecipesFragment();
        manager.beginTransaction().replace(R.id.head_container, recipesFragment).commit();
        executeNetQuery();
        initToolbar();
        updateBackArrow(false);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        executeNetQuery();
    }

    private void executeNetQuery() {
        service.getRecipes().enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                if (response.code() == 200 && response.body() != null) {
                    updateFragment(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    private void updateFragment(List<RecipeResponse> body) {
        mRecipes = new ArrayList<>();
        for (RecipeResponse recipeResponse : body) {
            mRecipes.add(new RecipeDto(recipeResponse));
        }

        if (recipesFragment != null) {
            recipesFragment.setRecipes(mRecipes);
        }
    }

    @Override
    public void onRecipeSelected(int position) {
        updateBackArrow(true);
        RecipeDto recipe = mRecipes.get(position);
        mDetailsFragment = new DetailsFragment();
        mDetailsFragment.setDetailsAndSteps(recipe.getSteps(), recipe.getServings(), position);
        manager.beginTransaction().replace(R.id.head_container, mDetailsFragment).commit();
    }

    @Override
    public void onDetailOrStepSelected(int positionRecipe, int positionDetail) {
        updateBackArrow(true);
        if (positionDetail != 0) {
            StepDto step = mRecipes.get(positionRecipe).getSteps().get(positionDetail - 1);
            mStepFragment = new StepFragment();
            mStepFragment.setStep(step);
            if (findViewById(R.id.second_container) == null) {
                manager.beginTransaction().replace(R.id.head_container, mStepFragment).commit();
            } else {
                manager.beginTransaction().replace(R.id.second_container, mStepFragment).commit();
            }
        }
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, message);
    }

    @Override
    public void onBackPressed() {
        if (mStepFragment != null) {
            if (findViewById(R.id.second_container) == null) {
                manager.beginTransaction().replace(R.id.head_container, mDetailsFragment).commit();
            } else {
                manager.beginTransaction().remove(mStepFragment).commit();
            }
            mStepFragment = null;
        } else if (mDetailsFragment != null) {
            manager.beginTransaction().replace(R.id.head_container, recipesFragment).commit();
            updateBackArrow(false);
            mDetailsFragment = null;
        } else {
            super.onBackPressed();
        }
    }

    private void updateBackArrow(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setDisplayShowHomeEnabled(show);
            if (show) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
        }
    }
}
