import Foundation


@objc(UserCentrics)
public class UserCentrics : CDVPlugin {
    @objc
    func initialize(_ command: CDVInvokedUrlCommand) {
        let pluginResult:CDVPluginResult
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: "initialize")
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }

    @objc
    func isReady(_ command: CDVInvokedUrlCommand) {
        let pluginResult:CDVPluginResult
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: "isReady")
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }

    @objc
    func clearUserSession(_ command: CDVInvokedUrlCommand) {
        let pluginResult:CDVPluginResult
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: "clearUserSession")
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }

    @objc
    func sdkReset(_ command: CDVInvokedUrlCommand) {
        let pluginResult:CDVPluginResult
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: "sdkReset")
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }

    @objc
    func getGoogleConsents(_ command: CDVInvokedUrlCommand) {
        let pluginResult:CDVPluginResult
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: "getGoogleConsents")
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
}
