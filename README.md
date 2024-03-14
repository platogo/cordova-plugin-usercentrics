# cordova-plugin-usercentrics

This plugin implements usercentric features for cordova.

## how to install

install it via cordova cli

```
cordova plugin add https://github.com/tbd.git
```

**note:** curently supports android and ios only

## usage

take screenshot with jpg and custom quality

```js
cordova.plugins.UserCentrics.coolMethod(
  "message",
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
