import { NativeModules } from 'react-native';

declare let _WORKLET: true | undefined;

const FHVitalsSDKModule = NativeModules.FHVitalsSDKModule;

if (FHVitalsSDKModule == null) console.error("FHVitalsSDKModule' was null! Did you run pod install?");

export interface SubjectInfoOptions {
    gender: number;
    age: number;
    height: number;
    weight: number;
    ht: number;
};

export enum ERROR_CODE {
  SUCCESS                         =  1,
  ERROR_AUTHENTICATION_FAILED     =  0,
  ERROR_INITIALIZATION_FAILED     = -1,
  ERROR_ACTIVATION_FAILED         = -2,
  ERROR_INVALID_DEVICE            = -3,
  ERROR_INVALID_ASSETS            = -4,
  ERROR_INVALID_LICENSE           = -5,
  ERROR_SERVER_CONNECTION_FAILED  = -6,
  ERROR_INVALID_APPLICATION_ID    = -7,
  ERROR_CONFIG_NOT_SUPPORTED      = -8
};

export enum VITAL {
  VITAL_HR_HRV = 1,
  VITAL_BP     = 2,
  VITAL_RESP   = 4,
  VITAL_SPO2   = 8,
  VITAL_ALL    = VITAL_HR_HRV + VITAL_BP + VITAL_RESP + VITAL_SPO2
 }

export enum IQA_TYPE {
  IQA_TYPE_BRIGHTNESS = 0,
  IQA_TYPE_CONTRAST = 1,
  IQA_TYPE_MOTION = 2
};

export enum VITALS_SYST {
  VITALS_SYST_HR_HRV = 0,
  VITALS_SYST_BP = 1,
  VITALS_SYST_RESP = 2,
  VITALS_SYST_SPO2 = 3,
};

export enum HRV_TYPE {
  HRV_TYPE_LF = 0,
  HRV_TYPE_HF = 1,
  HRV_TYPE_LF_HF_RATIO = 2,
  HRV_TYPE_PLF = 3,
  HRV_TYPE_SDNN = 4,
  HRV_TYPE_RRIV = 5,
  HRV_TYPE_MEAN_RR = 6,
  HRV_TYPE_RMSSD = 7,
  HRV_TYPE_PHF = 8,
  HRV_TYPE_SD1 = 9,
  HRV_TYPE_SD2 = 10
};

export enum RADAR_TYPE {
  RADAR_TYPE_ACTIVITY = 0,
  RADAR_TYPE_SLEEP = 1,
  RADAR_TYPE_EQUILIBRIUM = 2,
  RADAR_TYPE_METABOLISM = 3,
  RADAR_TYPE_HEALTH = 4,
  RADAR_TYPE_RELAXATION = 5
};

export enum HRV_ANS_TYPE {
  HRV_ANS_TYPE_PNS = 0,
  HRV_ANS_TYPE_SNS = 1
};

export enum BP_MODE {
  BP_MODE_BINARY = 0,
  BP_MODE_TERNARY = 1
};

export enum GENDER {
  FEMALE = 0,
  MALE = 1
};

export enum BP_GROUP {
  BP_NORMAL = 0,
  BP_PRE_HYPERTENSION = 1,
  BP_HYPERTENSION = 2
};

export default class FHVitals {
  public static async version(): Promise<string> {
    return await FHVitalsSDKModule.version();
  }

  public static async init(assets: string, license: string, host: string, port: number): Promise<number> {
    return await FHVitalsSDKModule.initSDK(assets, license, host, port);
  }

  public static async activate(): Promise<number> {
    return await FHVitalsSDKModule.activate();
  }

  public static async deactivate(): Promise<number> {
    return await FHVitalsSDKModule.deactivate();
  }

  public static async isActivated(): Promise<boolean> {
    return await FHVitalsSDKModule.isActivated();
  }

  public static async resetFPS(fps: number): Promise<boolean> {
    return await FHVitalsSDKModule.resetFPS(fps);
  }

  public static async getFPS(): Promise<number> {
    return await FHVitalsSDKModule.getFPS();
  }

  public static async useFaceTrackMode(): Promise<void> {
    return await FHVitalsSDKModule.useFaceTrackMode();
  }

