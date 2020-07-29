package com.ingteamsofindia.black_forest.Activity.AddPost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ingteamsofindia.black_forest.Activity.Profile.ProfileSettingsActivity;
import com.ingteamsofindia.black_forest.R;
import com.ingteamsofindia.black_forest.Util.FilePath;
import com.ingteamsofindia.black_forest.Util.FileSearch;
import com.ingteamsofindia.black_forest.Util.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private static final int NUM_GRID_COLUMNS = 3;


    //    Widget
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;
//    Vars
    private ArrayList<String> directory;
    private String mAppend = "file:/";
    private String mSelectedImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
        galleryImage = (ImageView)view.findViewById(R.id.galleryImageView);
        gridView = (GridView)view.findViewById(R.id.grid_view);
        directorySpinner = (Spinner)view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar)view.findViewById(R.id.galleryImageViewProgressBar);
        mProgressBar.setVisibility(View.GONE);
        directory = new ArrayList<>();
        Log.d(TAG, "onCreateView: Started...");
        ImageView shareClose = (ImageView)view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Closing the gallery fragment...");
                getActivity().finish();
            }
        });
        TextView nextScreen = (TextView) view.findViewById(R.id.tvNextShare);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to the final share screen...");

                if(isRootTask()){
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.fragment_edit_profile));
                    startActivity(intent);
                    getActivity().finish();
                }
                Intent intent = new Intent(getActivity(),NextActivity.class);
                intent.putExtra(getString(R.string.selected_image),mSelectedImage);
                startActivity(intent);

            }
        });
        init();
        return view;
    }
    private boolean isRootTask(){
        if(((AddPostActivity)getActivity()).getTask() == 0){
            return true;
        }
        else{
            return false;
        }
    }
    private void init(){
        FilePath filePath = new FilePath();
        if (FileSearch.getDirectoryPaths(filePath.CAMERA)!= null){
            directory = FileSearch.getDirectoryPaths(filePath.CAMERA);
        }else {
            Toast.makeText(getActivity(), "NO folder found", Toast.LENGTH_SHORT).show();
        }
        ArrayList<String>directoryName = new ArrayList<>();
        for (int i = 0; i < directory.size();i++){
            int index = directory.get(i).lastIndexOf("/")+1;
            String string = directory.get(i).substring(index);
            directoryName.add(string);

        }

        directory.add(filePath.PICTURES);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,directoryName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);
        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: selected "+directory.get(position));
                setupGridView(directory.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setupGridView(String selectedDirectory){
        final ArrayList<String> imageUrl = FileSearch.getFilePaths(selectedDirectory);

//        set the grid column with
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

//        use the grid adapter to adapter the image to Grid View
        GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,mAppend,imageUrl);
        gridView.setAdapter(adapter);

//        set the first image to be displayed when the activity fragment view is inflated
        setImage(imageUrl.get(0),galleryImage,mAppend);
        mSelectedImage = imageUrl.get(0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, "onItemClick: Selected an image: "+ imageUrl.get(position));
                setImage(imageUrl.get(position),galleryImage,mAppend);
                mSelectedImage = imageUrl.get(position);
            }
        });
    }
    private void setImage(String imgUrl,ImageView image,String append){
        Log.d(TAG, "setImage: Setting Image");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgUrl, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
