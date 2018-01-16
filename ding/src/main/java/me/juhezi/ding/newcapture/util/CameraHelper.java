package me.juhezi.ding.newcapture.util;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.view.Surface;
import android.view.View;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraHelper {

    /**
     * 获取最佳预览尺寸
     * @param parameters
     * @param pictureSize
     * @param viewHeight
     * @return
     */
    public static Camera.Size getOptimalPreviewSize(Camera.Parameters parameters,
                                                    Camera.Size pictureSize, int viewHeight) {

        if (parameters == null || pictureSize == null) {
            return null;
        }

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        Collections.sort(sizes, new CameraPreviewSizeComparator());

        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) pictureSize.width / pictureSize.height;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = pictureSize.height;
        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;

            if (optimalSize != null && size.height > viewHeight) {
                break;
            }

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        //Log.e("CameraHelper",
        //        "optimalSize : width=" + optimalSize.width + " height=" + optimalSize.height);
        return optimalSize;
    }

    //  这里只使用于旋转了90度
    public static Rect calculateTapArea(View v, float oldx, float oldy, float coefficient) {

        float x = oldy;
        float y = v.getHeight() - oldx;

        float focusAreaSize = 300;

        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / v.getWidth() * 2000 - 1000);
        int centerY = (int) (y / v.getHeight() * 2000 - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);

        return new Rect(left, top, right, bottom);
    }


    public static int determineDisplayOrientation(Activity activity, int defaultCameraId, int degrees) {
        int displayOrientation = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(defaultCameraId, cameraInfo);

            if (degrees == -1)
                degrees = getRotationAngle(activity);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                displayOrientation = (cameraInfo.orientation + degrees) % 360;
                displayOrientation = (360 - displayOrientation) % 360;
            } else {
                displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
            }
        }

        if (displayOrientation == 0) {
            displayOrientation = 180;
        } else if (displayOrientation == 180) {
            displayOrientation = 0;
        }

        return displayOrientation;
    }


    public static int determineDisplayOrientationForVideo(int cameraId) {
        int displayOrientation = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);

            displayOrientation = cameraInfo.orientation % 360;
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                displayOrientation = (360 - displayOrientation) % 360;
            }
        }

        if (displayOrientation == 0) {
            displayOrientation = 180;
        } else if (displayOrientation == 180) {
            displayOrientation = 0;
        }

        return displayOrientation;
    }

    public static void saveExif(String path, int orientation, boolean isFrontCamera) throws IOException {
        ExifInterface exifInterface = new ExifInterface(path);
        switch (orientation) {
            case 90:
                if (isFrontCamera) {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                            toString(ExifInterface.ORIENTATION_ROTATE_270));
                } else {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                            toString(ExifInterface.ORIENTATION_ROTATE_90));
                }
                break;
            case 180:
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                        toString(ExifInterface.ORIENTATION_ROTATE_180));
                break;
            case 270:
                if (isFrontCamera) {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                            toString(ExifInterface.ORIENTATION_ROTATE_90));
                } else {
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                            toString(ExifInterface.ORIENTATION_ROTATE_270));
                }
                break;
            case 0:
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                        toString(ExifInterface.ORIENTATION_NORMAL));
                break;
            default:
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.
                        toString(ExifInterface.ORIENTATION_NORMAL));
                break;
        }
        exifInterface.saveAttributes();
    }


    private static int getRotationAngle(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }


    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public static class CameraPreviewSizeComparator implements Comparator<Camera.Size> {

        // 预览尺寸建议从小到大，优先获取较小的尺寸
        public int compare(Camera.Size size1, Camera.Size size2) {
            return size1.width - size2.width;
        }
    }
}
