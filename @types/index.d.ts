interface UserCentrics {
  initialize: (
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
  isReady: (
    success: (message: IGoogleConsents | string) => void,
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
  ad_storage: Boolean;
  ad_user_data: Boolean;
  ad_personalization: Boolean;
  analytics_storage: Boolean;
}

interface CordovaPlugins {
  UserCentrics: UserCentrics;
}
