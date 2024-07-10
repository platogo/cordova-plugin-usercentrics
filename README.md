# cordova-plugin-usercentrics

This plugin implements UserCentrics SDK features for Cordova applications.

## how to install

Install it via the Cordova CLI.

```sh
cordova plugin add https://github.com/platogo/cordova-plugin-usercentrics.git
```

**note:** currently supports only Android

## usage

initialize usercentrics

```js
cordova.plugins.UserCentrics.initialize(
  function (success) {
    console.log(success);
  },
  function (error) {
    console.error(error);
  }
);
```

cause consent banner to show

```js
cordova.plugins.UserCentrics.isReady(
  function (success) {
    console.log(success);
  },
  function (error) {
    console.error(error);
  }
);
```

clear user session

```js
cordova.plugins.UserCentrics.clearUserSession(
  function (success) {
    console.log(success);
  },
  function (error) {
    console.error(error);
  }
);

```

get google consents

```js
cordova.plugins.UserCentrics.getGoogleConsents(
  function (success) {
    console.log(success);
  },
  function (error) {
    console.error(error);
  }
);
```

# License

this repo uses the tbd license
