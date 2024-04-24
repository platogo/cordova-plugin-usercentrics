package com.platogo.cordova.usercentrics

import com.usercentrics.sdk.*
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
            "getGoogleConsents" -> {
                getGoogleConsents(callbackContext)
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
                getGoogleConsents(callbackContext)
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
                    getGoogleConsents(callbackContext)
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

    private fun getGoogleConsents (callbackContext: CallbackContext){
        try {
            Usercentrics.instance.getTCFData { data ->
                val purposes = data.purposes
                val vendorGoogle = data.vendors.find { vendor -> vendor.id == 755 }

                val purpose1 = purposes.find { purpose -> purpose.id == 1 }
                val purpose3 = purposes.find { purpose -> purpose.id == 3 }
                val purpose4 = purposes.find { purpose -> purpose.id == 4 }
                val purpose7 = purposes.find { purpose -> purpose.id == 7 }
                val purpose9 = purposes.find { purpose -> purpose.id == 9 }
                val purpose10 = purposes.find { purpose -> purpose.id == 10 }

                val adStorage = purpose1?.consent == true && vendorGoogle?.consent == true
                val adUserData = purpose1?.consent == true &&  (purpose7?.consent == true || purpose7?.legitimateInterestConsent == true) && vendorGoogle?.consent == true
                val adPersonalization = purpose3?.consent == true && purpose4?.consent == true && vendorGoogle?.legitimateInterestConsent == true
                val analyticsStorage = (purpose9?.consent == true || purpose9?.legitimateInterestConsent == true) && (purpose10?.consent == true || purpose10?.legitimateInterestConsent == true) && vendorGoogle?.legitimateInterestConsent == true

                val consentMap = mapOf(
                    "ad_storage" to adStorage,
                    "ad_user_data" to adUserData,
                    "ad_personalization" to adPersonalization,
                    "analytics_storage" to analyticsStorage
                )

                callbackContext.success(JSONObject(consentMap))

            }
        } catch (e: Exception) {
            callbackContext.error(e.message)
        }
    }

    private fun getAppResource(): Int {
        return cordova.getActivity().getResources()
            .getIdentifier("RULE_SET_ID", "string", cordova.getActivity().packageName)
    }
}
