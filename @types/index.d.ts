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
}

interface CordovaPlugins {
  UserCentrics: UserCentrics;
}
