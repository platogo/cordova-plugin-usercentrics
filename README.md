# cordova-plugin-usercentrics

This plugin implements usercentrics features for cordova.

## how to install

install it via cordova cli

```
cordova plugin add https://github.com/platogo/cordova-plugin-usercentrics.git
```

**note:** curently supports android and ios only

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

cause banner to show

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

reset the sdk

```js
cordova.plugins.UserCentrics.sdkReset(
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
