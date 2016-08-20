package com.example.tim.thefarzaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

public class LaunchSpotifyLoginActivity extends AppCompatActivity implements PlayerNotificationCallback, ConnectionStateCallback {


    private static final String CLIENT_ID = "2d406bf647ef4d50b7c552ab9061d315";
    private static final String REDIRECT_URI = "logintime";

    private Player spottyPlaya;

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_spotify_login);

        Button loginButton = (Button)findViewById(R.id.launch_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                        AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

                builder.setScopes(new String[]{"user-read-private", "streaming"});
                AuthenticationRequest request = builder.build();

                AuthenticationClient.openLoginActivity(LaunchSpotifyLoginActivity.this,REQUEST_CODE, request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        spottyPlaya = player;
                        spottyPlaya.addConnectionStateCallback(LaunchSpotifyLoginActivity.this);
                        spottyPlaya.addPlayerNotificationCallback(LaunchSpotifyLoginActivity.this);
                        spottyPlaya.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("LoginActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }
    @Override
    public void onLoggedIn() {

        Log.d("LoginActivity","User has Logged in");
    }

    @Override
    public void onLoggedOut() {

        Log.d("LoginActivity","User has Logged out");
    }

    @Override
    public void onLoginFailed(Throwable throwable) {

        Log.d("LoginActivity","Log in has failed");
    }

    @Override
    public void onTemporaryError() {

        Log.d("LoginActivity","Temporary Error");
    }

    @Override
    public void onConnectionMessage(String s) {

        Log.d("LoginActivity","Received Connection Message:" + s);

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

        Log.d("LoginActivity","Playback event received" + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

        Log.d("LoginActivity","Playback error received:" + errorType.name());
    }

    @Override
    protected void onDestroy(){

        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}
