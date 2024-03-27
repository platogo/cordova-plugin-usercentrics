# cordova-plugin-usercentrics

This plugin implements usercentrics features for cordova.

## how to install

install it via cordova cli

```
cordova plugin add https://github.com/tbd.git
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

# License

this repo uses the tbd license
