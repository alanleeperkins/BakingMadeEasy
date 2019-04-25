package alpitsolutions.com.bakingmadeeasy.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
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

    private static final String sTAG = Constants.sTAG_FILTER + RecipeStepFragment.class.getSimpleName();

    private Integer mRecipeId = -1;
    private Integer mRecipeStepId = -1;
    private long mExoPlayerCurrentPosition =0;
    private long mExoPlayerStopPosition = 0;
    private Boolean mExoPlayerHasStopped = false;

    @Nullable @BindView(R.id.exo_player_instruction) SimpleExoPlayerView mSimpleExoPlayerView;
    @Nullable @BindView(R.id.txtShortDescription) TextView mTxtShortDescription;
    @Nullable @BindView(R.id.txtDescription) TextView mTxtDescription;

    private SimpleExoPlayer mSimpleExoPlayer;
    private RecipeStepViewModel mViewModel;

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
        this.mRecipeId = recipeId;
        this.mRecipeStepId = recipeStepId;
    }

    /***
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(sTAG,"RecipeStepFragment onAttach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(sTAG,"RecipeStepFragment onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(sTAG,"RecipeStepFragment onDestroy");
    }
    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(Constants.sKEY_RECIPE_ID, mRecipeId);
        currentState.putInt(Constants.sKEY_RECIPE_STEP_ID, mRecipeStepId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(sTAG,"RecipeStepFragment onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        Log.d(sTAG,"RecipeStepFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        Log.d(sTAG,"RecipeStepFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(sTAG,"RecipeStepFragment onDestroyView");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(sTAG,"RecipeStepFragment onViewCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(sTAG, "RecipeStepFragment onCreateView");

        if(savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.sKEY_RECIPE_ID);
            mRecipeStepId = savedInstanceState.getInt(Constants.sKEY_RECIPE_STEP_ID);
        }

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, rootView);

        initializePlayer();

        return rootView;
    }

    private void initializePlayer() {
        if(mSimpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        }
    }

    /**
     *
     * @param mediaUri
     */
    private void loadMediaIntoPlayer(Uri mediaUri) {
        Log.d(sTAG,"loadMediaIntoPlayer: "+mediaUri);

        String userAgent = Util.getUserAgent(getContext(),"BakingMadeEasy");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
        mSimpleExoPlayer.prepare(mediaSource);

        if (mExoPlayerStopPosition != 0 && !mExoPlayerHasStopped){
            mSimpleExoPlayer.seekTo(mExoPlayerCurrentPosition);
        } else {
            mSimpleExoPlayer.seekTo(mExoPlayerStopPosition);
        }

        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
    }

    private void hideMediaPlayer() {
        mSimpleExoPlayerView.setVisibility(View.INVISIBLE);
    }

    /**
     *
     */
    private void releasePlayer(){
        if(mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
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
        RecipeStepViewModelFactory factory = new RecipeStepViewModelFactory(getActivity().getApplication(), mRecipeId, mRecipeStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeStepViewModel.class);
    }

    /***
     *
     * @return
     */
    private Boolean reloadRecipeStepData() {
        Log.d(sTAG, "reloadRecipeStepData");

        mViewModel.getRecipesRepository().getRecipeStep(mRecipeId, mRecipeStepId, new OnGetRecipeStepCallback() {
            @Override
            public void onStarted() {
                Log.d(sTAG,"reloadRecipeStepData onStarted");
            }

            @Override
            public void onSuccess(RecipeStepEntity recipeStep) {
                Log.d(sTAG,"reloadRecipeStepData onSuccess");
                Log.d(sTAG,recipeStep.toString());

                updateStepData(recipeStep);
            }

            @Override
            public void onError() {
                Log.d(sTAG,"reloadRecipeStepData onError");
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

        mTxtShortDescription.setText(recipeStep.getShortDescription());
        mTxtDescription.setText(recipeStep.getDescription());

        return true;
    }
}
