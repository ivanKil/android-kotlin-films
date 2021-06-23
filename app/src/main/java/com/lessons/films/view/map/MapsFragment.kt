package com.lessons.films.view.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lessons.films.R
import com.lessons.films.databinding.FragmentMapsBinding
import com.lessons.films.snackBarError
import com.lessons.films.view.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

const val BUNDLE_ACTOR_ID: String = "extra_place"

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding: FragmentMapsBinding get() = _binding!!
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        //val sydney = LatLng(55.2, 56.4)
        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMapLongClickListener { getAddressAsync(it) }

    }
    private val zoom = 8f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_maps, container, false)
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
        viewModel.liveDataError.observe(viewLifecycleOwner, view::snackBarError)
        arguments?.getInt(BUNDLE_ACTOR_ID)?.let {
            viewModel.getActorPlaceOfBirth(it) { pob ->
                if (pob != "") {
                    binding.mapSearch.text = SpannableStringBuilder(pob)
                    binding.mapButtonSearch.callOnClick()
                }
                binding.mapAddressInfo.text =
                    if (pob != "") pob else resources.getString(R.string.place_unknow)
            }
        }
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            Thread {
                try {
                    val addresses =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    binding.mapAddressInfo.post {
                        binding.mapAddressInfo.text = addresses[0].getAddressLine(0)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker =
            setMarker(location, markers.size.toString(), android.R.drawable.arrow_down_float)
        markers.add(marker)
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    private fun initSearchByAddress() {
        binding.mapButtonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = binding.mapSearch.text.toString()
//            Thread {
//                try {
//                    val addresses = geoCoder.getFromLocationName(searchText, 1)
//                    if (addresses.size > 0) {
//                        goToAddress(addresses, it, searchText)
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }.start()
            Observable.fromCallable {
                val addresses = geoCoder.getFromLocationName(searchText, 1)
                if (addresses.size > 0)
                    goToAddress(addresses, it, searchText)
            }.doOnError { err ->
                err.printStackTrace()
                view?.snackBarError(
                    if (err.message != null) err.message!! else resources.getString(
                        R.string.error
                    )
                )
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe()
        }
    }

    private fun goToAddress(addresses: MutableList<Address>, view: View, searchText: String) {
        val location = LatLng(addresses[0].latitude, addresses[0].longitude)
        view.post {
            setMarker(location, searchText, android.R.drawable.checkbox_on_background)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
            binding.mapAddressInfo.text = addresses[0].getAddressLine(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}