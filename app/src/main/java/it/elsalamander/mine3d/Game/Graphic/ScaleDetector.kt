package it.elsalamander.mine3d.Game.Graphic

import android.util.Log
import android.view.ScaleGestureDetector
import android.widget.Toast

/****************************************************************
 * Oggetto per realizzare il detector del gesto di zoom o dezoom
 *
 * @author: Elsalamander
 * @data: 15 aprile 2021
 * @version: v1.0
 ****************************************************************/
class ScaleDetector(var surface : MyGLSurfaceView) : ScaleGestureDetector.OnScaleGestureListener {
    /**
     * Responds to scaling events for a gesture in progress.
     * Reported by pointer motion.
     *
     * @param detector The detector reporting the event - use this to
     * retrieve extended info about event state.
     * @return Whether or not the detector should consider this event
     * as handled. If an event was not handled, the detector
     * will continue to accumulate movement until an event is
     * handled. This can be useful if an application, for example,
     * only wants to update scaling factors if the change is
     * greater than 0.01.
     */
    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        if (detector != null) {
            var tmp = this.surface.mRenderer.zoom
            tmp += (detector.scaleFactor-1) / 10f
            this.surface.mRenderer.zoom = tmp
        }
        return true
    }

    /**
     * Responds to the beginning of a scaling gesture. Reported by
     * new pointers going down.
     *
     * @param detector The detector reporting the event - use this to
     * retrieve extended info about event state.
     * @return Whether or not the detector should continue recognizing
     * this gesture. For example, if a gesture is beginning
     * with a focal point outside of a region where it makes
     * sense, onScaleBegin() may return false to ignore the
     * rest of the gesture.
     */
    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    /**
     * Responds to the end of a scale gesture. Reported by existing
     * pointers going up.
     *
     * Once a scale has ended, [ScaleGestureDetector.getFocusX]
     * and [ScaleGestureDetector.getFocusY] will return focal point
     * of the pointers remaining on the screen.
     *
     * @param detector The detector reporting the event - use this to
     * retrieve extended info about event state.
     */
    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }
}