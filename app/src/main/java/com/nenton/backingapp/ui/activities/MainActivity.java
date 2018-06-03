package com.nenton.backingapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.managers.RealmManager;
import com.nenton.backingapp.data.network.RestService;
import com.nenton.backingapp.data.network.ServiceGenerator;
import com.nenton.backingapp.data.network.res.RecipeResponse;
import com.nenton.backingapp.data.storage.dto.DetailDto;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.ui.fragments.DetailsFragment;
import com.nenton.backingapp.ui.fragments.DetailsFragment.OnDetailOrStepClickListener;
import com.nenton.backingapp.ui.fragments.IngredientsFragment;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment.OnRecipeClickListener;
import com.nenton.backingapp.ui.fragments.StepFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener, OnDetailOrStepClickListener {
    private static final String TAG = MainActivity.class.getName();

    private static final String STATE_FRAGMENT_KEY = "STATE_FRAGMENT_KEY";
    private static final String ID_RECIPE_KEY = "ID_RECIPE_KEY";
    private static final String ID_STEP_KEY = "ID_STEP_KEY";
    private static final int STATE_FRAGMENT_RECIPE = 51;
    private static final int STATE_FRAGMENT_DETAILS = 151;
    private static final int STATE_FRAGMENT_STEP = 251;
    private static final int STATE_FRAGMENT_INGREDIENTS = 351;
    private int mRecipeId;
    private int mStepId;
    private int mStateFragment;

    private RestService service;
    private RealmResults<RecipeRealm> mRecipes;
    private RealmManager mRealmManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();
        mRealmManager = new RealmManager();
        mRealmManager.getRecipes(recipeRealms -> mRecipes = recipeRealms);
        initToolbar();
        initCurrentFragment(savedInstanceState);
    }

    private void initCurrentFragment(Bundle state) {
        if (state != null) {
            mRecipeId = state.getInt(ID_RECIPE_KEY, -1);
            mStepId = state.getInt(ID_STEP_KEY, -1);
            mStateFragment = state.getInt(STATE_FRAGMENT_KEY, STATE_FRAGMENT_RECIPE);
        } else {
            mRecipeId = -1;
            mStepId = -1;
            mStateFragment = STATE_FRAGMENT_RECIPE;
        }
        updateFragments();
    }

    private void updateFragments() {
        executeNetQuery();
        switch (mStateFragment) {
            case STATE_FRAGMENT_INGREDIENTS:
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                mRealmManager.getRecipeById(mRecipeId, ingredientsFragment::setIngredients);
                if (findViewById(R.id.second_container) == null) {
                    manager.beginTransaction().replace(R.id.head_container, ingredientsFragment).commit();
                } else {
                    manager.beginTransaction().replace(R.id.second_container, ingredientsFragment).commit();
                }
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_STEP:
                StepFragment stepFragment = new StepFragment();
                mRealmManager.getStepById(mStepId, stepFragment::setStep);
                if (findViewById(R.id.second_container) == null) {
                    manager.beginTransaction().replace(R.id.head_container, stepFragment).commit();
                } else {
                    manager.beginTransaction().replace(R.id.second_container, stepFragment).commit();
                }
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_DETAILS:
                DetailsFragment detailsFragment = new DetailsFragment();
                mRealmManager.getRecipeById(mRecipeId, detailsFragment::setDetailsAndSteps);
                manager.beginTransaction().replace(R.id.head_container, detailsFragment).commit();
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_RECIPE:
                updateBackArrow(false);
                MasterRecipesFragment fragment = new MasterRecipesFragment();
                updateRecipes(fragment);
                break;
            default:
                showMessage("Неизвестная ошибка. Попробуйте позже");
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_FRAGMENT_KEY, mStateFragment);
        outState.putInt(ID_RECIPE_KEY, mRecipeId);
        outState.putInt(ID_STEP_KEY, mStepId);
        super.onSaveInstanceState(outState);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }


    @Override
    protected void onStop() {
        if (mRecipes != null) {
            mRecipes.removeAllChangeListeners();
        }
        super.onStop();
    }

    private void executeNetQuery() {
        if (service == null) {
            service = ServiceGenerator.createService(RestService.class);
        }
        service.getRecipes().enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeResponse>> call,
                                   @NonNull Response<List<RecipeResponse>> response) {
                if (response.code() == 200 && response.body() != null) {
                    RealmManager realmManager = new RealmManager();
                    realmManager.saveRecipesFromNet(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeResponse>> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    @Override
    public void onRecipeSelected(int id) {
        mRecipeId = id;
        mStateFragment = STATE_FRAGMENT_DETAILS;
        updateBackArrow(true);
        DetailsFragment detailsFragment = new DetailsFragment();
        mRealmManager.getRecipeById(mRecipeId, detailsFragment::setDetailsAndSteps);
        manager.beginTransaction().replace(R.id.head_container, detailsFragment).commit();
    }

    @Override
    public void onDetailOrStepSelected(DetailDto.DetailType type, int stepId) {
        updateBackArrow(true);
        Fragment fragment = null;
        switch (type) {
            case STEP:
                mStateFragment = STATE_FRAGMENT_STEP;
                mStepId = stepId;
                fragment = new StepFragment();
                mRealmManager.getStepById(mStepId, ((StepFragment) fragment)::setStep);
                break;
            case INGREDIENTS:
                mStateFragment = STATE_FRAGMENT_INGREDIENTS;
                mStepId = -1;
                fragment = new IngredientsFragment();
                mRealmManager.getRecipeById(mRecipeId,
                        ((IngredientsFragment) fragment)::setIngredients);
                break;
        }

        if (findViewById(R.id.second_container) == null) {
            manager.beginTransaction().replace(R.id.head_container, fragment).commit();
        } else {
            manager.beginTransaction().replace(R.id.second_container, fragment).commit();
        }

    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, message);
    }

    @Override
    public void onBackPressed() {
        switch (mStateFragment) {
            case STATE_FRAGMENT_INGREDIENTS:
            case STATE_FRAGMENT_STEP:
                mStateFragment = STATE_FRAGMENT_DETAILS;
                mStepId = -1;
                if (findViewById(R.id.second_container) == null) {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    mRealmManager.getRecipeById(mRecipeId, detailsFragment::setDetailsAndSteps);
                    manager.beginTransaction().replace(R.id.head_container, detailsFragment).commit();
                } else {
                    manager.beginTransaction().replace(R.id.second_container, null).commit();
                }
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_DETAILS:
                mStateFragment = STATE_FRAGMENT_RECIPE;
                mRecipeId = -1;
                MasterRecipesFragment recipesFragment = new MasterRecipesFragment();
                updateRecipes(recipesFragment);
                updateBackArrow(false);
                break;
            case STATE_FRAGMENT_RECIPE:
            default:
                super.onBackPressed();
                break;
        }
    }

    private void updateRecipes(MasterRecipesFragment recipesFragment) {
        manager.beginTransaction().replace(R.id.head_container, recipesFragment).commit();
        mRealmManager.getRecipes(recipeRealms -> {
            mRecipes = recipeRealms;
            if (recipesFragment != null) {
                recipesFragment.setRecipes(mRecipes);
            }
        });
    }

    private void updateBackArrow(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setDisplayShowHomeEnabled(show);
            if (show) {
                toolbar.setNavigationOnClickListener(view -> onBackPressed());
            }
        }
    }
}
