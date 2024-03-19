interface UserCentrics {
  coolMethod: (
    arg0: string,
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
  initialize: (
    success: (message: string) => void,
    error: (message: string) => void
  ) => void;
}

interface CordovaPlugins {
  UserCentrics: UserCentrics;
}
