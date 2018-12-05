package org.sidia.evatest;

import android.util.Log;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRNode;
import com.samsungxr.animation.SXRAnimation;
import com.samsungxr.animation.SXRAnimator;
import com.samsungxr.animation.SXRAvatar;
import com.samsungxr.animation.SXRRepeatMode;

import org.joml.Matrix4f;

import java.io.IOException;
import java.io.InputStream;

public class TestMain extends SXRMain {
    private SXRNode boneModel;

    private SXRAvatar evaAvatar;
    private String boneMap;

    @Override
    public void onInit(SXRContext sxrContext) throws Throwable {
        boneMap = readFile(sxrContext, "anim/pet_skeleton_map.txt");
        evaAvatar = new SXRAvatar(sxrContext, "PetModel");
        evaAvatar.getEventReceiver().addListener(avatarEvents);
        evaAvatar.loadModel(new SXRAndroidResource(sxrContext, "pet_model.dae"));

        boneModel = sxrContext.getAssetLoader().loadModel("bone_model.obj");
        boneModel.getTransform().setPosition(10000f, 0f, 100f);
        boneModel.getTransform().setScale(20000f, 20000f, 20000f);
    }

    private final static String refName = "m_dnLipEnd_JNT";
    private SXRAvatar.IAvatarEvents avatarEvents = new SXRAvatar.IAvatarEvents() {
        @Override
        public void onAvatarLoaded(SXRAvatar sxrAvatar, SXRNode sxrNode, String s, String s1) {
            evaAvatar.getModel().getTransform().setPosition(0f, 0, -300f);
            getSXRContext().getMainScene().addNode(evaAvatar.getModel());
            Matrix4f e = evaAvatar.getModel().getTransform().getModelMatrix4f();
            Log.e("TEST_MAIN", "model mat=" + e);

            SXRNode refNode = getSXRContext().getMainScene().getNodeByName(refName);
            refNode.addChildObject(boneModel);

            try
            {
                SXRAndroidResource res = new SXRAndroidResource(getSXRContext(), "anim/pet_bone_anim_grab.bvh");
                evaAvatar.loadAnimation(res, boneMap);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onModelLoaded(SXRAvatar sxrAvatar, SXRNode sxrNode, String s, String s1) {

        }

        @Override
        public void onAnimationLoaded(SXRAvatar sxrAvatar, SXRAnimator sxrAnimator, String s, String s1) {
            sxrAnimator.setRepeatMode(SXRRepeatMode.REPEATED);
            sxrAnimator.setRepeatCount(-1);
            sxrAnimator.start();
        }

        @Override
        public void onAnimationStarted(SXRAvatar sxrAvatar, SXRAnimator sxrAnimator) {

        }

        @Override
        public void onAnimationFinished(SXRAvatar sxrAvatar, SXRAnimator sxrAnimator, SXRAnimation sxrAnimation) {

        }
    };

    static String readFile(SXRContext sxrContext, String filePath) {
        try
        {
            SXRAndroidResource res = new SXRAndroidResource(sxrContext, filePath);
            InputStream stream = res.getStream();
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            String s = new String(bytes);
            return s;
        }
        catch (IOException ex)
        {
            return null;
        }
    }

}
