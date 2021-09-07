package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.myapplication.databinding.ActivityDialBinding


class DialActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_PHONE_CALL = 1011

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

//        val navController = findNavController(R.id.nav_host_fragment_content_dial)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Dial", null).show()
            checkForDialNumberPermission()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dial)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun dialNumber() {

        val encodedPhoneNumber = String.format("tel:%s", Uri.encode("##72786#"));

       // val intent = Intent(Intent.ACTION_CALL)

        //intent.data = Uri.parse("tel:" + "##72786#")
        //intent.data = Uri.parse("tel:" + "+919550011199")
        //startActivity(intent)

        val callIntent = Intent(Intent.ACTION_CALL)
        //callIntent.data = Uri.parse("tel:##72786#")
        callIntent.data = Uri.parse(encodedPhoneNumber)

        startActivity(callIntent)
    }

    fun checkForDialNumberPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.READ_PHONE_STATE,
                    REQUEST_PERMISSION_PHONE_CALL
                )
            } else {
                requestPermission(
                    Manifest.permission.ANSWER_PHONE_CALLS,
                    REQUEST_PERMISSION_PHONE_CALL
                )
            }
        } else {
            dialNumber()
        }
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, id -> requestPermission(permission, permissionRequestCode) }
        builder.create().show()
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_PHONE_CALL -> if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                dialNumber()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}