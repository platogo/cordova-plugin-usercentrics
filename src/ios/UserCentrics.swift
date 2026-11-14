import Foundation
import UIKit
import Usercentrics
import UsercentricsUI


@objc(UserCentrics)
public class UserCentrics : CDVPlugin {
    @objc
    func initialize(_ command: CDVInvokedUrlCommand) {
        guard let ruleSetId = Bundle.main.object(forInfoDictionaryKey: "RULE_SET_ID") as? String,
              !ruleSetId.isEmpty else {
            let pluginResult = CDVPluginResult(status: .error, messageAs: "RULE_SET_ID is missing from the iOS app configuration.")
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            return
        }

        let options = UsercentricsOptions()
        options.ruleSetId = ruleSetId
        options.consentMediation = true
        UsercentricsCore.configure(options: options)
        let pluginResult = CDVPluginResult(status: .ok, messageAs: "sdk initialized")
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }

    @objc
    func isReady(_ command: CDVInvokedUrlCommand) {
        UsercentricsCore.isReady { [weak self] status in
            guard let self = self else { return }

            if status.geolocationRuleset?.bannerRequiredAtLocation == false {
                let pluginResult = CDVPluginResult(status: .ok, messageAs: "banner is not required at this location")
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            } else if status.shouldCollectConsent {
                DispatchQueue.main.async {
                    self.showFirstLayer(command)
                }
            } else {
                self.collectGoogleConsents(command)
            }
        } onFailure: { [weak self] error in
            guard let self = self else { return }
            let pluginResult = CDVPluginResult(status: .error, messageAs: error.localizedDescription)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
    }

    @objc
    func clearUserSession(_ command: CDVInvokedUrlCommand) {
        UsercentricsCore.shared.clearUserSession(onSuccess: { [weak self] _ in
            guard let self = self else { return }
            let pluginResult = CDVPluginResult(status: .ok, messageAs: "user session cleared")
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }, onError: { [weak self] error in
            guard let self = self else { return }
            let pluginResult = CDVPluginResult(status: .error, messageAs: error.localizedDescription)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        })
    }

    @objc
    func getGoogleConsents(_ command: CDVInvokedUrlCommand) {
        collectGoogleConsents(command)
    }

    private func showFirstLayer(_ command: CDVInvokedUrlCommand) {
        let banner = UsercentricsBanner(bannerSettings: nil)
        banner.showFirstLayer(hostView: viewController, layout: .popup(position: .center)) { [weak self] _ in
            self?.collectGoogleConsents(command)
        }
    }

    private func collectGoogleConsents(_ command: CDVInvokedUrlCommand) {
        UsercentricsCore.shared.getTCFData { [weak self] data in
            guard let self = self else { return }

            let purposes = data.purposes
            let vendorGoogle = data.vendors.first { $0.id == 755 }
            let purpose1 = purposes.first { $0.id == 1 }
            let purpose3 = purposes.first { $0.id == 3 }
            let purpose4 = purposes.first { $0.id == 4 }
            let purpose7 = purposes.first { $0.id == 7 }
            let purpose9 = purposes.first { $0.id == 9 }
            let purpose10 = purposes.first { $0.id == 10 }

            let adStorage = purpose1?.consent?.boolValue == true && vendorGoogle?.consent?.boolValue == true
            let adUserData = adStorage &&
                (purpose7?.consent?.boolValue == true || purpose7?.legitimateInterestConsent?.boolValue == true)
            let adPersonalization = purpose3?.consent?.boolValue == true &&
                purpose4?.consent?.boolValue == true &&
                vendorGoogle?.consent?.boolValue == true
            let analyticsStorage =
                (purpose9?.consent?.boolValue == true || purpose9?.legitimateInterestConsent?.boolValue == true) &&
                (purpose10?.consent?.boolValue == true || purpose10?.legitimateInterestConsent?.boolValue == true) &&
                (vendorGoogle?.legitimateInterestConsent?.boolValue == true || vendorGoogle?.consent?.boolValue == true)

            let consents: [String: Bool] = [
                "ad_storage": adStorage,
                "ad_user_data": adUserData,
                "ad_personalization": adPersonalization,
                "analytics_storage": analyticsStorage
            ]
            let pluginResult = CDVPluginResult(status: .ok, messageAs: consents)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
    }
}
