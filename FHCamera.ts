import { NativeModules } from 'react-native';

declare let _WORKLET: true | undefined;

const FHCameraModule = NativeModules.FHCameraModule;

if (FHCameraModule == null) console.error("FHCameraModule' was null! Did you run pod install?");

export default class FHCamera {
  
    public static async checkCameraAccess(): Promise<boolean> {
        return await FHCameraModule.checkCameraAccess();
    } 

    public static async getFrameQueueSize(): Promise<number>{
      return await FHCameraModule.getFrameQueueSize();
    }

    public static async lock3A(): Promise<void>{
      return await FHCameraModule.lock3A();
    } 

    public static async unlock3A(): Promise<void>{
      return await FHCameraModule.lock3A();
    } 

}