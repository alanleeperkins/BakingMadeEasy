package alpitsolutions.com.bakingmadeeasy.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import alpitsolutions.com.bakingmadeeasy.R;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeStepCallback;
import alpitsolutions.com.bakingmadeeasy.models.RecipeStepEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.utility.Helpers;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipeStepViewModel;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipeStepViewModelFactory;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    private static final String TAG = Constants.TAG_FILTER + RecipeStepFragment.class.getSimpleName();

    private Integer recipeId = -1;
    private Integer recipeStepId = -1;


    private long exoPlayerCurrentPosition =0;
    private long exoPlayerStopPosition = 0;

    private Boolean exoPlayerHasStopped = false;
    private Boolean exoPlayerIsFullScreen = false;


    @Nullable @BindView(R.id.exo_player_instruction) SimpleExoPlayerView simpleExoPlayerView;
    @Nullable @BindView(R.id.txtShortDescription) TextView txtShortDescription;
    @Nullable @BindView(R.id.txtDescription) TextView txtDescription;

    private SimpleExoPlayer simpleExoPlayer;

    private RecipeStepViewModel viewModel;

    /***
     *
     */
    public RecipeStepFragment() {

    }

    /**
     *
     * @param recipeId
     * @param recipeStepId
     */
    public void setRecipeStep(Integer recipeId, Integer recipeStepId) {
        this.recipeId = recipeId;
        this.recipeStepId = recipeStepId;
    }

    /***
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"RecipeStepFragment onAttach");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"RecipeStepFragment onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"RecipeStepFragment onDestroy");
    }
    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(Constants.KEY_RECIPE_ID, recipeId);
        currentState.putInt(Constants.KEY_RECIPE_STEP_ID, recipeStepId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"RecipeStepFragment onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"RecipeStepFragment onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"RecipeStepFragment onDestroyView");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"RecipeStepFragment onViewCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "RecipeStepFragment onCreateView");

        if(savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(Constants.KEY_RECIPE_ID);
            recipeStepId = savedInstanceState.getInt(Constants.KEY_RECIPE_STEP_ID);
        }

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, rootView);

        initializePlayer();

        return rootView;
    }

    private void initializePlayer() {
        if(simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
        }
    }

    /**
     *
     * @param mediaUri
     */
    private void loadMediaIntoPlayer(Uri mediaUri) {
        Log.d(TAG,"loadMediaIntoPlayer: "+mediaUri);

        String userAgent = Util.getUserAgent(getContext(),"BakingMadeEasy");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
        simpleExoPlayer.prepare(mediaSource);

        if (exoPlayerStopPosition != 0 && !exoPlayerHasStopped){
            simpleExoPlayer.seekTo(exoPlayerCurrentPosition);
        } else {
            simpleExoPlayer.seekTo(exoPlayerStopPosition);
        }

        simpleExoPlayerView.setVisibility(View.VISIBLE);
    }

    private void hideMediaPlayer() {
        simpleExoPlayerView.setVisibility(View.INVISIBLE);
    }

    /**
     *
     */
    private void releasePlayer(){
        if(simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    /***
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();
        reloadRecipeStepData();
    }

    /***
     *
     */
    private void setupViewModel()
    {
        RecipeStepViewModelFactory factory = new RecipeStepViewModelFactory(getActivity().getApplication(), recipeId, recipeStepId);
        viewModel = ViewModelProviders.of(this, factory).get(RecipeStepViewModel.class);
    }

    /***
     *
     * @return
     */
    private Boolean reloadRecipeStepData() {
        Log.d(TAG, "reloadRecipeStepData");

        viewModel.getRecipesRepository().getRecipeStep(recipeId, recipeStepId, new OnGetRecipeStepCallback() {
            @Override
            public void onStarted() {
                Log.d(TAG,"reloadRecipeStepData onStarted");
            }

            @Override
            public void onSuccess(RecipeStepEntity recipeStep) {
                Log.d(TAG,"reloadRecipeStepData onSuccess");
                Log.d(TAG,recipeStep.toString());

                updateStepData(recipeStep);
            }

            @Override
            public void onError() {
                Log.d(TAG,"reloadRecipeStepData onError");
            }
        });

        return true;
    }

    /**
     *
     * @param recipeStep
     */
    private boolean updateStepData(RecipeStepEntity recipeStep) {

        if (recipeStep == null)
            return false;

        // maybe the user accidentally put the video url into the thumbnail url variable
        if (Helpers.isLegalVideoUrl(recipeStep.getVideoUrl()))
            loadMediaIntoPlayer(Uri.parse(recipeStep.getVideoUrl()));
        else  if (Helpers.isLegalVideoUrl(recipeStep.getThumbnailUrl()))
            loadMediaIntoPlayer(Uri.parse(recipeStep.getThumbnailUrl()));
        else
            hideMediaPlayer();

        txtShortDescription.setText(recipeStep.getShortDescription());
        txtDescription.setText(recipeStep.getDescription());

        return true;
    }
}
