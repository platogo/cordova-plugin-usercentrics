import Foundation


@objc(UserCentrics)
public class UserCentrics : CDVPlugin {
    @objc
    func coolMethod(_ command: CDVInvokedUrlCommand) {
        let echo = command.argument(at: 0) as! String?
        let pluginResult:CDVPluginResult

        if echo != nil && echo!.count > 0 {
            pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: echo!)
        } else {
            pluginResult = CDVPluginResult.init(status: CDVCommandStatus_ERROR)
        }

        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
}