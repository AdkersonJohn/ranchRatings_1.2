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
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.ranchratings_12.R
import kotlinx.android.synthetic.main.main_fragment01.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    private val IMAGE_GALLERY_REQUEST_CODE: Int = 2001
    private val SAVE_IMAGE_REQUEST_CODE: Int = 1999
    private val CAMERA_REQUEST_CODE = 1998
    val CAMERA_PERMISSION_REQUEST_CODE = 1997
    private lateinit var currentPhotoPath: String

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment01, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //still need to create JSON to populate autocomplete search bar
      /*  viewModel = ViewModelProvider(this).get(MainViewModel::class.java)*/
      /*  viewModel.institutions.observe(this, Observer{
            institutions -> actInstitutionName.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, institutions))
        })*/
        btnTakePhoto.setOnClickListener {
            prepTakePhoto()
        }
        btnProfile.setOnClickListener(){
            prepOpenImageGallery()
        }
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