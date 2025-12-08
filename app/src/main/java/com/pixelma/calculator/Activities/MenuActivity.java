package com.pixelma.calculator.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pixelma.calculator.R;
import com.pixelma.calculator.Utils.GameConfig;

/**
 * The main menu activity of the application.
 * It displays game mode options and handles user authentication.
 */
public class MenuActivity extends AppCompatActivity {

    private int shadowOffset;
    private int shadowOffsetPressed;
    private int pressDuration;
    private int releaseDuration;

    private static final String TAG = "MenuActivity";
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_menu);

        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(rootView);
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        initResource();
        initGoogleSignIn();


        int[] buttonIds = {
                R.id.btn_plus, R.id.btn_divide, R.id.btn_minus, R.id.btn_multi,
                R.id.btn_random, R.id.btn_challenge, R.id.btn_timer, R.id.btn_progress
        };

        for (int i = 0; i < buttonIds.length; i++) {
            MaterialCardView button = findViewById(buttonIds[i]);
//            animateButtonAppearance(button, i * 100, 100);
        }

        // Starts a game with the addition operator
        setButtonTouchListener(R.id.btn_plus, () -> startGame(GameConfig.Operators.PLUS));
        // Starts a game with the division operator
        setButtonTouchListener(R.id.btn_divide, () -> startGame(GameConfig.Operators.DIVIDE));
        // Starts a game with the subtraction operator
        setButtonTouchListener(R.id.btn_minus, () -> startGame(GameConfig.Operators.MINUS));
        // Starts a game with the multiplication operator
        setButtonTouchListener(R.id.btn_multi, () -> startGame(GameConfig.Operators.MULTIPLY));
        // Starts a game with a random operator
        setButtonTouchListener(R.id.btn_random, () -> startGame(GameConfig.Operators.RANDOM));

        // ? No action defined
        setButtonTouchListener(R.id.btn_challenge, () -> {});
        // ? No action defined
        setButtonTouchListener(R.id.btn_timer, () -> {});
        // When the "Progress" button is clicked, sign out the user.
        setButtonTouchListener(R.id.btn_progress, this::signOut);

        MaterialButton googleSignInButton = findViewById(R.id.btn_google_sign_in);
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Initializes the Google Sign-In client, the sign-up request, and Firebase Auth.
     * It also initializes the ActivityResultLauncher to handle the sign-in result from Google.
     */
    private void initGoogleSignIn() {
        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // This server client ID is obtained from the Google Cloud Console.
                        // It's essential for authenticating with the Google backend.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        // Launcher for the Google Sign-In intent.
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    // Once we have the Google ID token, we can use it to authenticate with Firebase.
                    firebaseAuthWithGoogle(idToken);
                }
            } catch (ApiException e) {
                // Handle the error if the sign-in fails.
                Log.e(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        });
    }

    /**
     * Starts the Google Sign-In "One Tap" flow.
     * It attempts to sign in the user with their Google account.
     */
    private void signInWithGoogle() {
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, result -> {
                    // On success, launch the pending intent to show the Google One Tap UI.
                    IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                    activityResultLauncher.launch(intentSenderRequest);
                })
                .addOnFailureListener(this, e -> {
                    // This can happen if the user has no Google accounts on their device.
                    Log.e(TAG, "Google sign in failed", e);
                });
    }

    /**
     * Authenticates the user with Firebase using the Google ID token obtained from the sign-in flow.
     *
     * @param idToken The Google ID token.
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user and update UI.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    /**
     * Signs out the current user from Firebase and Google's One Tap.
     * After sign-out, it updates the UI to show the sign-in button again.
     */
    private void signOut() {
        Log.d(TAG, "Signing out user.");
        // Sign out from Firebase Auth, which is a synchronous operation.
        mAuth.signOut();

        // After Firebase sign-out, the user is considered logged out in the app.
        // Update the UI immediately.
        updateUI(null);

        // Also sign out from Google One Tap client for a clean sign-out experience.
        // This is an asynchronous operation.
        oneTapClient.signOut().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Signed out from Google One Tap successfully.");
            } else {
                Log.w(TAG, "Google One Tap sign-out failed", task.getException());
            }
        });
    }

    /**
     * Updates the UI based on the user's authentication status.
     * If the user is signed in, it hides the Google Sign-In button.
     * If the user is signed out, it shows the button.
     *
     * @param user The current FirebaseUser, or null if the user is not signed in.
     */
    private void updateUI(FirebaseUser user) {
        MaterialButton googleSignInButton = findViewById(R.id.btn_google_sign_in);
        if (user != null) {
            // User is signed in
            googleSignInButton.setVisibility(View.GONE);
            // TODO: You can add logic here to welcome the user, e.g., show their name.
        } else {
            // User is signed out
            googleSignInButton.setVisibility(View.VISIBLE);
        }
    }

    public void initResource() {
        shadowOffset = (int) getResources().getDimension(R.dimen.game_button_shadow_offset);
        shadowOffsetPressed = (int) getResources().getDimension(R.dimen.game_button_shadow_offset_pressed);
        pressDuration = getResources().getInteger(R.integer.game_button_press_duration);
        releaseDuration = getResources().getInteger(R.integer.game_button_release_duration);
    }

    private void animateButton(View view, int from, int to, int duration) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(duration);

        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            params.setMarginStart(shadowOffset - value);
            params.topMargin = shadowOffset - value;
            params.setMarginEnd(value);
            params.bottomMargin = value;
            view.setLayoutParams(params);
        });
        animator.start();
    }

    private void animateButtonAppearance(View button, long startDelay, long duration) {
        button.setScaleX(0.9f);
        button.setScaleY(0.9f);
        button.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .start();
    }

    private void setButtonTouchListener(int buttonId, Runnable action) {
        MaterialCardView button = findViewById(buttonId);
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateButton(view, shadowOffset, shadowOffsetPressed, pressDuration);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    animateButton(view, shadowOffsetPressed, shadowOffset, releaseDuration);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        action.run();
                    }
                    return true;
            }
            return false;
        });
    }

    private void startGame(int operator) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.OPERATOR, operator);
        startActivity(intent);
    }



}
