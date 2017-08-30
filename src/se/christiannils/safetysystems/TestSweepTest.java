
/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package se.christiannils.safetysystems;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
/**
 *
 * @author @wezrule
 */
public class TestSweepTest extends SimpleApplication {

    private BulletAppState bulletAppState = new BulletAppState();

    public static void main(String[] args) {
        new TestSweepTest().start();
    }

    @Override
    public void simpleInitApp() {
        stateManager.attach(bulletAppState);
        initCrossHair();

        Spatial s = assetManager.loadModel("Models/Sensors/forwardRadarBeam.obj");
        s.setLocalScale(0.1f);

        CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(s);
        Node n = new Node("elephant");
        n.addControl(new RigidBodyControl(collisionShape, 1));
        n.getControl(RigidBodyControl.class).setKinematic(true);
        bulletAppState.getPhysicsSpace().add(n);
        rootNode.attachChild(n);
        bulletAppState.setDebugEnabled(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        List<PhysicsRayTestResult> rayTest = bulletAppState.getPhysicsSpace().rayTest(cam.getLocation(), cam.getLocation().add(cam.getDirection()));
        if (rayTest.size() > 0) {
            PhysicsRayTestResult get = rayTest.get(0);
            PhysicsCollisionObject collisionObject = get.getCollisionObject();
            //do stuff
            fpsText.setText(collisionObject.getUserObject().toString());
        }
    }

    private void initCrossHair() {
        BitmapText bitmapText = new BitmapText(guiFont);
        bitmapText.setText("+");
        bitmapText.setLocalTranslation((settings.getWidth() - bitmapText.getLineWidth())*0.5f, (settings.getHeight() + bitmapText.getLineHeight())*0.5f, 0);
        guiNode.attachChild(bitmapText);
    }
}