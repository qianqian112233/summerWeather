package com.example.summerweather.ui.Demo

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import com.example.summerweather.R

class ContentProviderActivity : AppCompatActivity() {

    private val contactsList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
        val contactsView = findViewById<ListView>(R.id.contacts_view)
        contactsView.adapter = adapter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }else{
            readContacts()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts()
                } else {
                    Toast.makeText(this,  "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun readContacts(){
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)?.apply {
            while (moveToNext()){
                val displayNameColumnIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberColumnIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var displayName: String? = null
                var number: String? = null

                if (displayNameColumnIndex >= 0){
                    displayName = getString(displayNameColumnIndex)
                } else {
                    Log.e("ContactQuery", "Display name column not found")
                }

                if(numberColumnIndex >= 0){
                    number = getString(numberColumnIndex)
                } else {
                    Log.e("ContactQuery", "Number column not found")
                }

                if(displayName != null && number != null){
                    contactsList.add("$displayName\n$number")
                }

            }
            adapter.notifyDataSetChanged()
            close()
        }
    }
}