package com.platogo.cordova.usercentrics;

import com.usercentrics.sdk.*
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray


class UserCentrics :  CordovaPlugin() {

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        if (action == "coolMethod") {
            val message: String = args.getString(0)
            coolMethod(message, callbackContext)
            return true
        } else if (action == "initialize") {
            initialize(callbackContext)
            return true
        }

        return false
    }

    private fun coolMethod(message: String, callbackContext: CallbackContext) {
        if (message.isNotEmpty()) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private fun initialize(callbackContext: CallbackContext) {
        val settingsId = this.cordova.getActivity().getResources().getString(getAppResource())
        val options = UsercentricsOptions(settingsId = settingsId)
        Usercentrics.initialize(this.cordova.getActivity().applicationContext, options)
        callbackContext.success(settingsId);
    }

    private fun getAppResource(): Int {
        return cordova.getActivity().getResources()
            .getIdentifier("SETTINGS_ID", "string", cordova.getActivity().packageName)
    }
}