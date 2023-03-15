package com.example.weatherapp.ui.locationMap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.data.utils.Utility
import com.example.weatherapp.ui.home.PERMISSION_ID
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class LocationMapFragment : Fragment() , OnMapReadyCallback {
    lateinit var mMap: GoogleMap
    lateinit var mFusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment=childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title(place.name)
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))

                }
            }
            override fun onError(status: Status) {
                Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show()

            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        getLastMLocation()
        mMap = googleMap

    }

    private fun checkPermission():Boolean{
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMISSION_ID){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    private fun getLastMLocation(){
        if (checkPermission()){
            if (isLocationEnabled()){
                requestNewLocationData()
            }else{
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermission()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(200000)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback: LocationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation : Location? =locationResult.getLastLocation()

            val latLng = LatLng(mLastLocation!!.latitude,mLastLocation.longitude)

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marker in Sydney")
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f))

            mMap.setOnMapLongClickListener { latLng ->    mMap.addMarker(
                MarkerOptions().position(latLng).title("My Location")

            )
                val geoCoder = Geocoder(requireContext())
                val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if(address?.get(0)?.locality == null){
                    Utility.savePlaceToSharedPref(requireContext(), Utility.PLACE_KEY, address?.get(0)?.adminArea.toString())
                }else{
                    Utility.savePlaceToSharedPref(requireContext(), Utility.PLACE_KEY, address?.get(0)?.adminArea.toString()+"/"+address?.get(0)?.locality.toString())
                }

                confirmDesiredLocation(latLng.latitude,latLng.longitude,address?.get(0)?.adminArea.toString())
            }

        }
    }

    fun confirmDesiredLocation(lat: Double,long: Double, placeName: String) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        alert.setTitle(R.string.confir_loc)
        alert.setMessage(getString(R.string.do_you) + " $placeName " + getString(R.string.to_be))
        alert.setPositiveButton(getString(R.string.save)) {
                _: DialogInterface, _: Int ->
            saveLatLong(lat.toLong(),long.toLong())
            Navigation.findNavController(requireView())
                .navigate(R.id.nav_home)

            Toast.makeText(requireContext()
                , getString(R.string.save_Done)
                , Toast.LENGTH_SHORT).show()
        }
        val dialog = alert.create()
        dialog.show()

    }
private fun saveLatLong(lat : Long, long:Long){
    Utility.saveLatitudeToSharedPref(requireContext(), Utility.LATITUDE_KEY,lat)
    Utility.saveLongitudeToSharedPref(requireContext(), Utility.LONGITUDE_KEY, long)

}


}