package com.platogo.cordova.usercentrics

import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsBanner
import com.usercentrics.sdk.UsercentricsOptions
import com.usercentrics.sdk.UsercentricsServiceConsent
import com.usercentrics.sdk.models.common.UsercentricsLoggerLevel
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaPlugin
import org.json.JSONArray
import org.json.JSONObject


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
        val options = UsercentricsOptions(ruleSetId = ruleSetId, consentMediation = true, loggerLevel = UsercentricsLoggerLevel.DEBUG)
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
                processConsent(status.consents, callbackContext)
            }
        },{ error ->
            callbackContext.error(error.message)
        })
    }

    private fun showFirstLayer (callbackContext: CallbackContext){
        cordova.activity.runOnUiThread {
            try {
                val context = cordova.context
                val banner = UsercentricsBanner(context)
                banner.showFirstLayer { userResponse ->
                    // Handle userResponse
                    processConsent(userResponse?.consents, callbackContext)
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
            callbackContext.success("sdk reset success")
        } catch (e: Exception) {
            callbackContext.error(e.message)
        }
    }

    private fun processConsent(consents: List<UsercentricsServiceConsent>?, callbackContext: CallbackContext) {
        if (consents.isNullOrEmpty()) {
            callbackContext.error("no consent received")
            return
        }
        val consentMap = consents.associateBy({ it.templateId }, { mapOf("status" to it.status, "dataProcessor" to it.dataProcessor) })
        callbackContext.success(JSONObject(consentMap))
    }


    private fun getAppResource(): Int {
        return cordova.getActivity().getResources()
            .getIdentifier("RULE_SET_ID", "string", cordova.getActivity().packageName)
    }
}
