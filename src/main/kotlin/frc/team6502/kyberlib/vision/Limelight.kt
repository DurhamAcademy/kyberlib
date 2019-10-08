package frc.team6502.kyberlib.vision

import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.math.roundToInt

/**
 * A wrapper for the limelight vision camera.
 * [table] defines the network table the data will be pulled from ("limelight" by default)
 */
class Limelight(private val table: String = "limelight"){

    private val tbl = NetworkTableInstance.getDefault().getTable(table)

    /**
     * Is the camera currently detecting an object?
     */
    val valid
        get() = tbl.getEntry("tv").getDouble(0.0) != 0.0

    /**
     * The x-heading the detection is in
     */
    val x
        get() = tbl.getEntry("tx").getDouble(0.0)

    /**
     * The y-heading the detection is in
     */
    val y
        get() = tbl.getEntry("ty").getDouble(0.0)

    /**
     * The area of the detected contour
     */
    val area
        get() = tbl.getEntry("ta").getDouble(0.0)

    /**
     * The angle of the detected contour relative to the camera
     */
    val skew
        get() = tbl.getEntry("ts").getDouble(0.0)

    /**
     * The milliseconds it takes for the pipeline to be processed
     */
    val latency
        get() = tbl.getEntry("tl").getDouble(0.0)

    val short
        get() = tbl.getEntry("tshort").getDouble(0.0)

    val long
        get() = tbl.getEntry("tlong").getDouble(0.0)

    val horizontal
        get() = tbl.getEntry("thor").getDouble(0.0)

    val vertical
        get() = tbl.getEntry("tvert").getDouble(0.0)

    /**
     * The currently selected pipeline
     */
    val pipeline
        get() = tbl.getEntry("getpipe").getDouble(0.0).toInt()

    /**
     * 3D transform of the detected target if using PnP
     */
    val transform: VisionTransform
        get() {
            val cmt = tbl.getEntry("camtran").getDoubleArray(arrayOf(0.0,0.0,0.0,0.0,0.0,0.0))
            return VisionTransform(cmt[0], cmt[1], cmt[2], cmt[3], cmt[4], cmt[5])
        }
}

/**
 * Represents the transformation of a vision target in 3D space, as returned by the Limelight's 3D mode.
 */
data class VisionTransform(val x: Double, val y: Double, val z: Double, val pitch: Double, val yaw: Double, val roll: Double)