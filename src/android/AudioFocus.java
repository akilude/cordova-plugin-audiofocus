package com.brightminded.cordova.plugins;

import android.content.Context;
import android.media.AudioManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioFocus extends CordovaPlugin {
	private AudioFocusChangeListener audioFocusChangeListener;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		audioFocusChangeListener = new AudioFocusChangeListener(cordova);
	}

	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("requestFocus")) {
			this.requestFocus(callbackContext);
			return true;
		}

		if (action.equals("releaseFocus")) {
			this.releaseFocus(callbackContext);
			return true;
		}

		return false;
	}

	private void requestFocus(CallbackContext callbackContext) {
		AudioManager audioManager = (AudioManager)this.cordova.getActivity()
									.getApplicationContext()
									.getSystemService(Context.AUDIO_SERVICE);

		int result = audioManager.requestAudioFocus(audioFocusChangeListener,
										AudioManager.STREAM_MUSIC,
										AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);

		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			callbackContext.success("");
		} else {
			callbackContext.error("");
		}
	}

	private void releaseFocus(CallbackContext callbackContext) {
		AudioManager audioManager = (AudioManager)this.cordova.getActivity()
									.getApplicationContext()
									.getSystemService(Context.AUDIO_SERVICE);

		int result = audioManager.abandonAudioFocus(audioFocusChangeListener);

		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			callbackContext.success("");
		} else {
			callbackContext.error("");
		}
	}

	private class AudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener
	{
		private CordovaInterface cordova;

		public AudioFocusChangeListener(CordovaInterface cordova) {
			this.cordova = cordova;
		}

		@Override
		public void onAudioFocusChange(int focusChange) {

			AudioManager audioManager = (AudioManager)this.cordova.getActivity()
										.getApplicationContext()
										.getSystemService(Context.AUDIO_SERVICE);

			// Implement custom behavior on audio focus events
			// TODO maybe export those events through cordova
			switch (focusChange) {
				case AudioManager.AUDIOFOCUS_GAIN:
					break;
				case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
					break;
				case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
					break;
				case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
					break;
				case AudioManager.AUDIOFOCUS_LOSS:
					audioManager.abandonAudioFocus(audioFocusChangeListener);
					break;
				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
					break;
				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
					break;
			}
		}
	}
}
