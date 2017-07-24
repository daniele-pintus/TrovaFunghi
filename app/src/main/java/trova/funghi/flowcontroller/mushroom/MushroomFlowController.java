package trova.funghi.flowcontroller.mushroom;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import trova.funghi.persistence.entity.Mushroom;

/**
 * Created by danie on 22/07/2017.
 */

public class MushroomFlowController {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    // CONTEXT
    Context context;

    // LISTENERS
    MushroomFlowControllerListener listener;

    // STORAGE
    private StorageReference storageRef;

    // MODEL
    Mushroom mushroom;

    public MushroomFlowController(Context context){
        this.context = context;

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void loadMushroomData(){
        mushroom = new Mushroom();

        mushroom.setDescription("bla bla bla bla");
        mushroom.setEdibility("Ottima");
        mushroom.setHabitat("habitat");
        mushroom.setId("0001");
        mushroom.setPopularity("Alta");
        mushroom.setSciName("Agaricus arvensis");
        mushroom.setVulName("Prataiolo");

        buildThumbnailUri();
    }

    private void buildThumbnailUri() {
        StorageReference storageImg = storageRef.child("images/mushrooms/"+mushroom.getId()+".jpg");

        storageImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(LOG_TAG, "[buildThumbnailUri] Loading image success: " + uri.toString());
                mushroom.setThumbNailUri(uri.toString());
                listener.onMushroomDataLoaded(mushroom);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(LOG_TAG, "[buildThumbnailUri] - Loading image failed: ", exception);
            }
        });
    }

    public void setListener(MushroomFlowControllerListener listener) {
        this.listener = listener;
    }

    public interface MushroomFlowControllerListener {
        public void onMushroomDataLoaded(Mushroom mushroom);
    }
}
