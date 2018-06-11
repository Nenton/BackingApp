package com.nenton.backingapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
import com.nenton.backingapp.data.storage.dto.RecipeDto;
import com.nenton.backingapp.data.storage.dto.StepDto;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.data.storage.realm.StepRealm;
import com.nenton.backingapp.ui.fragments.DetailsFragment;
import com.nenton.backingapp.ui.fragments.DetailsFragment.OnDetailOrStepClickListener;
import com.nenton.backingapp.ui.fragments.IngredientsFragment;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment;
import com.nenton.backingapp.ui.fragments.MasterRecipesFragment.OnRecipeClickListener;
import com.nenton.backingapp.ui.fragments.StepFragment;
import com.nenton.backingapp.utils.Playable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener, OnDetailOrStepClickListener, Playable {
    private static final String TAG = MainActivity.class.getName();

    private static final String STATE_FRAGMENT_KEY = "STATE_FRAGMENT_KEY";
    private static final String ID_RECIPE_KEY = "ID_RECIPE_KEY";
    private static final String ID_STEP_KEY = "ID_STEP_KEY";
    private static final int STATE_FRAGMENT_RECIPE = 51;
    private static final int STATE_FRAGMENT_DETAILS = 151;
    private static final int STATE_FRAGMENT_STEP = 251;
    private static final int STATE_FRAGMENT_INGREDIENTS = 351;
    public static final String TAG_FRAGMENT_RECIPE = "TAG_FRAGMENT_RECIPE";
    public static final String TAG_FRAGMENT_STEP = "TAG_FRAGMENT_STEP";
    public static final String TAG_FRAGMENT_INGREDIENTS = "TAG_FRAGMENT_INGREDIENTS";
    public static final String TAG_FRAGMENT_DETAILS = "TAG_FRAGMENT_DETAILS";
    private int mRecipeId;
    private int mStepId;
    private int mStateFragment;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private RestService service;
    private RealmResults<RecipeRealm> mRecipes;
    private RealmManager mRealmManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FragmentManager manager;


    private void initCurrentFragment(Bundle state) {
        if (state != null) {
            mRecipeId = state.getInt(ID_RECIPE_KEY, -1);
            mStepId = state.getInt(ID_STEP_KEY, -1);
            mStateFragment = state.getInt(STATE_FRAGMENT_KEY, STATE_FRAGMENT_RECIPE);
            updateFragments(false);
        } else {
            mRecipeId = -1;
            mStepId = -1;
            mStateFragment = STATE_FRAGMENT_RECIPE;
            updateFragments(true);
        }
    }

    private void updateFragments(boolean isStart) {

        executeNetQuery();
        switch (mStateFragment) {
            case STATE_FRAGMENT_INGREDIENTS:
                if (isStart) {
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    mRealmManager.getRecipeById(mRecipeId, new RealmChangeListener<RecipeRealm>() {
                        @Override
                        public void onChange(RecipeRealm recipeRealm) {
                            ingredientsFragment.setIngredients(new RecipeDto(recipeRealm));
                        }
                    });
                    if (findViewById(R.id.second_container) == null) {
                        manager.beginTransaction().replace(R.id.head_container, ingredientsFragment, TAG_FRAGMENT_INGREDIENTS).commit();
                    } else {
                        manager.beginTransaction().replace(R.id.second_container, ingredientsFragment, TAG_FRAGMENT_INGREDIENTS).commit();
                    }
                }
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_STEP:
                if (isStart) {
                    StepFragment stepFragment = new StepFragment();
                    mRealmManager.getStepById(mStepId, new RealmChangeListener<StepRealm>() {
                        @Override
                        public void onChange(StepRealm stepRealm) {
                            stepFragment.setStep(new StepDto(stepRealm));
                        }
                    });
                    if (findViewById(R.id.second_container) == null) {
                        manager.beginTransaction().replace(R.id.head_container, stepFragment, TAG_FRAGMENT_STEP).commit();
                    } else {
                        manager.beginTransaction().replace(R.id.second_container, stepFragment, TAG_FRAGMENT_STEP).commit();
                    }
                }
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_DETAILS:
                if (isStart) {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    mRealmManager.getRecipeById(mRecipeId, new RealmChangeListener<RecipeRealm>() {
                        @Override
                        public void onChange(RecipeRealm recipeRealm) {
                            detailsFragment.setDetailsAndSteps(new RecipeDto(recipeRealm));
                        }
                    });
                    manager.beginTransaction().replace(R.id.head_container, detailsFragment, TAG_FRAGMENT_DETAILS).commit();
                }
                updateBackArrow(true);
                break;
            case STATE_FRAGMENT_RECIPE:
                if (isStart) {
                    MasterRecipesFragment fragment = new MasterRecipesFragment();
                    updateRecipes(fragment);
                }
                updateBackArrow(false);
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
    protected void onCreate(Bundle savedInstanceState) {
        initializeMediaSession();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();
        mRealmManager = new RealmManager();
        mRealmManager.getRecipes(recipeRealms -> mRecipes = recipeRealms);
        initToolbar();
        initCurrentFragment(savedInstanceState);
    }


    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setActive(true);
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
        mRealmManager.getRecipeById(mRecipeId, new RealmChangeListener<RecipeRealm>() {
            @Override
            public void onChange(RecipeRealm recipeRealm) {
                detailsFragment.setDetailsAndSteps(new RecipeDto(recipeRealm));
            }
        });
        manager.beginTransaction().replace(R.id.head_container, detailsFragment, TAG_FRAGMENT_DETAILS).commit();
    }

    @Override
    public void onDetailOrStepSelected(DetailDto.DetailType type, int stepId, boolean haveVideo) {
        updateBackArrow(true);
        Fragment fragment = null;
        String tag = null;
        switch (type) {
            case STEP:
                mStateFragment = STATE_FRAGMENT_STEP;
                mStepId = stepId;
                fragment = new StepFragment();
                ((StepFragment) fragment).setHaveVideo(haveVideo);
                Fragment finalFragment1 = fragment;
                mRealmManager.getStepById(mStepId, stepRealm -> ((StepFragment) finalFragment1)
                        .setStep(new StepDto(stepRealm)));
                tag = TAG_FRAGMENT_STEP;
                break;
            case INGREDIENTS:
                mStateFragment = STATE_FRAGMENT_INGREDIENTS;
                mStepId = -1;
                fragment = new IngredientsFragment();
                Fragment finalFragment = fragment;
                mRealmManager.getRecipeById(mRecipeId, recipeRealm ->
                        ((IngredientsFragment) finalFragment)
                                .setIngredients(new RecipeDto(recipeRealm)));
                tag = TAG_FRAGMENT_INGREDIENTS;
                break;
        }

        if (findViewById(R.id.second_container) == null) {
            manager.beginTransaction().replace(R.id.head_container, fragment, tag).commit();
        } else {
            manager.beginTransaction().replace(R.id.second_container, fragment, tag).commit();
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
                    mRealmManager.getRecipeById(mRecipeId, recipeRealm -> detailsFragment.setDetailsAndSteps(new RecipeDto(recipeRealm)));
                    manager.beginTransaction().replace(R.id.head_container, detailsFragment, TAG_FRAGMENT_DETAILS).commit();
                } else {
                    manager.beginTransaction().replace(R.id.second_container, new Fragment()).commit();
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
        mRealmManager.getRecipes(recipeRealms -> {
            mRecipes = recipeRealms;
            if (recipesFragment != null) {
                List<RecipeDto> recipeDtos = new ArrayList<>();
                for (RecipeRealm recipeRealm : recipeRealms) {
                    recipeDtos.add(new RecipeDto(recipeRealm));
                }
                recipesFragment.setRecipes(recipeDtos);
            }
        });
        manager.beginTransaction().replace(R.id.head_container, recipesFragment, TAG_FRAGMENT_RECIPE).commit();
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

    public MediaSessionCompat getMediaSession() {
        return mMediaSession;
    }

    @Override
    public PlaybackStateCompat.Builder getStatePlayback() {
        return mStateBuilder;
    }

    // region lifeCycle
    @Override
    protected void onStop() {
        if (mRecipes != null) {
            mRecipes.removeAllChangeListeners();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
