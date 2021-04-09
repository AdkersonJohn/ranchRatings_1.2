package com.example.ranchratings_12.ui.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ranchratings_12.R
import com.example.ranchratings_12.dtos.Photo
import com.example.ranchratings_12.dtos.Review
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.main_fragment01.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainFragment : Fragment() {
    private val IMAGE_GALLERY_REQUEST_CODE: Int = 2001
    private val SAVE_IMAGE_REQUEST_CODE: Int = 1999
    private val CAMERA_REQUEST_CODE = 1998
    private val CAMERA_PERMISSION_REQUEST_CODE = 1997
    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var currentPhotoPath: String
    private val AUTH_REQUEST_CODE = 2002
    private var user : FirebaseUser? = null
    private var photos : ArrayList<Photo> = ArrayList<Photo>()
    private var photoURI : Uri? = null
    companion object {
        fun newInstance() = MainFragment()
    }
    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment01, container, false)


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //still need to create JSON to populate autocomplete search bar
//      viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        viewModel.institutions.observe(this, Observer{
//            institutions -> actInstitutionName.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, institutions))
//        })
        btnTakePhoto.setOnClickListener {
            prepTakePhoto()
        }
        btnProfile.setOnClickListener(){
            prepOpenImageGallery()
        }
        btnAddReview.setOnClickListener(){
            imgFood.visibility = VISIBLE
            txtReview2.visibility = VISIBLE
            ratingBar2.visibility = VISIBLE
            btnBack1.visibility = VISIBLE
            btnTakePhoto.visibility = VISIBLE
            btnSave.visibility = VISIBLE
            txtLatitude.visibility = VISIBLE
            txtLongitude.visibility = VISIBLE
            txtAddReview.visibility = INVISIBLE
            btnAddReview.visibility = INVISIBLE
            txtInstitutionName.visibility = VISIBLE
            btnSearch.visibility = INVISIBLE
            btnProfile.visibility = INVISIBLE
            spnReviews.visibility = INVISIBLE
        }
        btnBack1.setOnClickListener(){
            imgFood.visibility = INVISIBLE
            txtReview2.visibility = INVISIBLE
            ratingBar2.visibility = INVISIBLE
            btnBack1.visibility = INVISIBLE
            btnTakePhoto.visibility = INVISIBLE
            btnSave.visibility = INVISIBLE
            txtLatitude.visibility = INVISIBLE
            txtLongitude.visibility = INVISIBLE
            txtAddReview.visibility = VISIBLE
            btnAddReview.visibility = VISIBLE
            txtInstitutionName.visibility = INVISIBLE
            btnSearch.visibility = VISIBLE
            btnProfile. visibility = VISIBLE
            spnReviews.visibility = VISIBLE
        }
        prepRequestLocationUpdates()
        btnSave.setOnClickListener(){
            saveReview()
        }
        btnProfile.setOnClickListener(){
            logon()
        }
    }
    private fun logon() {
        var providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), AUTH_REQUEST_CODE
        )
    }
    private fun saveReview() {
        var reviewIDCounter = 0
        var review = Review().apply{
            reviewIDCounter += 1
            latitude = txtLatitude.text.toString()
            longitutde = txtLongitude.text.toString()
            institutionName = txtInstitutionName.text.toString()
            reviewText = txtReview2.text.toString()
            rating = ratingBar2.numStars.toDouble()
            userID
            reviewID = firestore.collection("reviews").document().id
        }
        save(review, photos)

        review = Review()
        photos = ArrayList<Photo>()

    }
    private fun save(review: Review, photos: ArrayList<Photo>) {
        val document = firestore.collection("reviews").document()
        review.reviewID = document.id
        val set = document.set(review)
        .addOnSuccessListener {
                Log.d("Firebase", "Document saved")
                if(photos != null && photos.size > 0){
                    savePhotos(review, photos)
                }
            }
            .addOnFailureListener{
                Log.d( "Firebase", "Save Failed")
            }
    }

    private fun savePhotos(review: Review, photos: ArrayList<Photo>) {
        val collection = firestore.collection("reviews")
                .document(review.reviewID)
                .collection("photos")
        photos.forEach {
            photo -> val task = collection.add(photo)
            task.addOnSuccessListener {
                photo.id = it.id
            }
        }
    }

    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocationUpdates()
        } else{
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    private fun requestLocationUpdates() {
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                txtLongitude.text = it.longitude
                txtLatitude.text = it.latitude
            })
    }
    private fun prepOpenImageGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply{
            this.type = "image/*"
            startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
        }
    }
    private fun prepTakePhoto() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            takePhoto()
        }else{
            val permissionRequest = arrayOf(Manifest.permission.CAMERA);
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted lets do stuff
                    takePhoto()
                }else{
                    Toast.makeText(context, "Unable to take photo without permission", Toast.LENGTH_LONG).show()
                }
            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestLocationUpdates()
                } else{
                    Toast.makeText(context, "Unable to update location without permission", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
            takePictureIntent -> takePictureIntent.resolveActivity(context!!.packageManager)
            if(takePictureIntent == null){
                Toast.makeText(context, "Unable to save photo", Toast.LENGTH_LONG).show()
        }else{
                //if we are here we have a valid intent
                val photoFile:File = createImageFile()
                photoFile?.also{
                    photoURI = FileProvider.getUriForFile(activity!!.applicationContext, "com.ranchratings_12.android.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, SAVE_IMAGE_REQUEST_CODE)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                //now we can get the thumbnail
                val imageBitmap = data!!.extras!!.get("data") as Bitmap
                imgFood.setImageBitmap(imageBitmap)
            }else if(requestCode == SAVE_IMAGE_REQUEST_CODE){
                    Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show()
                    var photo = Photo(localUri = photoURI.toString())
                    photos.add(photo)
            }else if (requestCode == IMAGE_GALLERY_REQUEST_CODE){
                if (data != null && data.data != null){
                    val image = data.data
                    val source = ImageDecoder.createSource(activity!!.contentResolver, image!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                            imgFood.setImageBitmap(bitmap)
                }
            } else if(requestCode == AUTH_REQUEST_CODE){
                    user = FirebaseAuth.getInstance().currentUser
            }
        }
    }
    private fun createImageFile(): File {
        //generate a new filename with the date
        val timestamp: String = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        //get access to the directory where we can write pictures
        val storageDir:File? = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("RanchRatings${timestamp}", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }

    }
}
