package com.mdasrafulalam78.roomdbdemo


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.*
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.mdasrafulalam78.roomdbdemo.databinding.FragmentNewScheduleBinding
import com.mdasrafulalam78.roomdbdemo.model.BusSchedule
import com.mdasrafulalam78.roomdbdemo.viewmodel.ScheduleViewModel
import com.tanvir.training.actioninputsbatch04.customdialogs.DatePickerFragment
import com.tanvir.training.actioninputsbatch04.customdialogs.TimePickerFragment
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewScheduleFragment : Fragment() {
    private lateinit var preference: LoginPreference
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_SELECT = 2
    private val viewModel: ScheduleViewModel by activityViewModels()
    private lateinit var binding: FragmentNewScheduleBinding
    private var from = "Dhaka"
    private var to = "Dhaka"
    private var busType = "Economy"
    private var selectedDate = ""
    private var selectedTime = ""
    private var id: Long? = null
    private var currentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_notification -> Toast.makeText(requireContext(),"No new notification", Toast.LENGTH_SHORT).show()
            R.id.action_message -> Toast.makeText(requireContext(),"No new messsage", Toast.LENGTH_SHORT).show()
            R.id.action_search -> Toast.makeText(requireContext(),"Search is not implemented yet", Toast.LENGTH_SHORT).show()
            R.id.logout -> {
                preference.userIdFlow.asLiveData().observe(this, androidx.lifecycle.Observer {
                    ScheduleListFragment.userId = it
                })
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(false, ScheduleListFragment.userId, requireContext())
                }
            }
        }
        return super.onOptionsItemSelected(item)
//                Toast.makeText(this,"Your Profile will be visible soon!",Toast.LENGTH_SHORT).show()
    }
    private fun show(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
            Log.d("CameraExample", "createImageFile: $currentPhotoPath")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewScheduleBinding.inflate(inflater, container, false)
        initCitySpinner()
        initBusTypeRadioGroup()
        id = arguments?.getLong("id")
        if (id != null) {
            binding.saveBtn.setText("Update")
//            updateImage()
            viewModel.getScheduleById(id!!).observe(viewLifecycleOwner) {
                binding.busNameInputET.setText(it.name)
                binding.showDateTV.setText(it.departureDate)
                binding.showTimeTV.setText(it.departureTime)
                selectedDate = it.departureDate
                selectedTime = it.departureTime
                val fromIndex = cityList.indexOf(it.from)
                val toIndex = cityList.indexOf(it.to)
                binding.fromCitySpinner.setSelection(fromIndex)
                binding.toCitySpinner.setSelection(toIndex)
                if (it.busType == "Economy") {
                    binding.busTypeRG.check(R.id.economyRB)
                }else if (it.busType == "Business") {
                    binding.busTypeRG.check(R.id.businessRB)
                }
                binding.saveBtn.tag = it.favorite
            }
        }
        binding.dateBtn.setOnClickListener {
            DatePickerFragment {
                selectedDate = it
                binding.showDateTV.text = it
            }.show(childFragmentManager, null)
        }
        binding.timeBtn.setOnClickListener {
            TimePickerFragment {
                selectedTime = it
                binding.showTimeTV.text = it
            }.show(childFragmentManager, null)
        }
        binding.captureBtn.setOnClickListener {
            dispatchTakePictureIntent()
            CAMERA = true
        }
        binding.selectFromGalleryBtn.setOnClickListener(View.OnClickListener {
            val checkSelfPermission = ContextCompat.checkSelfPermission(requireActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            else{
                openGalery()
            }

            CAMERA = false
        })
        binding.saveBtn.setOnClickListener {
            saveInfo()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateImage() {

    }


//    /external/images/media/1000000476
//    /storage/emulated/0/Android/data/com.mdasrafulalam78.roomdbdemo/files/Pictures/JPEG_20230103_131946_399180082800963303.jpg
    private fun saveInfo() {
        val busName = binding.busNameInputET.text.toString()
        var busFare = 0
        if (binding.busFareInputET.text.toString()!=null){
             busFare = Integer.parseInt(binding.busFareInputET.text.toString())
        }
        //validate destination
        if (from == to) {
            Toast.makeText(
                activity,
                "Origin and destination cannot be the same",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val schedule = BusSchedule(
            name = busName,
            from = from,
            to = to,
            departureDate = selectedDate,
            departureTime = selectedTime,
            busType = busType,
            image = currentPhotoPath,
            fare = busFare
        )
        if (id != null) {
            schedule.id = id!!
            schedule.favorite = binding.saveBtn.tag as Boolean
            viewModel.updateSchedule(schedule)
        }else {
            viewModel.addSchedule(schedule)
        }
        findNavController().navigate(R.id.action_newScheduleFragment_to_schedulListFragment)
        Log.d("ScheduleInfoCheck", "saveInfo: $schedule")
    }

    private fun initBusTypeRadioGroup() {
        binding.busTypeRG.setOnCheckedChangeListener { radioGroup, i ->
            val rb: RadioButton = radioGroup.findViewById(i)
            busType = rb.text.toString()
        }
    }

    private fun initCitySpinner() {
        val cityAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            cityList
        )
        binding.fromCitySpinner.adapter = cityAdapter
        binding.toCitySpinner.adapter = cityAdapter
        binding.fromCitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    from = p0?.getItemAtPosition(p2).toString()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        binding.toCitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    to = p0?.getItemAtPosition(p2).toString()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun dispatchTakePictureIntent() {
        Intent(ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.mdasrafulalam78.roomdbdemo.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    private fun openGalery() {
        val gallery = Intent(Intent.ACTION_PICK)
        gallery.type = "image/*"
        gallery.data = Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(gallery, 1000)
    }
    fun getRealPathFromURI(contentUri: Uri, activityContext: Activity): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activityContext.managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>
                                            , grantedResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when(requestCode){
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED){
                    openGalery()
                }else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            binding.busImageView.setImageURI(Uri.parse(currentPhotoPath))
        }
        if (requestCode == 1000 && resultCode == RESULT_OK){
//            currentPhotoPath = data.toString()
            val imageUri: Uri = data?.data!!
            val finalFile = File(getRealPathFromURI(imageUri, requireActivity()))
            currentPhotoPath = finalFile.absolutePath
            Log.d("uri", currentPhotoPath.toString())
            binding.busImageView.setImageURI(imageUri)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    companion object{
        var CAMERA = false
    }
}

val cityList = listOf("Dhaka", "Chittagong", "Rajshahi",
    "Khulna", "Sylhet", "Cox's Bazar", "Comilla",
    "Faridpur", "Bashishal")