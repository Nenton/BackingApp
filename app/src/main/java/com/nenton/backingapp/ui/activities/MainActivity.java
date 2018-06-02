package com.nenton.backingapp.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.network.RestService;
import com.nenton.backingapp.data.network.ServiceGenerator;
import com.nenton.backingapp.data.network.res.RecipeResponse;
import com.nenton.backingapp.data.pojo.Recipe;
import com.nenton.backingapp.ui.fragments.DetailsFragment;
import com.nenton.backingapp.ui.fragments.DetailsFragment.OnDetailOrStepClickListener;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment.OnRecipeClickListener;
import com.nenton.backingapp.ui.fragments.StepFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener, OnDetailOrStepClickListener {

    private RestService service;
    private MasterRecipesFragment recipesFragment;
    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        service = ServiceGenerator.createService(RestService.class);
        recipesFragment = new MasterRecipesFragment();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.head_container, recipesFragment)
                .commit();
        executeNetQuery();
    }

    private void executeNetQuery() {
        service.getRecipes().enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                Toast.makeText(MainActivity.this, "ОТВЕТ", Toast.LENGTH_LONG).show();
                if (response.code() == 200 && response.body() != null) {
                    updateFragment(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                // TODO: 02.06.2018 сделать чтонибудь
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "ОШИБКА", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateFragment(List<RecipeResponse> body) {
        mRecipes = new ArrayList<>();
        for (RecipeResponse recipeResponse : body) {
            mRecipes.add(new Recipe(recipeResponse));
        }

        if (recipesFragment != null) {
            recipesFragment.setRecipes(mRecipes);
        }
    }

    @Override
    public void onRecipeSelected(int position) {
        Recipe recipe = mRecipes.get(position);
        DetailsFragment fragmentDetails = new DetailsFragment();
        fragmentDetails.setDetailsAndSteps(recipe.getSteps(), recipe.getServings(), position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.head_container, fragmentDetails)
                .commit();
    }

    @Override
    public void onDetailOrStepSelected(int positionRecipe, int positionDetail) {
        if (positionDetail != 0) {
            Recipe.Step step = mRecipes.get(positionRecipe).getSteps().get(positionDetail - 1);
            StepFragment stepFragment = new StepFragment();
            stepFragment.setStep(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.head_container, stepFragment)
                    .commit();
        }
    }
}
