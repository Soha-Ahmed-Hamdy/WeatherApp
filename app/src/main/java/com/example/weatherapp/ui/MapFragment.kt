package com.example.weatherapp.ui

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.data.model.FavouritePlace
import com.example.weatherapp.data.repository.Repository
import com.example.weatherapp.data.utils.Utility
import com.example.weatherapp.ui.favourite.favouriteViewModel.FactoryFavouriteWeather
import com.example.weatherapp.ui.favourite.favouriteViewModel.FavouriteViewModel
import com.example.weatherapp.ui.home.HomeViewModel.FactoryHomeWeather
import com.example.weatherapp.ui.home.HomeViewModel.HomeViewModel
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


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var fact: FactoryFavouriteWeather


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var repository=Repository.getRepositoryInstance(requireActivity().application)


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
        mLocationRequest.setInterval(400000)
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

            mMap.setOnMapClickListener {
                    latLng ->    mMap.addMarker(
                        MarkerOptions()
                        .position(latLng)
                        .title("My Location")

            )
                val geoCoder = Geocoder(requireContext())
                val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                checkSaveToFavorite(FavouritePlace(latLng.latitude,latLng.longitude,address?.get(0)?.countryName.toString(),address?.get(0)?.adminArea.toString()),address?.get(0)?.countryName.toString())
            }

        }
    }
    fun checkSaveToFavorite(favouritePlace: FavouritePlace, placeName: String) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        alert.setTitle(getString(R.string.fav))

        alert.setMessage(getString(R.string.want_save) + " $placeName " +getString(R.string.to_fav))
        alert.setPositiveButton(getString(R.string.save)) {
                _: DialogInterface, _: Int ->

            val repository = Repository.getRepositoryInstance(requireActivity().application)

            fact= FactoryFavouriteWeather(repository)

            val favouriteViewModel= ViewModelProvider(requireActivity(),fact).get(FavouriteViewModel::class.java)

            favouriteViewModel.insertFav(favouritePlace)

            Toast.makeText(requireContext()
                , getString(R.string.save_Done)
                , Toast.LENGTH_SHORT).show()
        }
        val dialog = alert.create()
        dialog.show()

    }


}