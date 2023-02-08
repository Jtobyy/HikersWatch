package com.example.hikerswatchinkotlin

import android.content.ContentProvider
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.location.LocationManager
import android.location.LocationListener
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.Geocoder
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var altitude: TextView
    lateinit var longitude: TextView
    lateinit var latitude: TextView
    lateinit var address: TextView


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        longitude = findViewById<TextView>(R.id.longitude)
        latitude = findViewById<TextView>(R.id.latitude)
        altitude = findViewById<TextView>(R.id.altitude)
        address = findViewById<TextView>(R.id.address)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
//                Log.i("Location", location.toString());
                longitude.setText("Longitude: " + location.longitude.toString())
                latitude.setText("Latitude: " + location.latitude.toString())
                altitude.setText("Altitude: " + location.altitude.toString())

                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                val listAddress: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (listAddress.isNotEmpty()) {
                    Log.i("Location", listAddress.get(0).toString())
                    var thisLocation: Address =  listAddress.get(0)

                    var result = ""
                    result += (thisLocation.subThoroughfare + " ") ?: ""
                    result += thisLocation.thoroughfare + ", " ?: ""
                    result += (thisLocation.locality + ", ") ?: ""
                    result += thisLocation.postalCode + ", " ?: ""
                    result += thisLocation.countryName

                    address.setText("Address: " + result)
                }
                else address.setText("Address can not be found")
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }
        };

        // Check SDK
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1){ android.Manifest.permission.ACCESS_FINE_LOCATION}, 1)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        }
    }
}