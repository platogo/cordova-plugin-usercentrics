package com.platogo.cordova.usercentrics

import com.usercentrics.sdk.*
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray


class UserCentrics :  CordovaPlugin() {

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        when (action) {
            "initialize" -> {
                this.initialize(callbackContext)
                return true
            }
            "isReady" -> {
                this.isReady(callbackContext)
                return true
            }
            "clearUserSession" -> {
                clearUserSession(callbackContext)
                return true
            }
            "sdkReset" -> {
                sdkReset(callbackContext)
                return true
            }
            else -> return false
        }
    }

    fun initialize(callbackContext: CallbackContext, args: JSONArray) {
        cordova.getThreadPool().execute {
            try {
                initialize(callbackContext)
            } catch (e: Exception) {
                callbackContext.error(e.message)
            }
        }
    }

    fun isReady(callbackContext: CallbackContext, args: JSONArray) {
        cordova.activity.runOnUiThread {
            try {
                isReady(callbackContext)
            } catch (e: Exception) {
                callbackContext.error(e.message)
            }
        }
    }

    private fun initialize(callbackContext: CallbackContext) {
        val ruleSetId = this.cordova.getActivity().getResources().getString(getAppResource())
        val options = UsercentricsOptions(ruleSetId = ruleSetId, consentMediation = true)
        Usercentrics.initialize(this.cordova.getActivity().applicationContext, options)
        callbackContext.success("sdk initialized")
    }

    private fun isReady (callbackContext: CallbackContext){
        Usercentrics.isReady({ status ->
            if (status.geolocationRuleset != null && status.geolocationRuleset?.bannerRequiredAtLocation == false) {
                callbackContext.success("banner is not required at this location")
            } else if (status.shouldCollectConsent) {
                showFirstLayer(callbackContext)
            } else {
                // Apply consent with status.consents
                processConsent(callbackContext)
            }
        },{ error ->
            callbackContext.error(error.message)
        })
    }

    private fun showFirstLayer (callbackContext: CallbackContext){
        cordova.activity.runOnUiThread {
            try {
                val generalStyleSettings = GeneralStyleSettings(
                    disableSystemBackButton = true,
                )

                val bannerSettings = BannerSettings(
                    generalStyleSettings = generalStyleSettings,
                )

                val context = cordova.context
                val banner = UsercentricsBanner(context, bannerSettings)
                banner.showFirstLayer { _ ->
                    // Handle userResponse
                    processConsent(callbackContext)
                }
            } catch (e: Exception) {
                callbackContext.error(e.message)
            }
        }
    }

    private fun clearUserSession (callbackContext: CallbackContext){
        Usercentrics.instance.clearUserSession({ _ ->
            callbackContext.success("user session cleared")
        }, { error ->
            callbackContext.error(error.message)
        })
    }


    private fun sdkReset (callbackContext: CallbackContext){
        try {
            Usercentrics.reset()
            callbackContext.success("sdk reset successfully")
        } catch (e: Exception) {
            callbackContext.error(e.message)
        }
    }

    private fun processConsent(callbackContext: CallbackContext) {
        callbackContext.success("user consent received")
    }


    private fun getAppResource(): Int {
        return cordova.getActivity().getResources()
            .getIdentifier("RULE_SET_ID", "string", cordova.getActivity().packageName)
    }
}
