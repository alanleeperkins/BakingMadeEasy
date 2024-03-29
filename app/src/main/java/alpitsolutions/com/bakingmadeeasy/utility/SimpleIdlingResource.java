package alpitsolutions.com.bakingmadeeasy.utility;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource{

    @Nullable private volatile ResourceCallback mIdlingCallback;

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mIdlingCallback = callback;
    }

    /**
     * Sets the new idle state, if mIsIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        this.mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mIdlingCallback != null) {
            mIdlingCallback.onTransitionToIdle();
        }
    }
}
