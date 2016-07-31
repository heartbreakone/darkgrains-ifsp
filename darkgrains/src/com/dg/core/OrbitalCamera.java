/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;

/**
 *
 * @author Victor
 */
public class OrbitalCamera extends ChaseCamera {

    private float maxX, maxY;
    private Vector3f direction;

    public OrbitalCamera(Camera cam, Spatial spat, InputManager input) {
        super(cam, spat, input);

        setSmoothMotion(true);
        setMinDistance(50f * 10);
        setMaxDistance(100f * 100);
        setDefaultDistance(99f * 10);
        setMinVerticalRotation(FastMath.QUARTER_PI / 2);
        setDragToRotate(true);
        setInvertVerticalAxis(true);
        setHideCursorOnRotate(false);
        //setChasingSensitivity(100f);
        setRotationSpeed(10f);
        setZoomSensitivity(100.0f);
        cam.setFrustumFar(50004f);
//
        
//        maxY = maxX = 1f;
//        direction = Vector3f.ZERO;
//        input.addMapping("MouseMoveUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
//        input.addMapping("MouseMoveDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
//        input.addMapping("MouseMoveLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
//        input.addMapping("MouseMoveRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
//        input.addListener(mouseListner, "MouseMoveUp", "MouseMoveDown", "MouseMoveLeft", "MouseMoveRight");

    }
    
//    private AnalogListener mouseListner = new AnalogListener() {
//        @Override
//        public void onAnalog(String name, float value, float tpf) {
//            direction = Vector3f.ZERO;
//            if (name.equals("MouseMoveUp")) {
//                if (value < maxY) {
//                    direction.z += (value * value);
//                } else {
//                    direction.z += (maxX * maxX);
//                }
//            }
//            if (name.equals("MouseMoveDown")) {
//                if (value < maxY) {
//                    direction.z -= (value * value);
//                } else {
//                    direction.z -= (maxX * maxX);
//                }
//            }
//            if (name.equals("MouseMoveLeft")) {
//                if (value < maxY) {
//                    direction.x -= (value * value);
//                } else {
//                    direction.x -= (maxY * maxY);
//                }
//            }
//            if (name.equals("MouseMoveRight")) {
//                if (value < maxY) {
//                    direction.x += (value * value);
//                } else {
//                    direction.x += (maxY * maxY);
//                }
//            }
//            
//            cam.lookAt(direction, Vector3f.UNIT_Y);
//            
//        }
//    };
}
