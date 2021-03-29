package com.example.ranchratings_12.ui.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.example.ranchratings_12.R
import kotlinx.android.synthetic.main.main_fragment01.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    private val IMAGE_GALLERY_REQUEST_CODE: Int = 2001
    private val SAVE_IMAGE_REQUEST_CODE: Int = 1999
    private val CAMERA_REQUEST_CODE = 1998
    private val CAMERA_PERMISSION_REQUEST_CODE = 1997
    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    private lateinit var currentPhotoPath: String

    companion object {
        fun newInstance() = MainFragment()
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
            btnProfile. visibility = INVISIBLE
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
        }

        prepRequestLocationUpdates()
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
                    val photoURI = FileProvider.getUriForFile(activity!!.applicationContext, "com.ranchratings_12.android.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
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
            }else if (requestCode == IMAGE_GALLERY_REQUEST_CODE){
                if (data != null && data.data != null){
                    val image = data.data
                    val source = ImageDecoder.createSource(activity!!.contentResolver, image!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                            imgFood.setImageBitmap(bitmap)
                }
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
