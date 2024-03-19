var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'UserCentrics', 'coolMethod', [arg0]);
};

exports.initialize = function (success, error) {
    exec(success, error, 'UserCentrics', 'initialize');
};
