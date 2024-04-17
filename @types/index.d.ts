interface UserCentrics {
  initialize: (
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
  isReady: (
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
  clearUserSession: (
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
  sdkReset: (
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
  getGoogleConsents: (
    success: (consents: IGoogleConsents) => void,
    error: (message: string) => void
  ) => void;
}

interface IGoogleConsents {
  adStorage: Boolean;
  adUserData: Boolean;
  adPersonalization: Boolean;
  analyticsStorage: Boolean;
}

interface CordovaPlugins {
  UserCentrics: UserCentrics;
}
