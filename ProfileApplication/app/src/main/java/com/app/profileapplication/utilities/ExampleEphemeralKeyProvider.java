package com.app.profileapplication.utilities;

import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;

import org.jetbrains.annotations.NotNull;

public class ExampleEphemeralKeyProvider implements EphemeralKeyProvider {

    @Override
    public void createEphemeralKey(@NotNull String s, @NotNull EphemeralKeyUpdateListener ephemeralKeyUpdateListener) {

    }
}
