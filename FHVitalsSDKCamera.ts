import { NativeModules } from 'react-native';

declare let _WORKLET: true | undefined;

const FHVitalsSDKCameraModule = NativeModules.FHVitalsSDKCameraModule;

if (FHVitalsSDKCameraModule == null) console.error("FHVitalsSDKCameraModule' was null! Did you run pod install?");

export default class FHVitalsSDKCamera {

  
    public static async startPushFrame(): Promise<void>{
      console.log("Push Frame");
      console.log(FHVitalsSDKCameraModule);
      const push = await FHVitalsSDKCameraModule.startPushFrame();
      console.log(push);
      
      return push;
    }      

    public static async stopPushFrame(): Promise<void>{
      return await FHVitalsSDKCameraModule.stopPushFrame();
    }      


}