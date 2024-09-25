var exec = require('cordova/exec')

exports.initialize = function (success, error) {
  exec(success, error, 'UserCentrics', 'initialize')
}

exports.isReady = function (success, error) {
  exec(success, error, 'UserCentrics', 'isReady')
}

exports.clearUserSession = function (success, error) {
  exec(success, error, 'UserCentrics', 'clearUserSession')
}

exports.getGoogleConsents = function (success, error) {
  exec(success, error, 'UserCentrics', 'getGoogleConsents')
}