  public static async useFixedRoIMode(roi_center_x: number, roi_center_y: number, roi_width: number, roi_height: number): Promise<void> {
    return await FHVitalsSDKModule.useFixedRoIMode(roi_center_x, roi_center_y, roi_width, roi_height);
  }
  
  public static async startMeasuring(vital: number): Promise<void> {
    return await FHVitalsSDKModule.startMeasuring(vital);
  }

  public static async stopMeasuring(): Promise<void> {
    return await FHVitalsSDKModule.stopMeasuring();
  }

  public static async getTrackedFaceBox(): Promise<number[]> {
    return await FHVitalsSDKModule.getTrackedFaceBox();
  }

  public static async getMissFaceFrameCount(): Promise<number> {
    return await FHVitalsSDKModule.getMissFaceFrameCount();
  }

  public static async isAnyQueueFull(): Promise<boolean> {
    return await FHVitalsSDKModule.isAnyQueueFull();
  }

  public static async getImageQualityScore(type: IQA_TYPE): Promise<number> {
    return await FHVitalsSDKModule.getImageQualityScore(type);
  }

  public static async getProcessedFrameCount(syst: VITALS_SYST): Promise<number> {
    return await FHVitalsSDKModule.getProcessedFrameCount(syst);
  }

  public static async getSignalQualityScore(syst: VITALS_SYST): Promise<number> {
    return await FHVitalsSDKModule.getSignalQualityScore(syst);
  }
  
  public static async getAverageProcessTime(syst: VITALS_SYST): Promise<number> {
    return await FHVitalsSDKModule.getAverageProcessTime(syst);
  }

  public static async getHR(): Promise<number> {
    return await FHVitalsSDKModule.getHR();
  }

  public static async getSmoothedHR(): Promise<number> {
    return await FHVitalsSDKModule.getSmoothedHR();
  }

  public static async getPRQ(): Promise<number> {
    return await FHVitalsSDKModule.getPRQ();
  }

  public static async getRecordRPPG(): Promise<number[]> {
    return await FHVitalsSDKModule.getRecordRPPG();
  }

  public static async checkFeatureBufferForHRV(): Promise<number> {
    return await FHVitalsSDKModule.checkFeatureBufferForHRV();
  }

  public static async getHRV(type: HRV_TYPE): Promise<number> {
    return await FHVitalsSDKModule.getHRV(type);
  }

  public static async getStressIndex(): Promise<number> {
    return await FHVitalsSDKModule.getStressIndex();
  }

  public static async getANSIndex(type: HRV_ANS_TYPE): Promise<number> {
    return await FHVitalsSDKModule.getANSIndex(type);
  }

  public static async getRadar(type: RADAR_TYPE): Promise<number> {
    return await FHVitalsSDKModule.getRadar(type);
  }

  public static async setBloodPressureMode(mode: BP_MODE): Promise<void> {
    return await FHVitalsSDKModule.setBloodPressureMode(mode);
  }

  public static async setSubjectInfo(gender: GENDER, age: number, height: number, weight: number, group: BP_GROUP): Promise<void> {
    return await FHVitalsSDKModule.setSubjectInfo(gender, age, height, weight, group);
  }

  public static async resetCalibrationBP(sbp: number, dbp: number): Promise<void> {
    return await FHVitalsSDKModule.resetCalibrationBP(sbp, dbp);
  }

  public static async getSBP(): Promise<number> {
    return await FHVitalsSDKModule.getSBP();
  }

  public static async getDBP(): Promise<number> {
    return await FHVitalsSDKModule.getDBP();
  }

  public static async getSmoothedSBP(): Promise<number> {
    return await FHVitalsSDKModule.getSmoothedSBP();
  }
  
  public static async getSmoothedDBP(): Promise<number> {
    return await FHVitalsSDKModule.getSmoothedDBP();
  }

  public static async getRR(): Promise<number> {
    return await FHVitalsSDKModule.getRR();
  }

  public static async getSmoothedRR(): Promise<number> {
    return await FHVitalsSDKModule.getSmoothedRR();
  }
  
  public static async getSpO2(): Promise<number> {
    return await FHVitalsSDKModule.getSpO2();
  }
}